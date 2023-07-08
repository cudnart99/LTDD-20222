package com.android.slap.ui.ai;

import java.util.ArrayList;

public class GetBody {
    public class ResultDTO {
        public String name = "A";
        public String percent = "B";
    }

    public ArrayList<ResultDTO> result;
    public String src;

    public GetBody(){

    }
    public GetBody(int x){
        result = new ArrayList<>();
        for(int i=0;i<x;i++){
            result.add(new ResultDTO());
        }
    }
}
