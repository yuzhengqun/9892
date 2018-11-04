package com.domain.icp.db.vo;

import java.util.Date;

public class DomainInfo {

	private String punycode;
	private String suffix;
	private String prefix;
	private String registrar;
	private Integer isIcp;
	private String icpNo;
	private Integer length;
	private String icpNature; // 备案性质: 1 企业 2 个人 3 社会团体 4 政府机关 5 其他
	private String siteTitle; // 网站标题
	private String registerDate;
	private String expireDate;
	private Integer qqStatus; // qq是否拦截 1 未拦截 0 已拦截
	private Integer wxStatus; // 微信是否拦截
	private Integer price;
	private String soldUrl;
	private Integer isRecommend; // 是否推荐 1 推荐
	private String icpJoinUp;
	private Integer icpInDependence; // 是否独立备案 1 独立备案 0 共享备案
	private Integer status;
	private String description;
	private String sellerId; // 卖家id
	private String icpDate; // 备案日期
	private String icpArea; // 备案地区
	private String icpUser;
	private String wxStatusUrl;
	private String qqStatusUrl;
	private Date createTime;
	private Date updateTime;
	private Integer dnsParser;

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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getRegistrar() {
		return registrar;
	}

	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}

	public Integer getIsIcp() {
		return isIcp;
	}

	public void setIsIcp(Integer isIcp) {
		this.isIcp = isIcp;
	}

	public String getIcpNo() {
		return icpNo;
	}

	public void setIcpNo(String icpNo) {
		this.icpNo = icpNo;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getIcpNature() {
		return icpNature;
	}

	public void setIcpNature(String icpNature) {
		this.icpNature = icpNature;
	}

	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getQqStatus() {
		return qqStatus;
	}

	public void setQqStatus(Integer qqStatus) {
		this.qqStatus = qqStatus;
	}

	public Integer getWxStatus() {
		return wxStatus;
	}

	public void setWxStatus(Integer wxStatus) {
		this.wxStatus = wxStatus;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getSoldUrl() {
		return soldUrl;
	}

	public void setSoldUrl(String soldUrl) {
		this.soldUrl = soldUrl;
	}

	public Integer getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getIcpJoinUp() {
		return icpJoinUp;
	}

	public void setIcpJoinUp(String icpJoinUp) {
		this.icpJoinUp = icpJoinUp;
	}

	public Integer getIcpInDependence() {
		return icpInDependence;
	}

	public void setIcpInDependence(Integer icpInDependence) {
		this.icpInDependence = icpInDependence;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getIcpDate() {
		return icpDate;
	}

	public void setIcpDate(String icpDate) {
		this.icpDate = icpDate;
	}

	public String getIcpArea() {
		return icpArea;
	}

	public void setIcpArea(String icpArea) {
		this.icpArea = icpArea;
	}

	public String getIcpUser() {
		return icpUser;
	}

	public void setIcpUser(String icpUser) {
		this.icpUser = icpUser;
	}

	public String getWxStatusUrl() {
		return wxStatusUrl;
	}

	public void setWxStatusUrl(String wxStatusUrl) {
		this.wxStatusUrl = wxStatusUrl;
	}

	public String getQqStatusUrl() {
		return qqStatusUrl;
	}

	public void setQqStatusUrl(String qqStatusUrl) {
		this.qqStatusUrl = qqStatusUrl;
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

	public Integer getDnsParser() {
		return dnsParser;
	}

	public void setDnsParser(Integer dnsParser) {
		this.dnsParser = dnsParser;
	}

	@Override
	public String toString() {
		return "DomainInfo [punycode=" + punycode + ", suffix=" + suffix
				+ ", prefix=" + prefix + ", registrar=" + registrar
				+ ", isIcp=" + isIcp + ", icpNo=" + icpNo + ", length="
				+ length + ", icpNature=" + icpNature + ", siteTitle="
				+ siteTitle + ", registerDate=" + registerDate
				+ ", expireDate=" + expireDate + ", qqStatus=" + qqStatus
				+ ", wxStatus=" + wxStatus + ", price=" + price + ", soldUrl="
				+ soldUrl + ", isRecommend=" + isRecommend + ", icpJoinUp="
				+ icpJoinUp + ", icpInDependence=" + icpInDependence
				+ ", status=" + status + ", description=" + description + "]";
	}

}
