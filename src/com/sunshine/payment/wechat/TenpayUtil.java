/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年4月20日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

@SuppressWarnings("unused")
public class TenpayUtil {
	private Log logger = LogFactory.getLog(TenpayUtil.class);
	private static Object Server;
	private static String QRfromGoogle;

	/**
	 * 把对象转换成字符串
	 * @param obj
	 * @return String 转换成字符串,若对象为null,则返回空字符串.
	 */
	public static String toString(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	/**
	 * 把对象转换为int数值.
	 * 
	 * @param obj
	 *            包含数字的对象.
	 * @return int 转换后的数值,对不能转换的对象返回0。
	 */
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * 获取当前日期 yyyyMMdd
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ( ( random * num ) );
	}

	/**
	 * 获取编码字符集
	 * @param request
	 * @param response
	 * @return String
	 */

	public static String getCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {

		if (null == request || null == response) {
			return "gbk";
		}

		String enc = request.getCharacterEncoding();
		if (null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}

		if (null == enc || "".equals(enc)) {
			enc = "gbk";
		}

		return enc;
	}

	public static String URLencode(String content) {

		String URLencode;

		URLencode = replace(Server.equals(content), "+", "%20");

		return URLencode;
	}

	private static String replace(boolean equals, String string, String string2) {

		return null;
	}

	/**
	 * 获取unix时间，从1970-01-01 00:00:00开始的秒数
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if (null == date) {
			return 0;
		}

		return date.getTime() / 1000;
	}

	public static String QRfromGoogle(String chl) {
		int widhtHeight = 300;
		String EC_level = "L";
		int margin = 0;
		String QRfromGoogle;
		chl = URLencode(chl);

		QRfromGoogle =
				"http://chart.apis.google.com/chart?chs=" + widhtHeight + "x" + widhtHeight + "&cht=qr&chld=" + EC_level + "|" + margin + "&chl="
						+ chl;

		return QRfromGoogle;
	}

	/**
	 * 时间转换成字符串
	 * @param date 时间
	 * @param formatType 格式化类型
	 * @return String
	 */
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}

	//	public static String createSign(String charSet,SortedMap<Object,Object> parameters){
	//        StringBuffer sb = new StringBuffer();
	//        Set es = parameters.entrySet();
	//        Iterator it = es.iterator();
	//        while(it.hasNext()) {
	//            Map.Entry entry = (Map.Entry)it.next();
	//            String k = (String)entry.getKey();
	//            String v = (String)entry.getValue();
	//            if(null != v && !"".equals(v)
	//                    && !"sign".equals(k) && !"key".equals(k)) {
	//                sb.append(k + "=" + v + "&");
	//            }
	//        }
	//        sb.append("key=" + YXWConfig.KEY);//xxxxxx换成你的API密钥
	//        String sign = MD5Util.MD5Encode(sb.toString(), charSet).toUpperCase();
	//        return sign;
	//    }

	public static String getSign(Map<String, String> params, String paykey) throws Exception {
		String sign = "";

		String[] keys = new String[params.size()];
		params.keySet().toArray(keys);

		Arrays.sort(keys);

		for (String key : keys) {
			String value = params.get(key);
			if (StringUtils.isNotBlank(value)) {
				sign += key + "=" + params.get(key) + "&";
				//sign += key + "=" + URLEncoder.encode(params.get(key), "utf-8") + "&";
			}
		}
		sign += "key=" + paykey;
		System.out.println(sign);
		sign = md5(sign.getBytes());

		return sign;
	}

	@SuppressWarnings("rawtypes")
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
			// TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ( ( str = bufferedReader.readLine() ) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			//logger .error("连接超时：{}", ce);
		} catch (Exception e) {
			//logger.error("https请求异常：{}", e);
		}
		return null;
	}

	public static String genGUID(String type) throws NoSuchAlgorithmException {

		String uuid = UUID.randomUUID().toString();

		if ("@guid32".equals(type)) {
			return md5(uuid.getBytes());
		}
		if ("@guid16".equals(type)) {
			return md5(uuid.getBytes()).substring(8, 24);
		} else {
			return uuid;
		}

	}

	public static String md5(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
		md5.update(bytes);
		byte[] md5bytes = md5.digest();
		String hs = "";
		String stmp = "";
		for (int n = 0; n < md5bytes.length; n++) {
			stmp = ( java.lang.Integer.toHexString(md5bytes[n] & 0XFF) );
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public static String bReqXML(Map<String, String> params) {
		Element xml = DocumentHelper.createElement("xml");
		for (String key : params.keySet()) {
			xml.addElement(key).addCDATA(params.get(key));
		}

		return xml.asXML();
	}
}
