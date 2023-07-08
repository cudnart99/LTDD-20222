package com.android.slap.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.android.slap.AuthActivity;
import com.android.slap.ChatActivity;
import com.android.slap.GameActivity;
import com.android.slap.MainActivity;
import com.android.slap.QuizCuaThayActivity;
import com.android.slap.QuizStartActivity;
import com.android.slap.R;
import com.android.slap.databinding.FragmentHomeBinding;
import com.android.slap.ui.diemdanh.DiemDanhFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        Button playGameBtn = binding.playGameBtn;
//        playGameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent;
//                if(MainActivity.THAY){
//                    myIntent = new Intent(getActivity(), QuizCuaThayActivity.class);
//                }else{
//                    myIntent = new Intent(getActivity(), QuizStartActivity.class);
//                }
//                getActivity().startActivity(myIntent);
//            }
//        });

//        Button chatBtn = binding.chatBtn;
//        chatBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent myIntent = new Intent(getActivity(), ChatActivity.class);
//                getActivity().startActivity(myIntent);
//            }
//        });

        Button diemDanhBtn = binding.diemDanhBtn;
        setupNavigate(diemDanhBtn,R.id.nav_diem_danh);
        Button dsLopBtn = binding.dsLopBtn;
        setupNavigate(dsLopBtn,R.id.nav_ds_lop);
        Button thongTinSvBtn = binding.thongTinSvBtn;
        setupNavigate(thongTinSvBtn,R.id.nav_thongtin);
//        Button leoNuiBtn = binding.leoNuiBtn;
//        setupNavigate(leoNuiBtn,R.id.nav_game_leo_nui);

        return root;
    }
    private void setupNavigate(Button b, int id){
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                navController.navigate(id);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}