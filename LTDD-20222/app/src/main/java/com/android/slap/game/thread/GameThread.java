package com.android.slap.game.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.android.slap.game.Control;

public class GameThread extends Thread {
    public static final int FPS = 30;

    //-----------
    public int SLEEP = 1000 / FPS;

    private boolean running;
    private final Control gameSurface;
    private final SurfaceHolder surfaceHolder;
    public boolean delay = false;

    public GameThread(Control gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        long start = 0;
        long waiting = 0;
        int count = 0;
        while (running) {
            Canvas canvas = null;
            start = System.currentTimeMillis();
//            System.out.println("thread game running---");
            try {
                // Get Canvas from Holder and lock it.
                canvas = this.surfaceHolder.lockCanvas();
                // Synchronized
                synchronized (canvas) {
                    this.gameSurface.update();

                    this.gameSurface.draw(canvas);

                    count++;
                    if (count == FPS) {
                        gameSurface.updatePerSecond();
                        count = 0;
                    }
                }
            } catch (Exception e) {
                // Do nothing.
            } finally {
                if (canvas != null) {
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.currentTimeMillis();
            waiting = SLEEP - (now - start);
            System.out.println("Time: " + (now - start));

            if (waiting < 0) {
                waiting = 1;
            }
//            System.out.println(waiting + "_ waiting");
            try {
                if (delay) {
                    sleep(800);
                    delay = false;
                } else
                    sleep(waiting);
            } catch (InterruptedException e) {
            }
        }
    }

    public void delay(){
        delay = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}