package cn.gavin.snmp.core.monitor;

import cn.gavin.snmp.core.model.DeviceImp;
import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.model.Oid;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gluo on 11/7/2016.
 */
public class Group implements Monitor {
    private String name;
    private String uuid;
    private Set<Oid> oids;
    private DeviceImp device;
    @Override
    public Set<Oid> getOIDs() {
        HashSet<Oid> set = new HashSet<Oid>(oids);
        return set;
    }

    public void addOID(Oid oid){
        if(oids == null){
            oids = new HashSet<Oid>();
        }
        oids.add(oid);
    }

    @Override
    public void build(Long time) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DeviceImp getDevice() {
        return device;
    }

    public void setDevice(DeviceImp device) {
        this.device = device;
    }
}
