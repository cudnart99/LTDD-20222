package com.android.slap.ui.ai;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.Fragment;

import com.android.slap.MainActivity;
import com.android.slap.R;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.FragmentDiemdanhAiBinding;
import com.android.slap.databinding.FragmentDiemdanhBinding;
import com.android.slap.event.DiemDanhEvent;
import com.android.slap.model.DiemDanhModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class DiemDanhBangAIFragment extends Fragment {
    public static int W_BTN = 120;
    public static int H_BTN = 150;
    public static int ROW = 5;
    public static int COLUMN = 6;
    public static int HEIGHT_NAME = 60;

    private FragmentDiemdanhAiBinding binding;
    public GridView dsSv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        APIRequest api = new APIRequest();

        binding = FragmentDiemdanhAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        W_BTN = (width - 30) / COLUMN;


        // initializing variables on below line.
        Button pickImageBtn = binding.buttonPicker;
        ImageView imageIV = binding.imagePicker;
//        TextView hoTenSvText = binding.hoTenSvText;
//        dsSv = binding.gridSv;
//        dsSv.setAdapter(new SinhVienAdapter(getContext(), new GetBody(5).result));

        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            imageIV.setImageURI(result.getData().getData());

                            try{
                                api.callUpload2(getImagePathFromUri(result.getData().getData()), new AfterFetch() {
                                    @Override
                                    public void callback(UploadBody uploadBody, GetBody getBody) {

                                        api.callGet(uploadBody.result, new AfterFetch() {
                                            @Override
                                            public void callback(UploadBody uploadBody, GetBody getBody) {
                                                updateList(getBody);
//                                                hoTenSvText.setText(getBody.src);
                                            }
                                        });
                                    }
                                });
                            }catch(Exception e){
                                int x = 0;
                            }

                        } else if(result.getResultCode() == 321) {

                        }
                    }
                });
        // adding click listener for button on below line.
        pickImageBtn.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLaunch.launch(intent);
            }
        });

        return root;
    }

    private String getImagePathFromUri(Uri imageUri) {
        String imagePath = null;
        if (imageUri != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    // For Android Q and above, use ContentResolver to query the image path
                    ContentResolver contentResolver = getContext().getContentResolver();
                    Cursor cursor = contentResolver.query(imageUri, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imagePath = cursor.getString(index);
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // For versions prior to Android Q, use the Uri's getPath method
//                imagePath = imageUri.getPath();
                try {
                    // For Android Q and above, use ContentResolver to query the image path
                    ContentResolver contentResolver = getContext().getContentResolver();
                    Cursor cursor = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        cursor = contentResolver.query(imageUri, null, null, null);
                    }
                    if (cursor != null && cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        imagePath = cursor.getString(index);
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePath;
    }

    public void updateList(GetBody getBody){
        TextView textView = binding.textView6;
        String s = "";
        for(int i =0;i<getBody.result.size();i++){
            GetBody.ResultDTO e = getBody.result.get(i);
            s += e.name + "(" + e.percent +"%)\n";
        }
        textView.setText(s);
//        SinhVienAdapter sinhVienAdapter =new SinhVienAdapter(getActivity(), new GetBody(10).result);
//        //new SinhVienAdapter(getContext(),getBody.result);
//        GridView g = binding.gridSv;
//        g.setAdapter(sinhVienAdapter);
//        sinhVienAdapter.notifyDataSetChanged();
    }
}