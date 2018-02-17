package com.clay.meditation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Stats extends AppCompatActivity {

    SharedPreferences settings;
    TextView timeTextView;

    public void reset(View view){
        settings.edit().putInt("medTimeToDate", 0).apply();
        timeTextView.setText("");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settings = this.getSharedPreferences("com.clay.meditation", Context.MODE_PRIVATE);

        int medTimeToDate = settings.getInt("medTimeToDate", 0);

        timeTextView = (TextView) findViewById(R.id.statsTotalTime);
        String text = medTimeToDate + " mins";
        timeTextView.setText(text);


    }

}
