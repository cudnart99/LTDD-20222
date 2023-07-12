package com.android.slap;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.slap.dao.ChatDAO;
import com.android.slap.databinding.FragmentChatBinding;
import com.android.slap.event.ChatModelEvent;
import com.android.slap.model.ChatModel;
import com.android.slap.ui.chat.ChatAdapter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity implements ChatModelEvent {

  private FragmentChatBinding fragmentChatBinding;
  private ChatAdapter chatAdapter;
  private GridView gridView;
  private ChatModel chatModel;
  private Timer timerRefresh;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    this.getWindow()
      .setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
      );
    fragmentChatBinding = FragmentChatBinding.inflate(getLayoutInflater());
    setContentView(fragmentChatBinding.getRoot());

    gridView = fragmentChatBinding.areaChating;
    gridView.setRotation(180);
    chatModel = new ChatModel(this);
    chatModel.getData();

    Button sendBtn = fragmentChatBinding.sendBtn;
    sendBtn.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          chatModel.sendMessage(
            fragmentChatBinding.sendInput.getText().toString()
          );
        }
      }
    );
    timerRefresh = new Timer();
    timerRefresh.scheduleAtFixedRate(
      new TimerTask() {
        @Override
        public void run() {
          chatModel.getData();
        }
      },
      0,
      500
    );
  }
  @Override
  public void afterGetData(List<ChatDAO> list, boolean newData) {
    if (newData) {
      chatAdapter = new ChatAdapter(getBaseContext(), list, this);
      gridView.setAdapter(chatAdapter);
    }
  }

  @Override
  public void afterSend() {
    fragmentChatBinding.sendInput.setText("");
  }
}
