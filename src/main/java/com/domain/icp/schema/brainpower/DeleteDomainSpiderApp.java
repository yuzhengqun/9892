package com.domain.icp.schema.brainpower;

import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import com.domain.icp.util.JumingCookieUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 删除域名爬取
 */
public class DeleteDomainSpiderApp {


    @Autowired
    private BrainPowerService brainPowerService;

//    @Scheduled(cron = "0/2 * * * * *")
    public void spider() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        try {
            List<BrainPowerOrder> delCnList = this.spiderDelDomain("cn,com.cn", currentDate, JumingCookieUtil.getJMCookie());
            this.saveOrUpdateBrainPowerOrders(delCnList);
            List<BrainPowerOrder> delComList = this.spiderDelDomain("com", currentDate, JumingCookieUtil.getJMCookie());
            this.saveOrUpdateBrainPowerOrders(delComList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveOrUpdateBrainPowerOrders(List<BrainPowerOrder> brainPowerOrders) {
        for (BrainPowerOrder brainPowerOrder : brainPowerOrders) {
            brainPowerService.saveOrUpdate(brainPowerOrder);
        }
    }


    private List<BrainPowerOrder> spiderDelDomain(String suffixs, String deleteDate, String cookie) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.juming.com/6/index.htm?cha=1");
        //set headers
        httpPost.setHeader("Host", "www.juming.com");
        httpPost.setHeader("Cookie", cookie);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        httpPost.setHeader("Referer", "http://www.juming.com/6/?2018-11-25");
        httpPost.setHeader("Origin", "http://www.juming.com");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("zairushezhi", "0"));
        params.add(new BasicNameValuePair("ymgc", "0"));
        params.add(new BasicNameValuePair("ymcd_1", "1"));
        params.add(new BasicNameValuePair("ymcd_2", "0"));
        params.add(new BasicNameValuePair("ymgcte", ""));
        params.add(new BasicNameValuePair("ymhzfs", "0"));
        params.add(new BasicNameValuePair("ymhz", suffixs));
        params.add(new BasicNameValuePair("sfba_1", "1"));
        params.add(new BasicNameValuePair("wxjc", "1"));
        params.add(new BasicNameValuePair("qqjc", "1"));
        params.add(new BasicNameValuePair("bdpj", ""));
        params.add(new BasicNameValuePair("tsym", "0"));
        params.add(new BasicNameValuePair("pr_1", "0"));
        params.add(new BasicNameValuePair("pr_2", "0"));
        params.add(new BasicNameValuePair("bdsl_1", "0"));
        params.add(new BasicNameValuePair("bdsl_2", "0"));
        params.add(new BasicNameValuePair("bdfl_1", "0"));
        params.add(new BasicNameValuePair("bdfl_2", "0"));
        params.add(new BasicNameValuePair("sclx", "2"));
        params.add(new BasicNameValuePair("scsj", deleteDate));
        params.add(new BasicNameValuePair("mysc", "8"));
        params.add(new BasicNameValuePair("jgpx", "0"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String responseStr = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        Document doc = Jsoup.parse(responseStr);
        Elements trElements = doc.select("body > form > div > table > tbody > tr");
        List<BrainPowerOrder> result = new ArrayList<>();
        BrainPowerOrder brainPowerOrder = null;
        for (int i = 0; i < trElements.size() - 1; i++) {
            Element trElement = trElements.get(i);
            Elements tdElements = trElement.select("td");
            String punycode = tdElements.get(0).selectFirst("input").val();
            Element absmiddleElement = tdElements.get(2).selectFirst("img");
            Integer absmiddle = 0;
            if (absmiddleElement != null) {
                String absmiddleTitle = absmiddleElement.attr("title");
                if (absmiddleTitle.contains("阿里云")) {
                    absmiddle = 1;
                } else if (absmiddleTitle.contains("腾汛云")) {
                    absmiddle = 2;
                }
            }
            brainPowerOrder = new BrainPowerOrder();
            brainPowerOrder.setPunycode(punycode);
            brainPowerOrder.setOrderDate(deleteDate);
            brainPowerOrder.setAbsmiddle(absmiddle);
            brainPowerOrder.setType(1);
            result.add(brainPowerOrder);
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://www.juming.com/6/index.htm?cha=1");
        //set headers
        httpPost.setHeader("Host", "www.juming.com");
        httpPost.setHeader("Cookie", "UM_distinctid=165d185038b13d-0e48b295f02a48-323b5b03-1fa400-165d185038c28a; _qddaz=QD.80ps6t.2rypca.jm061knx; pgv_pvi=8871671808; tencentSig=6469347328; skinName=default; ASPSESSIONIDAABBBATS=KIPGFCMAHIPCABDDPPLDHAJG; Hm_lvt_512ed551fae9428abd7d743009588c7a=1542778136,1542857531,1542950991,1543108948; Hm_lvt_f94e107103e3c39e0665d52b6d4a93e7=1542778136,1542857531,1542950991,1543108949; IESESSION=alive; pgv_si=s6241445888; CNZZDATA3432862=cnzz_eid%3D1557794136-1536815777-null%26ntime%3D1543126821; _qddab=3-ok0pav.jowhwh1j; _qddamta_4009972996=3-0; Juming%2Ecom=login%5Fuid=148574&islogincode=5efc8296b5528f5c88&t%5Ftuiguang=tiao%5Fjuming%2Ecom&new%5Fbanban%5Fzhu=1&sc%5Fcsrf=9d942d268bc9794662; Hm_lpvt_512ed551fae9428abd7d743009588c7a=1543129709; Hm_lpvt_f94e107103e3c39e0665d52b6d4a93e7=1543129709");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        httpPost.setHeader("Referer", "http://www.juming.com/6/?2018-11-25");
        httpPost.setHeader("Origin", "http://www.juming.com");


        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("baocun2", ""));
        params.add(new BasicNameValuePair("zairushezhi", "0"));
//        params.add(new BasicNameValuePair("gjz_cha", "可用,分隔"));
//        params.add(new BasicNameValuePair("gjz_cha2", "可用,分隔"));
//        params.add(new BasicNameValuePair("gjz_chab", "可用,分隔"));
//        params.add(new BasicNameValuePair("gjz_cha2b", "可用,分隔"));
        params.add(new BasicNameValuePair("ymgc", "0"));
        params.add(new BasicNameValuePair("ymcd_1", "1"));
        params.add(new BasicNameValuePair("ymcd_2", "0"));
        params.add(new BasicNameValuePair("ymgcte", ""));
        params.add(new BasicNameValuePair("ymhzfs", "0"));
        params.add(new BasicNameValuePair("ymhz", "cn,com.cn"));
        params.add(new BasicNameValuePair("sfba_1", "1"));
//        params.add(new BasicNameValuePair("badq", ""));
//        params.add(new BasicNameValuePair("baxz", ""));
//        params.add(new BasicNameValuePair("babh", "请输入关键字"));
//        params.add(new BasicNameValuePair("bajrs", ""));
        params.add(new BasicNameValuePair("wxjc", "1"));
        params.add(new BasicNameValuePair("qqjc", "1"));
        params.add(new BasicNameValuePair("bdpj", ""));
        params.add(new BasicNameValuePair("tsym", "0"));
        params.add(new BasicNameValuePair("pr_1", "0"));
        params.add(new BasicNameValuePair("pr_2", "0"));
        params.add(new BasicNameValuePair("bdsl_1", "0"));
        params.add(new BasicNameValuePair("bdsl_2", "0"));
        params.add(new BasicNameValuePair("bdfl_1", "0"));
        params.add(new BasicNameValuePair("bdfl_2", "0"));
//        params.add(new BasicNameValuePair("apm_1", "不限"));
//        params.add(new BasicNameValuePair("apm_2", "不限"));
        params.add(new BasicNameValuePair("sclx", "2"));
        params.add(new BasicNameValuePair("scsj", "2018-11-25"));

//        params.add(new BasicNameValuePair("zcsj", ""));
        params.add(new BasicNameValuePair("mysc", "8"));
        params.add(new BasicNameValuePair("jgpx", "0"));


//        HttpEntity httpEntity = multipartEntityBuilder.build();
//        httpPost.setEntity(httpEntity);
        httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));


        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        System.out.println(httpResponse.getStatusLine().getStatusCode());
        String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        Document doc = Jsoup.parse(result);
        Elements trElements = doc.select("body > form > div > table > tbody > tr");
        for (int i = 0; i < trElements.size() - 1; i++) {
            Element trElement = trElements.get(i);
            Elements tdElements = trElement.select("td");
            String punycode = tdElements.get(0).selectFirst("input").val();
            Element absmiddleElement = tdElements.get(2).selectFirst("img");
            Integer absmiddle = 0;
            if (absmiddleElement != null) {
                String absmiddleTitle = absmiddleElement.attr("title");
                if (absmiddleTitle.contains("阿里云")) {
                    absmiddle = 1;
                } else if (absmiddleTitle.contains("腾汛云")) {
                    absmiddle = 2;
                }
            }
            System.out.println("域名:" + punycode + ",备案接入商:" + absmiddle);
        }
    }


}
