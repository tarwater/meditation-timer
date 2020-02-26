package com.clay.meditation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HowTo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("How to Meditate");

        TextView textView = (TextView) findViewById(R.id.howToTextView);

        String howTo = "1. Sit upright in a comfortable position, keeping your spine straight. \n\n" +
                "2. Close your eyes (or don't) and breathe through your nose. \n\n" +
                "3. Try to keep your attention on the breath sensations in the body. Notice how it feels as it moves in and out. \n\n" +
                "4. Let all other thoughts and feelings pass by. Try not to grasp or judge them. \n\n" +
                "5. Don't worry! There is no wrong way. ";

        textView.setText(howTo);


    }



}
