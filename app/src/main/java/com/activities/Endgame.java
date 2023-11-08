package com.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
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

        submitScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
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

    private void loadLeaderboard() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("leaderboard", null, null, null, null, null, "score DESC");
        leaderboardTable.removeAllViews(); // Clear the existing rows

        while (cursor.moveToNext()) {
            int rank = cursor.getPosition() + 1; // Rank is based on the cursor position
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));


            addRowToLeaderboard(rank, name, score);
        }

        cursor.close();
    }

    private void addRowToLeaderboard(int rank, String name, int score) {
        TableRow row = new TableRow(this);


        TextView rankTextView = createFormattedTextView(String.format("  %5d ", rank));
        TextView nameTextView = createFormattedTextView(String.format("            %-10s", name));
        TextView scoreTextView = createFormattedTextView(String.format("%20d", score));
        row.addView(rankTextView);
        row.addView(nameTextView);
        row.addView(scoreTextView);

        leaderboardTable.addView(row);
    }

    private TextView createFormattedTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
}
