package com.domain.icp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.model.PageModel;
import com.domain.icp.model.QueryModel;
import com.domain.icp.service.DomainInfoService;

@Controller
public class DomainInfoController {

	@Autowired
	private DomainInfoService domainInfoService;

	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
	public String index(ModelMap map) {
		List<DomainInfo> indexDomainList = domainInfoService
				.getIndexDomainList();
		map.put("records", indexDomainList);
		return "index";
	}

	@RequestMapping("/all/{page}")
	public String getAllDomainList(@PathVariable("page") Integer page,
			QueryModel queryModel, ModelMap map) {
		map.put("queryModel", queryModel);
		PageModel pageMode = domainInfoService.getAllDomainList(page,
				queryModel);
		map.put("pageModel", pageMode);
		return "all";
	}

	@RequestMapping("/aliyun_wx/{page}")
	public String getAliyunWXDomainList(@PathVariable("page") Integer page,
			ModelMap map) {
		PageModel pageModel = domainInfoService.getAliyunWXDomainList(page);
		map.put("pageModel", pageModel);
		return "aliyun_wx";
	}

	@RequestMapping("/tencent_wx/{page}")
	public String getTencentWXDomainList(@PathVariable("page") Integer page,
			ModelMap map) {
		PageModel pageModel = domainInfoService.getTencentWXDomainList(page);
		map.put("pageModel", pageModel);
		return "tencent_wx";
	}

	@RequestMapping("/tencent/{page}")
	public String getTencentDomainList(@PathVariable("page") Integer page,
			ModelMap map) {
		PageModel pageModel = domainInfoService.getTencentDomainList(page);
		map.put("pageModel", pageModel);
		return "tencent";
	}

	@RequestMapping("/aliyun/{page}")
	public String getAliyunDomainList(@PathVariable("page") Integer page,
			ModelMap map) {
		PageModel pageModel = domainInfoService.getAliyunDomainList(page);
		map.put("pageModel", pageModel);
		return "aliyun";
	}

	@RequestMapping("/recommend/{page}")
	public String getRecommendDomainList(@PathVariable("page") Integer page,
			ModelMap map) {
		List<DomainInfo> recommends = domainInfoService
				.getRecommendDomainList();
		map.put("recommends", recommends);
		map.put("total", recommends.size());
		return "recommend";
	}

	@RequestMapping("/detail/{punycode:.+}")
	public String getAliyunDomainList(
			@PathVariable("punycode") String punycode, String path, ModelMap map) {
		DomainInfo domainInfo = domainInfoService.getDomainInfo(punycode);
		map.put("domainInfo", domainInfo);
		map.put("path", path);
		map.put("pathHref", this.getPathHref(path));
		return "detail";
	}

	private String getPathHref(String path) {
		String result = "/";
		if ("全部域名".equals(path)) {
			result = "/all/1";
		} else if ("阿里云微信未拦截".equals(path)) {
			result = "/aliyun_wx/1";
		} else if ("腾讯云微信未拦截".equals(path)) {
			result = "/tencent_wx/1";
		} else if ("阿里云接入域名".equals(path)) {
			result = "/aliyun/1";
		} else if ("腾讯云接入域名".equals(path)) {
			result = "/tencent/1";
		}
		return result;
	}
}
