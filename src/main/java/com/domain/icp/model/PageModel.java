package com.domain.icp.model;

import java.util.ArrayList;
import java.util.List;

import com.domain.icp.db.vo.DomainInfo;

public class PageModel {
	private List<DomainInfo> domainInfos = new ArrayList<DomainInfo>();
	private Integer pageTotal = 1;
	private Integer recordTotal = 0;
	private Integer currentPage = 1;

	public List<DomainInfo> getDomainInfos() {
		return domainInfos;
	}

	public void setDomainInfos(List<DomainInfo> domainInfos) {
		this.domainInfos = domainInfos;
	}

	public Integer getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}

	public Integer getRecordTotal() {
		return recordTotal;
	}

	public void setRecordTotal(Integer recordTotal) {
		this.recordTotal = recordTotal;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
