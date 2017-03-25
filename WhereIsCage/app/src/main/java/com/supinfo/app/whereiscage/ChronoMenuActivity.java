package com.supinfo.app.whereiscage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.supinfo.app.whereiscage.Utils.Gamemode;

public class ChronoMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono_menu);
    }

    public void startChrono(View view){
        Intent newFrame = new Intent(this, PlayActivity.class);
        newFrame.putExtra("gamemode", Gamemode.Chrono);
        startActivity(newFrame);
        finish();
    }

    public void startChrono2(View view){
        Intent newFrame = new Intent(this, PlayActivity.class);
        newFrame.putExtra("gamemode", Gamemode.Chrono_two);
        startActivity(newFrame);
        finish();
    }
}
