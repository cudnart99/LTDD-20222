package com.android.slap.ui.game.climbing;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.FragmentClimbingGameBinding;
import com.android.slap.databinding.FragmentDiemdanhBinding;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.model.DiemDanhModel;
import com.android.slap.event.SinhVienModelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ClimbingGameFragment extends Fragment implements DiemDanhEvent {
    public static int W_BTN = 120;
    public static int H_BTN = 150;
    public static int ROW = 5;
    public static int COLUMN = 6;
    public static int HEIGHT_NAME = 10;
    public static int[][] map = new int[][]{
            {14,15,16,17,18,19},
            {13,-1,-1,-1,-1,-1,},
            {12,11,10,9,8,7},
            {-1,-1,-1,-1,-1,6},
            {0,1,2,3,4,5},
    };

    private FragmentClimbingGameBinding binding;
    private List<Boolean> booleans;
    private List<TextView> textViews;
    private List<Button> buttonList;
    private Map<Integer,Button> mapButton;
    private List<ImageView> imageViews;
    private int sessionId = 2;

    private Timer timerRefresh;
    private DiemDanhModel sinhVienModel;
    private Dialog dialogUpdate;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

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

        binding = FragmentClimbingGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        booleans = new ArrayList<>();
        textViews = new ArrayList<>();
        imageViews = new ArrayList<>();
        buttonList = new ArrayList<>();
        mapButton = new HashMap<>();

        for (int i = 0;i< ROW * COLUMN;i++){
            booleans.add(Boolean.FALSE);
        }

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        W_BTN = (width - 30) / COLUMN;


        for (int i = 0;i<ROW*COLUMN;i++){
            ImageView doremonImage = new ImageView(binding.container.getContext());
            doremonImage.setZ(10);
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
                if(map[j][i] != -1){
                    Button btn = new Button(binding.constraintLayoutDiagram.getContext());
                    btn.setZ(1);
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
                    name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
                    mapButton.put(map[j][i],btn);
                    name.setLayoutParams(paramName);
                    int finalJ = j;
                    int finalI = i;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(MainActivity.THAY){
                                if(textViews.get(finalJ*COLUMN + finalI).getText() != null && !textViews.get(finalJ*COLUMN + finalI).getText().equals("")){
                                    if(booleans.get(finalJ*COLUMN + finalI).equals(Boolean.TRUE)){
                                        sinhVienModel.checkOut(getStudent(finalJ*COLUMN + finalI),sessionId);
                                    }else{
                                        sinhVienModel.checkIn(getStudent(finalJ*COLUMN + finalI),sessionId);
                                    }
                                }
                            }else{
//                            showDialog(finalI,finalJ);
                            }

                        }
                    });
                }


            }
        }


        String[] s = new String[20];
        for (int i=0;i<20;i++){
            s[i] = "Buổi " + (i+1);
        }
        ArrayAdapter<String> data = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, s);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            boolean rotate = false;

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                System.out.println("shaking ...");
                ImageView selectImage = binding.diceImage;

                int i = Math.round((float)Math.random() * 5);

                if(i == 0){
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_1);
                } else if(i == 1){
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_2);
                } else if(i == 2){
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_3);
                } else if(i == 3){
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_4);
                } else if(i == 4){
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_5);
                } else {
                    selectImage.setBackgroundResource(R.drawable.dice_sprite_6);
                }



                RotateAnimation rotate = new RotateAnimation(0, 10*360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setInterpolator(new LinearInterpolator());

                selectImage.startAnimation(rotate);


//                springAnim.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
//                springAnim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
//
//                springAnim.setStartValue(0);
//                springAnim.setStartVelocity(10000);
//                springAnim.start();
//                selectImage.setAnimation(new Animation() {
//                });
//                selectImage.getAnimation().setDuration(6000);
//                selectImage.getAnimation().start();


                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(500); // for 500 ms
                }

            }
        });
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

    public Button getIndexBtn(Integer idx){
        return mapButton.get(idx);
    }


    public void renderIn(int finalI, int finalJ){
        booleans.set(finalJ*COLUMN + finalI, Boolean.TRUE);
        ImageView selectImage = imageViews.get(finalJ*COLUMN + finalI);
        Button selectBtn = getIndexBtn(map[finalJ][finalI]);
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
        Button selectBtn = buttonList.get(map[finalJ][finalI]);
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
        renderIn(0,4);
//        for (SinhVienDAO entity : list) {
//            TextView text = textViews.get(entity.key);
//            text.setText(entity.name);
//            text.setTextColor(0xff000000);
//
//            if(entity.isCheckIn(sessionId)){
//                booleans.set(entity.key,Boolean.TRUE);
//                renderIn(entity.key%COLUMN,entity.key/COLUMN);
//            }else{
//                booleans.set(entity.key,Boolean.FALSE);
//                renderOut(entity.key%COLUMN,entity.key/COLUMN);
//            }
//        }
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
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}