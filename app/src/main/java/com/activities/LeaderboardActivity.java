package com.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardActivity extends AppCompatActivity {
    private TableLayout leaderboardTable;
    private SQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        leaderboardTable = findViewById(R.id.leaderboardTable);
        dbHelper = new LeaderboardDbHelper(this);

        // Load and display the leaderboard
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the "leaderboard" table, order by score in descending order
        Cursor cursor = db.query("leaderboard", null, null, null, null, null, "score DESC");

        leaderboardTable.removeAllViews(); // Clear the existing rows

        int rank = 1;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int score = cursor.getInt(cursor.getColumnIndex("score"));

            // Create a new row and add it to the TableLayout with formatted data
            addRowToLeaderboard(rank, name, score);
            rank++; // Move to the next rank
        }

        cursor.close();
    }

    private void addRowToLeaderboard(int rank, String name, int score) {
        TableRow row = new TableRow(this);

        // Create TextViews for rank, name, and score with specific formatting
        TextView rankTextView = createFormattedTextView(String.format("  %5d ", rank)); // Two digits for rank
        TextView nameTextView = createFormattedTextView(String.format("            %-10s", name)); // Left-align, padded to 20 characters
        TextView scoreTextView = createFormattedTextView(String.format("%20d", score)); // Right-align, padded to 10 characters

        // Add TextViews to the TableRow
        row.addView(rankTextView);
        row.addView(nameTextView);
        row.addView(scoreTextView);

        // Add the TableRow to the TableLayout
        leaderboardTable.addView(row);
    }

    private TextView createFormattedTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }
}
