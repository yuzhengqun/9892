package com.domain.icp.util;

import com.domain.icp.schema.brainpower.SpiderDomainTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class JumingLoginUtil {

    private static Logger logger = LoggerFactory.getLogger(JumingLoginUtil.class);

    private static HttpClientKit httpClientKit = new HttpClientKit();
    private static String user_name = "xxxx";
    private static String PASSWORD = "xxxx";

    private static boolean hasLogin = false;


    public synchronized static HttpClientKit getHttpClientKit() throws Exception {
        if (!hasLogin) {
            login();
            hasLogin = true;
        }
        return httpClientKit;
    }

    private static void login() throws Exception {
        String url = "http://www.juming.com/";
        String html = httpClientKit.exeGetMethodForString(url);
        Document doc = Jsoup.parse(html);
        String temp = JSoupPathUtil.getSelect(doc, "form#loginBox/[onsubmit]").get(0);
        String code = JSoupPathUtil.mid(temp, "'", "'");
        logger.info("code:" + code);
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
}
