package com.android.slap.ui.ds_lop.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.slap.R;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.event.DiemDanhEvent;

import java.util.List;

public class SinhVienAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<SinhVienDAO> arrayList;
    private DiemDanhEvent diemDanhEvent;

    public SinhVienAdapter(Context context, int layout, List<SinhVienDAO> arrayList,DiemDanhEvent sinhVienModelEvent) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
        this.diemDanhEvent = sinhVienModelEvent;
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

        SinhVienDAO sinhVienItem = arrayList.get(i);
        TextView textName =  view.findViewById(R.id.sv_name);
        TextView textGiuaKy =  view.findViewById(R.id.giua_ky_text);
        TextView textCuoiKy =  view.findViewById(R.id.cuoi_ky_text);
        TextView textDiemDanh =  view.findViewById(R.id.diem_danh_text);
        TextView textMssv =  view.findViewById(R.id.text_mssv);
        ConstraintLayout layoutSvItem = view.findViewById(R.id.layout_sv_item);

        layoutSvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diemDanhEvent.onTouchItem(sinhVienItem);
            }
        });

        if(sinhVienItem.fullname != null){
            textName.setText(sinhVienItem.fullname);
        }else{
            textName.setText(sinhVienItem.name);
        }

        textMssv.setText(""+sinhVienItem.mssv);
        textDiemDanh.setText("Đi học: " + sinhVienItem.diemDanh + "/19");
        textGiuaKy.setText("Giữa kỳ: "+sinhVienItem.giuaKy);
        textCuoiKy.setText("Cuối kỳ: "+sinhVienItem.cuoiKy+"");

        return view;
    }
}
