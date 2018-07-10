/**
 * <html>
 * <body>
 *  <P> Copyright 2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月12日</p>
 *  <p> Created by 党参</p>
 *  </body>
 * </html>
 */
package com.sunshine.mobileapp.order.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sunshine.framework.mvc.mongodb.MongoDBFactory;
import com.sunshine.framework.mvc.mongodb.dao.impl.BaseMongoDaoImpl;
import com.sunshine.mobileapp.order.dao.TradeOrderDao;
import com.sunshine.mobileapp.order.entity.TradeOrder;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.mobileapp.order.dao.impl
 * @ClassName: TradeOrderDaoImpl
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 党参
 * @Create Date: 2017年9月12日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class TradeOrderDaoImpl extends BaseMongoDaoImpl<TradeOrder, Serializable> implements TradeOrderDao {

	@Override
	public List<TradeOrder> getParams(Map<String, Object> params, List<String> fieldList) {
		BasicDBObject query = new BasicDBObject();
		for (String key : params.keySet()) {
			query.put(key, params.get(key));
		}
		BasicDBObject fields = new BasicDBObject();
		if (!CollectionUtils.isEmpty(fieldList)) {
			for (String string : fieldList) {
				fields.put(string, 1);
			}
		}
		MongoDatabase db = mongoClient.getDatabase(MongoDBFactory.DEFAULT_DATA_BASE);
		String className = getGenericClass().getName();
		MongoCollection<Document> collection = db.getCollection(className);
		MongoCursor<Document> cursor = collection.find(query).projection(fields).iterator();
		List<TradeOrder> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(JSON.parseObject(cursor.next().toJson(), TradeOrder.class));
		}
		return list;
	}

	/**
	 * 获取接口的泛型类型，如果不存在则返回null
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> getGenericClass() {
		Type t = getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ( (ParameterizedType) t ).getActualTypeArguments();
			return ( (Class<?>) p[0] );
		}
		return null;
	}
}
