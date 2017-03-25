package com.supinfo.app.whereiscage;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.supinfo.app.whereiscage.DAL.PictureRandom;
import com.supinfo.app.whereiscage.Utils.ActionType;
import com.supinfo.app.whereiscage.Utils.Gamemode;
import com.supinfo.app.whereiscage.Utils.SharedParam;

import java.util.Timer;
import java.util.TimerTask;

public class PlayActivity extends AppCompatActivity {

    Timer timer;
    int counter;
    ImageView image;

    PictureRandom srcImg;
    Gamemode gamemode;

    Matrix savedMatrix = new Matrix();
    Matrix matrix;
    PointF startPoint = new PointF();

    float oldDist;
    public PointF middlePoint = new PointF();
    public ActionType mode = ActionType.NONE;

    View.OnTouchListener t = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    startPoint.set(event.getX(), event.getY());
                    mode = ActionType.MOVE;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        float x = event.getX(0) + event.getX(1);
                        float y = event.getY(0) + event.getY(1);
                        middlePoint.set(x / 2, y / 2);
                        mode = ActionType.ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == ActionType.MOVE) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);

                    } else if (mode == ActionType.ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = ActionType.NONE;
                    break;
            }
            image.setImageMatrix(matrix);
            return true;

        }
    };

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle param = getIntent().getExtras();
        if (param != null && param.containsKey("gamemode")) {
            gamemode = (Gamemode) param.get("gamemode");
        } else {
            gamemode = Gamemode.Normal;
        }

        srcImg = new PictureRandom();

        image = (ImageView) findViewById(R.id.imageView);
        image.setOnTouchListener(t);
        applyPicture();

        SharedPreferences preferences = getSharedPreferences(SharedParam.PlayActivity, 0);
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
                        counter = gamemode == Gamemode.Normal ? ++counter : --counter;
                        TextView text = (TextView) findViewById(R.id.textView);
                        text.setText(String.valueOf(counter));
                    }
                });

            }
        };
        timer.schedule(task, 0, 1000);
    }

    private boolean applyPicture(){
        if (gamemode == Gamemode.Normal){
            image.setImageResource(srcImg.get());
        } else {
            int choice = srcImg.pop();
            if (choice == -1) return false;
            image.setImageResource(choice);
        }
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float ratio = (float) bitmapHeight / bitmapWidth;
        float w = this.getResources().getDisplayMetrics().widthPixels;
        float h = ratio * w;
        Bitmap newBitMap = Bitmap.createScaledBitmap(bitmap, (int) w, (int) h, true);
        image.setImageBitmap(newBitMap);
        float topOffset = ((this.getResources().getDisplayMetrics().heightPixels - h) / 2f) - 150;
        if (matrix == null) matrix = image.getImageMatrix();
        matrix.setTranslate(0, topOffset);
        image.setImageMatrix(matrix);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(SharedParam.PlayActivity, 0);

        int defaultValue;
        switch (gamemode) {
            case Chrono:
                defaultValue = 120;
                break;
            case Chrono_two:
                defaultValue = 300;
                break;
            default:
                defaultValue = 0;
        }
        counter = preferences.getInt("counter", defaultValue);

        setTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences(SharedParam.PlayActivity, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("counter", counter);
        editor.apply();

        timer.cancel();
    }

    public void win(View view) {

        if (gamemode == Gamemode.Chrono_two && applyPicture()){
            return;
        }

        // TODO : Add result score

        finish();
    }
}
