/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月30日下午2:57:49</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.MongoDBFactory;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;

/**
 * @param <T>
 * @Project: cashier_trade
 * @Package: com.sunshine.common.utils
 * @ClassName: MongodbUtils
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月30日下午2:57:49
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class MongodbUtils<T> {

	protected MongoClient mongoClient = SpringContextHolder.getBean(MongoClient.class);

	protected String dataBase;

	public String getDataBase() {
		return dataBase;
	}

	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * 根据排序和指定条件查询数据
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月30日下午3:31:52
	 */
	@SuppressWarnings("unchecked")
	public List<T> findByConditionAndSort(List<Condition> conditions, Map<String, Integer> sortMap, Class<?> clazz) {
		Bson sort = null;
		if (CollectionUtils.isEmpty(sortMap)) {
			sort = new Document();
		} else {
			sort = Document.parse(JSON.toJSONString(sortMap));
		}

		Bson filter = null;
		if (CollectionUtils.isEmpty(conditions)) {
			filter = new Document();
		} else {
			LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);
			filter = logicalCondition.buildConditionBson();
		}

		List<T> list = new ArrayList<>();
		MongoCollection<Document> collection = getConCollection(clazz);
		MongoCursor<Document> cursor = collection.find(filter).sort(sort).iterator();
		while (cursor.hasNext()) {
			list.add((T) com.alibaba.fastjson.JSON.parseObject(cursor.next().toJson(), clazz));
		}
		return list;
	}

	/**
	 * 获取当前数据库
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月30日下午3:31:38
	 */
	public MongoCollection<Document> getConCollection(Class<?> clazz) {
		MongoDatabase db = mongoClient.getDatabase(currentDataBase());
		String className = clazz.getName();
		MongoCollection<Document> collection = db.getCollection(className);
		return collection;
	}

	private String currentDataBase() {
		if (StringUtils.isBlank(dataBase)) {
			dataBase = MongoDBFactory.DEFAULT_DATA_BASE;
		}
		return dataBase;
	}
}
