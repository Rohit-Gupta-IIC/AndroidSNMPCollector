package cn.gavin.snmp.core.service;

import android.content.Context;

import java.util.List;

import cn.gavin.snmp.core.model.OIDImp;

/**
 * Created by gluo on 11/10/2016.
 */
public abstract class OIDManager extends Manager {
    public OIDManager(Context context) {
        super(context);
    }

    public abstract void save(OIDImp oidImp);
    public abstract void delete(OIDImp oidImp);
    public abstract void deleteValueByOID(String oidId);
    public abstract void deleteValueByDevice(String deviceId);
    public abstract void saveValue(OIDImp oidImp, String deviceId);
    public abstract List<OIDImp> getOidByDevice(String deviceId);
}
