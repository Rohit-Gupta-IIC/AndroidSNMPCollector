package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;

import org.snmp4j.smi.OID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.gavin.snmp.core.model.OIDImp;
import cn.gavin.snmp.core.model.OIDValueType;
import cn.gavin.snmp.core.model.Oid;

/**
 * Created by gluo on 11/10/2016.
 */
public class OIDManager extends cn.gavin.snmp.core.service.OIDManager {

    public OIDManager(Context context) {
        super(context);
    }

    public void save(OIDImp oidImp){
        if(oidImp.getId() == null){
            oidImp.setId(UUID.randomUUID().toString());
            dbHelper.execSQL("insert into oid (oid_id, oid_name,oid, type, group_id) values ('" + oidImp.getId() + "','" + oidImp.getName() + "','" + oidImp.getOidString() + "','"+ oidImp.getType() + "','" + oidImp.getGroupId() + "')");
        }
    }
    public void delete(OIDImp oidImp){
        dbHelper.execSQL("delete from oid where oid_id = '" + oidImp.getId() + "'");
        deleteValueByOID(oidImp.getId());
    }

    public void deleteValueByOID(String oidId){
        dbHelper.execSQL("delete from oid_value where oid_id = '" + oidId + "'");
    }

    public void deleteValueByDevice(String deviceID){
        dbHelper.execSQL("delete from oid_value where device_id = '" + deviceID + "'");
    }

    public void saveValue(OIDImp oidImp, String deviceId){
        dbHelper.execSQL("insert into oid_value (value_id, oid_id, device_id, oid_value, value_time) values ('" + UUID.randomUUID().toString() + "','" + oidImp.getId() + "','" + deviceId + "','" + oidImp.getOidValue() + "'," + oidImp.getLastedCollectTime() + ")");
    }

    public List<OIDImp> getOidByDevice(String deviceId){
        Cursor cursor = dbHelper.query("select * from oid join oid_group on oid.group_id = device_group.group_id where device_group.device_id = '" + deviceId + "'");
        List<OIDImp> oids = new ArrayList<>();
        while ((!cursor.isAfterLast())){
            OIDImp oid = new OIDImp(cursor.getString(cursor.getColumnIndex("oid")));
            oid.setId(cursor.getString(cursor.getColumnIndex("oid_id")));
            oid.setName(cursor.getString(cursor.getColumnIndex("oid_name")));
            oid.setGroupId(cursor.getString(cursor.getColumnIndex("group_id")));
            oid.setValueType(OIDValueType.valueOf(cursor.getString(cursor.getColumnIndex("type"))));
            oids.add(oid);
        }
        return oids;
    }
}
