package com.example.healingment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper{
    private final static String TAG = "DataBaseHelper"; // Logcat에 출력할 태그이름
    private final static int DATABASE_VERSION = 3; // 버전 NO 컬럼명변경
    // database 의 파일 경로
    private static String DB_PATH = "";
    private static String DB_NAME = "ment.db";
    private static String TABLE_NAME = "Ments";
    private SQLiteDatabase mDataBase;

    // 3. 테이블 항목 네임
    public static final String COL_1 = "NUMBER";
    public static final String COL_2 = "LANG";
    public static final String COL_3 = "CONTENTS";
    public static final String COL_4 = "RESONER";
    public static final String COL_5 = "ISLIKE";

    private Context mContext;

    public DataBaseHelper(Context context) {
        super(context,DB_NAME,null,2);


        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d(TAG,"Database is copied.");
        }
    }


    @Override
    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 구조 생성로직
        Log.d(TAG,"onCreate()");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Toast.makeText(mContext,"onOpen()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onOpen() : DB Opening!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제하고 onCreate() 다시 로드시킨다.
        Log.d(TAG,"onUpgrade() : DB Schema Modified and Excuting onCreate()");
    }

    // db를 assets에서 복사해온다.
    private void dbCopy() {

        try {
            File folder = new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String out_filename = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy","IOException 발생함");
        }
    }

    public boolean updateData(String NUMBER, String islike){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_4,RESONER);
        contentValues.put(COL_5,islike);
        db.update(TABLE_NAME,contentValues,"NUMBER = ?",new String[] { NUMBER });
        return true;
    }

    public void update(String CONTENTS, String RESONER) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 조건에 일치하는 행의 데이터 변경
        db.execSQL("UPDATE Ments SET islike= Y " + "'WHERE CONTENTS=" + CONTENTS + "',RESONER='");
    }


    // 데이터베이스 보여주기(읽어오기)
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }
}