package com.example.androidstudio2dgamedevelopment.Object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgamedevelopment.GameLoop;
import com.example.androidstudio2dgamedevelopment.Joystick;
import com.example.androidstudio2dgamedevelopment.R;

public class Player extends Circle{
    public static final double SPEED_PIXELS_PER_SECOND = 400;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius){
    //Görs denna för att player är en child class till GameObject, så ifall en player skapas
    //så måste ett GameObjects skapas och ett constructorn till GameObject vill ha två floats.
    //Nästan, tydligen när vi initialiserar Player så kallar den på parent klassens constructor,
    //men parent klassen har ingen constructor utan argument så vi måste skriva kommandot nedan så
    // den kallar på rätt constructor.
    super(context, ContextCompat.getColor(context, R.color.player), positionX,positionY, radius);
    this.joystick=joystick;
    }



    public void update() {
        velocityX=joystick.getActuatorX()*MAX_SPEED;
        velocityY=joystick.getActuatorY()*MAX_SPEED;
        positionX+=velocityX;
        positionY+=velocityY;
    }

    public void setPosition(double x, double y) {
    this.positionX=x;
    this.positionY=y;
    }


}
