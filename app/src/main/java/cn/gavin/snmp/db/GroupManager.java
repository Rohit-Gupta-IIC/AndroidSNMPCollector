package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.gavin.snmp.MainController;
import cn.gavin.snmp.core.model.Device;
import cn.gavin.snmp.core.model.DeviceImp;
import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Created by gluo on 11/9/2016.
 */
public class GroupManager extends cn.gavin.snmp.core.service.GroupManager {
    private Context context;
    private DBHelper dbHelper;

    public GroupManager(Context context) {
        super(context);
    }

    @Override
    public List<String> getAllGroupNames() {
        Cursor cursor = dbHelper.query("select group_name from oid_group");
        List<String> names = new ArrayList<>();
        while(!cursor.isAfterLast()){
            names.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return null;
    }

    @Override
    public Group getGroup(String name) {
        Cursor cursor = dbHelper.query("select * from oid_group where group_name = '" + name + "'");
        if (!cursor.isAfterLast()) {
            Group group = new Group();
            group.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
            group.setName(cursor.getString(cursor.getColumnIndex("name")));
            return group;
        }
        return null;
    }

    @Override
    public List<String> getGroupNamesByDevice(Device device) {
        Cursor cursor = dbHelper.query("select group_name from oid_group join device_group on oid_group.uuid = device_group.group_id where device_group.device_id = '" + ((DeviceImp)device).getId() + "'");
       List<String> names = new ArrayList<>();
        while(!cursor.isAfterLast()){
            names.add(cursor.getString(cursor.getColumnIndex("group_name")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    @Override
    public void save(Group group) {
        if (group.getUuid() == null) {
            group.setUuid(UUID.randomUUID().toString());
            String insert = "insert into oid_group (group_name,uuid) values (" + "'" + group.getName() + "'," +
                    "'" + group.getUuid() + "')";
            dbHelper.execSQL(insert);
        }

    }

    @Override
    public Group retrieveGroupData(Group group) {
        if (group.getDevice() == null) {
            throw new IllegalArgumentException("Group should always binding to a special device");
        }
        MainController.getOIDManger().retrieveOidValue(group);
        return group;
    }



    public void delete(Group group){
        dbHelper.execSQL("delete from oid_group where uuid = '" + group.getUuid());
        dbHelper.execSQL("delete from oid where group_id = '"+ group.getUuid() + "'");
        for(Oid oid: group.getOIDs()){
            MainController.getOIDManger().deleteValueByOID(((Oid)oid).getId());
        }
        dbHelper.execSQL("delete from device_group where group_id = '" + group.getUuid() + "'");
    }

    public void addGroupToDevice(Group group, DeviceImp device){
        dbHelper.execSQL("insert into device_group (device_group_id, device_id, group_id) values ('" + UUID.randomUUID().toString() + "','" + device.getId() + "','" + group.getUuid() + "')");
    }

    public void removeGroupFromDevice(Group group, DeviceImp device){
        dbHelper.execSQL("delete from device_group where group_id = '" + group.getUuid() + "' and device_id = '" + device.getId() + "'");
    }
}
