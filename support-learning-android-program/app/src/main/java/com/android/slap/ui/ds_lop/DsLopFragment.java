package com.android.slap.ui.ds_lop;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.FragmentDsLopBinding;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.model.DiemDanhModel;
import com.android.slap.event.SinhVienModelEvent;
import com.android.slap.ui.ds_lop.ui.SinhVienAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DsLopFragment extends Fragment implements DiemDanhEvent, View.OnClickListener {

    private FragmentDsLopBinding binding;
    private DiemDanhModel sinhVienModel;
    private SinhVienAdapter sinhVienAdapter;
    private GridView gridView;
    private Dialog dialogUpdate;
    private Timer timerRefresh;
    private List<SinhVienDAO> listShow = new ArrayList<>();
    private EditText searchInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sinhVienModel = new DiemDanhModel(this);
        sinhVienModel.getData();
        timerRefresh = new Timer();
        timerRefresh.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                sinhVienModel.getData();
            }
        },0,10000);

        binding = FragmentDsLopBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = binding.dsSvGrid;

        searchInput = binding.searchInput;
        Button searchBtn = binding.searchBtn;
        searchBtn.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onSuccessIn(SinhVienDAO sinhVienModel) {

    }

    @Override
    public void onSuccessOut(SinhVienDAO sinhVienModel) {

    }

    @Override
    public void afterGetData(List<SinhVienDAO> list) {
        if(String.valueOf(searchInput.getText()).equals("")){
            sinhVienAdapter = new SinhVienAdapter(getContext(), R.layout.sinh_vien_item, list,this);
            gridView.setAdapter(sinhVienAdapter);
        }
    }

    @Override
    public void afterSave() {
        sinhVienModel.getData();
        dialogUpdate.hide();
    }

    @Override
    public void onTouchItem(SinhVienDAO sv) {
        if(!MainActivity.THAY) return;
        dialogUpdate = new Dialog(getContext());
        dialogUpdate.setCancelable(true);
        dialogUpdate.setContentView(R.layout.input_point_layout);
        dialogUpdate.show();
        Button btn = dialogUpdate.findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pointGk = dialogUpdate.findViewById(R.id.point_giua_ky);
                EditText pointCk = dialogUpdate.findViewById(R.id.point_cuoi_ky);

                sv.giuaKy = Double.valueOf(String.valueOf(pointGk.getText()));
                sv.cuoiKy = Double.valueOf(String.valueOf(pointCk.getText()));

                sinhVienModel.save(sv);
            }
        });
    }

    @Override
    public void onClick(View view) {
        String s = String.valueOf(searchInput.getText());
        listShow.clear();
        for(SinhVienDAO sv : sinhVienModel.listSinhVienDAO){
            if(sv.fullname != null && sv.fullname.toLowerCase().contains(s.toLowerCase())){
                listShow.add(sv);
            }else if(sv.name != null && sv.name.toLowerCase().contains(s.toLowerCase())){
                listShow.add(sv);
            }
        }

        sinhVienAdapter = new SinhVienAdapter(getContext(), R.layout.sinh_vien_item, listShow,this);
        gridView.setAdapter(sinhVienAdapter);
    }
}