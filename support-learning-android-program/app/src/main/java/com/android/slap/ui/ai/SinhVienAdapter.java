package com.android.slap.ui.ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.ChatDAO;
import com.android.slap.event.ChatModelEvent;

import java.util.ArrayList;
import java.util.List;

public class SinhVienAdapter extends BaseAdapter {

    private Context context;
    private List<GetBody.ResultDTO> arrayList;

    public SinhVienAdapter(Context context, List<GetBody.ResultDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_sv_diem_danh,null);

        GetBody.ResultDTO sinhVien = arrayList.get(i);

        TextView contentText =  view.findViewById(R.id.name_sv);

        contentText.setText(sinhVien.name + " (" + sinhVien.percent + "%)");

        return view;
    }
}