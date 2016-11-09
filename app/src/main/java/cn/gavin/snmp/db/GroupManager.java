package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.gavin.snmp.core.model.DataSet;
import cn.gavin.snmp.core.model.Device;
import cn.gavin.snmp.core.model.OIDImp;
import cn.gavin.snmp.core.model.OIDValueType;
import cn.gavin.snmp.core.model.StringDataSet;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Created by gluo on 11/9/2016.
 */
public class GroupManager extends cn.gavin.snmp.core.service.GroupManager {
    private Context context;
    private DBHelper dbHelper;
    public GroupManager(Context context){
        this.context = context;
        dbHelper = DBHelper.getDbHelper(context);
    }
    @Override
    public List<String> getAllGroupNames() {
        return null;
    }

    @Override
    public Group getGroup(String name) {
        return null;
    }

    @Override
    public List<String> getGroupNamesByDevice(Device device) {
        return null;
    }

    @Override
    public void save(Group group) {
        if(group.getUuid() == null){
            group.setUuid(UUID.randomUUID().toString());
            StringBuilder insert = new StringBuilder("insert into oid_group (group_name,uuid) values (");
            insert.append("'").append(group.getName()).append("',");
            insert.append("'").append(group.getUuid()).append("')");
            dbHelper.execSQL(insert.toString());
        }
    }

    @Override
    public Group retrieveGroupData(Group group) {
        if(group.getDevice() == null){
            throw new IllegalArgumentException("Group should always binding to a special device");
        }
        String sql = "select * from oid where group_id = '" + group.getUuid() + "'";
        Cursor cursor = dbHelper.query(sql);
        String deviceId = group.getDevice().getId();
        while(!cursor.isAfterLast()){
            OIDImp oid = new OIDImp(cursor.getString(cursor.getColumnIndex("oid")));
            oid.setName(cursor.getString(cursor.getColumnIndex("oid_name")));
            Cursor cursorValue = dbHelper.query("select * from oid_value where oid_id = '" + cursor.getString(cursor.getColumnIndex("oid_id")) + "' and device_id = '" + deviceId + "'");
            OIDValueType type = OIDValueType.valueOf(cursor.getString(cursor.getColumnIndex("type")));
            oid.setValueType(type);
            DataSet valueSet;
            switch (type){
                case INTEGER:
                case Gauge32:
                case Gauge64:
                case TimeTicks:
                case Counter32:
                case Counter64:
                    valueSet = new DataSet<Long>(oid.getOidString());
                    break;
                default:
                    valueSet = new StringDataSet(oid.getOidString());
            }
            oid.setValues(valueSet);
            while(!cursorValue.isAfterLast()){
                if(valueSet instanceof StringDataSet) {
                    oid.appendValue(cursorValue.getLong(cursor.getColumnIndex("value_time")), cursor.getString(cursor.getColumnIndex("oid_value")));
                }else{
                    oid.appendValue(cursor.getLong(cursor.getColumnIndex("value_time")), cursor.getLong(cursor.getColumnIndex("oid_value")));
                }
                cursorValue.moveToNext();
            }
            cursorValue.close();
            cursor.moveToNext();
            group.addOID(oid);
        }
        cursor.close();
        return group;
    }
}
