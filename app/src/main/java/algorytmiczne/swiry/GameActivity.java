package algorytmiczne.swiry;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;

import static android.content.ContentValues.TAG;

public class GameActivity extends Activity {

    static final String AVAILABLE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        View decorView = getWindow().getDecorView();

        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        fillLettersGridLayout();
    }

    public void fillLettersGridLayout() {
        TableLayout table = findViewById(R.id.lettersTableLayout);
        Log.d(TAG, "Width :" + table.getWidth());
        int alphabetIndexer = 0;
        for (int row = 0; row < 4; row++) {
            TableRow tr = new TableRow(this);
            tr.setId(100 + row);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            for (int cell = 0; cell < 7; cell++) {
                if (row == 3 && (cell == 0 || cell == 6)) {
                    Space emptyView = new Space(this);
                    emptyView.setBackgroundColor(Color.RED);
                    TableRow.LayoutParams viewParams = new TableRow.LayoutParams();
                    viewParams.gravity = Gravity.CENTER;
                    emptyView.setLayoutParams(viewParams);
                    tr.addView(emptyView);
                    continue;
                }

                Button letterBtn = new Button(this);
                String letter = String.valueOf(AVAILABLE_LETTERS.charAt(alphabetIndexer));
                letterBtn.setText(letter);
                letterBtn.setBackgroundColor(Color.TRANSPARENT);
                Typeface typeface = ResourcesCompat.getFont(this, R.font.appfont);
                letterBtn.setTypeface(typeface);

                TableRow.LayoutParams btnParams = new TableRow.LayoutParams();
                btnParams.gravity = Gravity.CENTER;
                letterBtn.setLayoutParams(btnParams);
                tr.addView(letterBtn);
                alphabetIndexer++;
            }
            tr.setLayoutParams(params);
            table.addView(tr, params);
        }
        table.setStretchAllColumns(true);
    }
}
