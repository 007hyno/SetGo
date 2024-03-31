package com.paras.setgo.Activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.Utilities.apm;
import com.paras.setgo.dbHelper;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
private MaterialButton saveBtn,setInc,setDec,durationInc,durationDec,restInc,restDec;
private TextView set,duration,rest;
private String name;
private TextInputLayout taskNameCont;
private TextInputEditText taskName;
private boolean isUpdate=false;
private dbHelper dbH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();

        checkIsUpdate();

        setInc.setOnClickListener(this);
        setDec.setOnClickListener(this);
//        repInc.setOnClickListener(this);
//        repDec.setOnClickListener(this);
        durationInc.setOnClickListener(this);
        durationDec.setOnClickListener(this);
        restInc.setOnClickListener(this);
        restDec.setOnClickListener(this);
        setDec.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called when the text is changing
                String inputText = charSequence.toString().trim();
                if (!inputText.isEmpty()) {
                    if(dbH.checkDuplicate(inputText)){
                        Toast.makeText(CreateActivity.this, "Can't have Duplicate Name", Toast.LENGTH_SHORT).show();
                        taskNameCont.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE); // Set the outline mode
//                        taskNameCont.setBoxBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white)); // Set background color if needed
                        taskNameCont.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.pink)); // Set outline color
                        taskNameCont.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.pink))); // Optional: Set hint text color
                    taskNameCont.setHint("Duplicate Name");
                    }else{
                        taskNameCont.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE); // Set the outline mode
                        taskNameCont.setBoxStrokeColor(ContextCompat.getColor(getApplicationContext(), R.color.blue)); // Set outline color
                        taskNameCont.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.blue))); // Optional: Set hint text color
                    taskNameCont.setHint("Task Name");

                    }
                } else {
                    // The TextInputEditText is empty
                    // You can handle this case accordingly
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This method is called after the text changes
            }
        });


    }

    private void checkIsUpdate() {
        isUpdate = getIntent().getBooleanExtra("isUpdate",false);
        if(isUpdate){
            saveBtn.setText("Update");
            Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
            name=getIntent().getStringExtra("Name");
            taskName.setText(name);
            set.setText(getIntent().getIntExtra("Sets",0)+"");
            duration.setText(getIntent().getStringExtra("TotalTime"));
            rest.setText(getIntent().getStringExtra("Rest")+"");

        }
    }


    public void init(){
        saveBtn = findViewById(R.id.save);
        setInc = findViewById(R.id.setInc);
        set = findViewById(R.id.set);
        setDec = findViewById(R.id.setDec);
//        repInc = findViewById(R.id.repInc);
//        repDec = findViewById(R.id.repDec);
//        rep = findViewById(R.id.rep);
        durationInc = findViewById(R.id.durationInc);
        duration = findViewById(R.id.duration);
        durationDec = findViewById(R.id.durationDec);
        rest = findViewById(R.id.rest);
        restInc = findViewById(R.id.restInc);
        restDec = findViewById(R.id.restDec);
        taskName= findViewById(R.id.taskName);
        taskNameCont = findViewById(R.id.taskNameCont);
        duration.setText("00:05");
        rest.setText("00:10");

         dbH = new dbHelper(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.setInc){
            if(Integer.parseInt(set.getText()+"")+5>100){
            set.setText(""+100);
            }else{
            set.setText(""+(Integer.parseInt(set.getText()+"")+1));
            }
        }
//        else if(id==R.id.repInc){
//            if(Integer.parseInt(rep.getText()+"")+5>100){
//                rep.setText(""+100);
//            }else{
//            rep.setText(""+(Integer.parseInt(rep.getText()+"")+1));
//            }
//        }
        else if(id==R.id.durationInc){
                long durInt = apm.timeStringToSeconds(duration.getText()+"")+1;
            if(durInt<100)
            duration.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.restInc){
                long restInt = apm.timeStringToSeconds(rest.getText()+"")+1;
            if(restInt<100)
            rest.setText(apm.secondsToTimeString(restInt));
        }else if(id==R.id.setDec) {
            if(Integer.parseInt(set.getText()+"")-5<1){
                set.setText(""+1);
            }else{
            set.setText("" + (Integer.parseInt(set.getText()+"") - 1));
            }
        }
//        else if(id==R.id.repDec) {
//            if(Integer.parseInt(rep.getText()+"")-5<1){
//                rep.setText(""+1);
//            }else{
//            rep.setText("" + (Integer.parseInt(rep.getText()+"") - 1));
//            }
//        }
        else if(id==R.id.durationDec) {
                long durInt = apm.timeStringToSeconds(duration.getText()+"")-1;
            if(durInt>0)
            duration.setText(apm.secondsToTimeString(durInt));
        }else if(id==R.id.restDec) {
            long restInt = apm.timeStringToSeconds(rest.getText()+"")-1;
            if(restInt>0)
            rest.setText(apm.secondsToTimeString(restInt));
        }else if(id==R.id.save) {
            if(taskName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Task name can't be Duplicate!", Toast.LENGTH_SHORT).show();
            }else if(taskNameCont.getHint().equals("Duplicate Name")){
                Toast.makeText(this, "Task name can't be empty!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(isUpdate){
                    updateData();
                }else{
                insertData();
                }
                finish();

            }
        }



    }
    public void insertData(){
        dbH.insertTaskItem(new TaskItemModel(taskName.getText()+"",""+duration.getText(),Integer.parseInt(set.getText()+""), 108, 60L, rest.getText()+"", System.currentTimeMillis()));
    }
    public void updateData(){
        dbH.updateTaskItem(name,new TaskItemModel(taskName.getText()+"",""+duration.getText(),Integer.parseInt(set.getText()+""), 108, 60L, rest.getText()+"", System.currentTimeMillis()));
    }
}
