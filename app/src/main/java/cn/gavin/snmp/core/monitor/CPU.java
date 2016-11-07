/*
 * CPU.java
 * Date: 4/3/2015
 * Time: 3:52 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package cn.gavin.snmp.core.monitor;

import cn.gavin.snmp.core.model.DataSet;
import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.model.TableColumnOid;
import cn.gavin.snmp.core.model.TableOid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class build a model for collect cpu info on a special host, use 1.3.6.1.2.1.25.3.3(processor table)
 */
public class CPU implements Monitor {

    private Set<Oid> oids = new HashSet<Oid>(Arrays.asList(new TableOid("1.3.6.1.2.1.25.3.3", "1.3.6.1.2.1.25.3.3.1.2")));
    private DataSet<Double> utilization = new DataSet<Double>(" ");

    @Override
    public Set<Oid> getOIDs() {
        return oids;
    }

    public void build(Long time){
        TableOid oid = (TableOid)oids.iterator().next();
        double utilization = 0d;
        double size = 0;
        TableColumnOid column = oid.getColumns()[0];
        for(String index : column.getIndex()){
            Double load = column.getValue(index);
            if(load!=null){
                utilization+=load;
                size++;
            }
        }
        if(size!=0){
            utilization = utilization/size;
        }
        this.utilization.appendData(time, utilization);
    }

    /**
     * Get cpu total utilization
     * <br>
     * If a device has multiply cpu, it will return the average utilization.
     * @return
     */
    public DataSet<Double> getUtilization(){
        return utilization;
    }
}
