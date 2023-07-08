package com.android.slap.game.GameObject;

import android.graphics.Paint;

import com.android.slap.game.Control;
import com.google.firebase.firestore.DocumentSnapshot;

public class Player extends GameObject{
    public String fullname;
    public int nhom;
    public int diem;
    public int direction;

    public Player(DocumentSnapshot doc){
        super(doc);
        paint = new Paint();
    }

    public Player(){
        paint = new Paint();
    }

    @Override
    public void update() {
        if(direction == 1){
            y = y+ Control.SPEED;
        }else if(direction == 2){
            x = x+ Control.SPEED;
        }else if(direction == 3){
            y = y- Control.SPEED;
        }else if(direction == 4){
            x = x- Control.SPEED;
        }
//        threadFirebase.updatePosition(x,y);
    }
}
