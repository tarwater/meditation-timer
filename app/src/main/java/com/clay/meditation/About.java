package com.clay.meditation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("About");

        TextView textView = (TextView) findViewById(R.id.aboutTextView);

        String howTo = "This app was designed and programmed by Clay Holt \n\n" +
                "I hope you enjoy it! \n\n" +
                "If you'd like to say thanks, you may donate to me via Paypal at clayholt@gmx.com";

        textView.setText(howTo);



    }



}
