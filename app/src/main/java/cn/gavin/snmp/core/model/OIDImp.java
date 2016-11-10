package cn.gavin.snmp.core.model;

/**
 * Created by gluo on 11/7/2016.
 */
public class OIDImp extends Oid {
    private String id;
    private String name;
    private DataSet dataSet;
    private String groupId;
    private Long lastedCollectTime;
    public OIDImp(String oid) {
        super(oid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized void appendValue(Long time, Object value){
        if(dataSet == null){
            dataSet = new DataSet(getOidString());
        }
        dataSet.appendData(time, value);
        setOidValue(value.toString());
        lastedCollectTime = time;
    }

    public void setValues(DataSet dataSet){
        this.dataSet = dataSet;
        setOidValue(dataSet.getLatestData().toString());
        lastedCollectTime = dataSet.getLatestTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getLastedCollectTime() {
        return lastedCollectTime;
    }

    public void setLastedCollectTime(Long lastedCollectTime) {
        this.lastedCollectTime = lastedCollectTime;
    }
}
