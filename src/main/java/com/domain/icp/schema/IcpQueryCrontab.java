package com.domain.icp.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.service.DomainInfoService;
import com.domain.icp.util.JumingCookieUtil;

@Component
public class IcpQueryCrontab {

	
	@Autowired
	private DomainInfoService domainInfoService;

	// 每次取1000条记录查询
	private Integer pageSize = 50;

//	 @Scheduled(cron = "0 0/30 * * * *")
//	@Scheduled(cron = "0/2 * * * * *")
	public void exe() {
		if (JumingCookieUtil.getJMCookie() == null) {
			return;
		}
		// 从db获取没有备案信息的域名
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isIcp", 2);
		paramMap.put("limit", pageSize);
		int page = 1;
		while ((true)) {
			paramMap.put("offset", (page - 1) * pageSize);
			List<DomainInfo> domainInfos = domainInfoService
					.getDomainListForCrontab(paramMap);
			List<DomainInfo> domainInfoList = new ArrayList<DomainInfo>();
			for (DomainInfo domainInfo : domainInfos) {
				try {
					this.getIcpInfo(domainInfo);
					domainInfoList.add(domainInfo);
				} catch (Exception e) {
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			// batch update domain_info
			try {
				domainInfoService.batchUpdateDomainInfo(domainInfoList);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (domainInfos.size() < pageSize) {
				break;
			}
			page++;
		}
	}

	private void getIcpInfo(DomainInfo domainInfo) throws Exception {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		String punycode = domainInfo.getPunycode().toLowerCase();
		HttpGet httpGet = new HttpGet("http://www.juming.com/hao/?cha_ym="
				+ punycode);
		httpGet.setHeader("Host", "www.juming.com");
		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language",
				"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Referer", "http://www.juming.com/");
		httpGet.setHeader("Cookie", JumingCookieUtil.getJMCookie());
		httpGet.setHeader("Connection", " keep-alive");
		httpGet.setHeader("Upgrade-Insecure-Requests", "1");
		httpGet.setHeader("Cache-Control", "max-age=0, no-cache");
		httpGet.setHeader("Pragma", "no-cache");
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String response = EntityUtils.toString(responseEntity, "gbk");
		String indexStr = "/hao/index.htm?chaqqljapi=1&cha_ym=" + punycode;
		int qqIndex = response.indexOf(indexStr);
		String qqUrl = response.substring(qqIndex, qqIndex + indexStr.length()
				+ 39);
		int wxIndex = response.lastIndexOf(indexStr);
		String wxUrl = response.substring(wxIndex, wxIndex + indexStr.length()
				+ 45);
		Document doc = Jsoup.parse(response);
		// String punycode =
		// doc.selectFirst("div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1)").text();
		String icpNo = doc
				.selectFirst(
						"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)")
				.text();
		domainInfo.setIcpNo(icpNo);
		if ("未备案".equals(icpNo)) {
			domainInfo.setIsIcp(0);
		} else {
			domainInfo.setIsIcp(1);
			String icpUser = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3)")
					.text();
			String icpNature = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4)")
					.text();
			String siteTitle = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(1)")
					.text();
			String baTime = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2)")
					.text();
			domainInfo.setIcpUser(icpUser);
			domainInfo.setIcpNature(icpNature);
			domainInfo.setSiteTitle(siteTitle);
			if (baTime != null && !"".equals(baTime.trim())) {
				domainInfo.setIcpDate(baTime);
			}
			domainInfo.setWxStatusUrl(wxUrl);
			domainInfo.setQqStatusUrl(qqUrl);
		}
	}
}
