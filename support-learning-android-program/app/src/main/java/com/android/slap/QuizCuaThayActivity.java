package com.android.slap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.slap.adapter.Score;
import com.android.slap.adapter.ScoreAdapter;
import com.android.slap.dao.QuizDAO;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.ActivityQuizCuaThayTuanBinding;
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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class QuizCuaThayActivity extends AppCompatActivity implements ValueEventListener, SinhVienModelEvent {
    public static boolean isAnswer = false;
    private ActivityQuizCuaThayTuanBinding binding;
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
    public static int current = 0;
    public TextView numText,questText,correctText, ansText;
    public static String dapAn = "Đáp án", questStr = "Câu hỏi";
//    public List<QuizDAO> quizDAOS = new ArrayList<>();
    public static int correctNum = 0,numAnsInt = 0,trueColor = 0xffffffff;


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
        setContentView(R.layout.activity_quiz_cua_thay_tuan);
        myRef.child("flag").addValueEventListener(this);

        arrayList = new ArrayList<>();
        listViewRank = findViewById(R.id.list_rank);

        adapter = new ScoreAdapter(getApplicationContext(),R.layout.score_card, arrayList);
        listViewRank.setAdapter(adapter);

        sinhVienModel = new SinhVienModel(this);
        sinhVienModel.getData();
        loadQuiz();
        loadAnswer();

        numText = findViewById(R.id.number_ans);
        questText = findViewById(R.id.question_play_text);
        correctText = findViewById(R.id.correct_text);
        ansText = findViewById(R.id.answer_text);
        Button buttonPlay = findViewById(R.id.btn_start);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(current == QuizActivity.size){
                    buttonPlay.setText("Finish");
                    myRef.child("flag").child("status").setValue("finish");
                    return;
                }else if(current == 0){
                    myRef.child("flag").child("status").setValue("start");
                }else{
                    myRef.child("flag").child("status").setValue("playing");
                }

                next();
                questStr = listQuiz.get(current-1).question;
                dapAn = listQuiz.get(current-1).answer;
                correctNum = 0;
                trueColor = 0xffffffff;

                questText.setText(questStr);
                ansText.setText("Đáp án");
                ansText.setBackgroundColor(trueColor);
                correctText.setText("Đúng");
                myRef.child("flag").child("current").setValue(current);
                myRef.child("flag").child("answer").setValue("0");
            }
        });

        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                buttonPlay.setText("Start");
                myRef.child("flag").child("status").setValue("init");
                myRef.child("flag").child("current").setValue(0);
                myRef.child("flag").child("answer").setValue("0");
                myRef.child("answers").removeValue();

            }
        });

        Button ansBtn = findViewById(R.id.answer_btn);
        ansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dapAn.equals("Đáp án")){
                   return;
                }
                trueColor = QuizActivity.color[dapAn.charAt(0) - 'A'];

                correctText.setText(correctNum+"");
                ansText.setText(dapAn);
                ansText.setBackgroundColor(trueColor);
                myRef.child("flag").child("answer").setValue("true");
            }
        });

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
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if((""+snapshot.child("status").getValue()).equals("start")){
            current = Integer.parseInt(snapshot.child("current").getValue()+"");

        }

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
        },0,500);
    }

    @Override
    public void afterSave() {

    }


    public void loadQuiz(){
        FSInstance.db.collection("quiz1").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int count = 0;
                        for(DocumentSnapshot s : task.getResult()){
                            count++;
                            listQuiz.add(new QuizDAO(s));
                        }
                        QuizActivity.size = count;
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
                        numAnsInt = 0;
                        correctNum = 0;

                        for (DocumentSnapshot d : task.getResult()){
                            listAnswer.add(d.getData());
                            if(d.getId().equals(current+"")){
                                numText.setText("Trả lời: "+d.getData().size());

                                for(SinhVienDAO sv : sinhVienModel.listSinhVienDAO){
                                    if(d.getData().get(sv.mssv) != null && !(""+d.getData().get(sv.mssv)).equals("0")){
                                        correctNum++;
                                    }
                                }
                            }
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


    public void next(){
        current = current + 1;
    }

    public void reset(){
        current = 0;
        numText.setText("Trả lời: 0");
        questText.setText("Câu hỏi?");
        ansText.setText("Đáp án");
        ansText.setBackgroundColor(0xffffffff);
        correctText.setText("Đúng");
    }
}