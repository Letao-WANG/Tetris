package com.example.tetris20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;


public class ScoreBoardActivity extends AppCompatActivity {
    ScoreAdapter scoreAdapter;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreAdapter = new ScoreAdapter(this);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        Set<String> setScore = settings.getStringSet("setScore", new HashSet<String>());

        for(String s : setScore) {
            scoreAdapter.listRecord.add(new Record(s.substring(0, s.length()-3), s.substring(s.length()-3)));
        }

        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(scoreAdapter);


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(ScoreBoardActivity.this, MainActivity.class);
        startActivity(intent);
    }
}