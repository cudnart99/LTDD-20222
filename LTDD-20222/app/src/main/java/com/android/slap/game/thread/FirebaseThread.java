package com.android.slap.game.thread;

import androidx.annotation.NonNull;

import com.android.slap.game.Control;
import com.android.slap.game.GameObject.GameObject;
import com.android.slap.game.GameObject.Player;
import com.android.slap.model.FSInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseThread extends Thread {
    public static final int FPS = 5;

    //-----------
    public int SLEEP = 1000 / FPS;

    private boolean running;
    private final Control gameSurface;
    public boolean delay = false;

    public FirebaseThread(Control gameSurface) {
        this.gameSurface = gameSurface;
    }

    public void update(){
        FSInstance.db.collection("game").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Player> gameObjects = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        Player studentModel = new Player(doc);
                        gameObjects.add(studentModel);
                    }
                }
                gameSurface.listGameObjectConnect = gameObjects;
//                System.out.println("thread firebase running--- " + gameSurface.listGameObjectConnect.size());
            }
        });
    }

    public void updatePosition(int x,int y){
        Map<String,Object> map = new HashMap<>();
        map.put("x",x);
        map.put("y",y);
        FSInstance.db.collection("game")
                .document(gameSurface.idPlayer+"")
                .update(map);
    }

    @Override
    public void run() {
        while (running) {
            try {
                update();
            } catch (Exception e) {
                // Do nothing.
            }
            try {
                sleep(SLEEP);
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