package com.domain.icp.schema.brainpower;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.service.BrainPowerService;
import com.domain.icp.util.MD5Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SyncAuctionInfoTask {

    private static Logger logger = LoggerFactory.getLogger(SyncAuctionInfoTask.class);

    private static String uuid = "227988";
    private static String secretKey = "ab80d37b37a2f363";
    private static String openUrl = "http://openapi.juming.com/";

    @Autowired
    private BrainPowerService brainPowerService;

//    @Scheduled(cron = "0 0/10 * * * *")
    public void initAuctionInfo() {
        List<BrainPowerOrder> brainPowerOrderList = brainPowerService.listForInitAuctionInfo();
        for (BrainPowerOrder brainPowerOrder : brainPowerOrderList) {
            try {
                boolean flag = this.setAuctionInfo(brainPowerOrder);
                if (flag) {
                    brainPowerService.update(brainPowerOrder);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


//    @Scheduled(cron = "0 0/10 * * * *")
    public void syncAuctionInfo() {
        List<BrainPowerOrder> brainPowerOrderList = brainPowerService.listForSyncAuctionInfo();
        for (BrainPowerOrder brainPowerOrder : brainPowerOrderList) {
            try {
                boolean flag = this.setAuctionInfo(brainPowerOrder);
                if (flag) {
                    brainPowerService.update(brainPowerOrder);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private boolean setAuctionInfo(BrainPowerOrder brainPowerOrder) throws Exception {
        Long time = System.currentTimeMillis() / 1000;
        String key = MD5Util.encode(secretKey + "&" + String.valueOf(time));
        boolean result = false;
        try {
            String domainName = brainPowerOrder.getPunycode();
            CloseableHttpClient client = HttpClients.createDefault();
            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", uuid);
            params.put("tpsj", String.valueOf(time));
            params.put("key", key);
            params.put("fs", "jj_info");
            params.put("ym", domainName);

            HttpPost httppost = new HttpPost(openUrl);
            HttpEntity postBodyEnt = new UrlEncodedFormEntity(
                    produceFormEntity(params), "utf-8");
            httppost.setEntity(postBodyEnt);

            HttpResponse response = client.execute(httppost);
            String responseStr = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONObject responseObj = JSONObject.parseObject(responseStr);
            logger.info("域名:" + domainName + "，预定结果:" + responseObj.toJSONString());
            int code = responseObj.getInteger("code");
            if (code == 1) {
                JSONObject auctionObj = responseObj.getJSONArray("data").getJSONObject(0);
                String leadId = auctionObj.getString("uid");
                Integer isLead = uuid.equalsIgnoreCase(leadId) ? 1 : 0;
                brainPowerOrder.setIsLead(isLead);
                Integer price = auctionObj.getInteger("qian");
                brainPowerOrder.setPrice(price);
                Integer zt = auctionObj.getInteger("zt");
                Integer auctionStatus = ((zt.intValue() == 0) ? 1 : 2);
                brainPowerOrder.setAuctionStatus(auctionStatus);
                String endTime = auctionObj.getString("jssj");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                brainPowerOrder.setAuctionEndTime(sdf.parse(endTime));
                result = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }


    private List<NameValuePair> produceFormEntity(
            Map<String, String> paramsMap) throws UnsupportedEncodingException {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String key : paramsMap.keySet()) {
            list.add(new BasicNameValuePair(key, paramsMap.get(key)));
        }
        return list;
    }
}
