package com.example.myanimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawingView extends View {

    private Paint drawPaint;
    private Path drawPath;
    private Bitmap canvasBitmap;
    private Canvas drawCanvas;
    private HungerViewModel viewModel;
    private boolean isErasing = false;
    private int ColorSet;


    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPaint = new Paint();
        //Set initial draw color to black
        drawPaint.setColor(Color.BLACK);
        //Enables anti-aliasing which smooths out the edges of shapes and lines
        drawPaint.setAntiAlias(true);
        //Set stroke width (thickness)
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        //Rounded joins between line segments
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        //Set rounded end caps for drawing stroke
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //Used to store and manipulate a series of connected line segment
        drawPath = new Path();
        //Bitmap serves as the canvas on which drawing takes place
        canvasBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    // Toggle between drawing and erasing modes
    public void toggleEraser(boolean erasing) {
        isErasing = erasing;
        if (isErasing) {
            drawPaint.setColor(Color.WHITE);
        } else {
            drawPaint.setColor(ColorSet);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            //When user release touch from screen
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public Bitmap getDrawingBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(canvasBitmap.getWidth(), canvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        tempCanvas.drawBitmap(canvasBitmap, 0, 0, null);
        return bitmap;
    }

    //Get color chose by user
    public void setColor(int setColor) {
        this.ColorSet = setColor;
        drawPaint.setColor(setColor);
    }

    //Clear the canvas
    public void clear() {
        drawCanvas.drawColor(Color.WHITE);
        invalidate();
    }
}
