package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gluo on 11/8/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "";
    private static final int DB_VERSION = 1;
    private static DBHelper dbHelper;
    private  Context context;
    public synchronized static DBHelper getDbHelper(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        }
        return dbHelper;
    }
    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            SQLiteDatabase database = getWritableDatabase();
            database.beginTransaction();
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("db.sql")));
            String sql = reader.readLine();
            while (sql!=null){
                if(!sql.isEmpty()){
                    database.execSQL(sql);
                }
                sql = reader.readLine();
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(String sql){
        Cursor cursor = getWritableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void execSQL(String sql){
        getWritableDatabase().execSQL(sql);
    }

    public String save(String sql){
        return null;
    }
}
