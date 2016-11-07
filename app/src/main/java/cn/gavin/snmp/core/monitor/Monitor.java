/*
 * Monitor.java
 * Date: 3/30/2015
 * Time: 9:05 AM
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
 */
package cn.gavin.snmp.core.monitor;

import cn.gavin.snmp.core.model.Oid;

import java.util.Set;

public interface Monitor {
    /**
     * Get those oid use for this monitor
     * @return
     */
    public Set<Oid> getOIDs();

    /**
     * Build the data and set value, sometime need calculate, which this monitor need
     * @param time The data collect time
     */
    public void build(Long time);
}
