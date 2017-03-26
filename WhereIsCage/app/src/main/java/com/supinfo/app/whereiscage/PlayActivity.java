package com.supinfo.app.whereiscage;

import java.io.Console;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.supinfo.app.whereiscage.DAL.PictureRandom;
import com.supinfo.app.whereiscage.DAL.ScoreDAO;
import com.supinfo.app.whereiscage.Utils.ActionType;
import com.supinfo.app.whereiscage.Utils.Gamemode;
import com.supinfo.app.whereiscage.Utils.SharedParam;

import java.util.Objects;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.out;

public class PlayActivity extends AppCompatActivity {

    Timer timer;
    int counter;
    ImageView image;
    int imageId;
    boolean isOver = false;

    PictureRandom srcImg;
    Gamemode gamemode;

    Matrix savedMatrix = new Matrix();
    Matrix matrix;
    PointF startPoint = new PointF();

    int cageX= 10;
    int cageY = 10;
    float oldDist;
    public PointF middlePoint = new PointF();
    public ActionType mode = ActionType.NONE;
    int foundCount = 0;

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
                    float[] values = new float[9];
                    matrix.getValues(values);

// values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
// values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

// event is the touch event for MotionEvent.ACTION_UP
                    int relativeX = Math.round((event.getX() - values[2]) / values[0]);
                    int relativeY = Math.round((event.getY() - values[5]) / values[4]);
                    Log.v("Position", Integer.toString(relativeX).concat(" , ").concat(Integer.toString(relativeY)));

                    if(relativeX >= cageX && relativeX <= cageX+30)
                    {
                        if(relativeY >= cageY && relativeY <= cageY+30)
                        {
                            win(null);
                        }
                    }

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

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter");
            imageId = savedInstanceState.getInt("imageId");
            foundCount = savedInstanceState.getInt("foundCount");
            isOver = savedInstanceState.getBoolean("isOver");
        }

        Bundle param = getIntent().getExtras();
        if (param != null && param.containsKey("gamemode")) {
            gamemode = (Gamemode) param.get("gamemode");
        } else {
            gamemode = Gamemode.Normal;
        }

        srcImg = new PictureRandom();

        image = (ImageView) findViewById(R.id.imageView);
        image.setOnTouchListener(t);
        if (imageId == 0 || imageId == -1){
            applyPicture();
        } else {
            applyPicture(imageId);
        }

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
                        if (counter == 0) {
                            if (gamemode == Gamemode.Chrono_two && foundCount > 0) {
                                win(null);
                            } else {
                                loose();
                            }
                        }
                    }
                });

            }
        };
        timer.schedule(task, 0, 1000);
    }

    private boolean applyPicture() {
        if (gamemode == Gamemode.Normal) {
            imageId = srcImg.get();
        } else {
            imageId = srcImg.pop();
            if (imageId == -1) return false;
        }
        return applyPicture(imageId);
    }

    private boolean applyPicture(int id) {
        image.setImageResource(id);

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
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("counter", counter);
        bundle.putInt("imageId", imageId);
        bundle.putInt("foundCount", foundCount);
        bundle.putBoolean("isOver", isOver);
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
        if (counter == 0) {
            counter = preferences.getInt("counter", defaultValue);
        }

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
        if (!isOver) foundCount++;
        if (gamemode == Gamemode.Chrono_two && counter > 0 && applyPicture()) {
            return;
        }
        timer.cancel();
        isOver = true;

        final int score;

        switch (gamemode) {
            case Normal:
                score = counter;
                break;
            case Chrono:
                score = 120 - counter;
                break;
            case Chrono_two:
                score = foundCount;
                break;
            default:
                score = 0;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Well done ! Score: " + String.valueOf(score));
        final EditText input = new EditText(this);
        input.setHint("Enter a nickname");
        input.setMaxLines(1);
        input.setInputType(1);
        builder.setView(input);
        builder.setPositiveButton("Save score", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = input.getText().toString();
                if (Objects.equals(username, "")) {
                    username = "anonymous";
                }
                ScoreDAO.createScore(username, score, gamemode.value());
                finish();
            }
        });

        builder.show();

    }

    public void loose() {
        timer.cancel();
        Log.i("WhereIsCage", "Loose");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You Loose !");
        builder.setPositiveButton("Back to main menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
