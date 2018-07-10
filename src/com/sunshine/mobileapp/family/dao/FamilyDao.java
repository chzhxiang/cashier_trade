package com.sunshine.mobileapp.family.dao;

import java.util.Map;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.mobileapp.family.entity.Family;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.family.dao
 * @ClassName: FamilyDao
 * @Description: <p>	家人管理dao	</p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月7日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public interface FamilyDao extends BaseDao<Family, String> {
	
	/**
	 * 通过姓名和号码判断是否已关联了家人
	 * @return
	 */
	public Family findByMobileAndName(Map map);
}