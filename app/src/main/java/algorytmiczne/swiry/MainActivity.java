package algorytmiczne.swiry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import shared.Message;
import shared.MessageType;

public class MainActivity extends Activity {
    public boolean gameIsReady=false;
    public boolean waiting=true;

    private SocketSingleton socketSingleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.socketSingleton= new SocketSingleton();
        View decorView = getWindow().getDecorView();

        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        final EditText mEdit   = (EditText)findViewById(R.id.ipAddressEditText);
        Button playButton = findViewById(R.id.playButton);
        final MainActivity main = this;
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SocketSingleton.initializeIP(mEdit.getText().toString());

                socketSingleton = socketSingleton.getInstance(main);
                EditText loginEditText   = (EditText)findViewById(R.id.usernameEditText);
                socketSingleton.sendLogin(loginEditText.getText().toString());
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                while(waiting){

                    if(gameIsReady)startActivity(intent);

                }

            }
        });
    }

}
