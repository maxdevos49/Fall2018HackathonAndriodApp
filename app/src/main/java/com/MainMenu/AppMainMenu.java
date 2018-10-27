package com.MainMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.Settings.Settings;
import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.AlbumPicker;
import com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView.RemoteAlbumPicker;

public class AppMainMenu extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_app_main_menu);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        Typeface treb = Typeface.createFromAsset(getAssets(), "fonts/Trebuchet MS.ttf");
        ((Button) findViewById(R.id.UploadButton)).setTypeface(treb);
        ((Button) findViewById(R.id.ViewButton)).setTypeface(treb);
        ((Button) findViewById(R.id.HowToButton)).setTypeface(treb);
        ((Button) findViewById(R.id.SettingsButton)).setTypeface(treb);



        findViewById(R.id.UploadButton).setOnClickListener((view) -> {
            switchToUpload();
        });

        findViewById(R.id.ViewButton).setOnClickListener((view) -> {
            switchToView();
        });

        findViewById(R.id.SettingsButton).setOnClickListener((view) -> {
            switchToSettings();
        });

    }

    private void switchToUpload() {
        Intent intent = new Intent(this, AlbumPicker.class);
        startActivity(intent);
    }

    private void switchToView() {
        Intent intent = new Intent(this, RemoteAlbumPicker.class);
        startActivity(intent);
    }

    private void switchToSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}
