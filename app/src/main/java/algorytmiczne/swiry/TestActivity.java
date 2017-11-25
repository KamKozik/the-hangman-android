package algorytmiczne.swiry;

import android.os.Bundle;
import android.app.Activity;

public class TestActivity extends Activity {
    private SocketSingleton socketSingleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.socketSingleton= new SocketSingleton();
        this.socketSingleton = socketSingleton.getInstance();
    }

}
