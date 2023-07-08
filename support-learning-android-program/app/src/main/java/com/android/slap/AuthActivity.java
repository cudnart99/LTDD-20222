package com.android.slap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.FragmentLoginBinding;
import com.android.slap.event.SinhVienModelEvent;
import com.android.slap.model.FSInstance;
import com.android.slap.model.SinhVienModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthActivity
  extends AppCompatActivity
  implements SinhVienModelEvent {

  private FragmentLoginBinding binding;
  private String adminCode = "";
  private SinhVienModel sinhVienModel;
  private Dialog dialogUpdate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sinhVienModel = new SinhVienModel(this);
    sinhVienModel.getData();
    binding = FragmentLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    FSInstance.db
      .collection("admin")
      .get()
      .addOnCompleteListener(
        new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            for (DocumentSnapshot doc : task.getResult()) {
              if (doc.exists()) {
                String s = (String) doc.getData().get("code");
                if (s != null) {
                  adminCode = s;
                }
              }
            }
          }
        }
      );
    //ngon :V
    
    Button btnThay = binding.btnThay;
    btnThay.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Dialog dialogUpdate = new Dialog(view.getContext());
          dialogUpdate.setCancelable(true);
          dialogUpdate.setContentView(R.layout.input_login_layout);
          dialogUpdate.show();
          Button btn = dialogUpdate.findViewById(R.id.save);
          btn.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                EditText password = dialogUpdate.findViewById(R.id.password);
                if ((String.valueOf(password.getText())).equals(adminCode)) {
                  MainActivity.THAY = true;
                  MainActivity.USER_ID = "123456";
                  Intent myIntent = new Intent(
                    AuthActivity.this,
                    MainActivity.class
                  );
                  AuthActivity.this.startActivity(myIntent);
                } else {
                  Toast
                    .makeText(
                      view.getContext(),
                      "Code không chính xác",
                      Toast.LENGTH_LONG
                    )
                    .show();
                }
              }
            }
          );
        }
      }
    );

    Button btnSv = binding.btnSv;
    btnSv.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          dialogUpdate.show();
        }
      }
    );

    dialogUpdate = new Dialog(btnSv.getContext());
    dialogUpdate.setCancelable(true);
    dialogUpdate.setContentView(R.layout.input_login_student_layout);

    Button btn = dialogUpdate.findViewById(R.id.login);
    btn.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          EditText password = dialogUpdate.findViewById(R.id.password);
          if (
            MainActivity.MAP_STUDENT.containsKey(
              String.valueOf(password.getText())
            )
          ) {
            MainActivity.THAY = false;
            MainActivity.USER_ID = String.valueOf(password.getText());
            Intent myIntent = new Intent(AuthActivity.this, MainActivity.class);
            AuthActivity.this.startActivity(myIntent);
            return;
          }
          Toast
            .makeText(
              getWindow().getContext(),
              "MSSV không tồn tại",
              Toast.LENGTH_LONG
            )
            .show();
        }
      }
    );
    //        QuizStartActivity.myRef.child("quiz").child("0002").addListenerForSingleValueEvent(
    //                new ValueEventListener() {
    //                    @Override
    //                    public void onDataChange(@NonNull DataSnapshot snapshot) {
    //                        System.out.println("has child");
    ////                        snapshot.child("").child("pass").getValue();
    //                        for(DataSnapshot d : snapshot.getChildren()){
    //                            System.out.println("has child " + d.getKey());
    //                            FSInstance.db.collection("quiz1")
    //                                    .document(d.getKey())
    //                                    .set(new HashMap<String,Object>(){{
    //                                        put("A",d.child("A").getValue());
    //                                        put("B",d.child("B").getValue());
    //                                        put("C",d.child("C").getValue());
    //                                        put("D",d.child("D").getValue());
    //                                        put("answer",d.child("answer").getValue());
    //                                        put("question",d.child("question").getValue());
    //                                    }})
    //                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
    //                                        @Override
    //                                        public void onComplete(@NonNull Task<Void> task) {
    //
    //                                        }
    //                                    });
    //
    //                        }
    //
    //                    }
    //
    //                    @Override
    //                    public void onCancelled(@NonNull DatabaseError error) {
    //
    //                    }
    //                }
    //        );
  }

  @Override
  public void afterGetData(List<SinhVienDAO> list) {
    Map<String, SinhVienDAO> map = new HashMap<>();

    for (SinhVienDAO sv : list) {
      map.put(sv.mssv, sv);
    }
    MainActivity.MAP_STUDENT = map;
  }

  @Override
  public void afterSave() {}
}
