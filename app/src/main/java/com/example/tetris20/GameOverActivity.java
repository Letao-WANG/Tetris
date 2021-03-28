package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    TextView textScore;
    Button buttonAgain;
    Button buttonMenu;
    Button buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent i = getIntent();
        int score = i.getIntExtra("score", 0);

        textScore = findViewById(R.id.text_score);
        buttonAgain = findViewById(R.id.button_again);
        buttonMenu = findViewById(R.id.button_menu);
        buttonExit = findViewById(R.id.button_exit);

        textScore.setText("Your score : " + score);

        buttonAgain.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
        });
        buttonMenu.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });
        buttonExit.setOnClickListener(v -> {
            finishAffinity();
        });


    }
}