package com.example.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.tetris.MESSAGE";
    private Button btnStartGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnStartGame = findViewById(R.id.buttonStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
                EditText editText = (EditText) findViewById(R.id.editViewName);
                String name = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, name);
                startActivity(intent);
            }
        });

    }

}