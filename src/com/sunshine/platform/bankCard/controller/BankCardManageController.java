/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年10月31日</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.bankCard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.mvc.controller.PageParams;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.mongodb.vo.Condition;
import com.sunshine.framework.mvc.mongodb.vo.Restrictions;
import com.sunshine.framework.utils.DateUtils;
import com.sunshine.mobileapp.bankCard.entity.BankCard;
import com.sunshine.mobileapp.bankCard.service.BankCardService;
import com.sunshine.payment.CardConstant;
import com.sunshine.payment.TradeConstant;
import com.sunshine.platform.RuleConstant;
import com.sunshine.platform.application.entity.MerchantApplication;
import com.sunshine.platform.application.service.MerchantApplicationService;
import com.sunshine.platform.bankCard.entity.CardParamsVo;
import com.sunshine.platform.bankCard.service.BankCardManageService;
import com.sunshine.platform.channel.entity.TradeChannel;
import com.sunshine.platform.channel.service.TradeChannelService;
import com.sunshine.platform.merchant.entity.Merchant;
import com.sunshine.platform.merchant.service.MerchantService;
import com.sunshine.platform.rule.entity.SysRuleConfig;
import com.sunshine.platform.rule.service.SysRuleConfigService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.bankCard.controller
 * @ClassName: OrderController
 * @Description: 银行卡管理controller
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年10月31日上午9:45:41
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/bankCard")
public class BankCardManageController {
	private Logger logger = LoggerFactory.getLogger(BankCardManageController.class);
	@Autowired
	private BankCardManageService bankCardManageService;

	@Autowired
	protected MerchantService merchantService;

	@Autowired
	protected MerchantApplicationService merchantApplicationService;

	@Autowired
	protected TradeChannelService tradeChannelService;

	@Autowired
	protected BankCardService bankCardService;

	@Autowired
	protected SysRuleConfigService sysRuleConfigService;

