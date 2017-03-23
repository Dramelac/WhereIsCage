package com.supinfo.app.whereiscage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import android.content.SharedPreferences;

import com.supinfo.app.whereiscage.DAL.PictureRandom;

public class PlayActivity extends AppCompatActivity {

    //Global Variables
    Timer timer;
    TimerTask task;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        PictureRandom srcImg = new PictureRandom();

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(srcImg.get());

        //Make a method to make it cleaner
        setTimer();
    }

    private void setTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter++;

                        TextView text = (TextView) findViewById(R.id.textView);
                        text.setText("Temps: " + String.valueOf(counter));
                    }
                });

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        counter = preferences.getInt("counter", 0);

        //This will be called after onCreate or your activity is resumed
        timer.schedule(task, 0, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putInt("counter", counter);
        editor.apply();

        //This will be called if the app is sent to background or the phone is locked
        //Also this prevent you from duplicating the instance of your timer
        timer.cancel();
    }


    public void surrender(View view) {
        finish();
    }
}
