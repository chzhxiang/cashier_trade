package com.sunshine.mobileapp.family.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.mobileapp.family.dao.FamilyDao;
import com.sunshine.mobileapp.family.entity.Family;
import com.sunshine.mobileapp.family.service.FamilyService;

/**
 * 
 * @Project: Prescription_PlatForm 
 * @Package: com.sunshine.mobileapp.family.service
 * @ClassName: FamilyServiceImpl
 * @Description: <p>	家人管理	</p>
 * @JDK version used: 
 * @Author: 香薷
 * @Create Date: 2017年9月7日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
@Service
public class FamilyServiceImpl extends BaseServiceImpl<Family, String> implements FamilyService {

	@Autowired
	private FamilyDao FamilyDao;
	
	@Override
	protected BaseDao<Family, String> getDao() {
		return FamilyDao;
	}

	@Override
	public boolean findByMobileAndName(Family family) {
		Map map=new HashMap<>();
		map.put("idCard", family.getIdCard());
		Family entity=FamilyDao.findByMobileAndName(map);
		if(entity == null){
			return true;
		}else{
			if(entity.getId().equals(family.getId())){
				return true;
			}else{
				return false;
			}
		}
	}

}
