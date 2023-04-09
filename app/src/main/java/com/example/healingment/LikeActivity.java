package com.example.healingment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.healingment.db.DataBaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class LikeActivity extends AppCompatActivity {
    private static Activity likeActivity;
    private DrawerLayout drawerLayout;
    private View drawerView;
    String[] mentnumber;   // ArrayAdapter에 넣을 배열 생성
    String[] content;   // ArrayAdapter에 넣을 배열 생성
    String[] resoner;   // ArrayAdapter에 넣을 배열 생성
    String[] islike;   // ArrayAdapter에 넣을 배열 생성
    ImageButton screenshotbtn;
    ImageButton sharebtn;
    ImageButton menubtn;
    ImageButton likebtn;
    Button nextbtn;
    TextView maintxt;
    TextView mainauthor;
    TextView settingtxt;
    TextView mainbtn;
    ConstraintLayout nextlayout;
    int likeseq;
    private String val;
    private int seq = (int) (Math.random() * 49);
    private int backseq = (int) (Math.random() * 24);

    public SharedPreferences prefs;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        likeActivity = LikeActivity.this;
        dbHelper = new DataBaseHelper(this);
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        String mode = PreferenceManager.getString(getApplicationContext(), "mode");
        likeseq = getlikeseq();
//        Toast.makeText(getApplicationContext(),String.valueOf(likeseq),Toast.LENGTH_SHORT).show();
//        if(likeseq == 0){
//            LikeActivity likeActivity = (LikeActivity)LikeActivity.likeActivity;
//            Toast.makeText(getApplicationContext(),"선택한 언어에 좋아요를 남기지 않았습니다.",Toast.LENGTH_SHORT).show();
//            likeActivity.finish();
//            Intent MainActivity = new Intent(LikeActivity.this, MainActivity.class);
//            startActivity(MainActivity);
//        }
        seq = (int) (Math.random() * likeseq);
//        Toast.makeText(getApplicationContext(),lang,Toast.LENGTH_SHORT).show();
        //권한 부분
        getVal();
        verifyStoragePermission(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        screenshotbtn = findViewById(R.id.screenshotbtn);
        sharebtn = findViewById(R.id.sharebtn);
        menubtn = findViewById(R.id.menubtn);
        likebtn = findViewById(R.id.likebtn);
        mainbtn = findViewById(R.id.mainbtn);

        maintxt = findViewById(R.id.maintxt);
        mainauthor = findViewById(R.id.mainauthor);
        nextlayout = findViewById(R.id.nextlayout);


        drawerLayout = (DrawerLayout)findViewById(R.id.likelayout);
        drawerView = (View)findViewById(R.id.drawer3);

        settingtxt = findViewById(R.id.setting);


//        if(mode.equals("image")){
//            int[] images = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10,R.drawable.bg11,R.drawable.bg12,R.drawable.bg13,R.drawable.bg14,R.drawable.bg15,R.drawable.bg16,R.drawable.bg17,R.drawable.bg18,R.drawable.bg19,R.drawable.bg20,R.drawable.bg21,R.drawable.bg22,R.drawable.bg23,R.drawable.bg24};
//            int whitecolor = ContextCompat.getColor(getApplicationContext(), R.color.white);
//            maintxt.setTextColor(whitecolor);
//            mainauthor.setTextColor(whitecolor);
//            Random rand = new Random();
//            drawerLayout.setBackgroundResource(images[rand.nextInt(images.length)]);
//        }else{
//            int blackcolor = ContextCompat.getColor(getApplicationContext(), R.color.black);
//            maintxt.setTextColor(blackcolor);
//            mainauthor.setTextColor(blackcolor);
//        }

        settingtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SettingIntent = new Intent(LikeActivity.this, SettingActivity.class);
                startActivity(SettingIntent);
            }
        });

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MainIntent = new Intent(LikeActivity.this, MainActivity.class);
                startActivity(MainIntent);
            }
        });

        drawerLayout.addDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if(likeseq == 0) {
            maintxt.setText("보관한 명언이 없습니다.");
            mainauthor.setText(" ");
        }else{
            maintxt.setText(content[seq].replace("/n", System.getProperty("line.separator")));
            mainauthor.setText(resoner[seq].replace("/n", System.getProperty("line.separator")));
//        Toast.makeText(getApplicationContext(),islike[seq], Toast.LENGTH_SHORT).show();
            if (islike[seq].equals("Y")) {
                likebtn.setImageResource(R.drawable.heart);
            }
        }
        // nav 언어 설정
        String lang = PreferenceManager.getString(getApplicationContext(), "lang");
        if(lang.equals("eng")){
            mainbtn.setText("Main");
            settingtxt.setText("Options");
        }else{
            mainbtn.setText("메인");
            settingtxt.setText("환경설정");
        }

    }
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            //슬라이드 했을때
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            //Drawer가 오픈된 상황일때 호출
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            // 닫힌 상황일 때 호출
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            // 특정상태가 변결될 때 호출
        }
    };

    public void btnOnclick(View view) {
        switch (view.getId()){
            case R.id.menubtn:
                drawerLayout.openDrawer(drawerView);
        }
    }
    public void ShareMent(View view){
        if (likeseq != 0) {
            Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
            Sharing_intent.addCategory(Intent.CATEGORY_DEFAULT);
            Sharing_intent.setType("text/plain");

            String sharingUrl = content[seq].replace("/n", " ") + System.getProperty("line.separator") + resoner[seq].replace("/n", " ") + System.getProperty("line.separator") + "힐링 하이, 오늘의 명언 알려줘! (Link)";

            Sharing_intent.putExtra(Intent.EXTRA_TEXT, sharingUrl);

            Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
            startActivity(Sharing);
        }
    }
    public void nextMent(View view) {
        seq++;
        if(seq >= likeseq){
            seq = 0;
        }
        maintxt = findViewById(R.id.maintxt);
        mainauthor = findViewById(R.id.mainauthor);
        if(likeseq == 0) {
            maintxt.setText("보관한 명언이 없습니다.");
            mainauthor.setText(" ");
        }else {
            maintxt.setText(content[seq].replace("/n", System.getProperty("line.separator")));
            mainauthor.setText(resoner[seq].replace("/n", System.getProperty("line.separator")));

            int islikecnt = islike();
            if(islikecnt == 1){
                likebtn.setImageResource(R.drawable.heart);
            }else{
                likebtn.setImageResource(R.drawable.unheart);
            }
        }
        String mode = PreferenceManager.getString(getApplicationContext(), "mode");
//        Toast.makeText(getApplicationContext(),islike[seq], Toast.LENGTH_SHORT).show();

        if(mode.equals("image")){
            int[] images = {R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10,R.drawable.bg11,R.drawable.bg12,R.drawable.bg13,R.drawable.bg14,R.drawable.bg15,R.drawable.bg16,R.drawable.bg17,R.drawable.bg18,R.drawable.bg19,R.drawable.bg20,R.drawable.bg21,R.drawable.bg22,R.drawable.bg23,R.drawable.bg24};
            Random rand = new Random();
            drawerLayout.setBackgroundResource(images[rand.nextInt(images.length)]);
        }
    }

    public int getlikeseq(){
        String lang = PreferenceManager.getString(getApplicationContext(), "lang");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(lang.equals("eng")) {
            Cursor cursor = db.rawQuery("SELECT * FROM Ments where LANG = 'EN' AND ISLIKE = 'Y'", null);
            int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
            return count;
        }else{
            Cursor cursor = db.rawQuery("SELECT * FROM Ments where LANG = 'KR' AND ISLIKE = 'Y'", null);
            int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
            return count;
        }
    }
    public void getVal() {
        String lang = PreferenceManager.getString(getApplicationContext(), "lang");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(lang.equals("eng")){
            Cursor cursor = db.rawQuery("SELECT * FROM Ments where LANG = 'EN' AND ISLIKE = 'Y'", null);
            int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
            mentnumber = new String[count];
            content = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            resoner = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            islike = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            for (int i = 0; i < count; i++) {

                cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음

                String str_mentnumber = cursor.getString(0);   // 세번째 속성 0~

                String str_content = cursor.getString(2);   // 세번째 속성 0~

                String str_resoner = cursor.getString(3);   // 네번째 속성 0~

                String str_islike = cursor.getString(4);   // 다섯번째 속성 0~

                mentnumber[i] = str_mentnumber;
                content[i] = str_content;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                resoner[i] = str_resoner;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                islike[i] = str_islike;   // 각각의 속성값들을 해당 배열의 i번째에 저장
            }
            cursor.close();
        }else{
            Cursor cursor = db.rawQuery("SELECT * FROM Ments where LANG = 'KR' AND ISLIKE = 'Y'", null);
            int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
            mentnumber = new String[count];
            content = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            resoner = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            islike = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            for (int i = 0; i < count; i++) {

                cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음

                String str_mentnumber = cursor.getString(0);   // 세번째 속성 0~

                String str_content = cursor.getString(2);   // 세번째 속성

                String str_resoner = cursor.getString(3);   // 네번째 속성

                String str_islike = cursor.getString(4);   // 다섯번째 속성 0~

                mentnumber[i] = str_mentnumber;
                content[i] = str_content;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                resoner[i] = str_resoner;   // 각각의 속성값들을 해당 배열의 i번째에 저장
                islike[i] = str_islike;   // 각각의 속성값들을 해당 배열의 i번째에 저장
            }
            cursor.close();
        }
    }
    public void SreenshotButton(View view) {
        View rootView = getWindow().getDecorView();

        File screenShot = ScreenShot(rootView);
        if (screenShot != null){
            // 갤러리 추가
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
        }
        screenshotbtn.setVisibility(View.VISIBLE);
        sharebtn.setVisibility(View.VISIBLE);
        menubtn.setVisibility(View.VISIBLE);
        likebtn.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public File ScreenShot(View view){
        screenshotbtn.setVisibility(View.INVISIBLE);
        sharebtn.setVisibility(View.INVISIBLE);
        menubtn.setVisibility(View.INVISIBLE);
        likebtn.setVisibility(View.INVISIBLE);

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
    public int islike(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String NumStr =  mentnumber[seq];
        Cursor cursor = db.rawQuery("SELECT * FROM Ments where ISLIKE = 'Y' and NUMBER = ?", new String[]{NumStr});
        int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다

        return count;
    }

    public void UpdateData(View view) {
        if (likeseq != 0) {
            int islikecnt = islike();

            maintxt = findViewById(R.id.maintxt);
            mainauthor = findViewById(R.id.mainauthor);
            if (islikecnt == 0) {
                boolean isUpdated = dbHelper.updateData(mentnumber[seq], "Y");
                if (isUpdated == true) {
                    likebtn.setImageResource(R.drawable.heart);
                }
            } else {
                boolean isUpdated = dbHelper.updateData(mentnumber[seq], "N");
                if (isUpdated == true) {
                    likebtn.setImageResource(R.drawable.unheart);
                }
            }

        }
    }

}
