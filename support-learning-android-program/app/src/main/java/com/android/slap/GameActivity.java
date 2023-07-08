package com.android.slap;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.slap.game.Control;

public class GameActivity extends AppCompatActivity {

  public static int WIDTH = 720;
  public static int HEIGHT = 1280;
  public Control control;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    WIDTH = displayMetrics.widthPixels;
    HEIGHT = displayMetrics.heightPixels;
    this.getWindow()
      .setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
      );

    control = new Control(this);
    setContentView(control);
    System.out.println("game playing ...");
  }
}
