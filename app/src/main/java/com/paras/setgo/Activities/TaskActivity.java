package com.paras.setgo.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.paras.setgo.Fragments.CongoFragment;
import com.paras.setgo.R;
import com.paras.setgo.Utilities.apm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class TaskActivity extends AppCompatActivity {
    private TextView clockTextView,taskName,progressText;
    private MaterialButton playPause,reset;
    private long startTime,initialTime ,iRest;
    private boolean isRunning = false;
    private Handler handler;
    private int iReps,iSets;
    private String iName,iDuration;
    private CircularProgressBar circularProgressBar;
    private Party party;
    private Shape.DrawableShape drawableShape = null;
    private KonfettiView konfettiView;
    private EmitterConfig emitterConfig;

    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        clockTextView = findViewById(R.id.clockTextView);
        taskName = findViewById(R.id.taskName);
        playPause = findViewById(R.id.pp);
        reset = findViewById(R.id.reset);
        progressText = findViewById(R.id.progress_text);
        konfettiView= findViewById(R.id.konfettiView);
        circularProgressBar = findViewById(R.id.yourCircularProgressbar);
        handler = new Handler();

        initialTime= startTime;

        valueInit();

        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add);
        drawableShape = new Shape.DrawableShape(drawable, true);
        emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(90);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime=initialTime;
                updateStopwatchText(startTime);
                pauseTimer();
                circularProgressBar.setProgress(0);
                progressText.setText(iDuration);

            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    play();
                }
                else{
                    pauseTimer();
                }
            }
        });

    }

    private void valueInit() {
        Intent intent = getIntent();
        iName = intent.getStringExtra("Name");
        iSets= intent.getIntExtra("Sets",1);
        iReps= intent.getIntExtra("Reps",2);
        iDuration= intent.getStringExtra("TotalTime");
        iRest= intent.getLongExtra("Rest",20);
//        startTime= apm.timeStringToSeconds(iDuration)*1000;
        startTime=10000;
        taskName.setText(iName);
        progressText.setText(iDuration);


    }

    public void complete(){
        Dialog dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.bottom_sheet_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // setting width to 90% of display
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.9f);

        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

        MaterialButton rate1= dialog.findViewById(R.id.rate1);
        MaterialButton rate2= dialog.findViewById(R.id.rate2);
        MaterialButton rate3= dialog.findViewById(R.id.rate3);

        rate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskActivity.this, "rate1", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }
        });
        rate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskActivity.this, "rate2", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }
        });
        rate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TaskActivity.this, "rate3", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();

            }
        });

        konfettiView.start( new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build());
        //                    CongoFragment bottomSheetRegisterFragment = new CongoFragment();
//                    bottomSheetRegisterFragment.show(getSupportFragmentManager(),"Congratulation");
    }
    public void pauseTimer(){
        isRunning = false;
        handler.removeCallbacks(updateStopwatchRunnable);
    }
    public void play(){
        isRunning = true;
        handler.post(updateStopwatchRunnable);
    }
    private final Runnable updateStopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                startTime = startTime-40;
                updateStopwatchText(startTime);
                int progress = (int) (100 * (1 - (float) startTime / initialTime));
//                int progress = (int) (100 - ((float) startTime / initialTime) * 100);
                circularProgressBar.setProgress(progress);
//                circularProgressBar.setProgress(12);

                if(startTime <1){
                    updateStopwatchText(0);
                    complete();
                    return;
                }
                handler.postDelayed(this, 40); // Update every 10 milliseconds
            }
        }
    };

    private void updateStopwatchText(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) ((timeInMillis % 60000) / 1000);
        int milliseconds = (int) (timeInMillis % 1000);

        String timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        clockTextView.setText(timeText);
        progressText.setText(timeText);

    }


    public void onBackPressed(){
        pauseTimer();
        final AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this,R.style.RoundedCornersDialog);

        View dialogView = getLayoutInflater().inflate(R.layout.custom_exit_dialog, null);

        TextView btnYes = dialogView.findViewById(R.id.btnYes);
        TextView btnNo = dialogView.findViewById(R.id.btnNo);

        AlertDialog alertDialog = builder.setView(dialogView).create();  // Create the AlertDialog here

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskActivity.this.finish();
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
//        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3f);
        alertDialog.getWindow().setAttributes(layoutParams);
    }
}