package com.activities;

import java.util.Locale;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Endgame extends AppCompatActivity {
    private EditText playerNameEditText;
    private EditText playerScoreEditText;
    private Button submitScoreButton;
    private TableLayout leaderboardTable;
    private SQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        playerNameEditText = findViewById(R.id.playerNameEditText);
        playerScoreEditText = findViewById(R.id.playerScoreEditText);
        submitScoreButton = findViewById(R.id.submitScoreButton);
        leaderboardTable = findViewById(R.id.leaderboardTable);
        dbHelper = new LeaderboardDbHelper(this);

        submitScoreButton.setOnClickListener(v -> {
            String playerName = playerNameEditText.getText().toString();
            String playerScoreText = playerScoreEditText.getText().toString();

            if (playerName.isEmpty() || playerScoreText.isEmpty()) {
                Toast.makeText(Endgame.this, "Please enter both name and score.", Toast.LENGTH_SHORT).show();
            } else {
                int playerScore = Integer.parseInt(playerScoreText);
                addPlayerToLeaderboard(playerName, playerScore);
                playerNameEditText.setText("");
                playerScoreEditText.setText("");
                loadLeaderboard();

                addNewUserToLeaderboardTable1(playerName, playerScore);
            }
        });

        loadLeaderboard();
    }

    private void addPlayerToLeaderboard(String name, int score) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", score);

        db.insert("leaderboard", null, values);
        db.close();
    }

    private TextView createFormattedTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        return textView;
    }

    private void loadLeaderboard() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("leaderboard", null, null, null, null, null, "score DESC", "10");
        leaderboardTable.removeAllViews();

        int newPlayerRank = 10;
        int currentRank = 1;

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));

            String playerName = playerNameEditText.getText().toString();
            String playerScoreText = playerScoreEditText.getText().toString();

            if (name.equals(playerName) && String.valueOf(score).equals(playerScoreText)) {
                newPlayerRank = currentRank;
            }

            addRowToLeaderboard(currentRank, name, score);
            currentRank++;
        }

        cursor.close();

        String playerName = playerNameEditText.getText().toString();
        String playerScoreText = playerScoreEditText.getText().toString();

        if (!playerName.isEmpty() && !playerScoreText.isEmpty()) {
            int playerScore = Integer.parseInt(playerScoreText);

            if (newPlayerRank == 10) {
                newPlayerRank = currentRank;
            }

            addRowToLeaderboard(newPlayerRank, playerName, playerScore);

            if (newPlayerRank > 10) {
                addNewUserToLeaderboardTable1(playerName, playerScore);
            }
        }
    }

    private void addRowToLeaderboard(int rank, String name, int score) {
        TableRow row = new TableRow(this);

        String rankText = (rank != 10) ? String.format(Locale.getDefault(), "  %5d ", rank) : "  New  ";
        String nameText = String.format(Locale.getDefault(), "            %-10s", name);
        String scoreText = String.format(Locale.getDefault(), "%20d", score);

        addTextViewToRow(row, rankText);
        addTextViewToRow(row, nameText);
        addTextViewToRow(row, scoreText);

        leaderboardTable.addView(row);
    }

    private void addTextViewToRow(TableRow row, String text) {
        TextView textView = createFormattedTextView(text);
        row.addView(textView);
    }

    private void addNewUserToLeaderboardTable1(String name, int score) {
        TableLayout leaderboardTable1 = findViewById(R.id.leaderboardTable1);

        TableRow row = new TableRow(this);

        String rankText = String.format(Locale.getDefault(), "  %5d+ ", 10);
        String nameText = String.format(Locale.getDefault(), "            %-10s", name);
        String scoreText = String.format(Locale.getDefault(), "%20d", score);

        addTextViewToRow(row, rankText);
        addTextViewToRow(row, nameText);
        addTextViewToRow(row, scoreText);

        leaderboardTable1.addView(row);
    }
}
