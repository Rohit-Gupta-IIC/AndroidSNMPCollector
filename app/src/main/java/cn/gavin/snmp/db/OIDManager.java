package cn.gavin.snmp.db;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.gavin.snmp.core.model.DataSet;
import cn.gavin.snmp.core.model.OIDValueType;
import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.model.StringDataSet;
import cn.gavin.snmp.core.model.TableColumnOid;
import cn.gavin.snmp.core.model.TableOid;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Created by gluo on 11/10/2016.
 */
public class OIDManager extends cn.gavin.snmp.core.service.OIDManager {

    public OIDManager(Context context) {
        super(context);
    }

    public void save(Oid oidImp) {
        if (oidImp.getId() == null) {
            oidImp.setId(UUID.randomUUID().toString());
            dbHelper.execSQL("insert into oid (oid_id, oid_name,oid, type, group_id) values ('" + oidImp.getId() + "','" + oidImp.getName() + "','" + oidImp.getOidString() + "','" + oidImp.getType() + "','" + oidImp.getGroupId() + "')");
        }
    }

    public void delete(Oid oidImp) {
        dbHelper.execSQL("delete from oid where oid_id = '" + oidImp.getId() + "'");
        deleteValueByOID(oidImp.getId());
    }

    public void deleteValueByOID(String oidId) {
        dbHelper.execSQL("delete from oid_value where oid_id = '" + oidId + "'");
    }

    public void deleteValueByDevice(String deviceID) {
        dbHelper.execSQL("delete from oid_value where device_id = '" + deviceID + "'");
    }

    public void saveValue(Oid oidImp, String deviceId) {
        dbHelper.execSQL("insert into oid_value (value_id, oid_id, device_id, oid_value, value_time) values ('" + UUID.randomUUID().toString() + "','" + oidImp.getId() + "','" + deviceId + "','" + oidImp.getOidValue() + "'," + oidImp.getLastedCollectTime() + ")");
    }

    public List<Oid> getOidByDevice(String deviceId) {
        Cursor cursor = dbHelper.query("select * from oid join oid_group on oid.group_id = device_group.group_id where device_group.device_id = '" + deviceId + "'");
        List<Oid> oids = new ArrayList<>();
        while ((!cursor.isAfterLast())) {
            Oid oid = new Oid(cursor.getString(cursor.getColumnIndex("oid")));
            oid.setId(cursor.getString(cursor.getColumnIndex("oid_id")));
            oid.setName(cursor.getString(cursor.getColumnIndex("oid_name")));
            oid.setGroupId(cursor.getString(cursor.getColumnIndex("group_id")));
            oid.setValueType(OIDValueType.valueOf(cursor.getString(cursor.getColumnIndex("type"))));
            oids.add(oid);
        }
        return oids;
    }

    public void retrieveOidValue(Group group) {
        String sql = "select * from oid where group_id = '" + group.getUuid() + "' and parent_id = '0'";
        Cursor cursor = dbHelper.query(sql);
        String deviceId = group.getDevice().getId();
        String oidString = cursor.getString(cursor.getColumnIndex("oid"));
        while (!cursor.isAfterLast()) {
            OIDValueType type = OIDValueType.valueOf(cursor.getString(cursor.getColumnIndex("type")));
            DataSet valueSet = null;
            Oid oid = null;
            switch (type) {
                case INTEGER:
                case Gauge32:
                case Gauge64:
                case TimeTicks:
                case Counter32:
                case Counter64:
                    valueSet = new DataSet<Long>(oidString);
                    break;
                case Table:
                    Cursor coulumnCursor = dbHelper.query("select * from oid where group_id = '" + group.getUuid() + "' and parent_id = '" + cursor.getString(cursor.getColumnIndex("oid_id")));
                    List<TableColumnOid> columns = new ArrayList<>();
                    while (!coulumnCursor.isAfterLast()) {
                        TableColumnOid columnOid = new TableColumnOid(coulumnCursor.getString(coulumnCursor.getColumnIndex("oid")));
                        columnOid.setId(coulumnCursor.getString(coulumnCursor.getColumnIndex("oid_id")));
                        columnOid.setValueType(OIDValueType.valueOf(coulumnCursor.getString(coulumnCursor.getColumnIndex("type"))));
                        Cursor columnValue = dbHelper.query("select * from oid_value where device_id = '" + group.getDevice().getId() + "' and oid_id = '" + columnOid.getId() + "' order by column_index");
                        while (!columnValue.isAfterLast()) {
                            columnOid.appendValue(columnValue.getString(columnValue.getColumnIndex("column_index")), columnValue.getString(columnValue.getColumnIndex("oid_value")), columnValue.getLong(columnValue.getColumnIndex("value_time")));
                            columnValue.moveToNext();
                        }
                        columns.add(columnOid);
                        columnValue.close();
                        coulumnCursor.moveToNext();
                    }
                    coulumnCursor.close();
                    oid = new TableOid(oidString);
                    ((TableOid) oid).setColumns((TableColumnOid[]) columns.toArray());
                    break;
                default:
                    valueSet = new StringDataSet(oidString);
            }
            if (oid == null) {
                oid = new Oid(cursor.getString(cursor.getColumnIndex("oid")));
            }
            oid.setName(cursor.getString(cursor.getColumnIndex("oid_name")));
            Cursor cursorValue = dbHelper.query("select * from oid_value where oid_id = '" + cursor.getString(cursor.getColumnIndex("oid_id")) + "' and device_id = '" + deviceId + "'");
            oid.setValueType(type);

            oid.setValues(valueSet);
            while (!cursorValue.isAfterLast()) {
                if (valueSet instanceof StringDataSet) {
                    oid.appendValue(cursorValue.getLong(cursor.getColumnIndex("value_time")), cursor.getString(cursor.getColumnIndex("oid_value")));
                } else {
                    oid.appendValue(cursor.getLong(cursor.getColumnIndex("value_time")), cursor.getLong(cursor.getColumnIndex("oid_value")));
                }
                cursorValue.moveToNext();
            }
            cursorValue.close();
            cursor.moveToNext();
            group.addOID(oid);
        }
        cursor.close();
    }
}
