package com.supinfo.app.whereiscage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.w_cage1);
    }

    public void surrender(View view) {
        finish();
    }
}
