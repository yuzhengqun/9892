package com.domain.icp.util;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientKit {
    private static Logger logger = LoggerFactory.getLogger(HttpClientKit.class);
    /**
     * 内部对象 httpClient
     */
    private CloseableHttpClient httpClient = null;
    private Map<String, String> cookies = new HashMap<String, String>();

    public HttpClientKit() {
        try {
            initHttpCient(null, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public HttpClientKit(String proxyHostInfo, String localIp) {
        try {
            initHttpCient(proxyHostInfo, localIp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void initHttpCient(String proxyHostInfo, String localIp) throws Exception {
        HttpClientBuilder builder = HttpClients.custom();
        if (proxyHostInfo != null) {
            String[] arr = proxyHostInfo.split(";");
            String ip = arr[0];
            int port = Integer.parseInt(arr[1]);
            HttpHost proxyHost = new HttpHost(ip, port);
            builder.setProxy(proxyHost);
        }
        if (localIp != null) {
            InetAddress address;
            address = InetAddress.getByName(localIp);
            RequestConfig config = RequestConfig.custom().setLocalAddress(address).build();
            builder.setDefaultRequestConfig(config);
        }
        httpClient = builder.build();

    }

    public String exeGetMethodForString(Map<String, String> headers, String url) throws Exception {
        CloseableHttpResponse response = exeGetMethod(headers, url);
        handleRespone(response);
        String htmlResponse = EntityUtils.toString(response.getEntity());
        return htmlResponse;
    }

    public String exeGetMethodForString(String url) throws Exception {
        CloseableHttpResponse response = null;
        for (int i = 0; i < 3; i++) {
            try {
                response = exeGetMethod(url);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("getStatusCode = " + response.getStatusLine().getStatusCode());
                }
                handleRespone(response);
                String htmlResponse = EntityUtils.toString(response.getEntity(), "GBK");
                return htmlResponse;
            } catch (Exception e) {
                if (i == 2) {
                    throw e;
                }
            }

        }
        throw new Exception("未知错误");

    }

    public String exeGetMethodForString(String url, String charset) throws Exception {
        CloseableHttpResponse response = null;
        for (int i = 0; i < 3; i++) {
            try {
                response = exeGetMethod(url);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception("getStatusCode = " + response.getStatusLine().getStatusCode());
                }
                handleRespone(response);
                String htmlResponse = EntityUtils.toString(response.getEntity(), charset == null ? "UTF-8" : charset);
                return htmlResponse;
            } catch (Exception e) {
                if (i == 2) {
                    throw e;
                }
            }

        }
        throw new Exception("未知错误");

    }

    private void handleRespone(CloseableHttpResponse response) {
        Header[] headers = response.getAllHeaders();
        for (Header header : headers) {

            // Set-Cookie>>>>__cfduid=de8b3f6dfdacdcb66c1e6fb84bd9326a91478168074;
            // expires=Fri, 03-Nov-17 10:14:34 GMT; path=/; domain=.adndrc.org;
            // HttpOnly
            if ("Set-Cookie".equals(header.getName())) {
                String value = header.getValue();
                String key = value.substring(0, value.indexOf("="));
                String cValue = value.substring(value.indexOf("=") + 1, value.indexOf(";"));
                cookies.put(key, cValue);
            }
        }
    }

    public String getCookies() {
        StringBuffer buffer = new StringBuffer();
        for (String key : cookies.keySet()) {
            buffer.append(key + "=" + cookies.get(key) + "; ");
        }
        return buffer.toString();
    }

    public CloseableHttpResponse exeGetMethod(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        handleRespone(response);
        return response;
    }

    public CloseableHttpResponse exeGetMethod(Map<String, String> headers, String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                httpGet.setHeader(key, value);
            }
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        handleRespone(response);
        return response;
    }

    public String exePostMethodForString(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                CloseableHttpResponse response = exePostMethod(url, headers, params);
                handleRespone(response);
                String htmlResponse = EntityUtils.toString(response.getEntity());
                return htmlResponse;
            } catch (Exception e) {
                if (i == 2) {
                    throw e;
                }
            }

        }
        throw new Exception("exePostMethodForString unkown");
    }

    public String exePostMethodForString(String url, Map<String, String> headers, String body) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                httpPost.setHeader(key, value);
            }
        }
        httpPost.setEntity(new InputStreamEntity(new ByteArrayInputStream(body.getBytes())));

        CloseableHttpResponse response = httpClient.execute(httpPost);
        handleRespone(response);

        String htmlResponse = EntityUtils.toString(response.getEntity());
        return htmlResponse;
    }

    public CloseableHttpResponse exePostMethod(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                httpPost.setHeader(key, value);
            }
        }
        if (params != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                String value = params.get(key);
                list.add(new BasicNameValuePair(key, value));
            }
            HttpEntity postBodyEnt = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(postBodyEnt);
        }

        CloseableHttpResponse response = httpClient.execute(httpPost);
        handleRespone(response);

        return response;
    }

    public void downHttpFile(String url, File localFile) throws Exception {
        HttpResponse rs;
        FileOutputStream fos = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            // httpGet.setConfig(StaticHttpClient.bindingTimeOut());
            rs = httpClient.execute(httpGet);
            HttpEntity rsENti = rs.getEntity();
            if (rsENti != null) {
                InputStream is = rsENti.getContent();
                fos = new FileOutputStream(localFile);
                int n = -1;
                byte[] bytes = new byte[1024];
                while ((n = is.read(bytes)) > -1) {
                    fos.write(bytes, 0, n);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36");
        headers.put("Cookie",
                "PHPSESSID=qukulkk975ltd5i67tbgr43676; yumi_sid=a%3A20%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%2232d548550842f23c6a66e292387d1b60%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A13%3A%22192.168.1.203%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A109%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%3B+WOW64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F53.0.2785.116+Safari%2F537.36%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1479107540%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3Bs%3A10%3A%22login_time%22%3Bs%3A19%3A%222016-11-14+15%3A36%3A07%22%3Bs%3A8%3A%22from_url%22%3Bs%3A38%3A%22http%3A%2F%2Fwww.yumidev.com%2Fmember%2Fregister%22%3Bs%3A7%3A%22user_id%22%3Bs%3A6%3A%22340001%22%3Bs%3A9%3A%22user_code%22%3Bs%3A6%3A%22341001%22%3Bs%3A10%3A%22login_name%22%3Bs%3A9%3A%22huaxialxf%22%3Bs%3A15%3A%22show_login_name%22%3Bs%3A9%3A%22huaxialxf%22%3Bs%3A5%3A%22email%22%3Bs%3A17%3A%22huaxialxf%40163.com%22%3Bs%3A8%3A%22api_code%22%3Bs%3A36%3A%224145a38e-ae7f-fba8-f0b6-3fd25dd0007d%22%3Bs%3A13%3A%22user_level_id%22%3Bs%3A1%3A%221%22%3Bs%3A5%3A%22state%22%3Bs%3A6%3A%22Active%22%3Bs%3A12%3A%22weixin_state%22%3Bs%3A8%3A%22InActive%22%3Bs%3A4%3A%22type%22%3Bs%3A6%3A%22normal%22%3Bs%3A13%3A%22type_end_time%22%3BN%3Bs%3A8%3A%22group_id%22%3Bs%3A1%3A%225%22%3Bs%3A9%3A%22had_error%22%3Bi%3A0%3B%7D8a8f5d3797d0de0ab4df8436f5c65e9e; CNZZDATA1258295942=343686142-1479088861-%7C1479105180");
        HttpClientKit clientUtil = new HttpClientKit();
        String ret = clientUtil.exeGetMethodForString(headers, "http://www.yumidev.com/domain/xxshozou.com");
        FileOutputStream fos = new FileOutputStream("1.html");
        fos.write(ret.getBytes("UTF-8"));
        fos.close();
        System.out.println(ret);
    }

}
