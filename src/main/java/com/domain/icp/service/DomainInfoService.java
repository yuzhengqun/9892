package com.domain.icp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.model.PageModel;
import com.domain.icp.model.QueryModel;
import com.domain.icp.schema.model.JumingSiteModel;

public interface DomainInfoService {

	List<DomainInfo> getAllDomainInfo();

	List<DomainInfo> getRecommendDomainList();

	List<DomainInfo> getAliyunDomainList();

	List<DomainInfo> getTecentDomainList();

	List<DomainInfo> getWXDomainList();

	PageModel getAliyunWXDomainList(Integer pageNo);

	PageModel getTencentWXDomainList(Integer pageNo);

	PageModel getAliyunDomainList(Integer pageNo);

	PageModel getTencentDomainList(Integer pageNo);

	void saveSpiderData(List<JumingSiteModel> jumingSiteModels);

	List<DomainInfo> getIndexDomainList();

	PageModel getAllDomainList(Integer pageNo, QueryModel queryModel);

	List<DomainInfo> getDomainListForCrontab(Map<String,Object> paramMap);
	
	void  batchUpdateDomainInfo(List<DomainInfo> domainInfoList);
	
	DomainInfo getDomainInfo(String punycode);
	
	void updateStatusByUpdateTime(Date updateTime);
	
	List<DomainInfo> getWhoisTasks(Map<String,Object> paramMap);
}
