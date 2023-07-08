package com.android.slap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.QuizBinding;
import com.android.slap.model.FSInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuizActivity
  extends AppCompatActivity
  implements ValueEventListener {

  private QuizBinding binding;
  private static TextView questionText;
  private static TextView quesNumText;
  private static Button btn1, btn2, btn3, btn4;
  public static String keyQuiz = "0002";
  public static String keyQuestion = "0";
  public static String answer = "";
  private static List<Button> listBtn;
  private static boolean isClick = false;
  public static int size = 100;
  public static int[] color = new int[] {
    0xffff3758,
    0xff3793df,
    0xffffc108,
    0xff66be38,
  };

  void onUpdateQuestion(DataSnapshot snapshot) {
    for (int i = 0; i < 4; i++) {
      listBtn.get(i).setBackgroundColor(color[i]);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      btn1.setAlpha(1f);
      btn2.setAlpha(1f);
      btn3.setAlpha(1f);
      btn4.setAlpha(1f);
    }
    btn1.setText(
      "" +
      snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("A")
        .getValue()
    );
    btn2.setText(
      "" +
      snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("B")
        .getValue()
    );
    btn3.setText(
      "" +
      snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("C")
        .getValue()
    );
    btn4.setText(
      "" +
      snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("D")
        .getValue()
    );
    answer =
      snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("answer")
        .getValue() +
      "";
    questionText.setText(
      (String) snapshot
        .child("quiz")
        .child(keyQuiz)
        .child(keyQuestion)
        .child("question")
        .getValue()
    );
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= 19) {
      getWindow()
        .getDecorView()
        .setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
          View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
    //make fully Android Transparent Status bar
    if (Build.VERSION.SDK_INT >= 21) {
      setWindowFlag(
        this,
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        false
      );
      getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
    getWindow()
      .setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
      );
    binding = QuizBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());

    quesNumText = binding.quesNum;
    questionText = binding.questionText;
    btn1 = binding.buttonAnswer1;
    btn2 = binding.buttonAnswer2;
    btn3 = binding.buttonAnswer3;
    btn4 = binding.buttonAnswer4;
    listBtn = new ArrayList<>();
    listBtn.add(btn1);
    listBtn.add(btn2);
    listBtn.add(btn3);
    listBtn.add(btn4);
    for (int i = 0; i < 4; i++) {
      listBtn.get(i).setBackgroundColor(color[i]);
    }

    btn1.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickAnswer(1);
        }
      }
    );

    btn2.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickAnswer(2);
        }
      }
    );

    btn3.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickAnswer(3);
        }
      }
    );

    btn4.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          clickAnswer(4);
        }
      }
    );
  }

  @Override
  protected void onStart() {
    super.onStart();
    QuizStartActivity.myRef.addValueEventListener(this);
  }

  private void clickAnswer(int number) {
    if (isClick) return;
    isClick = true;
    String ans = "" + (char) ('A' + number - 1);

    FSInstance.db
      .collection("answer1")
      .document(keyQuestion)
      .get()
      .addOnCompleteListener(
        new OnCompleteListener<DocumentSnapshot>() {
          @Override
          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            Map<String, Object> s = task.getResult().getData();
            if (s == null) {
              s = new HashMap<>();
            }
            s.put(
              MainActivity.USER_ID,
              ans.equals(answer) ? "" + (100 - 2 * s.keySet().size()) : "0"
            );
            FSInstance.db.collection("answer1").document(keyQuestion).set(s);
          }
        }
      );
    for (int i = 0; i < 4; i++) {
      if (i != number - 1) {
        listBtn.get(i).setBackgroundColor(0xff888888);
      }
    }
  }

  public static void setWindowFlag(
    Activity activity,
    final int bits,
    boolean on
  ) {
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
  public void onDataChange(@NonNull DataSnapshot snapshot) {
    if (
      snapshot.child("flag").hasChild("answer") &&
      ("" + snapshot.child("flag").child("answer").getValue()).equals("true")
    ) {
      System.out.println("changing alpha ...");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        btn1.setAlpha(0.5f);
        btn2.setAlpha(0.5f);
        btn3.setAlpha(0.5f);
        btn4.setAlpha(0.5f);

        if (answer.equals("A")) {
          btn1.setAlpha(1f);
          btn1.setBackgroundColor(color[0]);
        } else if (answer.equals("B")) {
          btn2.setAlpha(1f);
          btn2.setBackgroundColor(color[1]);
        } else if (answer.equals("C")) {
          btn3.setAlpha(1f);
          btn3.setBackgroundColor(color[2]);
        } else {
          btn4.setAlpha(1f);
          btn4.setBackgroundColor(color[3]);
        }
      }
    }
    if (
      snapshot.child("flag").hasChild("current") &&
      !keyQuestion.equals(
        "" + snapshot.child("flag").child("current").getValue()
      )
    ) {
      keyQuestion =
        snapshot.child("flag").child("current").getValue().toString();
      isClick = false;
      new android.os.Handler(Looper.getMainLooper())
        .postDelayed(
          new Runnable() {
            public void run() {
              onUpdateQuestion(snapshot);
            }
          },
          200
        );

      quesNumText.setText("Câu " + keyQuestion + ":");
    }
    if (
      QuizStartActivity.isAnswer &&
      "init".equals("" + snapshot.child("flag").child("status").getValue())
    ) {
      QuizStartActivity.isAnswer = false;
      keyQuestion = "0";

      Intent intent = new Intent(this, QuizStartActivity.class); // New activity
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(intent);
      finish();
      QuizStartActivity.myRef.removeEventListener(this);
      //            if(this != null){
      //                QuizActivity.super.onBackPressed();
      //
      //            }
    }
  }

  @Override
  public void onBackPressed() {
    QuizStartActivity.myRef.removeEventListener(this);
    QuizStartActivity.isAnswer = false;
    keyQuestion = "0";
    super.onBackPressed();
  }

  @Override
  public void onCancelled(@NonNull DatabaseError error) {}
}
