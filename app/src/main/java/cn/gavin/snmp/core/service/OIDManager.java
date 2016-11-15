package cn.gavin.snmp.core.service;

import android.content.Context;

import java.util.List;

import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.monitor.Group;

/**
 * Created by gluo on 11/10/2016.
 */
public abstract class OIDManager extends Manager {
    public OIDManager(Context context) {
        super(context);
    }

    public abstract void save(Oid oidImp);
    public abstract void delete(Oid oidImp);
    public abstract void deleteValueByOID(String oidId);
    public abstract void deleteValueByDevice(String deviceId);
    public abstract void saveValue(Oid oidImp, String deviceId);
    public abstract List<Oid> getOidByDevice(String deviceId);
    public abstract void retrieveOidValue(Group group);

}
