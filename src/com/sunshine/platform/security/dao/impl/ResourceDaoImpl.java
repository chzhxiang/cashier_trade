package com.sunshine.platform.security.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.mvc.mysql.dao.impl.BaseDaoImpl;
import com.sunshine.platform.security.dao.ResourceDao;
import com.sunshine.platform.security.entity.Resource;

@Repository
public class ResourceDaoImpl extends BaseDaoImpl<Resource, String> implements ResourceDao {

	private static Logger logger = LoggerFactory.getLogger(ResourceDaoImpl.class);

	private final static String SQLNAME_FIND_MENU = "findMenuList"; // 查找一级菜单

	private final static String SQLNAME_FIND_ALL_MENU = "findAllMenuList"; // ]

	private final static String SQLNAME_FIND_MENU_BY_PARENT = "findMenuListByParentId"; // 根据父菜单查找子菜单

	private final static String SQLNAME_FIND_RESOURCE_BY_PARENT = "findResourceByParentId"; // 根据父菜单查找按钮

	private final static String SQLNAME_FIND_RESOURCE_BY_USER = "findResourceListByUser"; // 根据用户获取用户拥有的资源权限

	private final static String SQLNAME_FIND_RESOURCE_BY_NAME = "findResourceByName";

	private final static String SQLNAME_FIND_RESOURCE_BY_CODE = "findResourceByCode";

	private final static String SQLNAME_FIND_ALL_RESOURCE = "findAllResource";

	private final static String SQLNAME_FIND_RESOURCE_BY_PARENTCODE = "findResourceByParentCode";

	private final static String SQLNAME_FIND_BUTTON_BY_MENULIST = "findButtonListByMenuList";

	private final static String SQLNAME_FIND_DATA_AUTHORITY_RESOURCES = "findDataAuthorityResources";

	private final static String SQLNAME_DEL_RESOURCE_BY_PARENTID = "deleteResourceByParentId";

	private final static String SQLNAME_FIND_ALL_SUB_RESOURCE_BY_PARENTID = "findAllSubResourceByParentId";

	private final static String SQLNAME_FIND_RESOURCE_BY_ROLEIDS = "queryResourceByRoleIds";

	private final static String SQLNAME_FIND_ALL_SUB_ID_BY_PARENTID = "findAllSubIdByParentId";

	private final static String SQLNAME_FIND_ALL_ROOT_REOURCE = "findAllRootReource";

