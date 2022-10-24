package com.example.healingment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.healingment.db.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String[] content;   // ArrayAdapter에 넣을 배열 생성
    String[] resoner;   // ArrayAdapter에 넣을 배열 생성
    Button screenshotbtn;
    TextView maintxt;
    private String val;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한 부분
        getVal();
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        screenshotbtn = findViewById(R.id.screenshotbtn);
        maintxt = findViewById(R.id.maintxt);

        maintxt.setText(content[0].replace("/n", System.getProperty("line.separator")));
    }
    public void getVal() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Ments where LANG = 'KR'", null);
        int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
        content = new String[count];   // 저장된 행 개수만큼의 배열을 생성
        resoner = new String[count];   // 저장된 행 개수만큼의 배열을 생성
        for (int i = 0; i < count; i++) {

            cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음

            String str_content = cursor.getString(2);   // 세번째 속성

            String str_resoner = cursor.getString(3);   // 네번째 속성

            content[i] = str_content;   // 각각의 속성값들을 해당 배열의 i번째에 저장
            resoner[i] = str_resoner;   // 각각의 속성값들을 해당 배열의 i번째에 저장
        }
        cursor.close();
        dbHelper.close();
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
