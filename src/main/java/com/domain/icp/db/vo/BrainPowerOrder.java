package com.domain.icp.db.vo;

import java.util.Date;

public class BrainPowerOrder {

    private String punycode;
    private String suffix;
    private String orderDate;
    private Integer type;
    private Integer isIcp;
    private String icpNo;
    private String icpEntity;
    private String icpNature;
    private Integer icpDL;
    private Integer wxStatus;
    private String wxStatusUrl;
    private Integer qqStatus;
    private Integer absmiddle;
    private Integer orderStatus;
    private Integer price;
    private Integer isLead;
    private Integer auctionStatus;
    private Date auctionEndTime;
    private Integer giveUpBid;
    private Integer dnsParseStatus;
    private Date createTime;
    private Date updateTime;

    public String getPunycode() {
        return punycode;
    }

    public void setPunycode(String punycode) {
        this.punycode = punycode;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcpNo() {
        return icpNo;
    }

    public void setIcpNo(String icpNo) {
        this.icpNo = icpNo;
    }

    public Integer getIcpDL() {
        return icpDL;
    }

    public void setIcpDL(Integer icpDL) {
        this.icpDL = icpDL;
    }

    public Integer getWxStatus() {
        return wxStatus;
    }

    public void setWxStatus(Integer wxStatus) {
        this.wxStatus = wxStatus;
    }

    public Integer getQqStatus() {
        return qqStatus;
    }

    public void setQqStatus(Integer qqStatus) {
        this.qqStatus = qqStatus;
    }

    public Integer getAbsmiddle() {
        return absmiddle;
    }

    public void setAbsmiddle(Integer absmiddle) {
        this.absmiddle = absmiddle;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIsLead() {
        return isLead;
    }

    public void setIsLead(Integer isLead) {
        this.isLead = isLead;
    }

    public Integer getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(Integer auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public Date getAuctionEndTime() {
        return auctionEndTime;
    }

    public void setAuctionEndTime(Date auctionEndTime) {
        this.auctionEndTime = auctionEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsIcp() {
        return isIcp;
    }

    public void setIsIcp(Integer isIcp) {
        this.isIcp = isIcp;
    }

    public String getIcpEntity() {
        return icpEntity;
    }

    public void setIcpEntity(String icpEntity) {
        this.icpEntity = icpEntity;
    }

    public String getIcpNature() {
        return icpNature;
    }

    public void setIcpNature(String icpNature) {
        this.icpNature = icpNature;
    }

    public String getWxStatusUrl() {
        return wxStatusUrl;
    }

    public void setWxStatusUrl(String wxStatusUrl) {
        this.wxStatusUrl = wxStatusUrl;
    }

    public Integer getGiveUpBid() {
        return giveUpBid;
    }

    public void setGiveUpBid(Integer giveUpBid) {
        this.giveUpBid = giveUpBid;
    }

    public Integer getDnsParseStatus() {
        return dnsParseStatus;
    }

    public void setDnsParseStatus(Integer dnsParseStatus) {
        this.dnsParseStatus = dnsParseStatus;
    }

    @Override
    public String toString() {
        return "BrainPowerOrder{" +
                "punycode='" + punycode + '\'' +
                ", suffix='" + suffix + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", type=" + type +
                ", icpNo='" + icpNo + '\'' +
                ", icpDL=" + icpDL +
                ", wxStatus=" + wxStatus +
                ", qqStatus=" + qqStatus +
                ", absmiddle=" + absmiddle +
                ", orderStatus=" + orderStatus +
                ", price=" + price +
                ", isLead=" + isLead +
                ", auctionStatus=" + auctionStatus +
                ", auctionEndTime=" + auctionEndTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
