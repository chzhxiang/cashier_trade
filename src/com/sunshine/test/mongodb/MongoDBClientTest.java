/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.test.mongodb;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.utils.MongodbUtils;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.DBConstant.SortOperationalEnum;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.LogicalCondition;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.framework.utils.Junit4SpringContextHolder;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.mobileapp.order.service.TradeOrderService;
import com.sunshine.payment.CardConstant;
import com.sunshine.test.mongodb.service.TestEntityService;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package mongodb.test
 * @ClassName MongoDBClientTest.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月27日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class MongoDBClientTest extends Junit4SpringContextHolder {
	private static Logger logger = LoggerFactory.getLogger(MongoDBClientTest.class);

	@Test
	public void insert() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		TestEntity entity = new TestEntity();
		entity.setCode("yctApp");
		entity.setName("医程通");
		entity.setAreaCode("440444");
		entity.setIsCityApp(1);
		String pkId = testEntityService.insert(entity);
		logger.info("pkId:{}", pkId);
	}

	@Test
	public void batchInsert() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<TestEntity> entitys = new ArrayList<>();
		TestEntity entity = null;
		for (int i = 0; i < 100; i++) {
			entity = new TestEntity();
			entity.setCode("yctApp" + i);
			entity.setName("医程通" + i);
			entity.setAreaCode("areaCode" + i);
			entity.setIsCityApp(i);
			entitys.add(entity);
		}
		testEntityService.batchInsert(entitys);
		logger.info("batch insert entities end.");
	}

	@Test
	public void count() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		LogicalCondition condition = new LogicalCondition();
		long count = testEntityService.count(condition);
		logger.info("dataCount:{}", count);
	}

	@Test
	public void find() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		TestEntity entity = new TestEntity();
		entity.setId("2700dbc194dc424a81e9bdc6df686062");
		entity = testEntityService.find(entity);
		logger.info("data:{}", JSON.toJSONString(entity));
	}

	@Test
	public void findById() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		String id = "2700dbc194dc424a81e9bdc6df686062";
		TestEntity entity = testEntityService.findById(id);
		logger.info("data:{}", JSON.toJSONString(entity));
	}

	@Test
	public void findByIds() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<String> ids = new ArrayList<>();
		ids.add("ee08dc34a8e74f10b9a45f795641293f");
		ids.add("88a92fea5235482a9c19538dab0ebae0");
		ids.add("6c90c650f3cd420d910d68352b542a8e");
		List<TestEntity> entities = testEntityService.findByIds(ids);
		logger.info("count:{},datas:{}", entities.size(), JSON.toJSONString(entities));
	}

	@Test
	public void createIndex() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<String> fieldNames = new ArrayList<>();
		fieldNames.add("code");
		fieldNames.add("name");
		List<String> indexNames = testEntityService.createIndex(fieldNames);
		logger.info("indexNames:{}", indexNames);
	}

	@Test
	public void findByParams() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("code", "yctApp1");
		paraMap.put("name", "医程通1");
		List<TestEntity> entities = testEntityService.findByParams(paraMap);
		if (!CollectionUtils.isEmpty(entities)) {
			logger.info("data count:{}", entities.size());
			for (TestEntity entity : entities) {
				logger.info("data:{}", JSON.toJSONString(entity, SerializerFeature.NotWriteDefaultValue));
			}
		} else {
			logger.info("query no data");
		}
	}

	@Test
	public void findByCondition() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<Condition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("name", "医程通", QueryOperationalEnum.REGEX);
		conditions.add(condition);
		condition = new QueryCondition("code", "App1", QueryOperationalEnum.REGEX);
		conditions.add(condition);
		List<TestEntity> entities = testEntityService.findByAndCondition(conditions);
		if (!CollectionUtils.isEmpty(entities)) {
			logger.info("data count:{}", entities.size());
			for (TestEntity entity : entities) {
				logger.info("entity:{}", JSON.toJSONString(entity, SerializerFeature.NotWriteDefaultValue));
			}
		} else {
			logger.info("query no data");
		}
	}

	@Test
	public void update() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		TestEntity entity = new TestEntity();
		String id = "2700dbc194dc424a81e9bdc6df686062";
		entity.setId(id);
		entity.setAppIdSource(0);
		entity.setAreaCode("areaCode1");
		entity.setCode("yctApp1");
		entity.setName("医程通1");
		testEntityService.update(entity);
	}

	@Test
	public void updateForValue() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		TestEntity entity = new TestEntity();
		String id = "ee0d52afa01740c48ec2a8cd30c1b773";
		entity.setId(id);
		entity.setAppIdSource(0);
		entity.setAreaCode("areaCode1");
		entity.setCode("yctApp1");
		entity.setName("医程通1");
		entity.setIsCityApp(null);
		testEntityService.updateNotWithNull(entity);
	}

	@Test
	public void updateById() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		String id = "2700dbc194dc424a81e9bdc6df686062";
		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("code", "2700dbc194dc424a81e9bdc6df686062");
		updateMap.put("name", "2700dbc194dc424a81e9bdc6df686062");
		updateMap.put("areaCode", "2700dbc194dc424a81e9bdc6df686062");
		testEntityService.updateById(id, updateMap);
	}

	@Test
	public void updateByParams() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("code", "2700dbc194dc424a81e9bdc6df686062");
		conditionMap.put("name", "2700dbc194dc424a81e9bdc6df686062");

		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("code", "yctApp0");
		updateMap.put("name", "医程通0");
		Long updateCount = testEntityService.updateByParams(conditionMap, updateMap);
		logger.info("updateByParams updateCount:{}", updateCount);
	}

	@Test
	public void updateByAndCondition() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<Condition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("code", "yctApp0", QueryOperationalEnum.EQ);
		conditions.add(condition);

		// areaCode 为 null
		List<Object> inVals = new ArrayList<>();
		inVals.add(null);
		condition = new QueryCondition("isCityApp", inVals, QueryOperationalEnum.IN);
		conditions.add(condition);

		Map<String, Object> updateMap = new HashMap<>();
		updateMap.put("code", "yctApp_1");
		updateMap.put("name", "医程通_1");
		updateMap.put("isCityApp", 1);
		Long updateCount = testEntityService.updateByAndCondition(conditions, updateMap);
		logger.info("updateByAndCondition updateCount:{}", updateCount);
	}

	@Test
	public void findListByPage() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<Condition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("code", "yctApp", QueryOperationalEnum.REGEX);
		conditions.add(condition);
		Map<String, Integer> sortMap = new HashMap<>();
		sortMap.put("name", SortOperationalEnum.DESC.getSortVal());

		int pageSize = 15;
		for (int i = 0; i < 7; i++) {
			Page<TestEntity> page = new Page<>();
			page.setPageSize(pageSize);
			page.setPageNum(i + 1);
			PageInfo<TestEntity> infos = testEntityService.findListByPage(conditions, sortMap, page);
			logger.info("findListByPage dataCount:{}", infos.getList().size());
		}
	}

	@Test
	public void findByLogicalCondition() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);

		// (code='yctApp0' or code='yctApp_1') and name='医程通_1'
		List<Condition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("code", "yctApp0", QueryOperationalEnum.EQ);
		conditions.add(condition);
		condition = new QueryCondition("code", "yctApp1", QueryOperationalEnum.EQ);
		conditions.add(condition);
		LogicalCondition logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.OR);

		conditions = new ArrayList<>();
		condition = new QueryCondition("name", "医程通_1", QueryOperationalEnum.EQ);
		conditions.add(condition);
		conditions.add(logicalCondition);
		logicalCondition = new LogicalCondition(conditions, QueryOperationalEnum.AND);

		List<TestEntity> entities = testEntityService.findByLogicalCondition(logicalCondition);
		logger.info("findByLogicalCondition dataCount:{},datas:{}", entities.size(), JSON.toJSONString(entities));

	}

	@Test
	public void delete() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		TestEntity testEntity = new TestEntity();
		String id = "2700dbc194dc424a81e9bdc6df686062";
		testEntity.setId(id);
		testEntityService.delete(testEntity);
	}

	@Test
	public void deleteById() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		String id = "f11afd0ec78343b0939d0056e5fd8c0d";
		testEntityService.deleteById(id);
	}

	@Test
	public void deleteByParams() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("code", "yctApp1");
		paraMap.put("name", "医程通1");
		Long delCount = testEntityService.deleteByParams(paraMap);
		logger.info("deleteByParams delCount:{}", delCount);
	}

	@Test
	public void deleteByAndCondition() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		List<Condition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("name", "医程通", QueryOperationalEnum.REGEX);
		conditions.add(condition);
		condition = new QueryCondition("code", "App2", QueryOperationalEnum.REGEX);
		conditions.add(condition);
		Long delCount = testEntityService.deleteByAndCondition(conditions);
		logger.info("deleteByCondition delCount:{}", delCount);
	}

	@Test
	public void deleteAll() {
		TestEntityService testEntityService = SpringContextHolder.getBean(TestEntityService.class);
		Long delCount = testEntityService.deleteAll();
		logger.info("deleteAll delCount:{}", delCount);
	}

	@Test
	public void queryFromMongodb() {
		List<QueryCondition> conditions = new ArrayList<>();
		QueryCondition condition = new QueryCondition("pharmacyCode", "bbb");
		conditions.add(condition);
		condition = new QueryCondition("hospitalCode", "gzfe");
		conditions.add(condition);
		//		DrugService drugService = SpringContextHolder.getBean(DrugService.class);
		//		drugService.findByAndCondition(conditions);
	}

	@Test
	public void insertToMongodb() {
		//		DrugEntity drug = new DrugEntity();
		//		drug.setHospitalCode("gzfe---");
		//		drug.setPharmacyCode("bbb---");
		//		drug.setDrugCode("999---");
		//		drug.setDrugName("999感冒灵---");
		//		drug.setDrugSpec("aaabbb");
		//		drug.setDrugStock("10");
		//		DrugService drugService = SpringContextHolder.getBean(DrugService.class);
		//		drugService.insert(drug);
	}

	@Test
	public void sychronyDrugInfoToHospital() throws ClientProtocolException, IOException {
		//		DrugService drugService = SpringContextHolder.getBean(DrugService.class);
		//		List<DrugEntity> drugs = drugService.getDrugInfo("gzfe", "bbb", null);
		//		String url = "http://192.168.200.225:9090/services/rest/v1/drug/synchroDrugInfo";
		//		Map<String, String> headerParams = new HashMap<>();
		//		headerParams.put("content-type", "application/json");
		//		headerParams.put("charset", "utf-8");
		//		RestResponse response = HttpClientNew.post(url, headerParams, JSONArray.fromObject(drugs).toString());
		//		System.out.println(response.getData());
		/*
		 * HttpResponse response = PharmacyServiceImpl.client.post(url, JSONArray.fromObject(drugs).toString(), HttpConstants.JSON_TYPE,
		 * HttpConstants.CHARACTER_ENCODING_UTF8);
		 * 
		 * if (response != null && response.getStatusCode() == 200) { System.out.println(response.asString()); }
		 */

	}

	@Test
	public void findTest() {
		TradeOrderService orderService = SpringContextHolder.getBean(TradeOrderService.class);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", "sun90f99466b08c4c7");
		//params.put("tradeNo", "4010208201710220000005");
		String cardSerialNo = "{\"$elemMatch\":{\"outOrderNo\":\"20171128101010000014\"}}";
		params.put("partialRefunds.outRefundNo", "20171128101010000014");
		List<TradeOrder> item = null;//orderService.findByParams(params);
		List<Condition> conditions = new ArrayList<Condition>();
		Condition condition = Restrictions.eq("appId", "sun90f99466b08c4c7").add(Restrictions.eq("tradeNo", "4010208201710220000005"));
		condition.add(Restrictions.eq("bankCard", cardSerialNo));
		conditions.add(condition);
		List<String> queryfiled = new ArrayList<String>();
		queryfiled.add("_id");
		queryfiled.add("tradeStatus");
		queryfiled.add("outTradeNo");
		queryfiled.add("tradeNo");
		queryfiled.add("agtTradeNo");

		queryfiled.add("outRefundNo");
		queryfiled.add("refundNo");
		queryfiled.add("agtRefundNo");
		//item = orderService.findByAndCondition(conditions, queryfiled);
		item = orderService.findByParams(params);
		logger.info("--------------------size:{},item:{}", item.size(), JSON.toJSON(item));
	}

	/**
	 * 根据排序查询数据
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月4日上午9:50:59
	 */
	@Test
	public void queryBankCardBySort() {
		Map<String, Integer> sortMap = new LinkedHashMap<>();
		sortMap.put("createTime", SortOperationalEnum.DESC.getSortVal());
		sortMap.put("activateStatus", SortOperationalEnum.DESC.getSortVal());

		List<Condition> conditions = new ArrayList<>();
		Condition condition = null;
		condition = Restrictions.eq("activateStatus", CardConstant.ACTIVATE_STATUS_BUNDLING);
		conditions.add(condition);
		MongodbUtils<BankCard> mongodbUtils = new MongodbUtils<BankCard>();
		List<BankCard> list = mongodbUtils.findByConditionAndSort(null, sortMap, BankCard.class);
		System.out.println("-------------" + JSON.toJSONString(list));
	}

	/**
	 * 获取某个字段
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月4日上午9:52:28
	 */
	@Test
	public void queryBankCardByField() {
		BankCardService bankCardService = SpringContextHolder.getBean(BankCardService.class);
		List<Condition> conditions = new ArrayList<>();
		Condition condition = null;
		condition = Restrictions.eq("activateStatus", CardConstant.ACTIVATE_STATUS_BUNDLING);
		conditions.add(condition);
		List<String> lists = new ArrayList<>();
		/** 获取目标对象中所有的Field */
		Field[] fields = BankCard.class.getDeclaredFields();
		/** 循环所有的Field */
		for (Field field : fields) {
			String attr = field.getName();
			if (!attr.equals("logs")) {
				lists.add(attr);
			}
		}
		List<BankCard> list = bankCardService.findByAndCondition(conditions, lists);
		System.out.println("-------------" + JSON.toJSONString(list));
	}
}
