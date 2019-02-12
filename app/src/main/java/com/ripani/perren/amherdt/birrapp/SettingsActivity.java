package com.ripani.perren.amherdt.birrapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Switch s = findViewById(R.id.swNight);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean tgpref = preferences.getBoolean("switch", false);  //default is true


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor e = settings.edit();
        e.putBoolean("switch", s.isChecked());


        e.commit();





    }


}
