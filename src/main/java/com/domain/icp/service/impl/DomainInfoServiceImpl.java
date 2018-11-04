package com.domain.icp.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.domain.icp.db.dao.DomainInfoDao;
import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.model.PageModel;
import com.domain.icp.model.QueryModel;
import com.domain.icp.schema.model.JumingSiteModel;
import com.domain.icp.service.DomainInfoService;

@Service
public class DomainInfoServiceImpl implements DomainInfoService {

	@Autowired
	private DomainInfoDao domainInfoDao;

	public List<DomainInfo> getAllDomainInfo() {
		return domainInfoDao.getAllDomainInfo();
	}

	public List<DomainInfo> getRecommendDomainList() {
		return domainInfoDao.getRecommendDomainList();
	}

	public List<DomainInfo> getAliyunDomainList() {
		return domainInfoDao.getAliyunDomainList();
	}

	public List<DomainInfo> getTecentDomainList() {
		return domainInfoDao.getTecentDomainList();
	}

	public List<DomainInfo> getWXDomainList() {
		return domainInfoDao.getWXDomainList();
	}

	public PageModel getAliyunWXDomainList(Integer pageNo) {
		PageModel result = new PageModel();
		int pageSize = 50;
		Integer offset = (pageNo - 1) * pageSize;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("wxStatus", 1);
		param.put("icpJoinUp", "aliyun");
		param.put("offset", offset);
		param.put("limit", pageSize);
		List<DomainInfo> domainInfos = domainInfoDao.getDomainListByPage(param);
		Integer recordTotal = domainInfoDao.getDomainCount(param);
		result.setCurrentPage(pageNo);
		result.setDomainInfos(domainInfos);
		int pageTotal = (int) Math.ceil(recordTotal / (pageSize * 1d));
		result.setPageTotal(pageTotal);
		result.setRecordTotal(recordTotal);
		return result;
	}

	public PageModel getTencentWXDomainList(Integer pageNo) {
		PageModel result = new PageModel();
		int pageSize = 50;
		Integer offset = (pageNo - 1) * pageSize;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("wxStatus", 1);
		param.put("icpJoinUp", "tencent");
		param.put("offset", offset);
		param.put("limit", pageSize);
		List<DomainInfo> domainInfos = domainInfoDao.getDomainListByPage(param);
		Integer recordTotal = domainInfoDao.getDomainCount(param);
		result.setCurrentPage(pageNo);
		result.setDomainInfos(domainInfos);
		int pageTotal = (int) Math.ceil(recordTotal / (pageSize * 1d));
		result.setPageTotal(pageTotal);
		result.setRecordTotal(recordTotal);
		return result;
	}

	public PageModel getAliyunDomainList(Integer pageNo) {
		PageModel result = new PageModel();
		int pageSize = 50;
		Integer offset = (pageNo - 1) * pageSize;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("icpJoinUp", "aliyun");
		param.put("offset", offset);
		param.put("limit", pageSize);
		List<DomainInfo> domainInfos = domainInfoDao.getDomainListByPage(param);
		Integer recordTotal = domainInfoDao.getDomainCount(param);
		result.setCurrentPage(pageNo);
		result.setDomainInfos(domainInfos);
		int pageTotal = (int) Math.ceil(recordTotal / (pageSize * 1d));
		result.setPageTotal(pageTotal);
		result.setRecordTotal(recordTotal);
		return result;
	}

	public PageModel getTencentDomainList(Integer pageNo) {
		PageModel result = new PageModel();
		int pageSize = 50;
		Integer offset = (pageNo - 1) * pageSize;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("icpJoinUp", "tencent");
		param.put("offset", offset);
		param.put("limit", pageSize);
		List<DomainInfo> domainInfos = domainInfoDao.getDomainListByPage(param);
		Integer recordTotal = domainInfoDao.getDomainCount(param);
		result.setCurrentPage(pageNo);
		result.setDomainInfos(domainInfos);
		int pageTotal = (int) Math.ceil(recordTotal / (pageSize * 1d));
		result.setPageTotal(pageTotal);
		result.setRecordTotal(recordTotal);
		return result;
	}

