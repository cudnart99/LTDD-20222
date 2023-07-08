package com.android.slap.dao;

import com.google.firebase.firestore.DocumentSnapshot;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class QuizDAO {
    public int key;
    public String A,B,C,D,answer,question;

    public QuizDAO(DocumentSnapshot doc) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.map(doc.getData(),this);
        if(doc.getId() != null){
            this.key = Integer.parseInt(doc.getId());
        }
    }
}
