package com.example.jogo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Explosion {
    Bitmap explosion[] = new Bitmap[4];
    int explosionFrame = 0;
    int explosionX, explosionY;

    public Explosion(Context context){
        explosion[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosao);
        explosion[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosao1);
        explosion[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosao2);
        explosion[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosao3);
    }

    public Bitmap getExplosion(int explosionFrame){
        return explosion[explosionFrame];

    }
}
