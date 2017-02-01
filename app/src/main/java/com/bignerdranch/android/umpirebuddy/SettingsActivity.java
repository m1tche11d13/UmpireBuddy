package com.bignerdranch.android.umpirebuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton mCallsToggle;

    private static final String CALLS_KEY = "calls_key";
    private static final String PREFS = "preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mCallsToggle = (ToggleButton) findViewById(R.id.verbal_calls_toggle);
        mCallsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent resultIntent = new Intent();
                SharedPreferences prefs = getSharedPreferences(PREFS, 0);
                SharedPreferences.Editor editor = prefs.edit();
                if (isChecked) {
                    editor.putBoolean(CALLS_KEY, true);
                    resultIntent.putExtra(CALLS_KEY, true);
                } else {

                    editor.putBoolean(CALLS_KEY, false);
                    resultIntent.putExtra(CALLS_KEY, false);
                }
                editor.apply();
                setResult(Activity.RESULT_OK, resultIntent);
            }
        });



        SharedPreferences prefs = getSharedPreferences(PREFS, 0);
        mCallsToggle.setChecked(prefs.getBoolean(CALLS_KEY, false));
    }
}
