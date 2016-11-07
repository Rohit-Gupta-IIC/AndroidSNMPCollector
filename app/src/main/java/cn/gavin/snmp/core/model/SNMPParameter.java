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
public class SNMPParameter {
    private int port = 161;
    private SNMPVersion version;
    private String community;
    private int trapPort = 162;
    private String ip;
    private String authentication;
    private String privacy;
    private Protocol authProtocol;
    private Protocol privacyProtocol;
    private int retry = 3;
    private long timeout = 3000;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SNMPVersion getVersion() {
        return version;
    }

    public void setVersion(SNMPVersion version) {
        this.version = version;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
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

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Protocol getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(Protocol authProtocol) {
        this.authProtocol = authProtocol;
    }

    public Protocol getPrivacyProtocol() {
        return privacyProtocol;
    }

    public void setPrivacyProtocol(Protocol privacyProtocol) {
        this.privacyProtocol = privacyProtocol;
    }

    public void setUserName(String user){
        this.community = user;
    }
    public String getUserName(){
        return community;
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
