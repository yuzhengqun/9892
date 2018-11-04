package com.domain.icp.schema.model;

import java.util.Date;

public class JumingSiteModel {

	private String punycode;
	private String soldUrl;
	private String sellerId;
	private Date expireDate;
	private Integer price;
	private String icpJoinUp;
	private Date updateDate;

	public String getPunycode() {
		return punycode;
	}

	public void setPunycode(String punycode) {
		this.punycode = punycode;
	}

	public String getSoldUrl() {
		return soldUrl;
	}

	public void setSoldUrl(String soldUrl) {
		this.soldUrl = soldUrl;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getIcpJoinUp() {
		return icpJoinUp;
	}

	public void setIcpJoinUp(String icpJoinUp) {
		this.icpJoinUp = icpJoinUp;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	
}
