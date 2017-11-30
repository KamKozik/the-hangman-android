package algorytmiczne.swiry;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import shared.GameState;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GameActivity extends Activity {

    static final String AVAILABLE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final int PLAYERS_COUNT = 4;
    private SocketSingleton socketSingleton;
    static final int LINE_MAX_WIDTH = 12;
    private String currentWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new myview(this));
        setContentView(R.layout.activity_game);
        this.socketSingleton = new SocketSingleton();
        View decorView = getWindow().getDecorView();

        // Hide the status bar
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

        createKeyboard();

    }

    @Override
    protected void onResume() {
        super.onResume();
        socketSingleton = socketSingleton.getInstance(this);
        gameStateChanges(socketSingleton.getGameState());
    }

    public void gameStateChanges(final GameState newGameState) {
        System.out.println("Udało się!");
        final GameState copyGameState = newGameState;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //ustawienie graczy
                TextView loginEditText = (TextView) findViewById(R.id.usersPaceholder);
                StringBuilder usersPlaceholder = new StringBuilder();
                for (int i = 0; i < PLAYERS_COUNT; i++) {
                    if (copyGameState.players[i].hasTurn) {
                        usersPlaceholder.append("<").append(copyGameState.players[i].login).append(": ").append(copyGameState.players[i].points).append(">");
                    } else if (!copyGameState.players[i].isConnected) {
                        usersPlaceholder.append("?").append(copyGameState.players[i].login).append("!");
                    } else {
                        usersPlaceholder.append(" ").append(copyGameState.players[i].login).append(": ").append(copyGameState.players[i].points);
                    }
                }
                loginEditText.setText(usersPlaceholder.toString());

                int myNumberPlayer = 0;
                for (int i = 0; i < PLAYERS_COUNT; i++) {
                    if (copyGameState.players[i].login.equals(MainActivity.myLogin))
                        myNumberPlayer = i;
                }

                //update hangmana
                ImageView hangman = findViewById(R.id.imageView);
                switch (copyGameState.hangmanHealth) {
                    case 7:
                        hangman.setImageResource(R.drawable.stage2);
                        break;
                    case 6:
                        hangman.setImageResource(R.drawable.stage3);
                        break;
                    case 5:
                        hangman.setImageResource(R.drawable.stage4);
                        break;
                    case 4:
                        hangman.setImageResource(R.drawable.stage5);
                        break;
                    case 3:
                        hangman.setImageResource(R.drawable.stage6);
                        break;
                    case 2:
                        hangman.setImageResource(R.drawable.stage7);
                        break;
                    case 1:
                        hangman.setImageResource(R.drawable.stage8);
                        break;
                    case 0:
                        hangman.setImageResource(R.drawable.stage9);
                        break;
                }

                // update worda
                drawWord(copyGameState.word);

                //czy nasza tura
                boolean myTurn = copyGameState.players[myNumberPlayer].hasTurn;
                if (myTurn) {

                    //czy jest tura na odgadnięcie litery
                    if (copyGameState.phase == GameState.Phase.Guess) {
                        for (int i = 0; i < 26; i++) {
                            Button button = findViewById(300 + i);
                            Character a = button.getText().toString().toUpperCase().charAt(0);
                            button.setEnabled(!copyGameState.keyboard.get(a));
                        }
                    }

                    //czy jest tura na wybór słowa
                    if (copyGameState.phase == GameState.Phase.ChoosingWord) {
                        final EditText wordInput = new EditText(GameActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        wordInput.setLayoutParams(lp);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
                        alertDialogBuilder.setTitle("Enter your word here");
                        alertDialogBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                            socketSingleton.sendWord(wordInput.getText().toString());
                            Toast.makeText(GameActivity.this, wordInput.getText().toString(),
                                    Toast.LENGTH_LONG).show();
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setView(wordInput);
                        alertDialog.show();
                    }
                } else {
                    for (int i = 0; i < 26; i++) {
                        Button button = findViewById(300 + i);
                        Character a = button.getText().toString().toUpperCase().charAt(0);
                        button.setEnabled(false);
                    }
                }
            }
        });
    }

    public void createKeyboard() {
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
                    TableRow.LayoutParams viewParams = new TableRow.LayoutParams();
                    viewParams.gravity = Gravity.CENTER;
                    emptyView.setLayoutParams(viewParams);
                    tr.addView(emptyView);
                    continue;
                }

                Button letterBtn = new Button(this);
                String letter = String.valueOf(AVAILABLE_LETTERS.charAt(alphabetIndexer));
                letterBtn.setText(letter);
                letterBtn.setId(300 + alphabetIndexer);
                letterBtn.setOnClickListener((view) -> {
                    socketSingleton.sendLetter(letter.charAt(0));
                });

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

    public void drawWord(String word) {
        if(word == null) return;

        List<String> wordRows = new ArrayList<>();

        TableLayout wordTableLayout = findViewById(R.id.wordTableLayout);
        wordTableLayout.removeAllViews();
        Log.d(TAG, "Width :" + wordTableLayout.getWidth());

        String formattedString = WordUtils.wrap(word, LINE_MAX_WIDTH);
        wordRows.addAll(Arrays.asList(formattedString.split("\\r?\\n")));

        for (int row = 0; row < wordRows.size(); row++) {
            TableRow tr = new TableRow(this);
            tr.setId(200 + 1);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

            for (char c : wordRows.get(row).toCharArray()) {
                AppCompatTextView letterView = new AppCompatTextView(this);
                if (c == ' ') {
                    letterView.setBackgroundResource(R.drawable.letterspace);
                } else if(c == '_'){
                    letterView.setBackgroundResource(R.drawable.letterline);
                    letterView.setText(" ");
                } else {
                    letterView.setBackgroundResource(R.drawable.letterline);
                    letterView.setText(String.valueOf(c));
                }
                Typeface typeface = ResourcesCompat.getFont(this, R.font.appfont);
                letterView.setTypeface(typeface);
                TableRow.LayoutParams letterParams = new TableRow.LayoutParams();
                letterParams.gravity = Gravity.FILL;
                letterView.setLayoutParams(letterParams);
                tr.addView(letterView);
            }
            tr.setLayoutParams(params);
            wordTableLayout.addView(tr, params);
        }
//        wordTableLayout.setStretchAllColumns(true);
    }
}
