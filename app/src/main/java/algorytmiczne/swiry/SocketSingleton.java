package algorytmiczne.swiry;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketSingleton {
    private static Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static SocketSingleton instance;
    private static final short SERVER_PORT = 6969;
    private static final String SERVER_IP = "10.0.2.2";


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


    class ConnectingThread implements Runnable {
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(bufferedReader.readLine());
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}