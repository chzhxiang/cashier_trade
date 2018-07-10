package com.sunshine.platform.security.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sunshine.common.GlobalConstant;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.mvc.mysql.dao.BaseDao;
import com.sunshine.framework.mvc.mysql.service.impl.BaseServiceImpl;
import com.sunshine.platform.security.SecurityContant;
import com.sunshine.platform.security.dao.ResourceDao;
import com.sunshine.platform.security.dao.RoleResourceDao;
import com.sunshine.platform.security.entity.Resource;
import com.sunshine.platform.security.service.ResourceService;

@Service(value = "resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource, String> implements ResourceService {
	private ResourceDao resourceDao = SpringContextHolder.getBean(ResourceDao.class);
	private RoleResourceDao roleResourceDao = SpringContextHolder.getBean(RoleResourceDao.class);

	@Override
	protected BaseDao<Resource, String> getDao() {
		// TODO Auto-generated method stub
		return resourceDao;
	}

	// 查找一级菜单
	@Override
	public List<Resource> findMenuList() {
		return resourceDao.findMenuList();
	}

	@Override
	public List<Resource> findAllMenuList() {
		return resourceDao.findAllMenuList();
	}

	@Override
	public List<Resource> findMenuListByParentId(String resourceId) {
		return resourceDao.findMenuListByParentId(resourceId);
	}

	@Override
	public List<Resource> findResourceListByUser(String userName) {
		return resourceDao.findResourcesByUserAccount(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#isUniqueName(com.sunshine.platform.security.entity.SysResource)
	 */
	@Override
	public boolean isUniqueName(Resource resource) {
		// TODO Auto-generated method stub
		String id = resource.getId();
		Resource entity = resourceDao.findResourceByName(resource);
		if (entity == null) {
			return true;
		} else {
			if (entity.getId().equals(id)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#isUniqueCode(com.sunshine.platform.security.entity.SysResource)
	 */
	@Override
	public boolean isUniqueCode(Resource resource) {
		// TODO Auto-generated method stub
		String id = resource.getId();
		Resource entity = resourceDao.findResourceByCode(resource);
		if (entity == null) {
			return true;
		} else {
			if (entity.getId().equals(id)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#findAllResource()
	 */
	@Override
	public List<Resource> findAllResource() {
		// TODO Auto-generated method stub
		return resourceDao.findAllResource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#findButtonListByParentId(java.lang.String)
	 */
	@Override
	public List<Resource> findResourceByParentId(String resourceId) {
		// TODO Auto-generated method stub
		return resourceDao.findResourceByParentId(resourceId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#findButtonListByParentCode(java.lang.String)
	 */
	@Override
	public List<Resource> findResourceByParentCode(String code,String type) {
		// TODO Auto-generated method stub
		return resourceDao.findResourceByParentCode(code,type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunshine.platform.security.service.ResourceService#findButtonListByMenuList(java.util.List)
	 */
	@Override
	public List<Resource> findButtonListByMenuList(List<String> rslist) {
		// TODO Auto-generated method stub
		return resourceDao.findButtonListByParentCode(rslist);

	}

	@Override
	public List<Resource> findDataAuthorityResources() {
		// TODO Auto-generated method stub
		return resourceDao.findDataAuthorityResources();
	}

	@Override
	public void update(Resource entity) {
		// TODO Auto-generated method stub
		super.update(entity);
	}

	@Override
	public String insert(Resource entity) {
		// TODO Auto-generated method stub
		String pkId = super.insert(entity);
		return pkId;
	}

	/**
	 * @Description
	 * @param resource
	 * @return 更新信息
	 * @date 2017年7月3日
	 */
	@Override
	public Map<String, Object> updateResource(Resource resource) {
		// TODO Auto-generated method stub
		Map<String, Object> resMap = new HashMap<>();
		List<Resource> newSubResourceList = resource.getSubResourceList();
		super.update(resource);

		// 所有需要更新Role-resource关系的资源
		Map<String, Resource> oldResourceMap = new HashMap<>();
		List<Resource> oldResourcelist = findResourceByParentId(resource.getId());
		if (!CollectionUtils.isEmpty(oldResourcelist)) {
			for (Resource oldResource : oldResourcelist) {
				oldResourceMap.put(oldResource.getId(), oldResource);
			}

			StringBuffer inValidCodes = new StringBuffer();
			List<Resource> allSubResourceList = new ArrayList<>();

			Map<String, Resource> needUpdateRoleResourceMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(newSubResourceList)) {
				List<Resource> needSaveList = new ArrayList<>();
				for (Resource subResource : newSubResourceList) {
					String subResourceId = subResource.getId();
					// 新增的子资源
					if (StringUtils.isBlank(subResourceId)) {
						boolean isUniqueCode = isUniqueCode(subResource);
						if (!isUniqueCode) {
							if (inValidCodes.length() > 0) {
								inValidCodes.append(",");
							}
							inValidCodes.append(subResource.getCode());
							continue;
						}
						subResource.setParentId(resource.getId());
						subResource.setParentName(resource.getName());
						subResource.setType(SecurityContant.RESOURCE_TYPE_BUTTON);
						subResource.setStatus(GlobalConstant.YES);
						subResource.setCt(new Date());
						needSaveList.add(subResource);
					} else {
						Resource oldResource = oldResourceMap.get(subResourceId);
						// 未改变的按钮
						subResource.setId(subResourceId);
						if (oldResource.equals(subResource)) {
							oldResourceMap.remove(subResourceId);
						} else {
							// 只需更新的按钮
							subResource.setEt(new Date());
							update(subResource);
							needUpdateRoleResourceMap.put(subResourceId, subResource);
							oldResourceMap.remove(subResourceId);
						}
						allSubResourceList.add(subResource);
					}
				}

				if (!CollectionUtils.isEmpty(needSaveList)) {
					batchInsert(needSaveList);
					allSubResourceList.addAll(needSaveList);
				}
			}

			// 剩余oldResourceMap中的元素 需要删除
			if (!CollectionUtils.isEmpty(oldResourceMap.keySet())) {
				List<String> oldResourceList = new ArrayList<>();
				oldResourceList.addAll(oldResourceMap.keySet());
				deleteByIds(oldResourceList);

				// 需删除的角色-资源关系
				RoleResourceDao roleResourceDao = SpringContextHolder.getBean(RoleResourceDao.class);
				roleResourceDao.deleteByResourceIds(oldResourceList);
			}

			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITIES, allSubResourceList);
		} else {
			resMap.putAll(saveSubResources(resource));
		}

		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITY, resource);

		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "操作成功");
		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
		return resMap;
	}

	@Override
	public void deleteWithSubByIds(List<String> resourceIds) {
		if (!CollectionUtils.isEmpty(resourceIds)) {
			// 避免传入的参数为Arrays生成的不可变List,造成不能自增报错
			List<String> delResourceIds = new ArrayList<>();
			delResourceIds.addAll(resourceIds);

			// 删除资源下的子资源
			StringBuffer sb = new StringBuffer();
			for (String resourceId : resourceIds) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(resourceId);
			}

			String subResourceIds = resourceDao.findAllSubIdByParentId(sb.toString());
			if (StringUtils.isNotBlank(subResourceIds)) {
				String[] subResourceIdArray = subResourceIds.split(",");
				List<String> subResourceList = Arrays.asList(subResourceIdArray);
				delResourceIds.addAll(subResourceList);
			}

			roleResourceDao.deleteByResourceIds(delResourceIds);
			resourceDao.deleteByIds(delResourceIds);
		}
	}

	/**
	 * @Description 保存资源下的按钮资源
	 * @param resource
	 * @return
	 * @date 2017年7月3日
	 */
	private Map<String, Object> saveSubResources(Resource resource) {
		Map<String, Object> resMap = new HashMap<>();
		StringBuffer msg = new StringBuffer();
		List<Resource> subResourceList = resource.getSubResourceList();
		if (!CollectionUtils.isEmpty(subResourceList)) {
			for (Resource subResource : subResourceList) {
				boolean isUniqueCode = isUniqueCode(subResource);
				if (!isUniqueCode) {
					// return new RespBody(Status.ERROR, "保存资源失败，资源编号" + resource.getResourceCode() + "已经存在！");
					if (msg.length() > 0) {
						msg.append(",");
					}
					if (msg.length() == 0) {
						msg.append("因资源编码不唯一,下列资源保存失败:");
					}
					msg.append(subResource.getCode());
					continue;
				}

				subResource.setParentId(resource.getId());
				subResource.setParentName(resource.getName());
				subResource.setType(SecurityContant.RESOURCE_TYPE_BUTTON);
				subResource.setStatus(GlobalConstant.YES);
				subResource.setCt(new Date());
			}

			if (!CollectionUtils.isEmpty(subResourceList)) {
				batchInsert(subResourceList);
				resMap.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITIES, subResourceList);
			}

			if (msg.length() == 0) {
				msg.append("操作成功！");
			}
			resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, msg.toString());
		}
		return resMap;
	}

	@Override
	public Map<String, Object> saveResource(Resource resource) {
		// TODO Auto-generated method stub
		Map<String, Object> resMap = new HashMap<>();

		if (SecurityContant.RESOURCE_TYPE_MENU.equalsIgnoreCase(resource.getType()) && StringUtils.isNotBlank(resource.getValue())) {
			resource.setOperationType(SecurityContant.RESOURCE_OPEN_ACTION);
		}
		String resourceId = insert(resource);
		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_ENTITY_ID, resourceId);

		resMap.putAll(saveSubResources(resource));
		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_IS_SUCCESS, true);
		resMap.put(GlobalConstant.MOTHED_INVOKE_RES_MSG, "操纵成功");
		return resMap;
	}

	@Override
	public List<Resource> queryResourceByRoleIds(List<String> roleIds) {
		// TODO Auto-generated method stub
		return resourceDao.queryResourceByRoleIds(roleIds);
	}

}