	public void saveSpiderData(List<JumingSiteModel> jumingSiteModels) {
		for (JumingSiteModel jumingSiteModel : jumingSiteModels) {
			String punycode = jumingSiteModel.getPunycode();
			DomainInfo domainInfo = domainInfoDao
					.getDomainInfoByPunycode(punycode);
			if (domainInfo != null) {
				// update
				// domainInfo.setExpireDate(jumingSiteModel.getExpireDate());
				domainInfo.setSellerId(jumingSiteModel.getSellerId());
				domainInfo.setSoldUrl(jumingSiteModel.getSoldUrl());
				domainInfo.setPrice(jumingSiteModel.getPrice());
				domainInfo.setIcpJoinUp(jumingSiteModel.getIcpJoinUp());
				domainInfo.setUpdateTime(jumingSiteModel.getUpdateDate());
				domainInfoDao.update(domainInfo);
			} else {
				// insert
				domainInfo = new DomainInfo();
				domainInfo.setPunycode(punycode);
				int index = punycode.indexOf(".");
				String prefix = punycode.substring(0, index);
				String suffix = punycode.substring(index, punycode.length());
				int length = prefix.length();
				domainInfo.setPrefix(prefix);
				domainInfo.setSuffix(suffix);
				domainInfo.setLength(length);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				domainInfo.setExpireDate(sdf.format(jumingSiteModel
						.getExpireDate()));
				domainInfo.setSellerId(jumingSiteModel.getSellerId());
				domainInfo.setSoldUrl(jumingSiteModel.getSoldUrl());
				domainInfo.setPrice(jumingSiteModel.getPrice());
				domainInfo.setIcpJoinUp(jumingSiteModel.getIcpJoinUp());
				domainInfo.setUpdateTime(jumingSiteModel.getUpdateDate());
				domainInfoDao.save(domainInfo);
			}
		}
	}

	public List<DomainInfo> getIndexDomainList() {
		List<DomainInfo> result = new ArrayList<DomainInfo>();
		// 获取20个阿里云过微信备案域名,80阿里云备案域名
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("wxStatus", 1);
		paramMap.put("icpJoinUp", "aliyun");
		paramMap.put("limit", 20);
		List<DomainInfo> aliyunList = domainInfoDao
				.getRecommendDomainListByCondition(paramMap);
		// 获取阿里云备案微信拦截域名域名
		// paramMap.put("wxStatus", 0);
		paramMap.remove("wxStatus");
		paramMap.put("icpJoinUp", "aliyun");
		paramMap.put("limit", 100 - aliyunList.size());
		List<DomainInfo> wxFilterList = domainInfoDao
				.getRecommendDomainListByCondition(paramMap);
		result.addAll(aliyunList);
		result.addAll(wxFilterList);
		return result;
	}

	public PageModel getAllDomainList(Integer pageNo, QueryModel queryModel) {
		Integer offset = (pageNo - 1) * queryModel.pageSize;
		queryModel.setOffset(offset);
		List<DomainInfo> domainInfos = domainInfoDao
				.getAllDomainList(queryModel);
		Integer recordTotal = domainInfoDao.getAllDomainCount(queryModel);
		int pageTotal = (int) Math.ceil(recordTotal
				/ (queryModel.getPageSize() * 1d));
		PageModel result = new PageModel();
		result.setCurrentPage(pageNo);
		result.setDomainInfos(domainInfos);
		result.setPageTotal(pageTotal);
		result.setRecordTotal(recordTotal);
		return result;
	}

	public List<DomainInfo> getDomainListForCrontab(Map<String, Object> paramMap) {
		return domainInfoDao.getDomainListForCrontab(paramMap);
	}

	public void batchUpdateDomainInfo(List<DomainInfo> domainInfoList) {
		domainInfoDao.batchUpdateDomainInfo(domainInfoList);
	}

	public DomainInfo getDomainInfo(String punycode) {
		return domainInfoDao.getDomainInfoByPunycode(punycode);
	}

	public void updateStatusByUpdateTime(Date updateTime) {
		domainInfoDao.updateStatusByUpdateTime(updateTime);
	}

	public List<DomainInfo> getWhoisTasks(Map<String, Object> paramMap) {
		return domainInfoDao.getWhoisTasks(paramMap);
	}
}
