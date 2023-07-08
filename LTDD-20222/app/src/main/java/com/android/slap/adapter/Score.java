package com.android.slap.adapter;

import com.google.firebase.firestore.DocumentSnapshot;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class Score {
    public String mssv;
    public String name;
    public long point;
    public int key;

    public Score(String sdt,String name, long point) {
        this.name = name;
        this.mssv = sdt;
        this.point = point;
    }

    public Score(DocumentSnapshot doc) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.map(doc.getData(),this);
        if(doc.getId() != null){
            this.key = Integer.parseInt(doc.getId());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String name) {
        this.mssv = name;
    }

    public String getPoint() {
        return ""+ point;
    }

    public long getPointInt() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