	// 查找一级菜单
	@Override
	public List<Resource> findMenuList() {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_MENU));
		} catch (Exception e) {
			logger.error(String.format("查询一级菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_MENU)), e);
			throw new SystemException(String.format("查询一级菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_MENU)), e);
		}
	}

	@Override
	public List<Resource> findAllMenuList() {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_MENU));
		} catch (Exception e) {
			logger.error(String.format("查询菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_MENU)), e);
			throw new SystemException(String.format("查询菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_MENU)), e);
		}
	}

	@Override
	public List<Resource> findMenuListByParentId(String resourceId) {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_MENU_BY_PARENT), resourceId);
		} catch (Exception e) {
			logger.error(String.format("根据父菜单查询子菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_MENU_BY_PARENT)), e);
			throw new SystemException(String.format("根据父菜单查询子菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_MENU_BY_PARENT)), e);
		}
	}

	@Override
	public List<Resource> findResourceByParentCode(String code,String type) {
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("code", code);
			map.put("type", type);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENTCODE), map);
		} catch (Exception e) {
			logger.error(String.format("根据父菜单编码查询子菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENTCODE)), e);
			throw new SystemException(String.format("根据父菜单编码查询子菜单列表出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENTCODE)), e);
		}
	}

	@Override
	public List<Resource> findResourcesByUserAccount(String userAccount) {
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_BY_USER), userAccount);
		} catch (Exception e) {
			logger.error(String.format("根据用户获取用户拥有的资源权限出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_USER)), e);
			throw new SystemException(String.format("根据用户获取用户拥有的资源权限出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_USER)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.ResourceDao#findResourceByName(java.lang.String)
	 */
	@Override
	public Resource findResourceByName(Resource resource) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(resource.getName());
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_RESOURCE_BY_NAME), resource);
		} catch (Exception e) {
			logger.error(String.format("根据name查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_NAME)), e);
			throw new SystemException(String.format("根据name查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_NAME)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.ResourceDao#findResourceByCode(java.lang.String)
	 */
	@Override
	public Resource findResourceByCode(Resource resource) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(resource.getCode());
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_RESOURCE_BY_CODE), resource);
		} catch (Exception e) {
			logger.error(String.format("根据code查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_CODE)), e);
			throw new SystemException(String.format("根据code查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_CODE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.ResourceDao#findAllResource()
	 */
	@Override
	public List<Resource> findAllResource() {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_RESOURCE));
		} catch (Exception e) {
			logger.error(String.format("查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_RESOURCE)), e);
			throw new SystemException(String.format("查询资源出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_RESOURCE)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.ResourceDao#findButtonListByParentId(java.lang.String)
	 */
	@Override
	public List<Resource> findResourceByParentId(String resourceId) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(resourceId);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENT), resourceId);
		} catch (Exception e) {
			logger.error(String.format("根据父菜单查询按钮列表出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENT)), e);
			throw new SystemException(String.format("根据父菜单查询按钮列表出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_PARENT)), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.dao.ResourceDao#findButtonListByParentCode(java.util.List)
	 */
	@Override
	public List<Resource> findButtonListByParentCode(List<String> rslist) {
		// TODO Auto-generated method stub
		try {
			Assert.notNull(rslist);
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_BUTTON_BY_MENULIST), rslist);
		} catch (Exception e) {
			logger.error(String.format("根据父菜单查询按钮列表出错！语句：%s", getSqlName(SQLNAME_FIND_BUTTON_BY_MENULIST)), e);
			throw new SystemException(String.format("根据父菜单查询按钮列表出错！语句：%s", getSqlName(SQLNAME_FIND_BUTTON_BY_MENULIST)), e);
		}
	}

	@Override
	public List<Resource> findDataAuthorityResources() {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_DATA_AUTHORITY_RESOURCES));
		} catch (Exception e) {
			logger.error(String.format("查询所有需要进行数据权限过滤的资源出错！语句：%s", getSqlName(SQLNAME_FIND_DATA_AUTHORITY_RESOURCES)), e);
			throw new SystemException(String.format("查询所有需要进行数据权限过滤的资源出错！语句：%s", getSqlName(SQLNAME_FIND_DATA_AUTHORITY_RESOURCES)), e);
		}
	}

	@Override
	public void deleteResourceByParentId(String parentId, String resourceType) {
		// TODO Auto-generated method stub
		Assert.isNull(parentId);
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("parentId", parentId);
		paraMap.put("resourceType", resourceType);
		try {
			sqlSession.delete(getSqlName(SQLNAME_DEL_RESOURCE_BY_PARENTID), paraMap);
		} catch (Exception e) {
			logger.error(String.format("删除资源下子资源出错！语句：%s", getSqlName(SQLNAME_DEL_RESOURCE_BY_PARENTID)), e);
			throw new SystemException(String.format("删除资源下子资源出错！语句：%s", getSqlName(SQLNAME_DEL_RESOURCE_BY_PARENTID)), e);
		}
	}

	@Override
	public List<Resource> findAllSubResourceByParentId(List<String> resourceParentIds) {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_SUB_RESOURCE_BY_PARENTID), resourceParentIds);
		} catch (Exception e) {
			logger.error(String.format("查询父资源的所有下级资源！语句：%s", getSqlName(SQLNAME_FIND_ALL_SUB_RESOURCE_BY_PARENTID)), e);
			throw new SystemException(String.format("查询父资源的所有下级资源！语句：%s", getSqlName(SQLNAME_FIND_ALL_SUB_RESOURCE_BY_PARENTID)), e);
		}
	}

	@Override
	public List<Resource> queryResourceByRoleIds(List<String> roleIds) {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_RESOURCE_BY_ROLEIDS), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色ID查询角色对应的资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_ROLEIDS)), e);
			throw new SystemException(String.format("根据角色ID查询角色对应的资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_RESOURCE_BY_ROLEIDS)), e);
		}
	}

	@Override
	public String findAllSubIdByParentId(String roleIds) {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectOne(getSqlName(SQLNAME_FIND_ALL_SUB_ID_BY_PARENTID), roleIds);
		} catch (Exception e) {
			logger.error(String.format("根据角色ID查询角色对应的资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_SUB_ID_BY_PARENTID)), e);
			throw new SystemException(String.format("根据角色ID查询角色对应的资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_SUB_ID_BY_PARENTID)), e);
		}
	}

	@Override
	public List<Resource> findAllRootReource() {
		// TODO Auto-generated method stub
		try {
			return sqlSession.selectList(getSqlName(SQLNAME_FIND_ALL_ROOT_REOURCE));
		} catch (Exception e) {
			logger.error(String.format("查找所有根资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROOT_REOURCE)), e);
			throw new SystemException(String.format("查找所有根资源信息出错！语句：%s", getSqlName(SQLNAME_FIND_ALL_ROOT_REOURCE)), e);
		}
	}
}