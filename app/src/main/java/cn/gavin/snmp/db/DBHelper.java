package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gluo on 11/8/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "";
    private static final int DB_VERSION = 1;
    private static DBHelper dbHelper;
    public synchronized static DBHelper getDbHelper(Context context){
        if(dbHelper == null){
            dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        }
        return dbHelper;
    }
    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(String sql){
        Cursor cursor = getWritableDatabase().rawQuery(sql, (String[])null);
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
