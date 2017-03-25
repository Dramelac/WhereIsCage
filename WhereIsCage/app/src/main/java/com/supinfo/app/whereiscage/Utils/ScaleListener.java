package com.supinfo.app.whereiscage.Utils;

import android.graphics.Matrix;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;


public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private Float scale;
    private Matrix matrix;
    private ImageView image;

    public ScaleListener(ImageView image) {
        super();
        this.scale = 1f;
        this.matrix = new Matrix();
        this.image = image;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector){
        scale = scale * detector.getScaleFactor();
        scale = Math.max(0.1f, Math.min(scale, 5f));
        matrix.setScale(scale, scale);
        image.setImageMatrix(matrix);
        return true;
    }
}
