package com.example.healingment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {
    ImageView tutorial1;
    ImageView tutorial2;
    ImageView tutorial3;
    ImageView tutorial4;
    ImageView tutorial5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        tutorial1 = findViewById(R.id.tutorial1);
        tutorial2 = findViewById(R.id.tutorial2);
        tutorial3 = findViewById(R.id.tutorial3);
        tutorial4 = findViewById(R.id.tutorial4);
        tutorial5 = findViewById(R.id.tutorial5);


    }
    public void btnOnclick1(View view) {
        tutorial2.setVisibility(View.VISIBLE);
        tutorial1.setVisibility(View.INVISIBLE);
        tutorial3.setVisibility(View.INVISIBLE);
        tutorial4.setVisibility(View.INVISIBLE);
        tutorial5.setVisibility(View.INVISIBLE);
    }
    public void btnOnclick2(View view) {
        tutorial3.setVisibility(View.VISIBLE);
        tutorial1.setVisibility(View.INVISIBLE);
        tutorial2.setVisibility(View.INVISIBLE);
        tutorial4.setVisibility(View.INVISIBLE);
        tutorial5.setVisibility(View.INVISIBLE);
    }
    public void btnOnclick3(View view) {
        tutorial4.setVisibility(View.VISIBLE);
        tutorial1.setVisibility(View.INVISIBLE);
        tutorial2.setVisibility(View.INVISIBLE);
        tutorial3.setVisibility(View.INVISIBLE);
        tutorial5.setVisibility(View.INVISIBLE);
    }
    public void btnOnclick4(View view) {
        tutorial5.setVisibility(View.VISIBLE);
        tutorial1.setVisibility(View.INVISIBLE);
        tutorial2.setVisibility(View.INVISIBLE);
        tutorial3.setVisibility(View.INVISIBLE);
        tutorial4.setVisibility(View.INVISIBLE);
    }
    public void btnOnclick5(View view) {
        Intent MainIntent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(MainIntent);
        finish();
    }
}
