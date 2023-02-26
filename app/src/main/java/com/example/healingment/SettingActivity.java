package com.example.healingment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    Button setkorbtn;
    Button setengbtn;
    Button ebookmodebtn;
    Button imagemodebtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setkorbtn = findViewById(R.id.setkorbtn);
        setengbtn = findViewById(R.id.setengbtn);

        ebookmodebtn = findViewById(R.id.ebookmodebtn);
        imagemodebtn = findViewById(R.id.imagemodebtn);

    }
    public void setKor(View view){
        PreferenceManager.setString(getApplicationContext(), "lang","kor");
        Intent MainIntent = new Intent(SettingActivity.this, introActivity.class);
        startActivity(MainIntent);
        finish();
    }
    public void setEng(View view){
        PreferenceManager.setString(getApplicationContext(), "lang","eng");
        Intent MainIntent = new Intent(SettingActivity.this, introActivity.class);
        startActivity(MainIntent);
        finish();
    }

    public void setEbook(View view){
        PreferenceManager.setString(getApplicationContext(), "mode","ebook");
        Intent MainIntent = new Intent(SettingActivity.this, introActivity.class);
        startActivity(MainIntent);
        finish();
    }

    public void setImage(View view){
        PreferenceManager.setString(getApplicationContext(), "mode","image");
        Intent MainIntent = new Intent(SettingActivity.this, introActivity.class);
        startActivity(MainIntent);
        finish();
    }
}
