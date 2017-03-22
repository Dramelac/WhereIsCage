package com.supinfo.app.whereiscage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.supinfo.app.whereiscage.DAL.PictureRandom;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        PictureRandom srcImg = new PictureRandom();

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(srcImg.get());
    }

    public void surrender(View view) {
        finish();
    }
}
