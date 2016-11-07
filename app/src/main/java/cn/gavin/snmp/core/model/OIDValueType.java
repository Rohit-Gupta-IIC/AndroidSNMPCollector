/*
 * OIDValueType.java
 * Date: 3/31/2015
 * Time: 8:52 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/
package cn.gavin.snmp.core.model;

public enum OIDValueType {
    String,
    TimeTicks,
    INTEGER,
    TimeStamp,
    Gauge32,
    Gauge64,
    PhysAddress,
    Counter32,
    Counter64,
    NetworkAddress,
    ERROR,
    TimeOut
}
