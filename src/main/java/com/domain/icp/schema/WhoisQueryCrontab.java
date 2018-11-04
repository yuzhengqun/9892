package com.domain.icp.schema;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.domain.icp.db.vo.DomainInfo;
import com.domain.icp.service.DomainInfoService;

@Component
public class WhoisQueryCrontab {

	private static Logger logger = LoggerFactory
			.getLogger(WhoisQueryCrontab.class);

	@Autowired
	private DomainInfoService domainInfoService;

	// 每次取1000条记录查询
	private Integer pageSize = 50;

	// @Scheduled(cron = "0 0/5 * * * *")
	public void exe() {
		String ab = "{\"新网\":[\"北京新网数码信息技术有限公司\",\"whois.paycenter.com.cn\",\"whois.xinnet.com\"],\"阿里云(万网)\":[\"grs-whois.hichina.com\",\" 阿里云计算有限公司(万网)\"],\"易名\":[\"厦门易名科技有限公司(原厦门易名网络科技有限公司)\",\"whois.ename.com\"],\"22\":[\"浙江贰贰网络有限公司\",\"whois.22.cn\"],\"西数\":[\"whois.west263.com\",\"whois.west.cn\"],\"商务中国\":[\"whois.bizcn.com\"],\"namebright\":[\"whois.namebright.com\"],\"web\":[\"whois.web.com\"],\"易介\":[\"易介集团北京有限公司\",\"易介集团控股有限公司\"],\"美橙\":[\"上海美橙科技信息发展有限公司\",\"grs-whois.cndns.com\"]}";
		JSONObject jsonObject = JSONObject.parseObject(ab);
		Map<String, String> map = new HashMap<String, String>();
		for (String key : jsonObject.keySet()) {
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			for (int i = 0; i < jsonArray.size(); i++) {
				String registrar = jsonArray.getString(i);
				map.put(registrar, key);
			}
		}
		int page = 1;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("limit", pageSize);
		while (true) {
			paramMap.put("offset", (page - 1) * pageSize);
			List<DomainInfo> domainInfos = domainInfoService
					.getWhoisTasks(paramMap);
			for (DomainInfo domainInfo : domainInfos) {
				try {
					String punycode = domainInfo.getPunycode();
					JSONObject whoisObj = getBaseInfo(punycode);
					String registrarAlias = null;
					if (punycode.endsWith(".cn")) {
						String registrar = whoisObj.getString("registrar");
						if (registrar == null) {
							System.out.println("registrar null:" + punycode);
							continue;
						}
						registrarAlias = map.get(registrar.trim());
					} else {
						String whoisServer = whoisObj.getString("whoisServer");
						if (whoisServer == null) {
							continue;
						}
						registrarAlias = map.get(whoisServer.toLowerCase()
								.trim());
					}
					String registerDate = whoisObj.getString("registerDate");
					if (registerDate != null) {
						registerDate = registerDate.substring(0, 11).trim();
					}
					int dnsParser = 1;
					if (registrarAlias == null) {
						dnsParser = 0;
						registrarAlias = "其他";
					}
					domainInfo.setRegisterDate(registerDate);
					domainInfo.setRegistrar(registrarAlias);
					domainInfo.setDnsParser(dnsParser);
					Thread.sleep(1000);
				} catch (Exception e) {

				}
			}
			try {
				domainInfoService.batchUpdateDomainInfo(domainInfos);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (domainInfos.size() < pageSize) {
				break;
			}
			logger.info("第 " + page + " 页whois查询完成");
			page++;
		}
	}

	public static void main(String[] args) throws Exception {
		String ab = "{\"新网\":[\"北京新网数码信息技术有限公司\",\"whois.paycenter.com.cn\",\"whois.xinnet.com\"],\"阿里云(万网)\":[\"grs-whois.hichina.com\",\" 阿里云计算有限公司(万网)\"],\"易名\":[\"厦门易名科技有限公司(原厦门易名网络科技有限公司)\",\"whois.ename.com\"],\"22\":[\"浙江贰贰网络有限公司\",\"whois.22.cn\"],\"西数\":[\"whois.west263.com\",\"whois.west.cn\"],\"商务中国\":[\"whois.bizcn.com\"],\"namebright\":[\"whois.namebright.com\"],\"web\":[\"whois.web.com\"],\"易介\":[\"易介集团北京有限公司\",\"易介集团控股有限公司\"],\"美橙\":[\"上海美橙科技信息发展有限公司\",\"grs-whois.cndns.com\"]}";
		JSONObject jsonObject = JSONObject.parseObject(ab);
		Map<String, String> map = new HashMap<String, String>();
		for (String key : jsonObject.keySet()) {
			JSONArray jsonArray = jsonObject.getJSONArray(key);
			for (int i = 0; i < jsonArray.size(); i++) {
				String registrar = jsonArray.getString(i);
				map.put(registrar, key);
			}
		}
		String source = "/Users/yuzhengqun/Documents/work/9795/whois/whois_result.txt";
		String target = "/Users/yuzhengqun/Documents/work/9795/whois/update_sql.sql";
		FileReader fr = new FileReader(source);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(target);
		String line = null;
		String sql = null;
		while ((line = br.readLine()) != null) {
			JSONObject obj = JSONObject.parseObject(line);
			String punycode = obj.getString("punycode").toLowerCase();
			String registrarAlias = null;
			if (punycode.endsWith(".cn")) {
				String registrar = obj.getString("registrar");
				if (registrar == null) {
					System.out.println("registrar null:" + line);
					continue;
				}
				registrarAlias = map.get(registrar.trim());
			} else {
				String whoisServer = obj.getString("whoisServer");
				if (whoisServer == null) {
					System.out.println(line);
					continue;
				}
				registrarAlias = map.get(whoisServer.toLowerCase().trim());
			}
			String registerDate = obj.getString("registerDate");
			if (registerDate == null) {
				System.out.println(line);
			} else {
				registerDate = registerDate.substring(0, 11).trim();
			}
			int dnsParser = 1;
			if (registrarAlias == null) {
				dnsParser = 0;
				registrarAlias = "其他";
			}
			if (registerDate == null) {
				sql = "update domain_info set registrar='" + registrarAlias
						+ "',dns_parser=" + dnsParser + " where punycode = '"
						+ punycode + "'";
			} else {
				sql = "update domain_info set registrar='" + registrarAlias
						+ "',register_date='" + registerDate + "',dns_parser="
						+ dnsParser + " where punycode = '" + punycode + "'";
			}

			fw.write(sql + ";");
			fw.write("\n");
		}
		br.close();
		fw.close();
	}

	public static void main2(String[] args) throws Exception {
		String ab = "{\"新网\":[\"北京新网数码信息技术有限公司\",\"whois.paycenter.com.cn\",\"whois.xinnet.com\"],\"阿里云(万网)\":[\"grs-whois.hichina.com\",\" 阿里云计算有限公司(万网)\"],\"易名\":[\"厦门易名科技有限公司(原厦门易名网络科技有限公司)\",\"whois.ename.com\"],\"22\":[\"浙江贰贰网络有限公司\",\"whois.22.cn\"],\"西数\":[\"whois.west263.com\",\"whois.west.cn\"],\"商务中国\":[\"whois.bizcn.com\"],\"namebright\":[\"whois.namebright.com\"],\"web\":[\"whois.web.com\"],\"易介\":[\"易介集团北京有限公司\",\"易介集团控股有限公司\"],\"美橙\":[\"上海美橙科技信息发展有限公司\",\"grs-whois.cndns.com\"]}";
		WhoisQueryCrontab whoisQueryCrontab = new WhoisQueryCrontab();
		// while ((true)) {
		// try {
		// whoisQueryCrontab.getBaseInfo("365.com");
		// whoisQueryCrontab.getBaseInfo("365.cn");
		// whoisQueryCrontab.getBaseInfo("22.net.cn");
		// whoisQueryCrontab.getBaseInfo("22.cc");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		String source = "/Users/yuzhengqun/Documents/work/9795/whois/domain_info.txt";
		String target = "/Users/yuzhengqun/Documents/work/9795/whois/whois_result.txt";
		FileReader fr = new FileReader(source);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw = new FileWriter(target, true);
		String line = null;
		boolean isCN = false;
		int count = 0;
		while ((line = br.readLine()) != null) {
			count++;
			if (count < 6000) {
				continue;
			}
			System.out.println(line);
			if (line.toLowerCase().endsWith(".cn")) {
				isCN = true;
			} else {
				isCN = false;
			}
			try {
				if (isCN) {
					Thread.sleep(500);
				}
				JSONObject result = whoisQueryCrontab.getBaseInfo(line.trim()
						.toLowerCase());
				fw.append(result.toJSONString());
				fw.append("\n");
				fw.flush();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(line);
			}

		}
		br.close();
		fr.close();
		fw.close();
	}

	private JSONObject getBaseInfo(String punycode) throws Exception {
		int index = punycode.lastIndexOf(".");
		String suffix = punycode.substring(index + 1, punycode.length());
		String whoisServer = suffix + ".whois-servers.net";
		String pack = new String(this.send(whoisServer, 43,
				(punycode + "\r\n").getBytes()));
		JSONObject result = null;
		if (suffix.contains("cn")) {
			result = this.parseCNType(pack);
		} else {
			result = this.parseBaseType(pack);
		}
		result.put("punycode", punycode);
		return result;
	}

	private JSONObject parseBaseType(String domainPackage) throws Exception {
		JSONObject result = new JSONObject();
		String[] cols = domainPackage.split("\r\n");
		for (String col : cols) {
			String[] keyAndValue = col.trim().split(":");
			if (keyAndValue.length >= 2) {
				String key = keyAndValue[0];
				String value = keyAndValue[1];
				if ("Registrar WHOIS Server".equals(key)) {
					result.put("whoisServer", value);
				} else if ("Creation Date".equals(key)) {
					result.put("registerDate", value);
				} else if ("Registrar".equals(key)) {
					result.put("registrar", value);
				} else if ("Name Server".equals(key)) {
					if (result.containsKey("nameServer")) {
						result.put("nameServer", result.get("nameServer") + ";"
								+ value);
					} else {
						result.put("nameServer", value);
					}
				}
			}
		}
		return result;
	}

	private JSONObject parseCNType(String domainPackage) throws Exception {
		JSONObject result = new JSONObject();
		String[] cols = domainPackage.split("\n");
		for (String col : cols) {
			String[] keyAndValue = col.split(":");
			if (keyAndValue.length >= 2) {
				String key = keyAndValue[0];
				String value = keyAndValue[1];
				if ("Registration Time".equals(key)) {
					result.put("registerDate", value);
				} else if ("Sponsoring Registrar".equals(key)) {
					result.put("registrar", value);
				} else if ("Name Server".equals(key)) {
					if (result.containsKey("nameServer")) {
						result.put("nameServer", result.get("nameServer") + ";"
								+ value);
					} else {
						result.put("nameServer", value);
					}
				}
			}
		}
		return result;
	}

	private byte[] send(String host, int port, byte[] request)
			throws IOException {
		SocketAddress address = new InetSocketAddress(host, port);
		Socket socket = new Socket();
		InputStream is = null;
		OutputStream os = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			socket.setSoTimeout(5 * 1000);
			socket.connect(address, 5 * 1000);
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
