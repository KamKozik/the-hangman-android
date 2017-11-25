package algorytmiczne.swiry;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();

        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void onPlayButtonClicked(View view) {
//        Toast.makeText(getApplicationContext(), "Play button clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        //intent.putextra
        MainActivity.this.startActivity(intent);
    }
}
