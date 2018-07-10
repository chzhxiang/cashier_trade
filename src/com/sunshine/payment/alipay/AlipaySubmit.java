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
package com.sunshine.payment.alipay;

import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.sunshine.common.utils.sign.MD5;
import com.sunshine.common.utils.sign.RSA;
import com.sunshine.payment.TradeConstant;

/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipaySubmit {
	private static Logger logger = Logger.getLogger(AlipaySubmit.class);

	/**
	 * 生成签名结果
	 * @param sPara 要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildRequestMysign(Map<String, String> sPara, String privateKey, String signType) {
		String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		logger.info("“prestr”：" + prestr);
		String mysign = "";
		if (TradeConstant.SIGN_TYPE_MD5.equalsIgnoreCase(signType)) {
			mysign = MD5.sign(prestr, privateKey, TradeConstant.INPUT_CHARSET);
		} else if (TradeConstant.SIGN_TYPE_RSA.equalsIgnoreCase(signType)) {
			mysign = RSA.sign(prestr, privateKey, TradeConstant.INPUT_CHARSET);
		}
		return mysign;
	}

	public static void main(String[] args) {
		String prestr =
				"partner=\"2088221618187983\"&seller_id=\"15918786411@163.com\"&out_trade_no=\"20160512143544\"&subject=\"测试的商品\"&body=\"该测试商品的详细描述\"&total_fee=\"0.01\"&notify_url=\"http://dev.cloud.968309.com/trade/sunshineTest/pay/aliPayNotify\"&service=\"mobile.securitypay.pay\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"";
		String privateKey =
				"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL4Z/y1ELMGXUBc9FpdAGKbY5iHf3hB2BS1p5fphlsEwFyQfhqPIwkB8eTJwfCeGPAsrgXKFdqhkG0YXYjkoAVvFOOzBU6p6OIijNeuuZp3/SxXatmHgqBZuvnIQ9hHRi+T+/GNVM6t4nBKodWq+IR4Ja8G3zXKpBA2BtAhwbBVJAgMBAAECgYB2irl/3apXEecEhrhmmraEnG5tIy9TbnTuC8I4LtCWKUnU4b/QtCjFk5jz5gbVkrQwdvGimy0R9lK3k29Rmuu03GYILe8V3YnqqoLTC56hDA70f0gwZZi2WsHzJtHl8lrireQ+t1Eaxx+zSpjjSfAxfyP/m6g2vP2ouYrBtFwKzQJBAPZ2lf7i8D6Balj4ebJBt69yLCZ7EGzwoCUJWq5PYm8/LEkL5xEWCbztxzp9ciF4DKlEAHQWo8CbFOlrB49j1RsCQQDFdRqKsYA0L+QpXlqfdFg49TR1tEasMbcGlZbR5BA7yc4pKfsc8loGb2FldfBpxesMiynZDeWyIMiw61oMrTlrAkEAklLzWKFpWuLAvdO/hKppoJsbRTlZFiSCH5r/IG/3E+8gnrmXVNiNrYJJ/xrNMLuyYKgD0OhEDizEbJRjp6jwswJBAJcE9YPWNkOzzUGGlLEX/tGxWKodc2AHtgAnMO7fCztvIWqCXTjKcugxD8DV70Y2xYsWdK853c/bGyOVHY5MfnkCQH83Sj226YPvBjYbR3XkS8sVinaMTcFMURPVURWIEeOJJFB79sG5M8x5aM4ZJt7TrBu31YU/LysvDhiDUXc4WCw=";
		System.out.println(RSA.sign(prestr, privateKey, TradeConstant.INPUT_CHARSET));
	}

	/**
	 * 生成要请求给支付宝的参数数组
	 * @param sParaTemp 请求前的参数数组
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String privateKey, String signType) {
		//除去数组中的空值和签名参数
		Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
		//生成签名结果
		String mysign = buildRequestMysign(sPara, privateKey, signType);
		//签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		sPara.put("sign_type", signType);
		return sPara;
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * @param sParaTemp 请求参数数组
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	/*public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName, String privateKey, String signType) {
		//待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp, privateKey, signType);
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + AlipayUtils.ALIPAY_GATEWAY_NEW + "_input_charset="
				+ TradeConstant.INPUT_CHARSET + "\" method=\"" + strMethod + "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		//submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

		return sbHtml.toString();
	}*/

	/**
	 * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
	 * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
	 * 如：buildRequest("", "",sParaTemp)
	 * @param strParaFileName 文件类型的参数名
	 * @param strFilePath 文件路径
	 * @param sParaTemp 请求参数数组
	 * @return 支付宝处理结果
	 * @throws Exception
	 */
	/*public static String buildRequest(String strParaFileName, String strFilePath, Map<String, String> sParaTemp, String privateKey, String signType)
			throws Exception {
		//待请求参数数组
		Map<String, String> sPara = buildRequestPara(sParaTemp, privateKey, signType);
		logger.info("sPara:【" + sPara + "】");
		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);
		//设置编码集
		request.setCharset(TradeConstant.INPUT_CHARSET);

		request.setParameters(generatNameValuePair(sPara));
		request.setUrl(AlipayUtils.ALIPAY_GATEWAY_NEW + "_input_charset=" + TradeConstant.INPUT_CHARSET);
		logger.info("支付宝请求参数:" + JSON.toJSONString(request) + ",");
		HttpResponse response = httpProtocolHandler.execute(request, strParaFileName, strFilePath);
		if (response == null) {
			return null;
		}

		String strResult = response.getStringResult();
		return strResult;
	}*/

	/**
	 * MAP类型数组转换成NameValuePair类型
	 * @param properties  MAP类型数组
	 * @return NameValuePair类型数组
	 */
	private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
		}
		/*if (nameValuePair.length > 0) {
			for (NameValuePair nameValuePair2 : nameValuePair) {
				logger.info("nameValuePair2,name【" + nameValuePair2.getName() + "】----value【" + nameValuePair2.getValue() + "】");
			}
		}*/
		return nameValuePair;
	}

	/**
	 * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
	 * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
	 * @return 时间戳字符串
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	/*@SuppressWarnings("unchecked")
	public static String query_timestamp(String partner) throws MalformedURLException, DocumentException, IOException {

		//构造访问query_timestamp接口的URL串
		String strUrl =
				AlipayUtils.ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + partner + "&_input_charset" + TradeConstant.INPUT_CHARSET;
		StringBuffer result = new StringBuffer();

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(strUrl).openStream());

		List<Node> nodeList = doc.selectNodes("//alipay/*");

		for (Node node : nodeList) {
			// 截取部分不需要解析的信息
			if (node.getName().equals("is_success") && node.getText().equals("T")) {
				// 判断是否有成功标示
				List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
				for (Node node1 : nodeList1) {
					result.append(node1.getText());
				}
			}
		}

		return result.toString();
	}*/
}
