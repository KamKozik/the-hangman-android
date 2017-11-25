package algorytmiczne.swiry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends Activity {

    private SocketSingleton socketSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.socketSingleton= new SocketSingleton();
        this.socketSingleton = socketSingleton.getInstance();
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
    }
}
