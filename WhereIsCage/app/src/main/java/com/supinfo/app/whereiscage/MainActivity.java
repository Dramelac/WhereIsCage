package com.supinfo.app.whereiscage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.supinfo.app.whereiscage.Utils.Gamemode;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startStandartGame(View view){
        Intent newFrame = new Intent(this, PlayActivity.class);
        newFrame.putExtra("gamemode", Gamemode.Normal);
        startActivity(newFrame);
    }

    public void startChronoMenu(View view){
        Intent newFrame = new Intent(this, ChronoMenuActivity.class);
        startActivity(newFrame);
    }

    public void startScoreboardMenu(View view){
        Intent newFrame = new Intent(this, ScoreboardActivity.class);
        startActivity(newFrame);
    }

}
