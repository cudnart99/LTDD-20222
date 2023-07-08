package com.android.slap.ui.thongTinSv;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.FragmentDiemdanhBinding;
import com.android.slap.databinding.FragmentThongtinsvBinding;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.model.DiemDanhModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ThongTinSvFragment extends Fragment implements DiemDanhEvent {
    public static int W_BTN = 120;
    public static int H_BTN = 150;
    public static int ROW = 5;
    public static int COLUMN = 6;
    public static int HEIGHT_NAME = 60;

    private FragmentThongtinsvBinding binding;
    private List<Boolean> booleans;
    private List<TextView> textViews;
    private List<Button> buttonList;
    private List<ImageView> imageViews;
    private int sessionId = 2;

    private Timer timerRefresh;
    private DiemDanhModel sinhVienModel;
    private Dialog dialogUpdate;

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
        },0,1000);

        binding = FragmentThongtinsvBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        booleans = new ArrayList<>();
        textViews = new ArrayList<>();
        imageViews = new ArrayList<>();
        buttonList = new ArrayList<>();

        for (int i = 0;i< ROW * COLUMN;i++){
            booleans.add(Boolean.FALSE);
        }

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        W_BTN = (width - 30) / COLUMN;


        for (int i = 0;i<ROW*COLUMN;i++){
            ImageView doremonImage = new ImageView(binding.container.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                doremonImage.setZ(10);
            }
            doremonImage.setImageResource(R.drawable.doraemon_walk);

            binding.container.addView(doremonImage);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) doremonImage.getLayoutParams();
            params.topToTop = binding.constraintLayoutDiagram.getId();
            params.leftToLeft = binding.container.getId();
            params.topMargin = -150;

            doremonImage.setLayoutParams(params);
            doremonImage.getLayoutParams().width = W_BTN;
            doremonImage.getLayoutParams().height = W_BTN;

            imageViews.add(doremonImage);
        }

        for(int j=0;j<ROW;j++){
            for(int i=0;i<COLUMN;i++){
                Button btn = new Button(binding.constraintLayoutDiagram.getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn.setZ(1);
                }
                btn.setBackgroundColor(0xff999999);
                binding.constraintLayoutDiagram.addView(btn);

                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) btn.getLayoutParams();
                params.topMargin = j*(H_BTN + HEIGHT_NAME) + 55;
                params.topToTop = binding.constraintLayoutDiagram.getId();
                params.width = W_BTN;
                params.height = H_BTN;

                TextView name = new TextView(binding.constraintLayoutDiagram.getContext());
                name.setTextColor(0xffaaaaaa);
                name.setTextSize(16);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                binding.constraintLayoutDiagram.addView(name);
                textViews.add(name);

                ConstraintLayout.LayoutParams paramName = (ConstraintLayout.LayoutParams) name.getLayoutParams();
                paramName.topMargin = j*(H_BTN + HEIGHT_NAME);
                paramName.topToTop = binding.constraintLayoutDiagram.getId();
                paramName.width = W_BTN;

                if(i<COLUMN/2){
                    params.leftMargin = i*(W_BTN + 2);
                    params.leftToLeft = binding.constraintLayoutDiagram.getId();

                    paramName.leftMargin = i*(W_BTN + 2);
                    paramName.leftToLeft = binding.constraintLayoutDiagram.getId();
                }else{
                    params.rightMargin = (COLUMN-1-i)*W_BTN + 5*(COLUMN-1-i);
                    params.rightToRight = binding.constraintLayoutDiagram.getId();

                    paramName.rightMargin = (COLUMN-1-i)*W_BTN + 5*(COLUMN-1-i);
                    paramName.rightToRight = binding.constraintLayoutDiagram.getId();
                }
                btn.setLayoutParams(params);
                buttonList.add(btn);
                name.setLayoutParams(paramName);
                int finalJ = j;
                int finalI = i;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(MainActivity.THAY){
                            showDialog(finalI,finalJ);
                        }
                    }
                });

            }
        }

        return root;
    }

    public SinhVienDAO getStudent(int id){
        for(SinhVienDAO s : sinhVienModel.listSinhVienDAO){
            if(s.key.equals(id)){
                return s;
            }
        }
        return new SinhVienDAO();
    }

    public void showDialog(int finalI,int finalJ){
        dialogUpdate = new Dialog(getContext());
        dialogUpdate.setCancelable(true);
        dialogUpdate.setContentView(R.layout.input_info_layout);
        dialogUpdate.show();

        SinhVienDAO dao = sinhVienModel.getByKey(finalJ*COLUMN + finalI);
        EditText fullname = dialogUpdate.findViewById(R.id.fullname);
        EditText name = dialogUpdate.findViewById(R.id.name);
        EditText mssv = dialogUpdate.findViewById(R.id.mssv);
        fullname.setText(dao.fullname);
        name.setText(dao.name);
        mssv.setText(dao.mssv);

        Button btn = dialogUpdate.findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText fullname = dialogUpdate.findViewById(R.id.fullname);
//                EditText name = dialogUpdate.findViewById(R.id.name);
//                EditText mssv = dialogUpdate.findViewById(R.id.mssv);

                SinhVienDAO dao = sinhVienModel.getByKey(finalJ*COLUMN + finalI);
                if(!String.valueOf(fullname.getText()).equals("")){
                    dao.fullname = String.valueOf(fullname.getText());
                }
                if(!String.valueOf(name.getText()).equals("")){
                    dao.name = String.valueOf(name.getText());
                }
                if(!String.valueOf(mssv.getText()).equals("")){
                    dao.mssv = String.valueOf(mssv.getText());
                }

                sinhVienModel.save(dao);
            }
        });
    }


    public void renderIn(int finalI, int finalJ){
        booleans.set(finalJ*COLUMN + finalI, Boolean.TRUE);
        ImageView selectImage = imageViews.get(finalJ*COLUMN + finalI);
        Button selectBtn = buttonList.get(finalJ*COLUMN + finalI);
        selectBtn.setBackgroundColor(0xff3cb043);

        SpringAnimation springAnimX = new SpringAnimation(selectImage, DynamicAnimation.TRANSLATION_X,0);
        SpringAnimation springAnimY = new SpringAnimation(selectImage, DynamicAnimation.TRANSLATION_Y,0);
        springAnimX.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springAnimX.getSpring().setFinalPosition((float)(finalI *W_BTN + 30 + (finalI < 3 ? -30 : 0)));
        springAnimX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);

        springAnimY.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springAnimY.getSpring().setFinalPosition((float)(finalJ *(H_BTN + HEIGHT_NAME) + 200));
        springAnimY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);

        springAnimX.start();
        springAnimY.start();
    }

    public void renderOut(int finalI,int finalJ){
        booleans.set(finalJ*COLUMN + finalI, Boolean.FALSE);
        ImageView selectImage = imageViews.get(finalJ*COLUMN + finalI);
        Button selectBtn = buttonList.get(finalJ*COLUMN + finalI);
        selectBtn.setBackgroundColor(0xffD30000);

        SpringAnimation springAnimX = new SpringAnimation(selectImage, DynamicAnimation.TRANSLATION_X,0);
        SpringAnimation springAnimY = new SpringAnimation(selectImage, DynamicAnimation.TRANSLATION_Y,0);
        springAnimX.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springAnimX.getSpring().setFinalPosition(0);
        springAnimX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);

        springAnimY.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springAnimY.getSpring().setFinalPosition(0);
        springAnimY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);

        springAnimX.start();
        springAnimY.start();
    }

    @Override
    public void onSuccessIn(SinhVienDAO studentModel) {
        int key = Integer.valueOf(studentModel.key);
        renderIn(key%COLUMN,key/COLUMN);
    }

    @Override
    public void onSuccessOut(SinhVienDAO studentModel) {
        int key = Integer.valueOf(studentModel.key);
        renderOut(key%COLUMN,key/COLUMN);
    }

    @Override
    public void afterGetData(List<SinhVienDAO> list) {
        for (SinhVienDAO entity : list) {
                TextView text = textViews.get(entity.key);
                text.setText(entity.name);
                text.setTextColor(0xff000000);
                booleans.set(entity.key,Boolean.TRUE);
                renderIn(entity.key%COLUMN,entity.key/COLUMN);
        }
    }

    @Override
    public void afterSave() {
        dialogUpdate.hide();
        sinhVienModel.getData();
        Toast.makeText(getContext(),"Cập nhật thông tin thành công",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTouchItem(SinhVienDAO key) {

    }
}