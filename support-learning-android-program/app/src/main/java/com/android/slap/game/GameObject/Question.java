package com.android.slap.game.GameObject;

import android.graphics.Paint;

import com.google.firebase.firestore.DocumentSnapshot;

public class Question extends GameObject {
    public String question, answer, A,B,C,D;

    public Question(DocumentSnapshot doc){
        super(doc);
        paint = new Paint();
    }

    public Question(){
        paint = new Paint();
    }
}
