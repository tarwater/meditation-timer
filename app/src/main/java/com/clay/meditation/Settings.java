package com.clay.meditation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final SharedPreferences settings = this.getSharedPreferences("com.clay.meditation", Context.MODE_PRIVATE);

        boolean notificationsOn = settings.getBoolean("notifications", true);

        Switch mySwitch = (Switch) findViewById(R.id.switch1);

        if(notificationsOn){
            mySwitch.setChecked(true);
        } else {
            mySwitch.setChecked(false);
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settings.edit().putBoolean("notifications", true).apply();
                } else {
                    settings.edit().putBoolean("notifications", false).apply();
                }
            }
        });

        //Spinner stuff
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.session_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        int defaultTime = settings.getInt("defaultMedTime", 10);

        String myString = defaultTime + " mins";

        int spinnerPosition = spinnerAdapter.getPosition(myString);

        spinner.setSelection(spinnerPosition);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

               String selecteditem =  adapter.getItemAtPosition(i).toString();

                String[] arr = selecteditem.split(" ");
                int time = Integer.parseInt(arr[0]);

                settings.edit().putInt("defaultMedTime", time).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


    }

}
