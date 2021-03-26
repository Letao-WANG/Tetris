package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button mButtonStart;
    private Button mButtonScore;
    private Button mButtonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        mButtonStart = findViewById(R.id.button_start);
        mButtonScore = findViewById(R.id.button_score);
        mButtonQuit = findViewById(R.id.button_quit);

        mButtonStart.setOnClickListener(v -> {
            Intent intent = new Intent( WelcomeActivity.this, SelectActivity.class);
            startActivity(intent);
        });

        mButtonScore.setOnClickListener(v -> {
            Intent intent = new Intent( WelcomeActivity.this, ScoreBoardActivity.class);
            startActivity(intent);
        });

        mButtonQuit.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });

        ActivityManager.getInstance().addActivity(this);
    }
}