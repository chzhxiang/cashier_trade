/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月13日</p>
 *  <p> Created by hqy</p>
 *  </body>
 * </html>
 */
package com.sunshine.platform.applicationchannel.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sunshine.common.GlobalConstant;
import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.common.PKGenerator;
import com.sunshine.framework.config.SystemConstants;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.platform.TradeChannelConstants;
import com.sunshine.platform.applicationchannel.entity.ApplicationChannel;
import com.sunshine.platform.applicationchannel.service.ApplicationChannelService;
import com.sunshine.platform.applicationchannel.vo.ApplicationChannelSortVo;
import com.sunshine.platform.security.entity.User;

/**
 * @Project: cashier_trade 
 * @Package: com.sunshine.platform.application_channel.controller
 * @ClassName: ApplicationChannelController
 * @Description: <p>支付渠道-参数配置controller</p>
 * @JDK version used: 
 * @Author: 百部
 * @Create Date: 2017年9月13日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/applicationChannel")
public class ApplicationChannelController extends BasePlatformController<ApplicationChannel, String> {
	protected Logger logger = LoggerFactory.getLogger(ApplicationChannelController.class);
	@Autowired
	protected ApplicationChannelService channelService;

	@Override
	protected BaseService<ApplicationChannel, String> getService() {
		return channelService;
	}

	@Override
	public RespBody save(ApplicationChannel applicationChannel, HttpServletRequest request) {
		User user = getPlatformUser(request);
		setInformation(user, applicationChannel);
		logger.info("保存应用支付渠道信息:{}", JSON.toJSONString(applicationChannel));
		if (StringUtils.isNotBlank(applicationChannel.getId())) {
			channelService.updateChannelParams(applicationChannel);
			boolean isExits = channelService.isExitsApplicationChannel(applicationChannel);
			if (isExits) { //存在该缓存，更新缓存
				channelService.setTradeChannelCache(applicationChannel);
			}
		} else {
			applicationChannel.setUrlStatus(GlobalConstant.PAY_LINK_PLATFORM); //默认跳转平台地址
			applicationChannel.setChannelViewName(channelService.getChannelViewName(applicationChannel.getChannelCode()));//设置默认显示名称
			applicationChannel.setIsOpen(GlobalConstant.NO); //默认未开启
			applicationChannel.setId(channelService.insert(applicationChannel));
		}
		return new RespBody(StatusEnum.OK, applicationChannel, "保存成功!");
	}

	@ResponseBody
	@RequestMapping("/updateAppChannel")
	public RespBody updateAppChannel(String configParams, String openParams, String appId, HttpServletRequest request) {
		User user = getPlatformUser(request);
		JSONArray configArray = JSON.parseArray(configParams);
		List<ApplicationChannelSortVo> configList = configArray.toJavaList(ApplicationChannelSortVo.class);
		JSONArray openArray = JSON.parseArray(openParams);
		List<ApplicationChannelSortVo> openList = openArray.toJavaList(ApplicationChannelSortVo.class);
		for (ApplicationChannelSortVo config : configList) { //状态改为已配置(未开启)
			ApplicationChannel applicationChannel = new ApplicationChannel();
			applicationChannel.setId(config.getId());
			applicationChannel.setIsOpen(GlobalConstant.NO);
			setInformation(user, applicationChannel);
			channelService.updateChannelOpen(applicationChannel);
			//若缓存中存在，删除该缓存
			channelService.deleteTradeChannelCache(applicationChannel);
		}
		List<String> idList = new ArrayList<String>();
		for (ApplicationChannelSortVo open : openList) { //状态改为已开启
			ApplicationChannel applicationChannel = new ApplicationChannel();
			applicationChannel.setId(open.getId());
			applicationChannel.setSeq(Integer.parseInt(open.getSort()) + 1);
			applicationChannel.setIsOpen(GlobalConstant.YES); //1 表示开启
			setInformation(user, applicationChannel);
			channelService.update(applicationChannel);
			//  channelService.setTradeChannelCache(applicationChannel);
			idList.add(applicationChannel.getId());
		}
		//更新缓存
		if (idList.size() > 0) {
			channelService.setTradeChannelCache(idList);
		}
		return new RespBody(StatusEnum.OK, "", "保存成功!");
	}

	@ResponseBody
	@RequestMapping("/findTradeChannelByAppId")
	public RespBody findTradeChannelByAppId(String appId, HttpServletRequest request) {
		List<ApplicationChannel> channelList = channelService.findTradeChannelByAppId(appId);
		return new RespBody(StatusEnum.OK, channelList, "");
	}

	@ResponseBody
	@RequestMapping("/updateChannelView")
	public RespBody updateChannelView(String channelViewName, String urlStatus, String id, HttpServletRequest request) {
		User user = getPlatformUser(request);
		ApplicationChannel applicationChannel = channelService.findById(id);
		applicationChannel.setChannelViewName(channelViewName);
		setInformation(user, applicationChannel);
		channelService.updateChannelViewName(id, channelViewName, urlStatus);
		boolean isExits = channelService.isExitsApplicationChannel(applicationChannel);
		if (isExits) { //存在该缓存，更新缓存
			channelService.setTradeChannelCache(applicationChannel);
		}
		return new RespBody(StatusEnum.OK, "", "保存成功!");
	}

	/**
	 * 文件上传
	 * 
	 * @param uploadFile
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upload")
	public RespBody upload(MultipartFile uploadFile, HttpServletRequest request, String fileType) {
		RespBody respBody = null;
		String fileName = uploadFile.getOriginalFilename();
		logger.info("获取到文件，文件名：{}", fileName);
		if (!StringUtils.isEmpty(fileName)) {
			logger.info("文件 {} 上传开始", fileName);
			// 重命名文件
			String renameFileName = PKGenerator.generateId();
			// 文件拓展名
			String extName = fileName.substring(fileName.lastIndexOf("."));
			// 文件大小
			// Long fileSize = Long.valueOf(uploadFile.getSize());
			try {
				//String directory = SystemConstants.FILE_SEPARATOR.concat(GlobalConstant.FILE_PATH);
				String directory = "";
				if (TradeChannelConstants.TRADE_WECHAT_IDENTITY.equalsIgnoreCase(fileType)) {
					directory = directory.concat(SystemConstants.FILE_SEPARATOR).concat(GlobalConstant.WECHAT_SECRET);
				} else if (TradeChannelConstants.TRADE_UNIONPAY_IDENTITY.equalsIgnoreCase(fileType)) {
					directory = directory.concat(SystemConstants.FILE_SEPARATOR).concat(GlobalConstant.UNIONPAY_SECRET);
				} else {
					directory = directory.concat(SystemConstants.FILE_SEPARATOR).concat(GlobalConstant.OTHER_SECRET);
				}
				String relativePath = directory.concat(SystemConstants.FILE_SEPARATOR).concat(renameFileName).concat(extName);
				// File root = new
				// File(LoadProjectPathServlet.ROOT_ABSOLUTEPATH);
				String filePath = GlobalConstant.FILE_PATH.concat(SystemConstants.FILE_SEPARATOR).concat(relativePath);

				logger.info("文件上传路径： {}", filePath);
				File fileDirectory = new File(filePath);
				if (!fileDirectory.exists()) {
					fileDirectory.mkdirs();
				}
				String upLoadPath = filePath;
				File localFile = new File(upLoadPath);
				uploadFile.transferTo(localFile);
				logger.info("文件 {} 上传结束", fileName);
				respBody = new RespBody(StatusEnum.OK, relativePath, "文件上传成功");
			} catch (Exception e) {
				e.printStackTrace();
				respBody = new RespBody(StatusEnum.ERROR, e.getMessage(), "文件上传失败");
			}
		}
		return respBody;
	}

}
