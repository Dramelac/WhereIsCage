package com.supinfo.app.whereiscage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startStandartGame(View view){
        Intent newFrame = new Intent(this, PlayActivity.class);

        startActivity(newFrame);

    }

    public void startChronoMenu(View view){

    }

    public void startScoreboardMenu(View view){

    }

}
