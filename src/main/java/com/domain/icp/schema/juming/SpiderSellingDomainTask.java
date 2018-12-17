package com.domain.icp.schema.juming;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.domain.icp.db.vo.DomainSelling;
import com.domain.icp.schema.model.JumingSiteModel;
import com.domain.icp.service.DomainSellingService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SpiderSellingDomainTask {

    private static Integer pageSize = 500;
    private static Integer platform = 1;//聚名

    private static final Logger logger = (Logger) LoggerFactory.getLogger(SpiderSellingDomainTask.class);


    @Autowired
    private DomainSellingService domainSellingService;


    @Scheduled(cron = "0/2 * * * * *")
    public void exe() {
        try {
            Date updateTime = new Date();
            Map<String, JumingSiteModel> map = new HashMap<>();
//            String baseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
//            map.putAll(this.spider(baseUrl, 0, 100, 5, null, updateTime));
//            logger.info("爬取0-100价格区间的域名成功");
//            map.putAll(this.spider(baseUrl, 100, 2000, 20, null, updateTime));
//            logger.info("爬取100-2000价格区间的域名成功");
            // tencent
            String tencentBaseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1995&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
            map.putAll(this.spider(tencentBaseUrl, 0, 500, 100, "tencent", updateTime));
            logger.info("爬取腾讯云备案域名成功");
            String aliyunBaseUrl = "http://www.juming.com/ykj/?api_sou=1&sfba=1999&ymlx=0&qian1={priceMin}&qian2={priceMax}&jgpx=0&meiye=500&page={page}";
            map.putAll(this.spider(aliyunBaseUrl, 0, 500, 20, "aliyun", updateTime));
            logger.info("爬取阿里云备案域名成功");

            File jumingSellingFile = new File("juming_selling.txt");
            if (!jumingSellingFile.exists()) {
                //第一次采集
                List<JumingSiteModel> addList = new ArrayList<>();
                for (String punycode : map.keySet()) {
                    addList.add(map.get(punycode));
                }
                List<DomainSelling> addDomainSellingList = this.getDomainSellingFromJumingSiteModel(addList);
                domainSellingService.batchSave(addDomainSellingList);
            } else {
                FileReader fr = new FileReader(jumingSellingFile);
                BufferedReader br = new BufferedReader(fr);
                String line = null;
                JSONObject jsonObject = null;
                Set<String> set = new HashSet<>();
                while ((line = br.readLine()) != null) {
                    jsonObject = JSONObject.parseObject(line);
                    set.add(jsonObject.getString("punycode"));
                }
                br.close();
                fr.close();
                //获取新增列表
                List<JumingSiteModel> addList = new ArrayList<>();
                List<JumingSiteModel> updateList = new ArrayList<>();
                for (String punycode : map.keySet()) {
                    if (!set.contains(punycode)) {
                        addList.add(map.get(punycode));
                    } else {
                        updateList.add(map.get(punycode));
                    }
                }
                List<DomainSelling> addDomainSellingList = this.getDomainSellingFromJumingSiteModel(addList);
                domainSellingService.batchSave(addDomainSellingList);
                List<DomainSelling> updateDomainSellingList = this.getDomainSellingFromJumingSiteModel(updateList);
                domainSellingService.batchUpdate(updateDomainSellingList);
                //获取删除列表
                List<String> deletePunycodeList = new ArrayList<>();
                for (String punycode : set) {
                    if (!map.containsKey(punycode)) {
                        deletePunycodeList.add(punycode);
                    }
                }
                domainSellingService.batchDelete(deletePunycodeList, platform);
            }
            FileWriter fw = new FileWriter("juming_selling.txt");
            for (String punycode : map.keySet()) {
                String json = JSON.toJSONString(map.get(punycode));
                fw.write(json + "\n");
            }
            fw.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private List<DomainSelling> getDomainSellingFromJumingSiteModel(List<JumingSiteModel> jumingSiteModelList) {
        List<DomainSelling> result = new ArrayList<>();
        DomainSelling domainSelling = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (JumingSiteModel jumingSiteModel : jumingSiteModelList) {
            domainSelling = new DomainSelling();
            String punycode = jumingSiteModel.getPunycode();
            int index = punycode.indexOf(".");
            String prefix = punycode.substring(0, index);
            String suffix = punycode.substring(index, punycode.length());
            domainSelling.setPunycode(punycode);
            domainSelling.setPrefix(prefix);
            domainSelling.setSuffix(suffix);
            domainSelling.setPrice(jumingSiteModel.getPrice());
            domainSelling.setIcpJoinUp(jumingSiteModel.getIcpJoinUp());
            domainSelling.setExpireDate(sdf.format(jumingSiteModel.getExpireDate()));
            domainSelling.setLength(prefix.length());
            domainSelling.setPlatform(platform);
            domainSelling.setSoldUrl(jumingSiteModel.getSoldUrl());
            domainSelling.setSellerId(jumingSiteModel.getSellerId());
            result.add(domainSelling);
        }
        return result;
    }


    private Map<String, JumingSiteModel> spider(String baseUrl, int priceBegin, int priceEnd, int step,
                                                String icpJoinUp, Date updateTime) throws Exception {

        Map<String, JumingSiteModel> result = new HashMap<>();
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
                for (JumingSiteModel jumingSiteModel : list) {
                    result.put(jumingSiteModel.getPunycode(), jumingSiteModel);
                }
                if (list.size() < pageSize) {
                    break;
                }
                page++;
                Thread.sleep(1000 * 1);
            }
        }
        return result;
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
                    String punycode = punycodeElement.text().toLowerCase();
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
