package algorytmiczne.swiry;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import shared.Message;

public class SocketSingleton {
    private static Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static SocketSingleton instance;
    private static  short SERVER_PORT = 6969;
    public static String SERVER_IP ;
    private static boolean exit = false;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;


    public SocketSingleton() {
    }

    public  SocketSingleton getInstance() {
        if (instance == null)
            initSingleton();
        return instance;
    }

    public void initSingleton() {
        if (instance == null) {
            instance = new SocketSingleton();
            new Thread(new ConnectingThread()).start();

        }
    }


    public void sendMessage(Message message) {
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Message readMessage() {
        try {
            return (Message) objectInputStream.readObject();
        } catch (SocketException e) {
            switch (e.getMessage()) {
                case "Socket closed":   // client is exiting
                    return null;
                case "Connection reset":   // server side fault
                    System.out.println("Server is down. Trying to reconnect...");
                    // TO DO: reconnect
                    break;
                default:
                    e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    class ConnectingThread implements Runnable {
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(SERVER_IP, SERVER_PORT);
                System.out.print("\tCreating output stream...");
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println(" OK");
                System.out.print("\tCreating input stream...");
                objectInputStream = new ObjectInputStream(socket.getInputStream());   // will hang there if server doesn't accept connection
                System.out.println(" OK");
                Message message;
                while (!Thread.interrupted()) {
                    message = readMessage();
                    if (message == null)    // client should quit
                        return;
                    switch (message.type) {
                        case Disconnect:   // server wants to disconnect
                            exit = true;
                            System.out.println("Server closed connection. Closing client.");
                            return;
                        default:
                            System.out.println("Unknown message type received from server.");
                    }
                    System.out.println("Message from server: " + message.type + ": " + message.data);
                }
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}