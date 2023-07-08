package com.android.slap.model;

import androidx.annotation.NonNull;

import com.android.slap.dao.SinhVienDAO;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.event.SinhVienModelEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SinhVienModel {
    private SinhVienModelEvent sinhVienModelEvent;
    public List<SinhVienDAO> listSinhVienDAO = new ArrayList<>();

    public SinhVienModel(SinhVienModelEvent sinhVienModelEvent){
        this.sinhVienModelEvent = sinhVienModelEvent;
    }

    public void getData(){
        FSInstance.db.collection("student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<SinhVienDAO> sinhVienDTOs = new ArrayList<>();
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        SinhVienDAO studentModel = new SinhVienDAO(doc);
                        sinhVienDTOs.add(studentModel);
                    }
                }
                listSinhVienDAO = sinhVienDTOs;
                sinhVienModelEvent.afterGetData(sinhVienDTOs);
            }
        });
    }

    public void save(SinhVienDAO sv){
        FSInstance.db.collection("student")
                .document(""+sv.key)
                .set(sv)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sinhVienModelEvent.afterSave();
                    }
                });
    }
}
