/*
 * TableColumnOID.java
 * Date: 3/31/2015
 * Time: 8:58 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package cn.gavin.snmp.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Table column oid will set as child in {@link TableOid}
 */
public class TableColumnOid extends Oid {
    private Map<String, String> oidValue;
    private Map<String, StringDataSet> oidValues;
    public TableColumnOid(String oid) {
        super(oid);
        oidValue = new HashMap<String, String>();
        oidValues = new HashMap<>();
    }

    public void setOidValue(String value){
        throw new UnsupportedOperationException("This is a table column, use the setOidValue(index, value)!");
    }
    public void setOidValue(String index, String value){
        oidValue.put(index, value);
    }

    /**
     * See {@link TableOid#getColumns()}
     * @param index
     * @param <T>
     * @return
     */
    public <T> T getValue(String index){
        return super.getValue(oidValue.get(index));
    }

    /**
     * See {@link TableOid#getColumns()}
     * @return
     */
    public Set<String> getIndex(){
        return oidValue.keySet();
    }

    public void appendValue(String index, String value, Long time){
        setOidValue(index, value);
        StringDataSet dataSet = oidValues.get(index);
        if(dataSet == null){
            dataSet = new StringDataSet(getOidString() + "." + index);
            oidValues.put(index, dataSet);
        }
        dataSet.appendData(time, value);
    }
}
