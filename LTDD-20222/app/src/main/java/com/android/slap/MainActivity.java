package com.android.slap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.android.slap.dao.SinhVienDAO;
import com.android.slap.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity
  extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {

  private AppBarConfiguration mAppBarConfiguration;
  private ActivityMainBinding binding;
  public static boolean THAY = false;
  public static String USER_ID = "123456";
  public static Map<String, SinhVienDAO> MAP_STUDENT = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    //        setSupportActionBar(binding.appBarMain.toolbar);
    //        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
    //                        .setAction("Action", null).show();
    //            }
    //        });
    //        DrawerLayout drawer = binding.drawerLayout;
    //        NavigationView navigationView = binding.navView;
    //        navigationView.setNavigationItemSelectedListener(this);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    //        mAppBarConfiguration = new AppBarConfiguration.Builder(
    //                R.id.nav_home,
    //                R.id.nav_diem_danh,
    //                R.id.nav_slideshow,
    //                R.id.nav_game,
    //                R.id.nav_thongtin)
    //                .setOpenableLayout(drawer)
    //                .build();
    //        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

    //        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
    //        NavigationUI.setupWithNavController(navigationView, navController);
    //        navController.getGraph()

    //        Button logoutBtn = drawer.findViewById(R.id.btn_logout);
    //        logoutBtn.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View view) {
    //                Intent myIntent = new Intent(MainActivity.this, AuthActivity.class);
    //                MainActivity.this.startActivity(myIntent);
    //            }
    //        });
  }

  //    @Override
  //    public boolean onCreateOptionsMenu(Menu menu) {
  //        // Inflate the menu; this adds items to the action bar if it is present.
  //        getMenuInflater().inflate(R.menu.main, menu);
  //        return true;
  //    }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(
      this,
      R.id.nav_host_fragment_content_main
    );
    return (
      NavigationUI.navigateUp(navController, mAppBarConfiguration) ||
      super.onSupportNavigateUp()
    );
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int i = item.getItemId();
    return false;
  }
}
