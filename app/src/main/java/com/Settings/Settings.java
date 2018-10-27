package com.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.naber.fall2018hackathonandroidapp.R;

import java.text.Format;

public class Settings extends AppCompatActivity {

    private static final String LOG_ID = Settings.class.getSimpleName();

    private static final String DEFAULT_SERVER = "https://770b9445.ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        SharedPreferences server = getSharedPreferences("com.Setting.sharedpreferences", Context.MODE_PRIVATE);
        String curServer = server.getString("serverAddress", DEFAULT_SERVER);
        ((EditText) findViewById(R.id.ServerAddressField)).setText(curServer);

        (findViewById(R.id.saveServerButton)).setOnClickListener((view) -> {
            Log.i(LOG_ID, "Saving new server name");
            SharedPreferences.Editor editor = server.edit();
            editor.putString("serverAddress", ((EditText) findViewById(R.id.ServerAddressField)).getText().toString());
            editor.commit();
        });

    }

}
