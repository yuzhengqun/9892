package com.domain.icp.model;

/**
 * whois信息
 */
public class WhoisInfo {

    private String punycode;
    //注册人
    private String registrant;
    private String registrar;
    private String registrantEmail;
    private String dnsServer;
    private String createTime;
    private String expireTime;
    private String updateTime;

    public String getPunycode() {
        return punycode;
    }

    public void setPunycode(String punycode) {
        this.punycode = punycode;
    }

    public String getRegistrant() {
        return registrant;
    }

    public void setRegistrant(String registrant) {
        this.registrant = registrant;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getRegistrantEmail() {
        return registrantEmail;
    }

    public void setRegistrantEmail(String registrantEmail) {
        this.registrantEmail = registrantEmail;
    }

    public String getDnsServer() {
        return dnsServer;
    }

    public void setDnsServer(String dnsServer) {
        this.dnsServer = dnsServer;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WhoisInfo{" +
                "punycode='" + punycode + '\'' +
                ", registrant='" + registrant + '\'' +
                ", registrar='" + registrar + '\'' +
                ", registrantEmail='" + registrantEmail + '\'' +
                ", dnsServer='" + dnsServer + '\'' +
                ", createTime='" + createTime + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
