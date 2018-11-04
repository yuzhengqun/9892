package com.domain.icp.db.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.model.QueryModel;

public interface DomainInfoDao {

	List<DomainInfo> getAllDomainInfo();

	List<DomainInfo> getRecommendDomainList();

	List<DomainInfo> getAliyunDomainList();

	List<DomainInfo> getTecentDomainList();

	List<DomainInfo> getWXDomainList();

	List<DomainInfo> getAliyunWXDomainList();

	DomainInfo getDomainInfoByPunycode(String punycode);

	void update(DomainInfo domainInfo);

	void save(DomainInfo domainInfo);

	List<DomainInfo> getRecommendDomainListByCondition(Map<String, Object> param);

	List<DomainInfo> getDomainListByPage(Map<String, Object> param);

	Integer getDomainCount(Map<String, Object> param);
	
	List<DomainInfo> getAllDomainList(QueryModel queryModel);
	
	Integer getAllDomainCount(QueryModel queryModel);
	
	List<DomainInfo> getDomainListForCrontab(Map<String,Object> paramMap);
	
	void batchUpdateDomainInfo(@Param("domainInfoList") List<DomainInfo> domainInfoList);
	
	void updateStatusByUpdateTime(Date updateTime);
	
	List<DomainInfo> getWhoisTasks(Map<String,Object> paramMap);
	
}
