package com.example.a2.About;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.lang.ref.WeakReference;

/**
 * ImageView supporting gesture zoom, pan and double-click restoration
 **/
public class ZoomTranslateDoubleTapImageView extends androidx.appcompat.widget.AppCompatImageView
        implements ScaleGestureDetector.OnScaleGestureListener {

    private ScaleGestureDetector scaleGestureDetector;//Gesture zoom
    /**
     * MSCALE_X  MSKEW_X    MTRANS_X
     * MKEW_Y    MSCALE_Y   MTRANS_Y
     * MPERSP_0  MPERSP_1   MPERSP_2
     */
    private Matrix mMatrix;//Scaling matrix
    private float maxScale = 4.0f;//Maximum zoom to four times the original image
    private float minScale = 0.5f;//Minimum zoom to 0.5 times of the original image
    private float lastX = 0, lastY = 0;
    private int lastPointerCount;
    private GestureDetector gestureDetector;

    public ZoomTranslateDoubleTapImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomTranslateDoubleTapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomTranslateDoubleTapImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //Initialization parameters
    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);//Allow imageview zoom
        scaleGestureDetector = new ScaleGestureDetector(new WeakReference<Context>(context).get(),
                new WeakReference<ZoomTranslateDoubleTapImageView>(this).get());
        mMatrix = new Matrix();
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {//Double-click the picture to restore
                if (getPreScale() != 1.0f) {
                    ZoomTranslateDoubleTapImageView.this.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mMatrix.reset();
                            setImageMatrix(mMatrix);
                            makeDrawableCenter();
                        }
                    }, 16);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {//The function from OnScaleGestureListener
        if (getDrawable() == null) {
            return true;
        }
        //Get the current zoom value
        float scale = detector.getScaleFactor();
        Log.i("lihua", "scaleFactor = " + scale);
        float preScale = getPreScale();
        Log.i("lihua", "preScale = " + preScale);
        if (preScale * scale < maxScale &&
                preScale * scale > minScale) {
            //preScale * scale Can calculate the zoom value if this zoom is executed
            //detector.getFocusX() The x coordinate of the zoom gesture center，
            //detector.getFocusY() y coordinate
//            mMatrix.postScale(scale, scale, detector.getFocusX(), detector.getFocusY());
            mMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mMatrix);
            makeDrawableCenter();
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //The function from OnScaleGestureListener，Zoom start
        return true;//Must return true to have effect
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        //The function from OnScaleGestureListener，Zoom ended
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        scaleGestureDetector.onTouchEvent(event);

        if (getPreScale() > 1) {
            float x = 0, y = 0;
            final int pointerCount = event.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                x += event.getX(i);
                y += event.getY(i);
            }
            x = x / pointerCount;
            y = y / pointerCount;
            if (pointerCount != lastPointerCount) {
                lastX = x;
                lastY = y;
            }
            Log.i("lihua", "pointCount: " + pointerCount + ", lastPointCount: " + lastPointerCount);
            lastPointerCount = pointerCount;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float delX = x - lastX;
                    float delY = y - lastY;

                    RectF rectF = getMatrixRectF();
                    if ((rectF.left >= 0 && delX > 0) || (rectF.right <= getWidth() && delX < 0)) {
                        delX = 0;
                    }
                    if ((rectF.top >= 0 && delY > 0) || (rectF.bottom <= getHeight() && delY < 0)) {
                        delY = 0;
                    }

                    mMatrix.postTranslate(delX, delY);
                    setImageMatrix(mMatrix);
                    lastX = x;
                    lastY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return true;
    }

    //Get the current total zoom
    private float getPreScale() {
        float[] matrix = new float[9];
        mMatrix.getValues(matrix);
        return matrix[Matrix.MSCALE_X];
    }

    private RectF getMatrixRectF() {

        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (d != null) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            Log.i("lihua", "bitmapWidth = " + d.getIntrinsicWidth() + ", bitmapHeight = " + d.getIntrinsicHeight());
            mMatrix.mapRect(rect);
            Log.i("lihua", "matrixWidth = " + rect.width() + ", matrixHeight = " + rect.height());
            Log.i("lihua", "bmLeft: " + rect.left + " bmRight: " + rect.right + " bmTop: " + rect.top + " bmBottom: " + rect.bottom);
        }
        return rect;
    }

    //Center the picture when zooming out
    private void makeDrawableCenter() {

        RectF rect = getMatrixRectF();

        int width = getWidth();
        int height = getHeight();

        float dx = 0, dy = 0;

        // If the width or height is greater than the screen, the control range
        if (rect.width() >= width) {
            if (rect.left > 0) {
                dx = -rect.left;
            }
            if (rect.right < width) {
                dx = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                dy = -rect.top;
            }
            if (rect.bottom < height) {
                dy = height - rect.bottom;
            }
        }

        if (rect.width() <= width) {
            // The horizontal coordinate of the center point of the control
            // minus the horizontal coordinate of the center point of the picture is
            // the distance that should be moved in the X direction
            dx = width / 2 - (rect.right - rect.width() / 2);
        }
        if (rect.height() <= height) {
            dy = height / 2 - (rect.bottom - rect.height() / 2);
        }
        Log.i("lihua", "dx = " + dx + ", dy = " + dy);

        if (dx != 0 || dy != 0) {
            mMatrix.postTranslate(dx, dy);
            setImageMatrix(mMatrix);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
        scaleGestureDetector = null;
    }
}