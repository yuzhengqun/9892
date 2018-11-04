package com.domain.icp.schema;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class JMIcpApp {
	public static void main(String[] args) {
		try {
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet httpGet = new HttpGet(
					"http://www.juming.com/hao/?cha_ym=010jdwxlwj.com");
			httpGet.setHeader("Host", "www.juming.com");
			httpGet.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:62.0) Gecko/20100101 Firefox/62.0");
			httpGet.setHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpGet.setHeader("Accept-Language",
					"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			httpGet.setHeader("Accept-Encoding", "gzip, deflate");
			httpGet.setHeader("Referer",
					"http://www.juming.com/?tt=0&t=tiao_juming.com");
			httpGet.setHeader(
					"Cookie",
					"ASPSESSIONIDAAABBAST=CEKOANDAGPJGAGGCOPONBJGL; UM_distinctid=165337e9efd479-0ccd5ff2bb5506-16386951-fa000-165337e9eff4bf; IESESSION=alive; tencentSig=2720410624; _qddaz=QD.drm6i2.hshv7p.jksbksgq; pgv_pvi=965668864; pgv_si=s5659196416; ASPSESSIONIDAACDDARS=DFEBLGABFPHLDJGFBBFHDLPL; ASPSESSIONIDAACDAATS=ANPAPDNBIPNDKKFDIFGAPLOO; ASPSESSIONIDCCBBDBRT=FEFAMPJCFPOFNHIALADPLHEJ; ASPSESSIONIDCCDAACRS=PHCCMPBDNLGBBBLGAKHDFLDC; ASPSESSIONIDAABDACQT=FAMMNLHDHIABMBABJOKOFPGN; ASPSESSIONIDAABCABRS=DEMBOECAJCJCCBLGFJLBNAMG; ASPSESSIONIDACADABQS=JJKPIIEBDBFHFBJJLLLPKCNJ; ASPSESSIONIDCACABDRS=MEAMICCCOHBCGINGGMCCBKFO; ASPSESSIONIDCCACCASR=OFAHGLMCBGKMMNJOLEJLEIEK; ASPSESSIONIDCCDCACSQ=GEIPDEHDNKOCHFAPDLFAKPJO; ASPSESSIONIDAADBDCQT=PJODPMBAFGPHOIPAACAFMHGG; ASPSESSIONIDAABCDBTR=IJEFLFMAEJIPDEGIEOMBLDCJ; ASPSESSIONIDCCABAATT=BFJBIOGBIPIHFFPOAAIKHCCH; ASPSESSIONIDCCBCBBTS=GNLICHBCFKEFIICBFDCEPIKL; ASPSESSIONIDCCDBDBSQ=KOAOKPLCLOHGPIGGBJPHNCFL; ASPSESSIONIDCADAADSR=PFKLHIGDBMIAMFINDMPAMKCF; ASPSESSIONIDCCADAATT=LCFDHKLANEJHINKBENCACEAK; ASPSESSIONIDAABBDDSR=IDJIOCGBPHIHFKBJANFHMMHL; ASPSESSIONIDCACACDRT=GFFBDOACHNBLCIOIGBDMGFME; ASPSESSIONIDAAABABTS=LGKJOGLCPIFBJKOBLMBONODK; ASPSESSIONIDCABAADRT=MBEMEPFDDLPOCIMLKJMBJIOD; ASPSESSIONIDCCACADSR=AGOPLFAABJPGDFDPKHOMILMN; ASPSESSIONIDCACDDBRT=EADLMOKAEJHJAHMIFDDCJEBJ; ASPSESSIONIDAABADARS=GAPKMHFBIFOBFBCIJNIDBLCE; ASPSESSIONIDAAABCDTR=JDACBAACBEAGFOEEGJNLJOCI; ASPSESSIONIDCCBAACQS=KGBEOIKCFBFDBBACHFDGMAIF; ASPSESSIONIDACDBADRT=GIDFMDFDDDCHIDIMNMIMLOBI; ASPSESSIONIDAACBACQS=FMACOLPDGPFIMKOCBPCLOANJ; ASPSESSIONIDACBACDTQ=FMHFAFKANKCALAPJHEBFPCJJ; ASPSESSIONIDCCABDBRT=MGCOBMEBMPIPCKLNPNGHNHOC; ASPSESSIONIDAAADCCSQ=GOCBHNJCOMMHHIODJKGHJFEH; ASPSESSIONIDCCDCCDSQ=IINDBGEDCBOLBDOIIDHJOHFB; ASPSESSIONIDAAABCAQS=OOLONOODGHJCDKIMILHGICBA; Hm_lvt_512ed551fae9428abd7d743009588c7a=1536239280,1536411972,1536882027,1537222341; Hm_lvt_f94e107103e3c39e0665d52b6d4a93e7=1536239280,1536411972,1536882027,1537222341; ASPSESSIONIDCCCDCCRT=BLPBLAMADKGBBDEBCOOOIGBG; ASPSESSIONIDCABCCAQT=CEEKLENAOGMLNAKDLJLHJJNC; ASPSESSIONIDACABCAST=BIIJEAEBNAONCBMHDKNLEKNI; skinName=default; ASPSESSIONIDAACBADSR=ELCNADJCJLGCFCIDCBHMDOJP; CNZZDATA3432862=cnzz_eid%3D1639559901-1536880879-null%26ntime%3D1537605054; _qdda=3-1.1; _qddab=3-tjchb6.jmd79uj8; _qddamta_4009972996=3-0; Juming%2Ecom=islogincode=2e1efc2d69882129b8&login%5Fuid=233280&new%5Fbanban%5Fzhu=1&sc%5Fcsrf=76660d22ed8bd8e9b5; Hm_lpvt_512ed551fae9428abd7d743009588c7a=1537606658; Hm_lpvt_f94e107103e3c39e0665d52b6d4a93e7=1537606658");
			httpGet.setHeader("Connection", " keep-alive");
			httpGet.setHeader("Upgrade-Insecure-Requests", "1");
			httpGet.setHeader("Cache-Control", "max-age=0, no-cache");
			httpGet.setHeader("Pragma", "no-cache");
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity responseEntity = httpResponse.getEntity();
			String response = EntityUtils.toString(responseEntity, "gbk");
			System.out.println(response);
			String indexStr = "/hao/index.htm?chaqqljapi=1&cha_ym=010jdwxlwj.com";
			int qqIndex = response.indexOf(indexStr);
			String qqUrl = response.substring(qqIndex,
					qqIndex + indexStr.length() + 39);

			int wxIndex = response.lastIndexOf(indexStr);
			String wxUrl = response.substring(wxIndex,
					wxIndex + indexStr.length() + 45);
			Document doc = Jsoup.parse(response);
			String punycode = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1)")
					.text();
			String icpNo = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)")
					.text();
			String icpUser = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3)")
					.text();
			String icpNature = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4)")
					.text();
			String siteTitle = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(1)")
					.text();
			String baTime = doc
					.selectFirst(
							"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2)")
					.text();
			System.out.println(qqUrl);

			System.out.println(icpNature + " finished.");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main1(String[] args) throws Exception {
		String sourcePath = "F:\\temp\\domain_info.txt";
		String qqUrlPath = "F:\\temp\\qq_url.txt";
		String wxUrlPath = "F:\\temp\\wx_url.txt";
		String icpFilePath = "F:\\temp\\icp.txt";
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		FileReader fr = new FileReader(sourcePath);
		BufferedReader br = new BufferedReader(fr);
		FileWriter fw_icp = new FileWriter(icpFilePath);
		FileWriter fw_qq = new FileWriter(qqUrlPath);
		FileWriter fw_wx = new FileWriter(wxUrlPath);
		String line = null;
		while ((line = br.readLine()) != null) {
			try {
				HttpGet httpGet = new HttpGet(
						"http://www.juming.com/hao/?cha_ym=" + line);
				httpGet.setHeader("Host", "www.juming.com");
				httpGet.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:62.0) Gecko/20100101 Firefox/62.0");
				httpGet.setHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				httpGet.setHeader("Accept-Language",
						"zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
				httpGet.setHeader("Accept-Encoding", "gzip, deflate");
				httpGet.setHeader("Referer",
						"http://www.juming.com/?tt=0&t=tiao_juming.com");
				httpGet.setHeader(
						"Cookie",
						"UM_distinctid=165d185038b13d-0e48b295f02a48-323b5b03-1fa400-165d185038c28a; _qddaz=QD.80ps6t.2rypca.jm061knx; pgv_pvi=8871671808; tencentSig=6469347328; skinName=default; ASPSESSIONIDCCDCCDSQ=FILLEHEDDHKHAPIFKGNCIDMM; Hm_lvt_512ed551fae9428abd7d743009588c7a=1536822989,1536964917,1536968609,1537173246; Hm_lvt_f94e107103e3c39e0665d52b6d4a93e7=1536822989,1536964917,1536968609,1537173246; IESESSION=alive; pgv_si=s6808730624; CNZZDATA3432862=cnzz_eid%3D1557794136-1536815777-null%26ntime%3D1537183323; _qdda=3-1.2zfbem; _qddab=3-v8yljw.jm68smwq; _qddamta_4009972996=3-0; Juming%2Ecom=login%5Fuid=236619&sc%5Fcsrf=c806ee0c745472c606&new%5Fbanban%5Fzhu=1&islogincode=c2cf59e0994a586b47&t%5Ftuiguang=tiao%5Fjuming%2Ecom; Hm_lpvt_512ed551fae9428abd7d743009588c7a=1537186099; Hm_lpvt_f94e107103e3c39e0665d52b6d4a93e7=1537186099");
				httpGet.setHeader("Connection", " keep-alive");
				httpGet.setHeader("Upgrade-Insecure-Requests", "1");
				httpGet.setHeader("Cache-Control", "max-age=0, no-cache");
				httpGet.setHeader("Pragma", "no-cache");
				CloseableHttpResponse httpResponse = httpClient
						.execute(httpGet);
				HttpEntity responseEntity = httpResponse.getEntity();
				String response = EntityUtils.toString(responseEntity, "gbk");
				String indexStr = "/hao/index.htm?chaqqljapi=1&cha_ym=" + line;
				int qqIndex = response.indexOf(indexStr);
				String qqUrl = response.substring(qqIndex,
						qqIndex + indexStr.length() + 39);
				fw_qq.write(qqUrl);
				fw_qq.write("\n");
				int wxIndex = response.lastIndexOf(indexStr);
				String wxUrl = response.substring(wxIndex,
						wxIndex + indexStr.length() + 45);
				fw_wx.write(wxUrl);
				fw_wx.write("\n");
				Document doc = Jsoup.parse(response);
				String punycode = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(1)")
						.text();
				String icpNo = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(2)")
						.text();
				String icpUser = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(3)")
						.text();
				String icpNature = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(2) > td:nth-child(4)")
						.text();
				String siteTitle = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(1)")
						.text();
				String baTime = doc
						.selectFirst(
								"div.datas-list:nth-child(1) > table:nth-child(2) > tbody:nth-child(1) > tr:nth-child(4) > td:nth-child(2)")
						.text();
				fw_icp.write(punycode + "," + icpNo + "," + icpUser + ","
						+ icpNature + "," + siteTitle + "," + baTime);
				fw_icp.write("\n");
				fw_qq.flush();
				fw_wx.flush();
				fw_icp.flush();
				System.out.println(punycode + " finished.");
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		br.close();
		fw_qq.close();
		fw_wx.close();
		fw_icp.close();

	}

}
