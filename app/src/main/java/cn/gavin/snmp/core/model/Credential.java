package cn.gavin.snmp.core.model;

/**
 * Created by gluo on 11/15/2016.
 */
public class Credential {
    protected SNMPVersion version;
    protected String community;
    protected String authentication;
    protected String privacy;
    protected Protocol authProtocol;
    protected Protocol privacyProtocol;
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
