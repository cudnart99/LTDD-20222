package com.android.slap.model;

import androidx.annotation.NonNull;

import com.android.slap.dao.SinhVienDAO;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.event.SinhVienModelEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiemDanhModel extends SinhVienModel{
    private DiemDanhEvent sinhVienModelEvent;

    public DiemDanhModel(SinhVienModelEvent sinhVienModelEvent){
        super(sinhVienModelEvent);
        this.sinhVienModelEvent = (DiemDanhEvent) sinhVienModelEvent;
    }

    public void checkIn(SinhVienDAO sv, int sessionId){
        handleCheck(sv, sessionId,true);
    }

    public void checkOut(SinhVienDAO sv,int sessionId){
        handleCheck(sv, sessionId,false);
    }

    public SinhVienDAO getByKey(int key){
        for(SinhVienDAO sv : listSinhVienDAO){
            if(sv.key == key) return sv;
        }
        SinhVienDAO sv =new SinhVienDAO();
        sv.key = key;
        return sv;
    }

    private void handleCheck(SinhVienDAO sv, int sessionId, boolean status){
        System.out.println("onCheck " + status);
        sv.checks.put(""+sessionId, status);

        FSInstance.db.collection("student")
                .document(sv.key+"")
                .update("checks",sv.checks)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(status)
                            sinhVienModelEvent.onSuccessIn(sv);
                        else
                            sinhVienModelEvent.onSuccessOut(sv);
                    }
                });
    }

}
