package com.example.jogo1;

import android.hardware.Sensor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    private final AccelerometerListener accelerometerListener;

    private final float FILTER_FACTOR = 0.1f;
    private float x, y, z;
    Bitmap background, ground, tesla;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    public float teslaX, teslaY;
    float oldx;
    float oldTeslaX;
    ArrayList<raio> spikes;
    ArrayList<Explosion> explosions;

    public GameView(Context context) {
        super(context);
        this.context = context;
        accelerometerListener = new AccelerometerListener(this);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        tesla = BitmapFactory.decodeResource(getResources(), R.drawable.tesla);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        // posição do personagem
        teslaX = dWidth / 2 - tesla.getWidth() / 2;
        teslaY = 1070;
        spikes = new ArrayList<>();
        explosions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            raio spike = new raio(context);
            spikes.add(spike);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(tesla, teslaX, teslaY, null);
        for (int i = 0; i < spikes.size(); i++) {
            canvas.drawBitmap(spikes.get(i).getSpike(spikes.get(i).spikeFrame), spikes.get(i).spikeX, spikes.get(i).spikeY, null);
            spikes.get(i).spikeFrame++;
            if (spikes.get(i).spikeFrame > 2) {
                spikes.get(i).spikeFrame = 0;
            }
            spikes.get(i).spikeY += spikes.get(i).spikeVelocity;
            if (spikes.get(i).spikeY + spikes.get(i).getSpikeHeight() >= dHeight + 100 - ground.getHeight()) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = spikes.get(i).spikeX;
                explosion.explosionY = spikes.get(i).spikeY;
                explosions.add(explosion);
                spikes.get(i).resetPosition();
            }
        }
        //colisão
        for (int i=0; i < spikes.size(); i++){
            if (spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= teslaX
            && spikes.get(i).spikeX <= teslaX + tesla.getWidth()
            && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() >= teslaY
            && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() <= teslaY + tesla.getHeight()){
                life--;
                spikes.get(i).resetPosition();
                if (life == 0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("pontos", points);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }

        for (int i = 0; i < spikes.size(); i++) {
            if (spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= teslaX
                    && spikes.get(i).spikeX <= teslaX + tesla.getWidth()
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeHeight() >= teslaY
                    && spikes.get(i).spikeY <= teslaY + tesla.getHeight()) {
                life--;
                spikes.get(i).resetPosition();
                if (life == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("pontos", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3) {
                explosions.remove(i);
            }
        }

        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

// mover
public void updatePosition(float x) {
    // Realiza o cálculo da nova posição do boneco tesla com base na leitra do accelerometro

    // Ajusta a sensibilidade ou velocidade com que o boneco irá se mover com base na inclinação do celular
    float movementFactor = 10.0f;
    float shift = x * movementFactor;


    // Calcula a posição no eixo X do personagem
    float newTeslaX = teslaX + shift;

    // Garante que o boneco não irá sair dos limites da tela sendo 0 a posição a esquerda
    // e o valor retornado por tesla.getWidth() o valor máximo da tela (depende do celular)
    if (newTeslaX <= 0)
        teslaX = 0;
    else if (newTeslaX >= dWidth - tesla.getWidth())
        teslaX = dWidth - tesla.getWidth();
    else
        teslaX = newTeslaX;
}
}