<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
	<div id="content-header">
	    <div class="widget-title radius-3 w-pading">
	        <h3 class="title">角色列表</h3>
	    </div>
	</div>
<!--content str-->
    <div class="container-fluid">
         <div class="row-fluid">
          <!--内容 标题 start-->
          	<div class="operation-button operation-button-order w-pading">
		       <@shiro.hasPermission name="sys:role:list:create"> 
		          <a href="javascript:toAdd();" class="operation-button-a">新增角色</a>        
		       </@shiro.hasPermission>
		       <@shiro.hasPermission name="sys:role:list:update"> 
		          <a href="javascript:toEdit();" class="operation-button-a">修改角色</a>        
		       </@shiro.hasPermission>
		       <@shiro.hasPermission name="sys:role:list:delete"> 
		          <a href="javascript:toDelete();" class="operation-button-a">删除角色</a>        
		       </@shiro.hasPermission>
				<div class="cur_set">
                    <div class="pull-right" id="search">
                         <input type="text" class="" id="searchValue" maxlength="20" placeholder="请输入角色编号或名称">
                         <button id="search" class="tip-bottom" type="submit">
                              <i class="icon-search icon-white"></i>
                          </button>
                    </div>
                </div>
		    </div>
            <!--内容 标题 end-->
                <table class="table table-bordered table-textCenter table-striped table-hover">
                    <thead>
                    <tr>
                     <th width="60" ><input id="checkAll" onclick="selectAll()" type="checkbox"></th>
                        <th width="60" >序号</th>
                        <th>角色编号</th>
                        <th>角色名称</th>
                        <th>创建日期</th>
               
                      <!-- 
                        <#if operalist??>
                        <th>操作</th>
                        </#if>
                     -->
                    </tr>
                    </thead>
                    <tbody class="list-data">
                    <!--
                    	<#if item?exists>
	        				<#list item.list as role>
		                        <tr>
		                           <td><input type="checkbox" name="check" value="${role.id}"></td>
		                            <td>${role_index + 1}</td>
		                            <td>${role.roleCode}</td>
		                            <td>${role.roleName}</td>
		                            <td>${role.ct?string("yyyy-MM-dd HH:mm")}</td>
		                            <td>
		                            	<#if role.status == 0>
		                            		停用
		                            	<#elseif role.status == 2>
		                            		删除
		                                <#else>
		                                  	启用
		                            	</#if>
		                            </td>
		                            <#if operalist??>
		                            <td>
		                             <#list operalist as resource>
                                     <@shiro.hasPermission name="${resource.resourceCode}">                                      
                                      <a href="javascript:openUrl('${resource.resourceUrl}?id=${role.id}&menuId=${resource.resourceParent}');">${resource.resourceName}</a>                                                            
                                     </@shiro.hasPermission>  
                                     </#list> 
		                            </td>
		                            </#if>
		                        </tr>
		                	</#list>
		                <#else>
		                	<tr><td colspan="6">暂无角色数据</td><tr>
                        </#if>
                  -->
                    </tbody>
                  </table>
         </div>
         <div class="pagination pagination-right pagination-style">
         </div>
    </div>

<script type="text/javascript" src="${tradeBasePath}/platform/js/security/role/role.js?v=${js_version}"></script>