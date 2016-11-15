/*
 * SNMPParameter.java
 * Date: 4/27/2015
 * Time: 10:11 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package cn.gavin.snmp.core.model;
/*
* The SNMP parameter bean class<br>
*     If you want to init the snmp util, you should set those snmp config,the default snmp port are 163
 */
public class SNMPParameter extends Credential {
    private int port = 161;

    private int trapPort = 162;
    private String ip;

    private int retry = 3;
    private long timeout = 3000;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTrapPort() {
        return trapPort;
    }

    public void setTrapPort(int trapPort) {
        this.trapPort = trapPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
