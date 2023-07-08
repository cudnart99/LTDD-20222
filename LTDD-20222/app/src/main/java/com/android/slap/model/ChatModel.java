package com.android.slap.model;

import androidx.annotation.NonNull;

import com.android.slap.MainActivity;
import com.android.slap.dao.ChatDAO;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.event.ChatModelEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatModel {
    private ChatModelEvent chatModelEvent;
    public List<ChatDAO> listChatDAO = new ArrayList<>();

    public ChatModel(ChatModelEvent chatModelEvent){
        this.chatModelEvent = chatModelEvent;
    }
    public void getData(){
        FSInstance.db.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ChatDAO> sinhVienDTOs = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        ChatDAO studentModel = new ChatDAO(doc);
                        sinhVienDTOs.add(studentModel);
                    }
                }
                boolean newData = listChatDAO.size() != sinhVienDTOs.size();
                if(newData) {
                    Collections.sort(sinhVienDTOs, new Comparator<ChatDAO>() {
                        @Override
                        public int compare(ChatDAO lhs, ChatDAO rhs) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                            return lhs.timestamp > rhs.timestamp ? -1 : (lhs.timestamp < rhs.timestamp) ? 1 : 0;
                        }
                    });
                    listChatDAO = sinhVienDTOs;
                }
                chatModelEvent.afterGetData(listChatDAO,newData);
            }
        });
    }

    public void sendMessage(String content){
        ChatDAO chatDAO = new ChatDAO();
        chatDAO.content = content;
        chatDAO.userId = MainActivity.USER_ID;
        chatDAO.timestamp = System.currentTimeMillis();

        FSInstance.db.collection("chat")
                .document(""+chatDAO.timestamp)
                .set(chatDAO)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        chatModelEvent.afterSend();
                    }
                });
    }
}
