package com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button playButton, leaderboardButton, aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons by their IDs
        playButton = findViewById(R.id.playButton);
        leaderboardButton = findViewById(R.id.leaderboardButton);
        aboutButton = findViewById(R.id.aboutButton);

        playButton.setOnClickListener(view -> {
            // Start the PlayActivity
            Intent playIntent = new Intent(this, selectDifficulty.class);
            startActivity(playIntent);
        });

        leaderboardButton.setOnClickListener(view -> {
            // Start the LeaderboardActivity
            Intent leaderboardIntent = new Intent(this, LeaderboardActivity.class);
            startActivity(leaderboardIntent);
        });

        aboutButton.setOnClickListener(view -> {
            // Start the AboutActivity
            Intent aboutIntent = new Intent(this, about.class);
            startActivity(aboutIntent);
        });
    }
}
