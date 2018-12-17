package com.domain.icp.schema.brainpower;

import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import com.domain.icp.util.HttpClientKit;
import com.domain.icp.util.JSoupPathUtil;
import com.domain.icp.util.JumingCookieUtil;
import com.domain.icp.util.MD5Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SpiderDomainTask {

    private static Logger logger = LoggerFactory.getLogger(SpiderDomainTask.class);

    private static HttpClientKit httpClientKit = new HttpClientKit();
    public static String user_name = "350363013@qq.com";
    public static String PASSWORD = "Lxf791205";

    @Autowired
    private BrainPowerService brainPowerService;


//    @Scheduled(cron = "0/2 * * * * *")
    public void spider() {
        try {
            this.login();
        } catch (Exception e) {
            logger.error("登录聚名网失败", e);
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            List<BrainPowerOrder> delCnList = this.spiderDelDomain("cn,com.cn", currentDate);
            this.saveOrUpdateBrainPowerOrders(delCnList);
            List<BrainPowerOrder> delComList = this.spiderDelDomain("com", currentDate);
            this.saveOrUpdateBrainPowerOrders(delComList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static void main(String[] args) throws Exception {
        SpiderDomainTask spiderDomainTask = new SpiderDomainTask();
        spiderDomainTask.login();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        List<BrainPowerOrder> delDomains = spiderDomainTask.spiderDelDomain("com,net", currentDate);
        logger.info("删除域名数量:" + delDomains.size());
    }

    private void saveOrUpdateBrainPowerOrders(List<BrainPowerOrder> brainPowerOrders) {
        for (BrainPowerOrder brainPowerOrder : brainPowerOrders) {
            brainPowerService.saveOrUpdate(brainPowerOrder);
        }
    }

    public void login() throws Exception {
        String url = "http://www.juming.com/";
        String html = httpClientKit.exeGetMethodForString(url);
        System.out.println(httpClientKit.getCookies());

        Document doc = Jsoup.parse(html);
        String temp = JSoupPathUtil.getSelect(doc, "form#loginBox/[onsubmit]").get(0);
        String code = JSoupPathUtil.mid(temp, "'", "'");
        System.out.println("code:" + code);

        url = "http://www.juming.com/if.htm";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("tj_fs", "1");
        map.put("re_yx", user_name);
        map.put("re_code", code);

        String passwd = MD5Util.encode("[jiami" + PASSWORD + "mima]").substring(0, 19);
        passwd = MD5Util.encode(code + passwd).substring(0, 19);
        map.put("re_mm", passwd);
        html = httpClientKit.exePostMethodForString(url, null, map);
        System.out.println(html);

        System.out.println(httpClientKit.getCookies());
        if (html.startsWith("1登陆成功")) {
            logger.info("1登陆成功");
            return;
        }
        throw new Exception("登录失败:" + html);

    }

    private List<BrainPowerOrder> spiderDelDomain(String suffixs, String delDate) throws Exception {
        int page = 1;
        List<BrainPowerOrder> result = new ArrayList<>();
        while (true) {
            String url = "http://www.juming.com/6/index.htm?cha=1";
            if (page > 1) {
                url = "http://www.juming.com/6/index.htm?cha=1&page=" + page;
            }
            Map<String, String> headers = new LinkedHashMap<String, String>();
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
            headers.put("Accept", "*/*");
            headers.put("Accept-Encoding", "gzip, deflate");
            headers.put("Referer", "http://www.juming.com/6/?2018-11-27");
            headers.put("X-Requested-With", "XMLHttpRequest");
            headers.put("Pragma", "no-cache");
            headers.put("Cache-Control", "no-cache");
            headers.put("Origin", "http://www.juming.com");
            headers.put("Referer", "http://www.juming.com/6/?" + delDate);
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("zairushezhi", "0");
            map.put("ymcd_1", "1");// 是否备案
            map.put("ymcd_2", "0");// 删除类型delete
            map.put("ymgcte", "");//
            map.put("ymhzfs", "0");//
            map.put("ymhz", suffixs);//
            map.put("sfba_1", "1");//
            map.put("wxjc", "1");//
            map.put("qqjc", "1");//
            map.put("bdpj", "");//
            map.put("tsym", "0");//
            map.put("pr_1", "0");//
            map.put("pr_2", "0");//
            map.put("bdsl_1", "0");//
            map.put("bdsl_2", "0");//
            map.put("bdfl_1", "0");//
            map.put("bdfl_2", "0");//
            map.put("sclx", "2");//
            map.put("scsj", delDate);//
            map.put("mysc", "8");// 每页显示1000条
            map.put("jgpx", "0");//
            map.put("ymgc", "0");//
            String html = httpClientKit.exePostMethodForString(url, headers, map);
            if (html.startsWith("10没有记录")) {
                break;
            }

            Document doc = Jsoup.parse(html);
            Elements trElements = doc.select("body > form > div > table > tbody > tr");
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
                int index = punycode.indexOf(".");
                String suffix = punycode.substring(index, punycode.length());
                brainPowerOrder.setSuffix(suffix);
                brainPowerOrder.setOrderDate(delDate);
                brainPowerOrder.setAbsmiddle(absmiddle);
                brainPowerOrder.setType(1);
                result.add(brainPowerOrder);
            }
            if (trElements.size() - 1 < 1000) {
                break;
            }
            page++;
            Thread.sleep(1000 * 3);
        }
        logger.info("删除域名数量:" + result.size());
        return result;
    }
}