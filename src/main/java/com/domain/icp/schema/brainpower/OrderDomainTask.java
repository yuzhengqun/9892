package com.domain.icp.schema.brainpower;

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
public class OrderDomainTask {

    private static Logger logger = LoggerFactory.getLogger(OrderDomainTask.class);

    private static String uuid = "227988";
    private static String secretKey = "ab80d37b37a2f363";
    private static String openUrl = "http://openapi.juming.com/";

    @Autowired
    private BrainPowerService brainPowerService;

    @Scheduled(cron = "0 34 22 * * *")
    public void orderDelDomain() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(System.currentTimeMillis());
        String[] suffixs = {".cn", ".com.cn", ".com"};
        String[] channels = {"17", "17", "6"};
        for (int i = 0; i < suffixs.length; i++) {
            String suffix = suffixs[i];
            String channel = channels[i];
            List<BrainPowerOrder> brainPowerOrderList = brainPowerService.listOrderTasks(suffix, currentDate, 1, 1, 1);
            for (BrainPowerOrder brainPowerOrder : brainPowerOrderList) {
                try {
                    boolean orderResult = this.order(brainPowerOrder.getPunycode(), channel);
                    if (orderResult) {
                        brainPowerOrder.setOrderStatus(1);
                        brainPowerService.update(brainPowerOrder);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private boolean order(String domainName, String channel) throws Exception {
        Long time = System.currentTimeMillis() / 1000;
        String key = MD5Util.encode(secretKey + "&" + String.valueOf(time));
        boolean result = false;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            Map<String, String> params = new HashMap<String, String>();
            params.put("uid", uuid);
            params.put("tpsj", String.valueOf(time));
            params.put("key", key);
            params.put("fs", "yuding_add");
            params.put("ym", domainName);
            params.put("ydfs", channel);

            HttpPost httppost = new HttpPost(openUrl);
            HttpEntity postBodyEnt = new UrlEncodedFormEntity(
                    produceFormEntity(params), "utf-8");
            httppost.setEntity(postBodyEnt);

            HttpResponse response = client.execute(httppost);
            String responseStr = EntityUtils.toString(response.getEntity(), "utf-8");
            logger.info("域名:" + domainName + "，预定结果:" + responseStr);
            JSONObject responseObj = JSONObject.parseObject(responseStr);
            int code = responseObj.getInteger("code");
            if (code == 1) {
                result = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
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