	/**
	 * 跳转到银行卡列表页面
	 * @return
	 */
	@RequestMapping(value = "/bankCardList")
	public ModelAndView toBankCardList() {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/bankCardList");
		List<Merchant> allMerchant = merchantService.getAllMerchant();
		List<TradeChannel> allChannel = tradeChannelService.findAll();
		modelAndView.addObject("merchant", allMerchant);
		modelAndView.addObject("channel", allChannel);

		Condition codition = Restrictions.ne("activateStatus", CardConstant.ACTIVATE_STATUS_DEL);

		String groupJson = "{ $group : { _id : null, cardBalance : { $sum:\"$cardBalance\" } } }";
		List<BankCard> lists = bankCardService.aggregatesQuery(codition, groupJson, null, null, null, null, BankCard.class);
		if (lists.size() > 0) {
			modelAndView.addObject("allCardBalance", lists.get(0).getCardBalance().doubleValue() / 100);
		}

		String curDay = DateUtils.getDateStr(new Date());

		List<Integer> tradeStatus = new ArrayList<>();
		tradeStatus.add(TradeConstant.TRADE_STATUS_PAYMENT);
		tradeStatus.add(TradeConstant.TRADE_STATUS_REFUND);
		Integer totalPrice = bankCardService.queryTotalFeeByDate(curDay, curDay, null, tradeStatus);
		modelAndView.addObject("totalPrice", totalPrice.doubleValue() / 100);

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
	 *  分页查询银行卡
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年10月31日上午10:03:12
	 */
	@ResponseBody
	@RequestMapping("/findBankCardList")
	public PageInfo<CardParamsVo> findBankCardList(HttpServletRequest request,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize, PageParams pageParams) {
		logger.info("银行卡查询条件, pageParams:{}", JSON.toJSONString(pageParams));
		Page<BankCard> page = new Page<BankCard>();
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		PageInfo<CardParamsVo> pageInfo = bankCardManageService.queryByPage(pageParams.getParams(), page);
		return pageInfo;
	}

	/**
	 * 跳转银行卡信息详细页面
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月2日下午1:38:33
	 */
	@RequestMapping(value = "/bankCardDetail")
	public ModelAndView bankCardDetail(String id) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/bankCardDetail");
		BankCard bankCard = bankCardManageService.findById(id);
		CardParamsVo cardParamsVo = bankCardManageService.dataFormate(bankCard);
		cardParamsVo = bankCardManageService.queryTotalFee(cardParamsVo);
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, cardParamsVo);
		String backUrl = "/bankCard/bankCardList";
		modelAndView.addObject("backUrl", backUrl);
		return modelAndView;
	}

	/**
	 * 修改状态
	 * @param merchantNo
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeStatus")
	public RespBody changeStatus(String id, Integer status, String bankCardNo, String appId) {
		String resultMessage = null;

		BankCard bankCard = bankCardService.findById(id);
		bankCard.setActivateStatus(status);
		bankCard.setBankCardNo(bankCardNo);
		bankCard.setAppId(appId);
		bankCardService.updateCardForDbAndCache(bankCard);

		if (status == GlobalConstant.STATUS_VALID) {
			resultMessage = "【" + bankCardNo + "】已开通";
		} else {
			resultMessage = "【" + bankCardNo + "】已解绑";
		}

		return new RespBody(StatusEnum.OK, resultMessage);
	}

	/**
	 * 跳转银行卡信息修改页面
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月2日下午1:38:33
	 */
	@RequestMapping(value = "/updateBankCard")
	public ModelAndView updateBankCard(String id) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/updateBankCard");
		BankCard bankCard = bankCardManageService.findById(id);
		CardParamsVo cardParamsVo = bankCardManageService.dataFormate(bankCard);
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, cardParamsVo);
		String backUrl = "/bankCard/bankCardList";
		modelAndView.addObject("backUrl", backUrl);
		return modelAndView;
	}

	/**
	 * 修改金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月7日上午10:49:30
	 */
	@ResponseBody
	@RequestMapping("/updateCardBalance")
	public RespBody updateCardBalance(String id, String bankCardNo, String appId, String singleQuota, String dayQuota, String monthlyQuota,
			String bankCardName) {
		BankCard bankCard = bankCardService.findById(id);
		bankCard.setBankCardName(bankCardName);
		bankCard.setBankCardNo(bankCardNo);
		bankCard.setAppId(appId);
		Double sQ = Double.parseDouble(singleQuota) * 100; //单笔限额
		bankCard.setSingleQuota(sQ.intValue());
		Double mQ = Double.parseDouble(monthlyQuota) * 100;//单月限额
		bankCard.setMonthlyQuota(mQ.intValue());
		Double dQ = Double.parseDouble(dayQuota) * 100;//单天限额
		bankCard.setDayQuota(dQ.intValue());
		bankCardService.updateCardForDbAndCache(bankCard);
		return new RespBody(StatusEnum.OK, "添加金额成功！");
	}

	/**
	 * 余额阀值设置
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月28日上午11:10:55
	 */
	@RequestMapping(value = "/thresholdSetting")
	public ModelAndView thresholdSetting(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/thresholdSetting");
		SysRuleConfig sysRuleConfig = sysRuleConfigService.findByRuleCode(RuleConstant.BANKCARD_BALANCE_LIMIT_RULE);
		if (sysRuleConfig == null) {
			sysRuleConfig = new SysRuleConfig();
			sysRuleConfig.setRuleCode(RuleConstant.BANKCARD_BALANCE_LIMIT_RULE);
		}
		modelAndView.addObject("sysRuleConfig", sysRuleConfig);
		return modelAndView;
	}

	/**
	 * 当日消费金额上限设置
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月30日下午4:52:24
	 */
	@RequestMapping(value = "/amountLimitSetting")
	public ModelAndView amountLimitSetting(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/amountLimitSetting");
		SysRuleConfig sysRuleConfig = new SysRuleConfig();
		sysRuleConfig = sysRuleConfigService.findByRuleCode(RuleConstant.CONSUMPTION_AMOUNT_LIMIT_RULE);
		if (sysRuleConfig == null) {
			sysRuleConfig = new SysRuleConfig();
			sysRuleConfig.setRuleCode(RuleConstant.CONSUMPTION_AMOUNT_LIMIT_RULE);
		}
		modelAndView.addObject("sysRuleConfig", sysRuleConfig);
		return modelAndView;
	}

	/**
	 * 跳转到充值页面
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月1日上午11:01:29
	 */
	@RequestMapping(value = "/rechargeBalance")
	public ModelAndView rechargeBalance(HttpServletRequest request, String ids) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/rechargeBalance");
		String[] cardIds = ids.split(",");
		List<CardParamsVo> lists = new ArrayList<>();
		for (String cardId : cardIds) {
			BankCard bankCard = bankCardService.findById(cardId);
			CardParamsVo cardParamsVo = bankCardManageService.dataFormate(bankCard);
			cardParamsVo.setRecordDate(DateUtils.getDateStr(new Date()));
			lists.add(cardParamsVo);
		}
		modelAndView.addObject("bankCards", lists);
		return modelAndView;
	}

	/**
	 * 添加充值金额
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年11月7日上午10:49:30
	 */
	@ResponseBody
	@RequestMapping("/saveRechargeBalance")
	public RespBody saveRechargeBalance(String array) {
		List<BankCard> bankCards = JSONArray.parseArray(array, BankCard.class);
		for (BankCard bankC : bankCards) {
			BankCard bankCard = bankCardService.findById(bankC.getId());

			String activateStatus = "";
			int status = bankCard.getActivateStatus();
			switch (status) {
			case CardConstant.ACTIVATE_STATUS_NOBUNDLING: //待开通
				activateStatus = "待开通";
				break;
			case CardConstant.ACTIVATE_STATUS_BUNDLING: //使用中
				activateStatus = "使用中";
				break;
			case CardConstant.ACTIVATE_STATUS_UNBUNDLING: //停止使用
				activateStatus = "停止使用";
				break;
			case CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH: //余额不足
				activateStatus = "余额不足";
				break;
			case CardConstant.ACTIVATE_STATUS_PAYMENT_CONSTRAINT: //支付受限
				activateStatus = "支付受限";
				break;
			default:
				break;
			}
			Double balance = Double.parseDouble(bankC.getRechargeBalance());
			StringBuffer content = new StringBuffer("充值记录：");
			content.append("充值金额：").append(balance).append(", 充值前的余额：").append(bankCard.getCardBalance().doubleValue() / 100).append(", 充值前的状态：")
					.append(activateStatus).append(", 记账日期：").append(DateUtils.dateToString(new Date()));
			balance = balance * 100;
			bankCard.setCardBalance(balance.intValue() + bankCard.getCardBalance());
			bankCard.formatHandleLog(content.toString());
			if (status == CardConstant.ACTIVATE_STATUS_BALANCE_NOTENOUGH) {
				bankCard.setActivateStatus(CardConstant.ACTIVATE_STATUS_BUNDLING);
			}
			bankCardService.updateCardForDbAndCache(bankCard);
		}
		return new RespBody(StatusEnum.OK, "充值记录添加成功");
	}

	/**
	 * 跳转银行卡充值记录
	 * @author CHANG.CHI.HUNG
	 * @Email zhenzx@sun309.com
	 * @data 2017年12月4日上午11:06:22
	 */
	@RequestMapping(value = "/rechargeRecord")
	public ModelAndView rechargeRecord(String id) {
		ModelAndView modelAndView = new ModelAndView("/platform/bankCard/rechargeRecord");
		BankCard bankCard = bankCardManageService.findById(id);
		CardParamsVo cardParamsVo = bankCardManageService.dataFormate(bankCard);
		modelAndView.addObject(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, cardParamsVo);
		String backUrl = "/bankCard/bankCardList";
		modelAndView.addObject("backUrl", backUrl);
		return modelAndView;
	}

}
