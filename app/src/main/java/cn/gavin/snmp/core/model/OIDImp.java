package cn.gavin.snmp.core.model;

/**
 * Created by gluo on 11/7/2016.
 */
public class OIDImp extends Oid {
    private String name;
    private DataSet dataSet;
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
    }

    public void setValues(DataSet dataSet){
        this.dataSet = dataSet;
        setOidValue(dataSet.getLatestData().toString());
    }
}
