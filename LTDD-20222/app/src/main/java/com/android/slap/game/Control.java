package com.android.slap.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.android.slap.GameActivity;
import com.android.slap.game.GameObject.GameObject;
import com.android.slap.game.GameObject.Player;
import com.android.slap.game.thread.FirebaseThread;
import com.android.slap.game.thread.GameThread;

import java.util.ArrayList;
import java.util.List;

public class Control extends SurfaceView implements SurfaceHolder.Callback {
    public static final int DEFAULT_ROW = 23;
    public static final int DEFAULT_COLUMN = 13;

    public static int WIDTH;
    public static int HEIGHT;
    public static int PIXEL = 40;

    public static int BTN_X;
    public static int BTN_Y;
    public static int BTN_SIZE;
    public static int MAP_X = 0;
    public static int MAP_Y = 0;
    public static int MAP_W = 0;
    public static int MAP_H = 0;
    public static int SPEED = 30;

    public static int SIZE_PACMAN = 0;
    //----------------

    private GameActivity gameActivity;
    private Rect up, down, left, right;
    private GameThread thread;
    private FirebaseThread threadFirebase;
    private Paint paint;
    public List<Player> listGameObjectConnect;
    public int idPlayer = 1;

    public Control(GameActivity context) {
        super(context);
        paint = new Paint();
        gameActivity = context;
        listGameObjectConnect = new ArrayList<>();
        this.setFocusable(true);
        this.getHolder().addCallback(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WIDTH = (displayMetrics.widthPixels / 28) * 28;
        HEIGHT = (displayMetrics.heightPixels / 28) * 31;
        MAP_W = PIXEL * 28;
        MAP_H = PIXEL * 31;
        MAP_X = (WIDTH - MAP_W) / 2;
        MAP_Y = (HEIGHT - MAP_H) / 3;

        BTN_SIZE = (HEIGHT - MAP_H - 40) / 6;
        BTN_X = (WIDTH / 2 - BTN_SIZE * 3 / 2);
        BTN_Y = (MAP_Y + PIXEL * 31 + 10);
        SIZE_PACMAN = (int)(PIXEL * 4);

        up = new Rect(BTN_X + BTN_SIZE, BTN_Y, BTN_X + 2 * BTN_SIZE, BTN_Y + BTN_SIZE);
        down = new Rect(BTN_X + BTN_SIZE, BTN_Y + 2 * BTN_SIZE, BTN_X + 2 * BTN_SIZE, BTN_Y + 3 * BTN_SIZE);
        left = new Rect(BTN_X, BTN_Y + BTN_SIZE, BTN_X + BTN_SIZE, BTN_Y + 2 * BTN_SIZE);
        right = new Rect(BTN_X + 2 * BTN_SIZE, BTN_Y + BTN_SIZE, BTN_X + 3 * BTN_SIZE, BTN_Y + 2 * BTN_SIZE);
    }

    public void update(){

    }

    public void updatePerSecond(){

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setColor(0xFFFFFFFF);
        paint.setTextSize(50);
        canvas.drawText("Time playing... ", 200, 200, paint);
        for(GameObject g : listGameObjectConnect){
            g.draw(canvas);
        }
        paint.setColor(Color.rgb(100, 100, 100));
        canvas.drawRect(up, paint);
        canvas.drawRect(down, paint);
        canvas.drawRect(left, paint);
        canvas.drawRect(right, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        System.out.println("touch");
        if (true) {
//            if (endgame) {
//                init(this.getHolder());
//            }
            int x = (int) e.getX();
            int y = (int) e.getY();
            if (up.contains(x, y)) {
//                threadFirebase.updatePosition(
//                        listGameObjectConnect.get(idPlayer-1).x,
//                        listGameObjectConnect.get(idPlayer-1).y - SPEED);
//              pacman.setModeMove(Pacman.ModeMove.UP);
                return true;
            }
            if (down.contains(x, y)) {
                System.out.println("DOWN");
//                pacman.setModeMove(Pacman.ModeMove.DOWN);

//                threadFirebase.updatePosition(
//                        listGameObjectConnect.get(idPlayer-1).x,
//                        listGameObjectConnect.get(idPlayer-1).y + SPEED);
                return true;
            }
            if (left.contains(x, y)) {
//                threadFirebase.updatePosition(
//                        listGameObjectConnect.get(idPlayer-1).x - SPEED,
//                        listGameObjectConnect.get(idPlayer-1).y);
//                pacman.setModeMove(Pacman.ModeMove.LEFT);
                return true;
            }
            if (right.contains(x, y)) {
//                pacman.setModeMove(Pacman.ModeMove.RIGHT);
                threadFirebase.updatePosition(
                        listGameObjectConnect.get(idPlayer-1).x + SPEED,
                        listGameObjectConnect.get(idPlayer-1).y);
                return true;
            }
        }
        return false;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        init(holder);

        thread = new GameThread(this, holder);
        thread.setRunning(true);
        thread.start();

        threadFirebase = new FirebaseThread(this);
        threadFirebase.setRunning(true);
        threadFirebase.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(false);
        threadFirebase.setRunning(false);
    }

    public void init(SurfaceHolder holder) {
//        pacman = new Player();

    }
}
