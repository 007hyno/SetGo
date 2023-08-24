package com.paras.setgo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paras.setgo.Activities.NotificationActivity;
import com.paras.setgo.R;

public class MeFragment extends Fragment {
private LinearLayout notification;
private TextView test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
                View view = inflater.inflate(R.layout.fragment_me, container, false);
    notification = view.findViewById(R.id.notification);
    test = view.findViewById(R.id.test);
    notification.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        }

    });
test.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        CongoFragment bottomSheetRegisterFragment = new CongoFragment();
        bottomSheetRegisterFragment.show(getActivity().getSupportFragmentManager(),"Congratulation");
    }

});

        return view;
    }
}