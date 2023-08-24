package com.paras.setgo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.paras.setgo.Activities.CreateActivity;
import com.paras.setgo.Adapters.taskAdapter;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.dbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {
private MaterialButton createBtn,createNewBtn;
private TextView week,date;
private Calendar calendar;
private RecyclerView taskRecyclerView;
private taskAdapter adapter;
private ArrayList<TaskItemModel> taskItemData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        calendar = Calendar.getInstance();
        taskItemData= new ArrayList<>();
        init(view);
        taskRecyclerView= view.findViewById(R.id.taskRecyclerView);

        dbHelper db = new dbHelper(getActivity());
        taskItemData = db.retrieveTaskItem();
        adapter = new taskAdapter(getActivity(),taskItemData);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecyclerView.setAdapter(adapter);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreate();
            }
        });
        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreate();
            }
        });

    return  view;
    }

    public void onResume(){
        super.onResume();
        dbHelper db = new dbHelper(getActivity());
        taskItemData = db.retrieveTaskItem();
        adapter = new taskAdapter(getActivity(),taskItemData);
        taskRecyclerView.setAdapter(adapter);


        Log.i("TAG", "onResume: ");
    }

    public void openCreate(){
        Intent intent = new Intent(getActivity(), CreateActivity.class);
        startActivity(intent);
    }
    public void setDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        date.setText(formattedDate);
    }
    public void setWeek(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String currentWeekday = dayFormat.format(calendar.getTime());

        week.setText(currentWeekday);
    }
    public void init(View v){
        taskRecyclerView = v.findViewById(R.id.taskRecyclerView);
        createBtn= v.findViewById(R.id.createBtn);
        createNewBtn = v.findViewById(R.id.createNewBtn);
        week = v.findViewById(R.id.weekId);
        date = v.findViewById(R.id.dayId);
        setDate();
        setWeek();
    }

}