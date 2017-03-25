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

    Timer timer;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        PictureRandom srcImg = new PictureRandom();

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(srcImg.get());

        // reset all prefs on reset (reset just counter ?)
        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        preferences.edit().clear().apply();
    }

    private void setTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
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
        timer.schedule(task, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        counter = preferences.getInt("counter", 0);

        //This will be called after onCreate or your activity is resumed
        setTimer();

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        SharedPreferences.Editor editor = preferences.edit();
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
