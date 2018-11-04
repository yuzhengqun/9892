package com.domain.icp.model;

public class QueryModel {

	public String icpJoinUp = "-1";
	public String icpNature = "-1";
	public Integer icpIndependence = -1;
	public String icpArea = "-1";
	public Integer wxStatus = -1;
	public Integer qqStatus = -1;
	public String registrar = "-1";
	public String suffix = "-1";
	public Integer registerDate = -1;
	public Integer expireDate = -1;
	public String domainContains;
	public Integer domainContainsStart;
	public Integer domainContainsEnd;
	public String domainNotContains;
	public Integer domainNotContainsStart;
	public Integer domainNotContainsEnd;
	public Integer priceStart;
	public Integer priceEnd;
	public Integer dnsParser = -1;
	public Integer offset;
	public Integer pageSize = 50;
	public Integer limit;

	public String getIcpJoinUp() {
		return icpJoinUp;
	}

	public void setIcpJoinUp(String icpJoinUp) {
		this.icpJoinUp = icpJoinUp;
	}

	public String getIcpNature() {
		return icpNature;
	}

	public void setIcpNature(String icpNature) {
		this.icpNature = icpNature;
	}

	public Integer getIcpIndependence() {
		return icpIndependence;
	}

	public void setIcpIndependence(Integer icpIndependence) {
		this.icpIndependence = icpIndependence;
	}

	public String getIcpArea() {
		return icpArea;
	}

	public void setIcpArea(String icpArea) {
		this.icpArea = icpArea;
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

	public String getRegistrar() {
		return registrar;
	}

	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Integer getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Integer registerDate) {
		this.registerDate = registerDate;
	}

	public Integer getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Integer expireDate) {
		this.expireDate = expireDate;
	}

	public String getDomainContains() {
		return domainContains;
	}

	public void setDomainContains(String domainContains) {
		if ("".equals(domainContains) ||"null".equals(domainContains)) {
			domainContains = null;
		}
		this.domainContains = domainContains;
	}

	public Integer getDomainContainsStart() {
		return domainContainsStart;
	}

	public void setDomainContainsStart(Integer domainContainsStart) {
		this.domainContainsStart = domainContainsStart;
	}

	public Integer getDomainContainsEnd() {
		return domainContainsEnd;
	}

	public void setDomainContainsEnd(Integer domainContainsEnd) {
		this.domainContainsEnd = domainContainsEnd;
	}

	public String getDomainNotContains() {
		return domainNotContains;
	}

	public void setDomainNotContains(String domainNotContains) {
		if ("".equals(domainNotContains) || "null".equals(domainNotContains)) {
			domainNotContains = null;
		}
		this.domainNotContains = domainNotContains;
	}

	public Integer getDomainNotContainsStart() {
		return domainNotContainsStart;
	}

	public void setDomainNotContainsStart(Integer domainNotContainsStart) {
		this.domainNotContainsStart = domainNotContainsStart;
	}

	public Integer getDomainNotContainsEnd() {
		return domainNotContainsEnd;
	}

	public void setDomainNotContainsEnd(Integer domainNotContainsEnd) {
		this.domainNotContainsEnd = domainNotContainsEnd;
	}

	public Integer getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Integer priceStart) {
		this.priceStart = priceStart;
	}

	public Integer getPriceEnd() {
		return priceEnd;
	}

	public void setPriceEnd(Integer priceEnd) {
		this.priceEnd = priceEnd;
	}

	public Integer getDnsParser() {
		return dnsParser;
	}

	public void setDnsParser(Integer dnsParser) {
		this.dnsParser = dnsParser;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
