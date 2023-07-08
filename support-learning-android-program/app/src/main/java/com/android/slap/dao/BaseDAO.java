package com.android.slap.dao;

import com.google.firebase.firestore.DocumentSnapshot;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class BaseDAO {
    public int x,y;
    public String name = "";

    public BaseDAO(){
        name = this.getClass().getName();
    }

    public BaseDAO(DocumentSnapshot doc){
        name = this.getClass().getName();
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.map(doc.getData(),this);
    }
}
