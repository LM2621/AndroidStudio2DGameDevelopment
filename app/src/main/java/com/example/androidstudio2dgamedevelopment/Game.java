package com.example.androidstudio2dgamedevelopment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.Object.Circle;
import com.example.androidstudio2dgamedevelopment.Object.Enemy;
import com.example.androidstudio2dgamedevelopment.Object.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Game class manages all objects in the game and is responsible for updating all states and render all objects to the screen

class Game extends SurfaceView implements SurfaceHolder.Callback{
    private final Player player;
    private final Joystick joystick;
    //private final Enemy enemy;
    private GameLoop gameLoop;
    private Context context;
    private List<Enemy> enemyList = new ArrayList<Enemy>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Handle touch event actions
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double ) event.getX(), (double) event.getY())){
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double ) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
            }

        return super.onTouchEvent(event);
    }

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context=context;
        gameLoop = new GameLoop(this, surfaceHolder);

        //Initialize game objects
        joystick = new Joystick(275,750,70,40);
        player = new Player(getContext(),joystick,500,500,30);
      //  enemy = new Enemy(getContext(),player,500,200,30);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        joystick.draw(canvas);
        player.draw(canvas);
       // enemy.draw(canvas);
        for (Enemy enemy:enemyList){
            enemy.draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString((gameLoop.getAverageUPS()));
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context,R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100,100,paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString((gameLoop.getAverageFPS()));
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context,R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100,400,paint);
    }

    public void update(){
        joystick.update();
        player.update();
        //enemy.update();
        if(Enemy.readyToSpawn()){
            enemyList.add(new Enemy(getContext(),player));
        }

        for (Enemy enemy:enemyList){
            enemy.update();
        }

        Iterator<Enemy> iteratorEnemy=enemyList.iterator();
        while(iteratorEnemy.hasNext()){
            if(Circle.isColliding(iteratorEnemy.next(),player)){
                iteratorEnemy.remove();
            }
        }

    }

}
