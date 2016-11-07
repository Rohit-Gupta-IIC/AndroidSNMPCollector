/*
 * DataQuerier.java
 * Date: 4/15/2015
 * Time: 2:51 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/
package cn.gavin.snmp.core.service;

public abstract class DataQuerier implements Runnable{
    public abstract void query();
    public void run(){
        query();
    }
}
