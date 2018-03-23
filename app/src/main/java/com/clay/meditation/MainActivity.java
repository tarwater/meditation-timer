package com.clay.meditation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;

import java.io.IOException;
import java.util.Calendar;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button startButton;
    Button resetButton;
    Button pauseButton;

    SeekBar seekBar;
    TextView timeTextView;
    int msToMed;

    SharedPreferences settings;

    boolean timerActive;
    CountDownTimer timer;

    long millisecondsLeft;

    MediaPlayer mp;
    AudioManager mAudioManager;
    int userVolume;

    public void start(View view) {

        if (!timerActive && msToMed != 0) {

            createTimer(msToMed);
            timerActive = true;
            startButton.setVisibility(View.INVISIBLE);
            resetButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }

    public void pause(View view) {

        if (timerActive) {
            timer.cancel();
            timerActive = false;
            msToMed = (int) millisecondsLeft;
            String setText = "Unpause";
            pauseButton.setText(setText);
        } else {
            createTimer(msToMed);
            timerActive = true;
            String setText = "Pause";
            pauseButton.setText(setText);
        }
    }

    public void reset(View view) {

        if (timerActive) {
            timer.cancel();
            timerActive = false;
        }

        updateTimeNoSeconds((msToMed / 1000) / 60);

        startButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);

    }

    public void updateTimeNoSeconds(int minutes) {

        String value;

        if (minutes == 1) {
            value = minutes + " min";
        } else {
            value = minutes + " mins";
        }
        timeTextView.setText(value);
        msToMed = minutes * 60 * 1000;

    }

    public void updateTimeWithSeconds(int seconds) {

        int minutes = (seconds - (seconds % 60)) / 60;
        seconds = seconds - (minutes * 60);

        String secondsString;
        String minutesString;

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = String.valueOf(seconds);
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = String.valueOf(minutes);
        }

        String time = minutesString + ":" + secondsString;

        timeTextView.setText(time);

    }

    public void setNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int curHr = calendar.get(Calendar.HOUR_OF_DAY);

        if (curHr > 6) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.add(Calendar.DATE, 1);

        } else {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
        }

        Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        settings = this.getSharedPreferences("com.clay.meditation", Context.MODE_PRIVATE);

        int defaultMedTime = settings.getInt("defaultMedTime", 10);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        userVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.i("volume:" , userVolume + "");
        mp = new MediaPlayer();

        Typeface quicksand = Typeface.createFromAsset(getAssets(), "fonts/quicksand.ttf");

        timeTextView = (TextView) findViewById(R.id.textView);
        timeTextView.setTypeface(quicksand);

        startButton = (Button) findViewById(R.id.button);
        startButton.setTypeface(quicksand);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setTypeface(quicksand);
        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setTypeface(quicksand);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(3540);
        seekBar.setProgress((defaultMedTime * 60) - 60);

        msToMed = defaultMedTime * 60 * 1000; //default starting value
        updateTimeNoSeconds(defaultMedTime);
        timerActive = false;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress = progress + 60;

                if (!timerActive) {

                    updateTimeNoSeconds(progress / 60);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onStart();

        boolean notificationsOn = settings.getBoolean("notifications", true);

        if (notificationsOn) {
            setNotification();
        } else {
            Intent intent = new Intent(this, Notification_receiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender);
        }

    }

    public void createTimer(int millis) {

        final int mins = millis / 60 / 1000;

        timer = new CountDownTimer(millis + 150, 1000) { //150 extra ms to fix CountDownTimer's flaws
            @Override
            public void onTick(long millisUntilFinished) {

                updateTimeWithSeconds((int) millisUntilFinished / 1000);
                millisecondsLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {

                playSound();

                String finishMessage = "Good!";
                timeTextView.setText(finishMessage);
                timerActive = false;
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);

                int medTimeToDate = settings.getInt("medTimeToDate", 0);
                int medTimeToAdd = mins + medTimeToDate;

                settings.edit().putInt("medTimeToDate", medTimeToAdd).apply();


            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
            return true;
        } else if (id == R.id.how_to) {
            Intent i = new Intent(getApplicationContext(), HowTo.class);
            startActivity(i);
            return true;
        } else if (id == R.id.about) {
            Intent i = new Intent(getApplicationContext(), About.class);
            startActivity(i);
            return true;
        } else if (id == R.id.stats) {
            Intent i = new Intent(getApplicationContext(), Stats.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void playSound(){

        Uri alarmSound = Uri.parse("android.resource://com.clay.meditation/" + R.raw.bell2);

        int level = Math.round(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 0.7f);

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, level, 0);


        try {

            if(!mp.isPlaying()){
                mp.setDataSource(getApplicationContext(), alarmSound);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopSound();
                    }
                });
            }


        } catch (IOException e) {


        }

    }

    public void stopSound(){
// reset the volume to what it was before we changed it.
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, userVolume, 0);
        mp.stop();
        mp.reset();
    }



}
