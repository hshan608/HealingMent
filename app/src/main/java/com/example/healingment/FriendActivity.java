package com.example.healingment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FriendActivity extends AppCompatActivity {
    Button screenshotbtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //권한 부분
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        screenshotbtn = findViewById(R.id.screenshotbtn);
    }

        public void SreenshotButton(View view) {
            View rootView = getWindow().getDecorView();

            File screenShot = ScreenShot(rootView);
            if (screenShot != null){
                // 갤러리 추가
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
            }
            screenshotbtn.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }

        public File ScreenShot(View view){
            screenshotbtn.setVisibility(View.INVISIBLE);

            view.setDrawingCacheEnabled(true);

            Bitmap screenBitmap = view.getDrawingCache();

            String filename = "screenshot"+System.currentTimeMillis()+".png";
            File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
                os.close();
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
            view.setDrawingCacheEnabled(false);
            return file;
        }

        private static final int REQUEST_EXTENAL_STORAGE = 1;
        private static String[] PERMISSION_STORAGE = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };


    private void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTENAL_STORAGE);
        }
    }
}
