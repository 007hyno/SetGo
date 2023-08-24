package com.paras.setgo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.paras.setgo.Fragments.HistoryFragment;
import com.paras.setgo.Fragments.HomeFragment;
import com.paras.setgo.Fragments.MeFragment;
import com.paras.setgo.R;

public class MainActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    bottomNavigationView = findViewById(R.id.bottomNav);

    bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.home){
            setFrag(new HomeFragment(),true);
        }else if(id==R.id.history){
            setFrag(new HistoryFragment(),true);
        }else{
            setFrag(new MeFragment(),true);
        }
        return true;
    }
});
setFrag(new HomeFragment(),false);
    }
    public void setFrag(Fragment f, Boolean b){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        if(b)
            ft.replace(R.id.container,f);
        else
            ft.add(R.id.container,f);
        ft.commit();
    }
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.RoundedCornersDialog);

        View dialogView = getLayoutInflater().inflate(R.layout.main_exit_dialog, null);

        TextView btnYes = dialogView.findViewById(R.id.btnYes);
        TextView btnNo = dialogView.findViewById(R.id.btnNo);

        AlertDialog alertDialog = builder.setView(dialogView).create();  // Create the AlertDialog here

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                alertDialog.dismiss();
                MainActivity.this.finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();  // Dismiss the AlertDialog using the variable

            }
        });

        alertDialog.show();  // Show the AlertDialog here


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

        // setting width to 90% of display
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.8f);

        // setting height to 90% of display
//        layoutParams.height = (int) (displayMetrics.heightPixels * 0.4f);
        alertDialog.getWindow().setAttributes(layoutParams);

    }

}