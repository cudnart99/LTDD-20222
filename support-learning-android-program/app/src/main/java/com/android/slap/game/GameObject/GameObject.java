package com.android.slap.game.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.android.slap.dao.BaseDAO;
import com.android.slap.game.Control;
import com.google.firebase.firestore.DocumentSnapshot;

public class GameObject extends BaseDAO {
    public int deltaPosition;
    public Paint paint;


    public GameObject(DocumentSnapshot doc){
        super(doc);
        paint = new Paint();
    }

    public GameObject(){
        paint = new Paint();
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        paint.setColor(0xFFFFFFFF);
        canvas.drawRect(x,y,x+ Control.SIZE_PACMAN,y+Control.SIZE_PACMAN,paint);
    }

    public void checkBorder() {
//        if (this.x < Control.MAP_X - Control.PIXEL) this.x = Control.MAP_X + 28 * Control.PIXEL;
//        else if (this.x > Control.MAP_X + 28 * Control.PIXEL)
//            this.x = Control.MAP_X - Control.PIXEL;

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
