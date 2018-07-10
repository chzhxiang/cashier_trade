/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月10日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.order.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.utils.CommonUtil;
import com.sunshine.framework.mvc.controller.PageParams;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mongodb.DBConstant.QueryOperationalEnum;
import com.sunshine.framework.mvc.mongodb.vo.QueryCondition;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.mobileapp.order.entity.TradeOrder;
import com.sunshine.payment.CardConstant;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.service.TradeChannelService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.platform.order.entity.OrderDataView;
import com.sunshine.platform.order.entity.OrderParamsVo;
import com.sunshine.platform.order.service.OrderService;
import com.sunshine.platform.utils.ExcelView;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.order.controller
 * @ClassName: OrderController
 * @Description: <p>订单管理controller</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年10月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/order")
public class OrderController {
	private Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrderService orderService;

	@Autowired
	protected MerchantService merchantService;

	@Autowired
	protected MerchantApplicationService merchantApplicationService;

	@Autowired
	protected TradeChannelService tradeChannelService;

	@Autowired
	protected BankCardService bankCardService;

	/**
	 * 跳转到订单列表页面
	 * @return
	 */
	@RequestMapping(value = "/orderList")
	public ModelAndView toOrderList() {
		ModelAndView modelAndView = new ModelAndView("/platform/order/orderList");
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		List<TradeChannel> allChannel = tradeChannelService.findAll();

		QueryCondition condition = new QueryCondition("activateStatus", CardConstant.ACTIVATE_STATUS_DEL, QueryOperationalEnum.NE);
		List<BankCard> allBankCard = bankCardService.findByCondition(condition);
		for (BankCard bankCard : allBankCard) {
			bankCard.setBankCardNo(CommonUtil.formatMedicalCard(bankCard.getBankCardNo()));
		}
		modelAndView.addObject("merchant", allMerchant);
		modelAndView.addObject("channel", allChannel);
		modelAndView.addObject("bankCard", allBankCard);
		return modelAndView;
	}

