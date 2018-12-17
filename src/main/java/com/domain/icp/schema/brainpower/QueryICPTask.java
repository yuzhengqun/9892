package com.domain.icp.schema.brainpower;

import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import com.domain.icp.util.HttpClientKit;
import com.domain.icp.util.JSoupPathUtil;
import com.domain.icp.util.JumingLoginUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryICPTask {

    private static Logger logger = LoggerFactory.getLogger(QueryICPTask.class);

    @Autowired
    private BrainPowerService brainPowerService;


//    @Scheduled(cron = "0/2 * * * * *")
    public void queryICP() {
        try {
            QueryICPTask queryICPTask = new QueryICPTask();
            HttpClientKit clientKit = JumingLoginUtil.getHttpClientKit();
            List<BrainPowerOrder> brainPowerOrders = brainPowerService.listNotQueryICPInfos();
            for (BrainPowerOrder brainPowerOrder : brainPowerOrders) {
                try {
                    this.queryICPInfo(brainPowerOrder, clientKit, 5, 1);
                    brainPowerService.update(brainPowerOrder);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public static void main(String[] args) throws Exception {
        QueryICPTask queryICPTask = new QueryICPTask();
        HttpClientKit clientKit = JumingLoginUtil.getHttpClientKit();
        queryICPTask.queryICPInfo(null, clientKit, 5, 1);
    }

    public void queryICPInfo(BrainPowerOrder brainPowerOrder, HttpClientKit clientKit, int tryTimes, int queryTimes) throws Exception {
        try {
            if (queryTimes > tryTimes) {
                throw new Exception("查询失败");
            }
            String url = String.format("http://www.juming.com/hao/?%s=a&tj_fs=1&_=%s", brainPowerOrder.getPunycode(), String.valueOf(System.currentTimeMillis()));
            String html = clientKit.exeGetMethodForString(url);
            Document doc = Jsoup.parse(html);
            List<String> list = JSoupPathUtil.getSelect(doc, "div.datas-list/table/td/text");
            String beianNo = list.get(1);
            if ("查询出错".equals(beianNo)) {
                logger.info("备案信息查询失败，等3秒再访问...");
                Thread.sleep(1000 * 3);
                this.queryICPInfo(brainPowerOrder, clientKit, tryTimes, queryTimes++);
                return;
            }
            if ("未备案".equals(beianNo)) {
                brainPowerOrder.setIsIcp(2);
            } else {
                brainPowerOrder.setIsIcp(1);
                brainPowerOrder.setIcpNo(beianNo);
            }
            String beianEntity = list.get(2);
            brainPowerOrder.setIcpEntity(beianEntity);
            String beianType = list.get(3);
            brainPowerOrder.setIcpNature(beianType);
            String href = JSoupPathUtil.mid(html, "gettong('wxsc','", "'");
            html = clientKit.exeGetMethodForString("http://www.juming.com" + href);
            brainPowerOrder.setWxStatusUrl(href);
            String type = JSoupPathUtil.mid(html, ">", "<");
            if ("未拦截".equals(type)) {
                brainPowerOrder.setWxStatus(1);
            } else if ("已拦截".equals(type)) {
                brainPowerOrder.setWxStatus(2);
            }
            logger.info("域名:" + brainPowerOrder.getPunycode() + ",备案号:" + beianNo + ",备案实体:" + beianEntity + ",备案类型:" + beianType + ",微信是否拦截:" + type);
        } catch (Exception e) {
            Thread.sleep(1000 * 3);
            this.queryICPInfo(brainPowerOrder, clientKit, tryTimes, queryTimes++);
        }

    }
}
