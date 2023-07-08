package com.android.slap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.android.slap.adapter.Score;
import com.android.slap.adapter.ScoreAdapter;
import com.android.slap.dao.QuizDAO;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.ActivityQuizStartBinding;
import com.android.slap.event.SinhVienModelEvent;
import com.android.slap.model.FSInstance;
import com.android.slap.model.SinhVienModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class QuizStartActivity extends AppCompatActivity implements ValueEventListener, SinhVienModelEvent {
    public static boolean isAnswer = false;
    private ActivityQuizStartBinding binding;
    FirebaseFirestore firestore;
    public static DatabaseReference myRef = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl("https://slap-43b88-default-rtdb.firebaseio.com/");;
    private SinhVienModel sinhVienModel;
    public List<QuizDAO> listQuiz = new ArrayList<>();
    public List<Score> listScore = new ArrayList<>();
    public List<Map<String,Object>> listAnswer = new ArrayList<>();
    public boolean loadQuiz = false, loadAnswer = false, loadStudent = false;

    ListView listViewRank;
    ScoreAdapter adapter;
    ArrayList<Score> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_quiz_start);

        arrayList = new ArrayList<>();
        listViewRank = findViewById(R.id.list_rank);

        adapter = new ScoreAdapter(getApplicationContext(),R.layout.score_card, arrayList);
        listViewRank.setAdapter(adapter);

        sinhVienModel = new SinhVienModel(this);
        sinhVienModel.getData();
        loadQuiz();
        loadAnswer();
        System.out.println("onCreate =========");
    }
    void onChangeData(){
        ArrayList<Score> arrayList = new ArrayList<>();

        for(SinhVienDAO d : sinhVienModel.listSinhVienDAO){
            arrayList.add(new Score(d.mssv,d.fullname,d.diemQuiz));
        }
        this.arrayList = arrayList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.arrayList.sort((a,b) -> (int) (b.getPointInt() - a.getPointInt()));
        }

        adapter.setList(arrayList);
        adapter.notifyDataSetChanged();
//        listViewRank
//        adapter = new ScoreAdapter(getApplicationContext(), R.layout.score_card, arrayList);
//        listViewRank.setAdapter(adapter);
    }


    void onChangeDataScore(DataSnapshot snapshot){
        for(Score s : this.arrayList){
            s.setPoint(0);
            for(DataSnapshot d : snapshot.getChildren()){
                if(d.child(s.getMssv()).getValue() != null && d.child(s.getMssv()).child("point").getValue() != null){
                    s.setPoint(s.getPointInt() + (Long)d.child(s.getMssv()).child("point").getValue() );
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            arrayList.sort((a,b) -> (int) (b.getPointInt() - a.getPointInt()));
        }
        adapter.setList(arrayList);
        adapter.notifyDataSetChanged();
//        if(binding.getRoot().getContext() != null && binding.getRoot().getContext() != null){
//            adapter = new ScoreAdapter(binding.getRoot().getContext(), R.layout.score_card, arrayList);
//            listViewRank.setAdapter(adapter);
//        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(!MainActivity.THAY && !isAnswer
                && snapshot.hasChild("status")
                && Objects.equals((String) snapshot.child("status").getValue(), "start")){
            Intent intent = new Intent(this, QuizActivity.class);// New activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.out.println("active ==========");
            myRef.child("flag").removeEventListener(this);

            isAnswer = true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("flag").addValueEventListener(this);
        isAnswer = false;

        System.out.println("onStart =========");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void afterGetData(List<SinhVienDAO> list) {
        loadStudent = true;
        loaded();
        Timer timerRefresh = new Timer();
        timerRefresh.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                loadAnswer();
            }
        },0,5000);
    }

    @Override
    public void afterSave() {

    }

    @Override
    public void onBackPressed() {
        myRef.child("flag").removeEventListener(this);
        isAnswer = false;
        super.onBackPressed();
    }

    public void loadQuiz(){
        FSInstance.db.collection("quiz1").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot s : task.getResult()){
                            listQuiz.add(new QuizDAO(s));
                        }
                        loadQuiz = true;
                        loaded();
                    }
                }
        );
    }

    public void loadAnswer(){
        FSInstance.db.collection("answer1").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listAnswer = new ArrayList<>();
                        for (DocumentSnapshot d : task.getResult()){
                            listAnswer.add(d.getData());
//                            listScore.add(new Score(d));
                        }
                        loadAnswer = true;
                        loaded();
                    }
                }
        );
    }

    public void loaded(){
        if(loadStudent && loadAnswer){
            if(listAnswer != null){
                for (SinhVienDAO sv : sinhVienModel.listSinhVienDAO){
                    int sum = 0;
                    for (Map<String,Object> q : listAnswer){
                        Object d = q.get(sv.mssv);
                        if(d != null){
                            sum += Integer.parseInt(d + "");
                        }
                    }
                    sv.diemQuiz = sum;
                }
                onChangeData();
            }
        }

    }
}