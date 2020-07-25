package com.example.androidstudio2dgamedevelopment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Circle extends GameObject {

    protected double radius;
    protected Paint paint;

    public Circle(Context context, double radius,int color,double positionX, double positionY) {
        super(positionX, positionY);
        this.radius=radius;
        paint = new Paint();
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle( (float) positionX, (float) positionY, (float) radius, paint);
    }

    @Override
    public void update() {

    }
}
