package com.sunshine.mobileapp.family.service;

import java.util.Map;

import com.sunshine.framework.mvc.service.BaseService;
import com.sunshine.mobileapp.family.entity.Family;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.family.service
 * @ClassName: FamilyService
 * @Description: <p></p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月7日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface FamilyService extends BaseService<Family, String> {

	/**
	 * 通过姓名和号码判断是否已关联了家人
	 * @return
	 */
	public boolean findByMobileAndName(Family family);
}
