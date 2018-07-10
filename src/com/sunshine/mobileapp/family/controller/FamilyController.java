package com.sunshine.mobileapp.family.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sunshine.common.controller.BasePlatformController;
import com.sunshine.framework.mvc.controller.RespBody;
import com.sunshine.framework.mvc.controller.RespBody.StatusEnum;
import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.mobileapp.family.entity.Family;
import com.sunshine.mobileapp.family.service.FamilyService;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.family.controller
 * @ClassName: FamilyController
 * @Description: <p>	家人管理	</p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月7日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/mobileapp/family")
public class FamilyController extends BasePlatformController<Family, String> {

	@Autowired
	FamilyService familyService;

	@Override
	protected BaseService<Family, String> getService() {
		return familyService;
	}

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public RespBody saveOrUpdate(Family entity, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String birthday = request.getParameter("myBirthday");
		try {
			entity.setBirthday(sdf.parse(birthday));
			boolean flag = familyService.findByMobileAndName(entity);
			if (!flag) {
				return new RespBody(StatusEnum.ERROR, "该家人信息已经添加，不可重复添加!");
			}
			if (StringUtils.isNotBlank(entity.getId())) {
				entity.setEt(new Date());
				getService().update(entity);
			} else {
				entity.setEt(new Date());
				entity.setCt(new Date());
				getService().insert(entity);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new RespBody(StatusEnum.OK, entity, "保存成功!");
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView saveOrUpdate(HttpServletRequest request) {
		return new ModelAndView("mobileapp/family/indexFamily");
	}
}
