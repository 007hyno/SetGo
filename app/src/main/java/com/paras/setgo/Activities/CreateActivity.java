package com.paras.setgo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.Utilities.apm;
import com.paras.setgo.dbHelper;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
private MaterialButton saveBtn,setInc,setDec,repInc,repDec,durationInc,durationDec,restInc,restDec;
private TextView set,rep,duration,rest;
private TextInputEditText taskName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        init();
        setInc.setOnClickListener(this);
        setDec.setOnClickListener(this);
        repInc.setOnClickListener(this);
        repDec.setOnClickListener(this);
        durationInc.setOnClickListener(this);
        durationDec.setOnClickListener(this);
        restInc.setOnClickListener(this);
        restDec.setOnClickListener(this);
        setDec.setOnClickListener(this);
        saveBtn.setOnClickListener(this);





    }

    public void Inc(int i){
        if(i==1){
            set.setText(""+(Integer.parseInt(set.getText()+"")+5));
        }else if(i==2){
            rep.setText(""+(Integer.parseInt(rep.getText()+"")+5));

        }else if(i==3){
            duration.setText(""+(Integer.parseInt(duration.getText()+"")+5));
        }else{
            rest.setText(""+(Integer.parseInt(rest.getText()+"")+5));

        }
    }

    public void init(){
        saveBtn = findViewById(R.id.save);
        setInc = findViewById(R.id.setInc);
        set = findViewById(R.id.set);
        setDec = findViewById(R.id.setDec);
        repInc = findViewById(R.id.repInc);
        repDec = findViewById(R.id.repDec);
        rep = findViewById(R.id.rep);
        durationInc = findViewById(R.id.durationInc);
        duration = findViewById(R.id.duration);
        durationDec = findViewById(R.id.durationDec);
        rest = findViewById(R.id.rest);
        restInc = findViewById(R.id.restInc);
        restDec = findViewById(R.id.restDec);
        taskName= findViewById(R.id.taskName);


        duration.setText("00:05");
        rest.setText("00:10");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.setInc){
            if(Integer.parseInt(set.getText()+"")+5>100){
            set.setText(""+100);
            }else{
            set.setText(""+(Integer.parseInt(set.getText()+"")+5));
            }
        }else if(id==R.id.repInc){
            if(Integer.parseInt(rep.getText()+"")+5>100){
                rep.setText(""+100);
            }else{
            rep.setText(""+(Integer.parseInt(rep.getText()+"")+5));
            }
        }else if(id==R.id.durationInc){
                long durInt = apm.timeStringToSeconds(duration.getText()+"")+5;
            if(durInt<100)
            duration.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.restInc){
                long durInt = apm.timeStringToSeconds(rest.getText()+"")+5;
            if(durInt<100)
            rest.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.setDec) {
            if(Integer.parseInt(set.getText()+"")-5<1){
                set.setText(""+1);
            }else{
            set.setText("" + (Integer.parseInt(set.getText()+"") - 5));
            }
        }else if(id==R.id.repDec) {
            if(Integer.parseInt(rep.getText()+"")-5<1){
                rep.setText(""+1);
            }else{
            rep.setText("" + (Integer.parseInt(rep.getText()+"") - 5));
            }
        }else if(id==R.id.durationDec) {
                long durInt = apm.timeStringToSeconds(duration.getText()+"")-5;
            if(durInt>0)
            duration.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.restDec) {
            long durInt = apm.timeStringToSeconds(rest.getText()+"")-5;
            if(durInt>0)
            rest.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.save) {
            if(!taskName.getText().toString().isEmpty()) {
                insertData();
                finish();
            }
            else{
                Toast.makeText(this, "Task name can't be empty!", Toast.LENGTH_SHORT).show();
            }
        }



    }
    public void insertData(){
        dbHelper dbH = new dbHelper(this);
        dbH.insertTaskItem(new TaskItemModel(taskName.getText()+"",""+duration.getText(),Integer.parseInt(set.getText()+""), 15, 60L, apm.timeStringToSeconds(rest.getText()+""), System.currentTimeMillis()));

    }
}