	/**
	 * 查询商户的所有应用
	 * @param merchantNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getApplicationByMerchan")
	public RespBody getApplicationByMerchan(String merchantNo) {
		List<MerchantApplication> list = merchantApplicationService.findApplicationByMechNo(merchantNo);
		return new RespBody(StatusEnum.OK, list, "");
	}

	/**
	 * 分页查询订单
	 * @param request
	 * @param pageNum
	 * @param pageSize
	 * @param pageParams
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findOrderList")
	public PageInfo<OrderParamsVo> findOrderList(HttpServletRequest request,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",
					required = false, defaultValue = "10") Integer pageSize, PageParams pageParams) {
		logger.info("订单查询条件, pageParams:{}", JSON.toJSONString(pageParams));
		Page<TradeOrder> page = new Page<TradeOrder>();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		PageInfo<OrderParamsVo> pageInfo = orderService.queryByPage(pageParams.getParams(), page);
		return pageInfo;
	}

	/**
	 * 下载excel
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/dowmExcel")
	public ModelAndView dowmExcel(HttpServletResponse response, HttpServletRequest request, ModelMap model, @RequestParam(value = "pageNum",
			required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "100000") Integer pageSize, String pageParams) {
		Map<String, Object> params = null;
		try {
			pageParams = new String(java.net.URLDecoder.decode(pageParams, "utf-8"));
			params = new HashMap<String, Object>();
			logger.info("导出功能查询参数条件, pageParams:{}", pageParams);
			ObjectMapper mapper = new ObjectMapper();
			params = mapper.readValue(pageParams, HashMap.class);
		} catch (Exception e1) {
			logger.info("导出excel报表出错！");
			e1.printStackTrace();
		}
		// 转成map
		ModelAndView modelAndView = new ModelAndView();
		Page<TradeOrder> page = new Page<TradeOrder>();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		PageInfo<OrderParamsVo> pageInfo = orderService.queryByPage(params, page);
		String filename = "收银台支付订单" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
		List<OrderParamsVo> list = pageInfo.getList();
		//		List<OrderParamsVo> resultList = new ArrayList<OrderParamsVo>();
		//		for (TradeOrder order : list) {
		//			OrderParamsVo orderFormate = orderService.dataFormate(order);
		//			resultList.add(orderFormate);
		//		}
		modelAndView =
				new ModelAndView(new ExcelView(filename, null, new String[] { "merchantName",
						"appName",
						"tradeNo",
						"outTradeNo",
						"agtTradeNo",
						"payTimeVo",
						"subject",
						"totalFeeVo",
						"actualPayFreeVo",
						"refundFeeVo",
						"actualGetFreeVo",
						"tradeStatusVo",
						"channelName" }, new String[] { "商户	",
						"应用",
						"平台订单号",
						"商户订单号",
						"第三方订单号",
						"交易时间",
						"订单名称",
						"订单金额（元）",
						"实付金额（元）",
						"退款金额（元）",
						"实收金额（元）",
						"支付状态",
						"支付渠道" }, null, null, list, null), model);
		return modelAndView;
	}

	/**
	 * 跳转订单详细页面
	 * 
	 */
	@RequestMapping(value = "/orderDetail")
	public ModelAndView orderDetail(String id) {
		ModelAndView modelAndView = new ModelAndView("/platform/order/orderDetail");
		TradeOrder tradeOrder = orderService.findById(id);
		OrderParamsVo orderFormate = orderService.dataFormate(tradeOrder);
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, orderFormate);
		String backUrl = "/order/orderList";
		modelAndView.addObject("backUrl", backUrl);
		return modelAndView;
	}

	/**
	 * 跳转到订单总览页面
	 * @return
	 */
	@RequestMapping(value = "/orderDataView")
	public ModelAndView orderDataView() {
		ModelAndView modelAndView = new ModelAndView("/platform/order/orderDataView");
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		List<TradeChannel> allChannel = tradeChannelService.findAll();
		//计算汇总数据
		OrderDataView countTotalFree = orderService.countTotalFree(null);
		List<OrderDataView> mechanTotalFreeList = orderService.countMechanTotalFree(allMerchant, null);
		List<OrderDataView> mechanApplicationTotalFreeList = orderService.countMechanApplicationTotalFree(allMerchant, null);
		List<OrderDataView> channelTotalFreeList = orderService.countChannelTotalFree(allChannel, null);
		modelAndView.addObject("merchant", allMerchant);
		modelAndView.addObject("countTotalFree", countTotalFree);
		modelAndView.addObject("mechanTotalFreeList", mechanTotalFreeList);
		modelAndView.addObject("mechanApplicationTotalFreeList", mechanApplicationTotalFreeList);
		modelAndView.addObject("channelTotalFreeList", channelTotalFreeList);
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("appId", "sun90f99466b08c4c7");
		Map<String, Object> resultMap = orderService.polymerizationStatistics(params);
		logger.info("聚合统计所有订单结果:{}", JSON.toJSON(resultMap));
		modelAndView.addObject("polymerizationOrderData", resultMap);
		return modelAndView;
	}

	/**
	 * 查询订单总览页面
	 * @return
	 */
	@RequestMapping(value = "/searchOrderDataView")
	public ModelAndView searchOrderDataView(String pageParams) {
		ModelAndView modelAndView = new ModelAndView("/platform/order/orderDataView");
		Map<String, Object> params = null;
		try {
			pageParams = new String(java.net.URLDecoder.decode(pageParams, "utf-8"));
			params = new HashMap<String, Object>();
			logger.info("功能查询参数条件, pageParams:{}", pageParams);
			ObjectMapper mapper = new ObjectMapper();
			params = mapper.readValue(pageParams, HashMap.class);
		} catch (Exception e1) {
			logger.info("查询出错！");
			e1.printStackTrace();
		}
		Map<String, Object> map = orderService.countOneMechanApplicationTotalFree(params);
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		List<TradeChannel> allChannel = tradeChannelService.findAll();

		OrderDataView countTotalFree = orderService.countTotalFree(params); //汇总数据
		List<OrderDataView> mechanTotalFreeList = null;
		List<OrderDataView> mechanApplicationTotalFreeList = null;
		String merchantNo = (String) params.get("merchantNo");
		if (!StringUtils.isEmpty(merchantNo)) {
			Merchant merchant = merchantService.findByMerchantNo(merchantNo);
			List<Merchant> merchantList = new ArrayList<>();
			merchantList.add(merchant);
			mechanTotalFreeList = orderService.countMechanTotalFree(merchantList, params);
			mechanApplicationTotalFreeList = orderService.countMechanApplicationTotalFree(merchantList, params);
			List<MerchantApplication> list = merchantApplicationService.findApplicationByMechNo(merchantNo);
			modelAndView.addObject("application", list); //初始化应用
		} else {
			mechanTotalFreeList = orderService.countMechanTotalFree(allMerchant, params);
			mechanApplicationTotalFreeList = orderService.countMechanApplicationTotalFree(allMerchant, params);
		}
		List<OrderDataView> channelTotalFreeList = orderService.countChannelTotalFree(allChannel, params);
		modelAndView.addObject("params", params);
		modelAndView.addObject("merchant", allMerchant);
		modelAndView.addObject("countTotalFree", countTotalFree);
		modelAndView.addObject("mechanTotalFreeList", mechanTotalFreeList);
		modelAndView.addObject("mechanApplicationTotalFreeList", mechanApplicationTotalFreeList);
		modelAndView.addObject("channelTotalFreeList", channelTotalFreeList);
		return modelAndView;
	}

	/**
	 * 统计订单
	 * @return
	 */
	@RequestMapping(value = "/orderStatistics")
	public ModelAndView orderStatistics(HttpServletResponse response, HttpServletRequest request, ModelMap model) {
		ModelAndView view = new ModelAndView("/platform/order/orderStatistics");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String merchantNo = request.getParameter("merchantNo");
			String appId = request.getParameter("appId");
			if (!StringUtils.isEmpty(startDate)) {
				params.put("startDate", startDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				params.put("endDate", endDate);
			}
			if (!StringUtils.isEmpty(merchantNo)) {
				params.put("merchantNo", merchantNo);
				List<MerchantApplication> applications = merchantApplicationService.findApplicationByMechNo(merchantNo);
				model.put("application", applications);
			}
			if (!StringUtils.isEmpty(appId)) {
				params.put("appId", appId);
			}
			logger.info("查询参数:{}", JSON.toJSON(params));
			List<Merchant> merchants = merchantService.getAllMerchant();
			model.putAll(params);
			Map<String, Object> resultMap = orderService.polymerizationStatistics(params);
			logger.info("聚合统计所有订单结果:{}", JSON.toJSON(resultMap));
			model.put("merchants", merchants);

			model.putAll(resultMap);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计订单出错:{}", e);
		}
		return view;
	}

	/**
	 * 图表统计
	 * @param response
	 * @param request 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chartsStatistics")
	public ModelAndView chartsStatistics(HttpServletResponse response, HttpServletRequest request, ModelMap model) {
		ModelAndView view = new ModelAndView("/platform/order/chartsStatistics");
		List<Merchant> merchants = merchantService.getAllMerchant();
		model.put("merchants", merchants);
		return view;
	}

	/**
	 * 加载图形报表数据
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/chartsStatisticsData")
	public Object chartsStatisticsData(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			String merchantNo = request.getParameter("merchantNo");
			//String appIds = request.getParameter("appIds");
			if (!StringUtils.isEmpty(startDate)) {
				params.put("startDate", startDate);
			}
			if (!StringUtils.isEmpty(endDate)) {
				params.put("endDate", endDate);
			}
			if (!StringUtils.isEmpty(merchantNo)) {
				params.put("merchantNo", merchantNo);
			}
			/*if (!StringUtils.isEmpty(appIds)) {
				String[] appIdItem = appIds.split(",");
				params.put("appIds", java.util.Arrays.asList(appIdItem));
			}*/

			retMap = orderService.chartsStatistics(params);
		} catch (Exception e) {
			retMap.put(GlobalConstant.TRADE_IS_EXCEPTION, true);
			retMap.put(GlobalConstant.TRADE_IS_SUCCESS, false);
			retMap.put(GlobalConstant.TRADE_FAIL_MSG, e.getMessage());
			e.printStackTrace();
		}
		logger.info("retMap:{}", JSON.toJSONString(retMap));
		return retMap;
	}
}
