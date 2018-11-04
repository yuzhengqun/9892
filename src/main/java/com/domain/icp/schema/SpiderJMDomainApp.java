package com.domain.icp.schema;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.domain.icp.schema.model.JumingSiteModel;
import com.domain.icp.service.DomainInfoService;

@Component
public class SpiderJMDomainApp {

	private static Integer pageSize = 500;

	@Autowired
	private DomainInfoService domainInfoService;

	public static void main(String[] args) throws Exception {
		SpiderJMDomainApp spiderApp = new SpiderJMDomainApp();
		Date updateTime = new Date();
		// spiderApp.spiderAliyun();
		spiderApp.spiderCommon(updateTime);
	}

//	 @Scheduled(cron = "0/2 * * * * *")
	public void spiderCommon(Date updateTime) throws Exception {
		int priceBegin = 0;
		int priceEnd = 2000;
		int step = 20;
		int times = (priceEnd - priceBegin) / step;
		String baseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
		List<JumingSiteModel> jumingSiteModels = new ArrayList<JumingSiteModel>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		for (int i = 0; i < times; i++) {
			int priceMin = priceBegin + i * step;
			int priceMax = priceBegin + (i + 1) * step;
			int page = 1;
			while ((true)) {
				String url = baseUrl
						.replace("{priceMin}", String.valueOf(priceMin))
						.replace("{priceMax}", String.valueOf(priceMax))
						.replace("{page}", String.valueOf(page));
				String response = this.doGetForReturnString(httpClient, url,
						new HashMap<String, String>());
				List<JumingSiteModel> list = this.parseSitePage(response, null,
						updateTime);
				jumingSiteModels.addAll(list);
				System.out.println("min price:" + priceMin + ",max price:"
						+ priceMax + "page no:" + page + ", total:"
						+ jumingSiteModels.size());
				if (list.size() < pageSize) {
					break;
				}
				page++;
				Thread.sleep(1000 * 3);
			}
		}
		domainInfoService.saveSpiderData(jumingSiteModels);
	}

//	@Scheduled(cron = "0 50 7 * * *")
	@Scheduled(cron = "0/2 * * * * *")
	public void spider() {
		System.out.println("spider begin.");
		Date updateTime = new Date();
		try {
			// aliyun
			String aliyunBaseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1999&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
			this.spider(aliyunBaseUrl, 0, 500, 20, "aliyun", updateTime);
			System.out.println("aliyun finished.");
			// tencent
			String tencentBaseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1995&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
			this.spider(tencentBaseUrl, 0, 500, 100, "tencent", updateTime);
			System.out.println("tencent finished.");
			// all
			String baseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
			this.spider(baseUrl, 0, 100, 5, null, updateTime);
			System.out.println("0 100 finished");
			this.spider(baseUrl, 100, 2000, 20, null, updateTime);
			System.out.println("finished.");
			domainInfoService.updateStatusByUpdateTime(updateTime);
			System.out.println("update status finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void spider(String baseUrl, int priceBegin, int priceEnd, int step,
			String icpJoinUp, Date updateTime) throws Exception {

		int times = (priceEnd - priceBegin) / step;
		List<JumingSiteModel> jumingSiteModels = new ArrayList<JumingSiteModel>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		for (int i = 0; i < times; i++) {
			int priceMin = priceBegin + i * step;
			int priceMax = priceBegin + (i + 1) * step;
			int page = 1;
			while ((true)) {
				String url = baseUrl
						.replace("{priceMin}", String.valueOf(priceMin))
						.replace("{priceMax}", String.valueOf(priceMax))
						.replace("{page}", String.valueOf(page));
				String response = this.doGetForReturnString(httpClient, url,
						new HashMap<String, String>());
				List<JumingSiteModel> list = this.parseSitePage(response,
						icpJoinUp, updateTime);
				jumingSiteModels.addAll(list);
				System.out.println("min price:" + priceMin + ",max price:"
						+ priceMax + "page no:" + page + ", total:"
						+ jumingSiteModels.size());
				if (list.size() < pageSize) {
					break;
				}
				page++;
				Thread.sleep(1000 * 3);
			}
		}
		domainInfoService.saveSpiderData(jumingSiteModels);
	}

	// @Scheduled(cron = "0 42 21 * * ? *")
	// @Scheduled(cron = "0/2 * * * * *")
	public void spiderAliyun(Date updateTime) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String baseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1999&dqsj=300&ymlx=0&jgpx=0&meiye="
				+ pageSize + "&page=";

		List<JumingSiteModel> jumingSiteModels = new ArrayList<JumingSiteModel>();
		int page = 1;
		while ((true)) {
			String url = baseUrl + page;
			String response = this.doGetForReturnString(httpClient, url,
					new HashMap<String, String>());
			List<JumingSiteModel> list = this.parseSitePage(response, null,
					updateTime);
			jumingSiteModels.addAll(list);
			System.out.println("page no:" + page + ", total:"
					+ jumingSiteModels.size());
			if (list.size() < pageSize) {
				break;
			}
			page++;
			Thread.sleep(1000 * 3);
		}
		domainInfoService.saveSpiderData(jumingSiteModels);
	}

	private String doGetForReturnString(CloseableHttpClient httpClient,
			String url, Map<String, String> heardMap) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		for (String heard : heardMap.keySet()) {
			httpGet.setHeader(heard, heardMap.get(heard));
		}
		HttpResponse result = httpClient.execute(httpGet);
		return EntityUtils.toString(result.getEntity(), "utf-8");
	}

	private List<JumingSiteModel> parseSitePage(String response,
			String icpJoinUp, Date updateTime) {
		List<JumingSiteModel> result = new ArrayList<JumingSiteModel>();
		try {
			Document doc = Jsoup.parse(response);
			Elements trElements = doc
					.select("body > form > div > table > tbody > tr");
			JumingSiteModel jumingSiteModel = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Element trElement : trElements) {
				Elements tdElements = trElement.select("td");
				if (tdElements != null && tdElements.size() != 0) {
					Element punycodeElement = tdElements.get(0).selectFirst(
							"div > a");
					if (punycodeElement == null) {
						continue;
					}
					String punycode = punycodeElement.text();
					String soldUrl = punycodeElement.attr("href");
					String sellerUrl = null;
					try {
						sellerUrl = tdElements.get(3).selectFirst("a")
								.attr("href");
					} catch (Exception e) {

					}
					String expireDate = tdElements.get(4).text();
					String price = tdElements.get(5).text().replace("元", "");
					jumingSiteModel = new JumingSiteModel();
					jumingSiteModel.setPrice(Integer.parseInt(price));
					jumingSiteModel.setPunycode(punycode);
					jumingSiteModel.setExpireDate(sdf.parse(expireDate));
					jumingSiteModel.setSoldUrl(soldUrl);
					jumingSiteModel.setIcpJoinUp(icpJoinUp);
					jumingSiteModel.setUpdateDate(updateTime);
					String[] array = sellerUrl.split("/");
					jumingSiteModel.setSellerId(array[array.length - 1]);
					result.add(jumingSiteModel);
				}
			}
		} catch (Exception e) {

		}
		return result;
	}
}
