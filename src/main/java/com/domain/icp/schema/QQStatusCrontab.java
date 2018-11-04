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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.service.DomainInfoService;
import com.domain.icp.util.JumingCookieUtil;

@Component
public class QQStatusCrontab {
	private static Logger logger = LoggerFactory
			.getLogger(QQStatusCrontab.class);

	@Autowired
	private DomainInfoService domainInfoService;

	// 每次取1000条记录查询
	private Integer pageSize = 50;

	// @Scheduled(cron = "0 0/30 * * * *")
	public void exe() {
		logger.info("check qq status begin.");
		// 尚未查询qq状态的列表
		this.exeStatus(2);
		logger.info("check qq status end.");
	}

	public void exeStatus(int status) {
		if (JumingCookieUtil.getJMCookie() == null) {
			return;
		}
		// 从db获取
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isIcp", 1);
		paramMap.put("qqStatus", status);
		paramMap.put("limit", pageSize);
		int page = 1;
		while ((true)) {
			paramMap.put("offset", (page - 1) * pageSize);
			List<DomainInfo> domainInfos = domainInfoService
					.getDomainListForCrontab(paramMap);
			List<DomainInfo> domainInfoList = new ArrayList<DomainInfo>();
			for (DomainInfo domainInfo : domainInfos) {
				try {
					this.getWxStatus(domainInfo);
					domainInfoList.add(domainInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
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

	private void getWxStatus(DomainInfo domainInfo) throws Exception {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		String line = domainInfo.getQqStatusUrl();
		HttpGet httpGet = new HttpGet("http://www.juming.com" + line);
		httpGet.setHeader("Host", "www.juming.com");
		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:62.0) Gecko/20100101 Firefox/62.0");
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language",
				"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate");
		httpGet.setHeader("Referer",
				"http://www.juming.com/?tt=0&t=tiao_juming.com");
		httpGet.setHeader("Cookie", JumingCookieUtil.getJMCookie());
		httpGet.setHeader("Connection", " keep-alive");
		httpGet.setHeader("Upgrade-Insecure-Requests", "1");
		httpGet.setHeader("Cache-Control", "max-age=0, no-cache");
		httpGet.setHeader("Pragma", "no-cache");
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String response = EntityUtils.toString(responseEntity, "gbk");
		Document doc = Jsoup.parse(response);
		String content = doc.selectFirst("a").text();
		if ("未知".equals(content)) {
			Thread.sleep(1000 * 3);
			return;
		}
		if ("未拦截".equals(content)) {
			domainInfo.setQqStatus(1);
		} else if ("已拦截".equals(content)) {
			domainInfo.setQqStatus(0);
		}
	}
}
