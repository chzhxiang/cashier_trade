/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年1月10日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.payment.yctWallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Project: sunshine_trade 
 * @Package: com.sunshine.trade.payrefund.utils.yctWallet
 * @ClassName: WalletHC
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年1月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class WalletHC {
	private static Logger logger = LoggerFactory.getLogger(WalletHC.class);

	public static String doPost(String url, Map<String, String> map, String charset) {
		logger.info("钱包请求地址:{}", url);
		String result = "";
		// 创建默认的httpClient实例.    
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost    
		HttpPost httppost = new HttpPost(url);
		UrlEncodedFormEntity uefEntity;
		Long startTime = System.currentTimeMillis();
		try {
			uefEntity = new UrlEncodedFormEntity(generatNameValuePair(map), "UTF-8");
			httppost.setEntity(uefEntity);
			startTime = System.currentTimeMillis();
			logger.info("开始执行钱包请求交易startTime:{}", startTime);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源    
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("请求耗时:{} ms", System.currentTimeMillis() - startTime);
		return result;

	}

	private static List<NameValuePair> generatNameValuePair(Map<String, String> properties) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return pairs;
	}
}
