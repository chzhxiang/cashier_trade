/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年11月29日上午9:46:21</p>
 *  <p> Created by 熊胆</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.rule.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.rule.entity.SysRuleConfig;
import com.sunshine.platform.rule.service.SysRuleConfigService;

/**
 * @Project: cashier_trade
 * @Package: com.sunshine.platform.rule
 * @ClassName: SysRuleConfigController
 * @Description: 规则配置控制器
 * @JDK version used: 
 * @Author: 熊胆
 * @Create Date: 2017年11月29日上午9:46:21
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/platform/sysRuleConfig")
public class SysRuleConfigController extends BasePlatformController<SysRuleConfig, String> {

	private Logger logger = LoggerFactory.getLogger(SysRuleConfigController.class);

	@Autowired
	protected SysRuleConfigService sysRuleConfigService;

	@Override
	protected BaseService<SysRuleConfig, String> getService() {
		return sysRuleConfigService;
	}

	/**
	 * 保存
	 * @param request
	 * @return
	 */
	@Override
	@ResponseBody
	@RequestMapping("/save")
	public RespBody save(SysRuleConfig sysRuleConfig, HttpServletRequest request) {
		logger.info("规则配置的数据:{}", JSON.toJSONString(sysRuleConfig));
		if (StringUtils.isNotBlank(sysRuleConfig.getId())) {
			sysRuleConfig.setEt(new Date());
			getService().update(sysRuleConfig);
		} else {
			sysRuleConfig.setCt(new Date());
			sysRuleConfig.setFlag(1);
			getService().insert(sysRuleConfig);
		}
		return new RespBody(StatusEnum.OK, "保存成功!");
	}

}
