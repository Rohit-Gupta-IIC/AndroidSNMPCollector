package cn.gavin.snmp.core.model;

import java.util.List;

import cn.gavin.snmp.MainController;

/**
 * Created by gluo on 11/7/2016.
 */
public class DeviceImp extends Device {
    public final static String SYSOBJECTID = "1.3.6.1.2.1.1.2";
    private StringDataSet sysId;
    private String id;
    private List<String> groupNames;
    private String name;
    private String communityId;

    public DeviceImp(String ip) {
        super(ip);
    }

    public void discovery() {
        sysId = null;
        Oid sys = snmp.get(new Oid(SYSOBJECTID));
        sysId = new StringDataSet(SYSOBJECTID);
        sysId.appendData(System.currentTimeMillis(), sys.getOidValue());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StringDataSet getSysId() {
        return sysId;
    }

    public synchronized void addSysIdValue(long time, String value) {
        if (sysId == null) {
            sysId = new StringDataSet(SYSOBJECTID);
        }
        sysId.appendData(time, value);
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommunity() {
        return communityId;
    }

    public void setCommunity(String communityId) {
        this.communityId = communityId;
    }

    public void doCollection() {
        List<Oid> oidByDevice = MainController.getOIDManger().getOidByDevice(getId());
        addOids(oidByDevice.toArray(new Oid[oidByDevice.size()]));
        super.doCollection();
        for(Oid oid : oidByDevice){
            MainController.getOIDManger().saveValue(oid, getId());
        }
    }
}
