package com.example.bouncingballs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MultiTouchView extends View {
    private SparseArray<PointF> mActivePointers = new SparseArray<>();
    ArrayList<Ball> list;
    MyThread t;

    public MultiTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        list = new ArrayList<>();
        list.add(new Ball());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // get all balls
        for(int j = 0; j < list.size(); j++){
            Ball ball = list.get(j);
            ball.paint.setColor(Color.BLUE);
            // change ball color
            for (int i = 0; i < mActivePointers.size(); i++) {
                PointF point = mActivePointers.valueAt(i);
                if (point.x < ball.posX + ball.radius && point.x > ball.posX - ball.radius
                && point.y < ball.posY + ball.radius && point.y > ball.posY - ball.radius) {
                    ball.paint.setColor(Color.RED);
                }
            }
            canvas.drawCircle(ball.posX, ball.posY, ball.radius, ball.paint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int pointerIndex = event.getActionIndex(); // get pointer index from the event object
                int pointerId = event.getPointerId(pointerIndex); // get pointer ID
                System.out.println("Pointer Index " + pointerIndex + " Pointer ID " + pointerId);
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(event.getPointerId(event.getActionIndex()));
                break;
            }
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    PointF point = mActivePointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
        }
        invalidate();
        return true;
    }


    public void AddBall(){
        if (list.size() < 3){
            list.add(new Ball());
        }
    }

    class Ball{
        int posX, posY;
        int radius;
        int des;
        Paint paint;

        Ball(){
            posX = 500;
            posY = 500;
            radius = 30;
            paint = new Paint();
            des = 1;
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while(!this.isInterrupted()) {
            try {
                // postInvalidate() will hold this thread
                // until the drawing is done!!!!
                for(int i = 0; i < list.size(); i++){
                    Ball b = list.get(i);
                    if (b.posY >= 1000) {
                        b.des = -1;
                    }
                    if (b.posY <= 100){
                        b.des = 1;
                    }
                    b.posY += 5*b.des;
                }
                MultiTouchView.this.postInvalidate();
                Thread.sleep(16); // around 60fps
            } catch (InterruptedException e) {
                return; // stop if this thread is interrupted
            }
            }
        }
    }
}
