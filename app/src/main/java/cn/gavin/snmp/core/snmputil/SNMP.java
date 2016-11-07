/*
 * SNMP.java
 * Date: 3/31/2015
 * Time: 8:39 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package cn.gavin.snmp.core.snmputil;

import cn.gavin.snmp.core.model.Oid;
import cn.gavin.snmp.core.model.TableOid;

public interface SNMP {
    public Oid get(Oid oid);
    public Oid walk(Oid oid);
    public Oid getNext(Oid oid);
    public TableOid getTable(TableOid table);
    public Oid[] get(Oid...oids);
    public Oid[] getNext(Oid...oids);
}
