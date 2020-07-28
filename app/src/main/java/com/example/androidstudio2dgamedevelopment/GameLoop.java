package com.example.androidstudio2dgamedevelopment;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread{
    private Game game;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private double AverageUPS;
    private double AverageFPS;
    public static final double MAX_UPS=60;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public double getAverageUPS() {
        return AverageUPS;
    }

    public  double getAverageFPS() {
        return AverageFPS;
    }

    @Override
    public void run() {
        super.run();
        Canvas canvas = null;

        int updateCount = 0;
        int frameCount = 0;

        long elapsedTime;
        long startTime;
        long sleepTime;

        startTime = System.currentTimeMillis();
        while(isRunning){

        //Try to update and render game
        try {
            canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                game.update();
                updateCount++;

                game.draw(canvas);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    frameCount++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Pause game loop to not exceed target UPS
        elapsedTime = System.currentTimeMillis() - startTime;
        sleepTime= (long) (updateCount*UPS_PERIOD-elapsedTime);
        if(sleepTime>0){
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //Skip frames to keep up with target UPS
            while(sleepTime < 0 && updateCount<MAX_UPS-1){
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime= (long) (updateCount*UPS_PERIOD-elapsedTime);
            }

        //Calculate average UPS and FPS
        elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime >= 1000) {
            AverageUPS = updateCount / (1E-3 * elapsedTime);
            AverageFPS = frameCount / (1E-3 * elapsedTime);
            updateCount=0;
            frameCount=0;
            startTime=System.currentTimeMillis();
        }

    }
    }

    public void startLoop() {
        isRunning=true;
        start();
    }
}
