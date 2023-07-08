package com.android.slap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slap.R;

import java.util.List;

public class ScoreAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Score> arrayList;

    public ScoreAdapter(Context context, int layout, List<Score> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout,null);

        Score device = arrayList.get(i);
        TextView textRoomName =  view.findViewById(R.id.ho_ten_text);
        TextView textNumDevice =  view.findViewById(R.id.point_text);

        textRoomName.setText(device.getName());
        textNumDevice.setText(device.getPoint());

        return view;
    }

    public void setList(List<Score> list){
        arrayList = list;
    }
}
