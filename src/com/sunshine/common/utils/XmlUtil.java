package com.sunshine.common.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XmlUtil {

	public static String map2Xml(Map<String, String> map) {
		if (null == map) {
			return "";
		}
		StringBuffer body = new StringBuffer(100);
		if (map != null) {
			for (Object key : map.keySet()) {
				body.append("<").append(key).append(">");
				body.append(map.get(key));
				body.append("</").append(key).append(">");
			}
		}
		return body.toString();
	}

	public static JSONObject xmlToJson(String methodParams) throws DocumentException {
		Element node = DocumentHelper.parseText(changeToXml(methodParams)).getRootElement();
		return elementToJSONObject(node);
	}

	private static JSONObject elementToJSONObject(Element node) {
		JSONObject result = new JSONObject();
		// 当前节点的名称、文本内容和属性
		List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
		for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
			result.put(attr.getName(), attr.getValue());
		}
		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		if (!listElement.isEmpty()) {
			for (Element e : listElement) {// 遍历所有一级子节点
				if (e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
					result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
				else {
					if (!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
						result.put(e.getName(), new JSONArray());// 没有则创建
					( (JSONArray) result.get(e.getName()) ).add(elementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
				}
			}
		}
		return result;
	}

	/**
	 * @param str
	 * @return
	 */
	private static String changeToXml(String str) {

		if (StringUtils.isNotEmpty(str)) {
			str = str.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
		}
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<response>").append(str).append("</response>");
		return sb.toString();
	}

	/**
	 * 将Javabean转换为Map。 包括父类自动
	 * @param javaBean javaBean
	 * @return Map对象
	 */
	public static Map toMap(Object javaBean) {
		Map result = new HashMap();
		try {
			Method[] methods = javaBean.getClass().getMethods();
			for (Method method : methods) {
				try {
					// get方法获取字段，剔除getClass
					if (method.getName().startsWith("get") && !method.getName().equals("getClass")) {
						String field = method.getName();
						field = field.substring(field.indexOf("get") + 3);
						field = field.toLowerCase().charAt(0) + field.substring(1);

						Object value = method.invoke(javaBean, (Object[]) null);
						result.put(field, null == value ? "" : value.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		} catch (SecurityException e) {
			return result;
		}
	}

	public static void main(String[] args) {
		String xml =
				"<tradeTotalFee>1</tradeTotalFee><subject>聚合二维码测试支付</subject><sign>Y7Q0nCKoBnDujUvlvAtuiyQKM4FS8nS+mdZ8kPrKEuOw4QXACiRExFdi7axL+xtqqbcNH6a5s1Wx1BbdgVP1D0aOkBeFf7s1j0pVK9iBue+FYJWhcReNbKBqoVCDGkLLu3b+gr9TzaYw1faBGq/xYDYbBVApw02NpfQi2eLIyh0=</sign><signMode>RSA</signMode><outOrderNo>1513849004438</outOrderNo><nonceStr>b969e5a8f9a04617a6e3196c2c960288</nonceStr><timeStamp>2017-12-21 17:36:44</timeStamp><appId>sun02c4640d1aed483</appId><notifyUrl>https://xxx/payNotify</notifyUrl><attach>name=123&type=999</attach><returnUrl>https://www.baidu.com/</returnUrl><outTime></outTime><merchantNo>9683096567809952</merchantNo>";
		try {
			JSONObject json = xmlToJson(xml);
			System.out.println(json.toJSONString());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}