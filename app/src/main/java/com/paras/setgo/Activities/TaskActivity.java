package com.paras.setgo.Activities;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.paras.setgo.Fragments.CongoFragment;
import com.paras.setgo.Models.TaskItemModel;
import com.paras.setgo.R;
import com.paras.setgo.Utilities.apm;
import com.paras.setgo.dbHelper;

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
    private TextView clockTextView,taskName,progressText,progressText2;
    private MaterialButton playPause,reset;
    private long restTime,startTime,initialTime ,progIncValSet,progIncValRest,progBarVal=0,clockSpeed,st,ir;
    private boolean isRunning = false,isTimerState = false,isSound=true,isVibration=true;
    private Handler handler;
    private int iReps,iSets,totalSets;
    private String iName,iDuration,iRest;
    private CircularProgressBar circularProgressBar;
    private Party party;
    private Shape.DrawableShape drawableShape = null;
    private KonfettiView konfettiView;

    private EmitterConfig emitterConfig;
    private ImageButton backIcon,soundButton;

    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        clockTextView = findViewById(R.id.clockTextView);
        taskName = findViewById(R.id.taskName);
        backIcon = findViewById(R.id.backIcon);

        playPause = findViewById(R.id.pp);
        reset = findViewById(R.id.reset);
        progressText = findViewById(R.id.progress_text);
        progressText2 = findViewById(R.id.progress_text2);
        konfettiView= findViewById(R.id.konfettiView);
        circularProgressBar = findViewById(R.id.yourCircularProgressbar);
        soundButton = findViewById(R.id.sound_setting);
        handler = new Handler();

        valueInit();

        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add);
        drawableShape = new Shape.DrawableShape(drawable, true);
        emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(90);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueInit();
                startTime=initialTime;
                updateStopwatchText(startTime);
                progressText.setText(iDuration);
                pauseTimer();
                circularProgressBar.setProgress(0);
                progBarVal=0;
            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    play();
                    playPause.setText("Pause");
                    playPause.setIconResource(R.drawable.ic_pause);
                }
                else{
                    pauseTimer();
                    playPause.setText("Play");
                    playPause.setIconResource(R.drawable.ic_play);
                }
            }
        });

    }

    private void valueInit() {
        Intent intent = getIntent();
        iName = intent.getStringExtra("Name");
        iSets= intent.getIntExtra("Sets",1);
        iReps= intent.getIntExtra("Reps",2);
        totalSets = iSets;
        iDuration= intent.getStringExtra("TotalTime");
        iRest= intent.getStringExtra("Rest");

        Log.e(TAG, "name: "+getIntent().getStringExtra("Name") );
        Log.e(TAG, "sets: "+getIntent().getIntExtra("Sets",0) );
        Log.e(TAG, "totaltime: "+getIntent().getStringExtra("TotalTime") );
        Log.e(TAG, "rest: "+getIntent().getStringExtra("Rest") );

        startTime= apm.timeStringToSeconds(iDuration)*1000;
        restTime= apm.timeStringToSeconds(iRest)*1000;

        initialTime= startTime;

        progIncValSet = 100/(startTime/1000);
        progIncValRest= 100/(restTime/1000);
        clockSpeed=1000;
        Log.e("TAG", "Reps : Sets "+iReps+" : "+iSets +" : "+iRest);
        taskName.setText(iName);
        progressText.setText(iDuration);
        playPause.setText("Play");
        playPause.setIconResource(R.drawable.ic_play);

        progressText2.setText((totalSets-iSets)+"/"+totalSets);

        st=startTime;
        ir=restTime;

    }

    public void complete(){
        playPause.performClick();
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
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.95f);

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

    public void Timer(long totalTime,long time){
        updateStopwatchText(time);
        progBarVal=   (int) (((float) (totalTime-time) / totalTime) * 100);
        circularProgressBar.setProgress(progBarVal);
        if(time<1){
            updateStopwatchText(0);
            progBarVal=0;
            isTimerState=!isTimerState;
        }
    }

    private final Runnable updateStopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                Log.e("Fun", "SET : "+iSets+"|| st  : "+st+"|| ir  : "+ir+"|| isTimer : "+isTimerState+"|| progressbarval  : "+progBarVal  );
                if(iSets>0){
                    progressText2.setText((totalSets-iSets)+1+"/"+totalSets);
                if(!isTimerState){
                    workoutAudio();
                    ir=restTime;
                    st-=clockSpeed;
                    circularProgressBar.setProgressBarColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    clockTextView.setText("SET");
                Timer(startTime,st);

                }else{
                    restAudio();
                    if(iSets!=1){
                    st=startTime;
                    ir-=clockSpeed;
                    clockTextView.setText("REST");
                    circularProgressBar.setProgressBarColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
                    Timer(restTime,ir);
                }else {
                        ir = 0;
                    }
                    if(ir<1)
                        iSets--;
                }
                }else{
                    audio("complete");
                    complete();
                }

                handler.postDelayed(this, clockSpeed); // Update every 10 milliseconds
            }
        }
    };

    public void workoutAudio(){
        if(iSets==1 && st==startTime){
            audio("last_set");
        }else if(st==startTime){
            audio("go");
        }else if(st<4000){
            audio("ping");
            vibrate();
        }else{
//            audio("tick");
        }

    }
    public void restAudio(){
        if(ir==restTime && iSets!=1){
            audio("rest");
        }else if(ir<4000 && iSets!=1) {
            audio("long_ping");
            vibrate();
        }else{
//            audio("tick");
        }
    }

    private void updateStopwatchText(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) ((timeInMillis % 60000) / 1000);
        int milliseconds = (int) (timeInMillis % 1000);

        String timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//        clockTextView.setText(timeText);
        progressText.setText(timeText);
    }

    public void audio(String name){
        Log.e("TAG", "audio: "+name );
        MediaPlayer mp = MediaPlayer.create(this,R.raw.complete);
        if(name.equals("go")){
                mp = MediaPlayer.create(this, R.raw.go);
        }else if(name.equals("complete")){
                mp = MediaPlayer.create(this, R.raw.complete);
        }else if (name.equals("rest")) {
                mp = MediaPlayer.create(this, R.raw.rest);
        } else if (name.equals("ping")) {
                mp = MediaPlayer.create(this, R.raw.ping);
        }else if(name.equals("one")){
                mp = MediaPlayer.create(this, R.raw.one);
        }else if(name.equals("two")){
            mp = MediaPlayer.create(this, R.raw.two);
        }else if(name.equals("three")){
            mp = MediaPlayer.create(this, R.raw.three);
        }else if(name.equals("pause")){
            mp = MediaPlayer.create(this, R.raw.pause);
        }else if(name.equals("last_round")){
            mp = MediaPlayer.create(this, R.raw.last_round);
        }else if(name.equals("last_set")){
            mp = MediaPlayer.create(this, R.raw.last_set);
        }else if(name.equals("long_ping")){
            mp = MediaPlayer.create(this, R.raw.long_ping);
        }else if(name.equals("ready")){
            mp = MediaPlayer.create(this, R.raw.ready);
        }else if(name.equals("set")){
            mp = MediaPlayer.create(this, R.raw.set);
        }else if(name.equals("well_done")){
            mp = MediaPlayer.create(this, R.raw.well_done);
        }else if(name.equals("tick")){
            mp = MediaPlayer.create(this, R.raw.tick);
        }else if(name.equals("click")){
            mp = MediaPlayer.create(this, R.raw.click_button);
        }

        try {
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.sound_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                switch (itemId) {
                    case R.id.action_sound:
                        MenuItem m = findViewById(R.id.action_sound);
                        isSound=!isSound;
                        if(isSound){
                        m.setIcon(R.drawable.ic_vol);
                        m.setTitle("Sound");
                        }else{
                        m.setIcon(R.drawable.ic_no_vol);
                        m.setTitle("NoSound");
                        }


                        return true;
                    case R.id.action_vibration:
                        MenuItem im = findViewById(R.id.action_vibration);
                        isVibration = !isVibration;
                        if(isVibration){
                        im.setIcon(R.drawable.baseline_vibration_24);
                        }else{
                        im.setIcon(R.drawable.ic_not_vibrate);
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50);
        }
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
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.91f);

        // setting height to 90% of display
//        layoutParams.height = (int) (displayMetrics.heightPixels * 0.3f);
        alertDialog.getWindow().setAttributes(layoutParams);
    }
}