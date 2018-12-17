package com.domain.icp.util;

import com.domain.icp.model.WhoisInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class WhoisUtil {


    public static void main(String[] args) throws Exception {
        WhoisInfo whoisInfo = WhoisUtil.getWhoisInfo("365.com");
        System.out.println(whoisInfo.toString());
    }

    public static WhoisInfo getWhoisInfo(String punycode) throws Exception {
        int index = punycode.lastIndexOf(".");
        String suffix = punycode.substring(index + 1, punycode.length());
        String whoisServer = getWhoisServer(suffix);
        String domainPackage = new String(send(whoisServer, 43,
                (punycode + "\r\n").getBytes()));
        WhoisInfo whoisInfo = parsePackage(domainPackage);
        return whoisInfo;
    }

    private static WhoisInfo parsePackage(String pack) throws Exception {
        String[] fields = pack.split("\n");
        WhoisInfo result = new WhoisInfo();
        for (String field : fields) {
            if (field.contains("Domain Name:")) {
                int index = field.indexOf("Domain Name:");
                String domainName = field.substring(index + "Domain Name:".length(), field.length()).trim().toLowerCase();
                result.setPunycode(domainName);
                continue;
            }
            if (field.contains("Registrant:")) {
                int index = field.indexOf("Registrant:");
                String registrant = field.substring(index + "Registrant:".length() , field.length()).trim().toLowerCase();
                result.setRegistrant(registrant);
                continue;
            }
            if (field.contains("Registry Expiry Date:")) {
                int index = field.indexOf("Registry Expiry Date:");
                String expireTime = field.substring(index + "Registry Expiry Date:".length(), field.length()).trim();
                result.setExpireTime(expireTime);
                continue;
            }
            if (field.contains("Expiration Time:")) {
                int index = field.indexOf("Expiration Time:");
                String expireTime = field.substring(index + "Expiration Time:".length() , field.length()).trim();
                result.setExpireTime(expireTime);
                continue;
            }
            if (field.contains("Creation Date:")) {
                int index = field.indexOf("Creation Date:");
                String registerTime = field.substring(index + "Creation Date:".length(), field.length()).trim();
                result.setCreateTime(registerTime);
                continue;
            }
            if (field.contains("Registration Time:")) {
                int index = field.indexOf("Registration Time:");
                String registerTime = field.substring(index + "Registration Time:".length(), field.length());
                result.setCreateTime(registerTime);
                continue;
            }
            if (field.contains("Name Server:")) {
                int index = field.indexOf("Name Server:");
                String dnsServer = field.substring(index + "Name Server:".length(), field.length()).trim().toLowerCase();
                result.setDnsServer(dnsServer);
                continue;
            }
            if(field.contains("Registrar:")){
                int index = field.indexOf("Registrar:");
                String registrar = field.substring(index+"Registrar:".length(),field.length()).trim();
                result.setRegistrar(registrar);
                continue;
            }
        }
        return result;
    }


    private static String getWhoisServer(String suffix) {
        String result = null;
        if ("com".equalsIgnoreCase(suffix) || "net".equalsIgnoreCase(suffix) || "cc".equalsIgnoreCase(suffix)) {
            result = "com.whois-servers.net";
        } else {
            result = suffix.toLowerCase() + ".whois-servers.net";
        }
        return result;
    }

    /**
     * 发送短连接
     *
     * @param host
     * @param port
     * @param request 报文
     * @return
     * @throws IOException
     */
    private static byte[] send(String host, int port, byte[] request)
            throws IOException {
        SocketAddress address = new InetSocketAddress(host, port);
        Socket socket = new Socket();
        InputStream is = null;
        OutputStream os = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            socket.setSoTimeout(5 * 60 * 1000);
            socket.connect(address, 5 * 60 * 1000);
            is = socket.getInputStream();
            os = socket.getOutputStream();
            os.write(request);
            os.flush();

            int n = -1;
            byte[] bytes = new byte[1024];
            while ((n = is.read(bytes)) > -1) {
                bos.write(bytes, 0, n);
            }
            return bos.toByteArray();

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
