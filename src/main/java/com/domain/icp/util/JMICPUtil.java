package com.domain.icp.util;

import ch.qos.logback.classic.Logger;
import com.domain.icp.db.vo.BrainPowerOrder;
import com.domain.icp.db.vo.DomainSelling;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JMICPUtil {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JMICPUtil.class);

    public static DomainSelling getICPInfo(String punycode) throws Exception {
        HttpClientKit clientKit = JumingLoginUtil.getHttpClientKit();
        DomainSelling result = queryICPInfo(punycode, clientKit, 3, 0);
        return result;
    }


    public static String getWXStatusInfo(String wxStatusUrl) throws Exception {
        HttpClientKit clientKit = JumingLoginUtil.getHttpClientKit();
        String html = clientKit.exeGetMethodForString("http://www.juming.com" + wxStatusUrl);
        String type = JSoupPathUtil.mid(html, ">", "<");
        return type;
    }

    private static DomainSelling queryICPInfo(String punycode, HttpClientKit clientKit, int tryTimes, int queryTimes) throws Exception {
        try {
            if (queryTimes > tryTimes) {
                throw new Exception("查询失败");
            }
            String url = String.format("http://www.juming.com/hao/?%s=a&tj_fs=1&_=%s", punycode, String.valueOf(System.currentTimeMillis()));
            String html = clientKit.exeGetMethodForString(url);
            Document doc = Jsoup.parse(html);
            List<String> list = JSoupPathUtil.getSelect(doc, "div.datas-list/table/td/text");
            String beianNo = list.get(1);
            if ("查询出错".equals(beianNo)) {
                logger.info("备案信息查询失败，等3秒再访问...");
                Thread.sleep(1000 * 3);
                return queryICPInfo(punycode, clientKit, tryTimes, queryTimes++);
            }
            DomainSelling domainSelling = new DomainSelling();
            if ("未备案".equals(beianNo)) {
                domainSelling.setIsIcp(2);
            } else {
                domainSelling.setIsIcp(1);
                domainSelling.setIcpNo(beianNo);
            }
            String beianEntity = list.get(2);
            String beianType = list.get(3);
            String href = JSoupPathUtil.mid(html, "gettong('wxsc','", "'");
            domainSelling.setIcpNature(beianType);
            domainSelling.setIcpUser(beianEntity);
            domainSelling.setWxStatusUrl(href);
            logger.info("域名:" + punycode + ",备案号:" + beianNo + ",备案实体:" + beianEntity + ",备案类型:" + beianType);
            return domainSelling;
        } catch (Exception e) {
            Thread.sleep(1000 * 3);
            return queryICPInfo(punycode, clientKit, tryTimes, queryTimes++);
        }
    }
}
