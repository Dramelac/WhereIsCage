package com.supinfo.app.whereiscage;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import android.content.SharedPreferences;

import com.supinfo.app.whereiscage.DAL.PictureRandom;
import com.supinfo.app.whereiscage.Utils.ScaleListener;

public class PlayActivity extends AppCompatActivity {

    Timer timer;
    int counter;
    ImageView image;

    ScaleGestureDetector SGD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        PictureRandom srcImg = new PictureRandom();

        image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(srcImg.get());
        image.setOnTouchListener(t);
//        SGD = new ScaleGestureDetector(this, new ScaleListener(image));

        // reset all prefs on reset (reset just counter ?)
        SharedPreferences preferences = getSharedPreferences("testFile", 0);
        preferences.edit().clear().apply();
    }

    public static final int DRAG = 1;
    public static final int NONE = 0;
    private static final String TAG = "Touch";
    public static final int ZOOM = 2;
    public static PointF mid = new PointF();
    public static int mode = 0;
    float d = 0.0F;
    Matrix savedMatrix = new Matrix();
    Matrix matrix = new Matrix();
    PointF start = new PointF();
    float oldDist;

    View.OnTouchListener t = new View.OnTouchListener() {
        public boolean onTouch(View v,MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    Log.d(TAG, "mode=DRAG");
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:

                    oldDist = spacing(event);
                    Log.d(TAG, "oldDist=" + oldDist);
                    if (oldDist > 10f) {

                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                        Log.d(TAG, "mode=ZOOM");
                    }
                    break;

                case MotionEvent.ACTION_MOVE:

                    if (mode == DRAG) {

                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    } else if (mode == ZOOM) {

                        float newDist = spacing(event);
                        Log.d(TAG, "newDist=" + newDist);
                        if (newDist > 10f) {

                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:

                    mode = NONE;
                    Log.d(TAG, "mode=NONE");
                    break;
            }
            image.setImageMatrix(matrix);
            return true;

        }
    };

    private void midPoint(PointF point, MotionEvent event) {

        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
////        SGD.onTouchEvent(event);
//
//        switch(event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN :
//            {
//                x = event.getX();
//                y = event.getY();
//                dx = x-image.getX();
//                dy = y-image.getY();
//            }
//            break;
//            case MotionEvent.ACTION_MOVE :
//            {
//                Matrix matrix = image.getImageMatrix();
//                matrix.setScale(0.3f, 0.3f, event.getX()-dx, event.getY()-dy);
//                image.setImageMatrix(matrix);
////                image.setX(event.getX()-dx);
////                image.setY(event.getY()-dy);
//            }
//            break;
//        }
//
//        return true;
//    }

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
