package com.android.slap.game;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.android.slap.game.GameObject.GameObject;

public class Player extends GameObject {
    public static final int PIXEL = Control.PIXEL;
    public static final int SPEED = Control.SPEED;

//    Bitmap[] image;

    public int score = 0;
    public int countDot;
    public int count = 0;
    public int animateIndex = 0, directionImage = 0;
//    private Level lv;
    public int level;
    public int live;

    protected enum ModeMove {UP, DOWN, LEFT, RIGHT, NON}

    public ModeMove mode;
    public ModeMove moving;

    Player() {
        super();
        paint.setColor(0xFFFFFFFF);
//        this.image = bitmaps;

        mode = ModeMove.NON;
        moving = ModeMove.NON;

//        lv = new Level();
        this.countDot = getcountdot();
        this.level = 1;
        this.live = 3;

        this.deltaPosition = Control.PIXEL / 2 - Control.SIZE_PACMAN / 2;
        this.x = Control.MAP_X + Control.DEFAULT_COLUMN * Control.PIXEL;
        this.y = Control.MAP_Y + Control.DEFAULT_ROW * Control.PIXEL;

    }

    public void move() {
        int xInMap = x - Control.MAP_X;
        int yInMap = y - Control.MAP_Y;

        animateIndex++;
        if (animateIndex == 5)
            animateIndex = 0;

//        if (lv.checkBorder(xInMap / PIXEL, yInMap / PIXEL)) {
//            checkBorder();
//        } else {
//            checkWay(xInMap, yInMap);
//        }
        switch (moving) {
            case LEFT:
                this.x -= SPEED;
                directionImage = 2;
                break;
            case RIGHT:
                this.x += SPEED;
                directionImage = 0;
                break;
            case UP:
                this.y -= SPEED;
                directionImage = 3;
                break;
            case DOWN:
                this.y += SPEED;
                directionImage = 1;
                break;
            case NON:
                if (animateIndex != 0) animateIndex--;
                break;
        }
    }

    public void checkWay(int xInMap, int yInMap) {

        boolean check = (xInMap % PIXEL == 0) && (yInMap % PIXEL == 0);

        int column = xInMap / PIXEL + 1;
        int row = yInMap / PIXEL;
        if (xInMap % PIXEL == 0)
            column--;

//        if (mode != moving) switch (mode) {
//            case LEFT:
//                if (lv.canRun(column - 1, row, 1) && check) moving = mode;
//                break;
//            case RIGHT:
//                if (lv.canRun(column + 1, row, 1) && check) moving = mode;
//                break;
//            case UP:
//                if (lv.canRun(column, row - 1, 1) && check) moving = mode;
//                break;
//            case DOWN:
//                if (lv.canRun(column, row + 1, 1) && check) moving = mode;
//                break;
//            case NON:
//                break;
//        }
//
//        switch (moving) {
//            case LEFT:
//                if (lv.checkMap(row, column - 1, 1) && check) moving = ModeMove.NON;
//                break;
//            case RIGHT:
//                if (lv.checkMap(row, column + 1, 1) && check) moving = ModeMove.NON;
//                break;
//            case UP:
//                if (lv.checkMap(row - 1, column, 1) && check) moving = ModeMove.NON;
//                break;
//            case DOWN:
//                if (lv.checkMap(row + 1, column, 1) && check) moving = ModeMove.NON;
//                break;
//            case NON:
//                break;
//        }

        if (moving == ModeMove.LEFT && mode == ModeMove.RIGHT) moving = ModeMove.RIGHT;
        else if (moving == ModeMove.RIGHT && mode == ModeMove.LEFT) moving = ModeMove.LEFT;
        else if (moving == ModeMove.DOWN && mode == ModeMove.UP) moving = ModeMove.UP;
        else if (moving == ModeMove.UP && mode == ModeMove.DOWN) moving = ModeMove.DOWN;
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        canvas.drawRect(400,400,450,450,paint);
//        canvas.drawBitmap(image[animateIndex + directionImage * 5], x + deltaPosition, y + deltaPosition, null);
    }

    public boolean checkdead(int x, int y) {

        if (x - PIXEL <= this.x && this.x <= x + PIXEL && y - PIXEL <= this.y && this.y <= y + PIXEL)
            this.live--;
        if (live == 0) return true;
        return false;
    }

    public int getcountdot() {
        int count = 0;
//        for (int i = 0; i < 31; i++)
//            for (int j = 0; j < 28; j++) if (lv.checkMap(i, j, 0) || lv.checkMap(i, j, 3)) count++;
        return count;
    }

    //	public void endgame() {
//		dead=true;
//	}
    private int idstream = 0;

//    public void setScore(Level lv, Sound sound) {
//        int xInMap = x - Control.MAP_X;
//        int yInMap = y - Control.MAP_Y;
//
//        boolean check = (xInMap % PIXEL == 0) && (yInMap % PIXEL == 0);
//        int column = xInMap / PIXEL;
//        int row = yInMap / PIXEL;
//        if (check)
//            if (lv.checkMap(row, column, 0)) {
//                if (idstream != 0)
//                    sound.stop(idstream);
//                idstream = sound.play(sound.eat);
//
//                score += 10;
//                this.countDot--;
//                lv.setMap(row, column, 2);
//            } else if (lv.checkMap(row, column, 3)) {
//                sound.play(sound.eat);
//                score += 10;
//                this.countDot--;
//
//                lv.setMap(row, column, 4);
//            }
//    }

    public void setposition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean getlevelup() {
        if (countDot == 0) {
            this.countDot = this.count;
            return true;
        }
        return false;
    }

    public void setModeMove(ModeMove m) {
        this.mode = m;
    }

    //	public Bitmap getPacman(boolean i,movemode j) {
//		if(i)
//			return getImage(j);
//		return oval;
//	}
    public boolean isStop() {
        if (moving == ModeMove.NON)
            return true;
        return false;
    }
}
