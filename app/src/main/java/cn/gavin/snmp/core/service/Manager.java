package cn.gavin.snmp.core.service;

import android.content.Context;

import cn.gavin.snmp.db.DBHelper;

/**
 * Created by gluo on 11/10/2016.
 */
public abstract class Manager {
    protected DBHelper dbHelper;
    public Manager(Context context){
        this.dbHelper = DBHelper.getDbHelper(context);
    }
}
