package com.paras.setgo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseLongArray;

import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.Utilities.apm;
import com.paras.setgo.dbHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Boolean isfirstTime = apm.getFirstTime(this);
//        if(isfirstTime){
//            dbHelper dbH = new dbHelper(this);
//            dbH.insertTaskItem(new TaskItemModel("Pushups","02:21",3, 15, 60L, 30L, System.currentTimeMillis()));
//
//        }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1500);

    }
}