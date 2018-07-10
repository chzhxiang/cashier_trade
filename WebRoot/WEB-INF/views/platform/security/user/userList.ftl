<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
	<div id="content-header">
	    <div class="widget-title radius-3 w-pading">
	        <h3 class="title">用户列表</h3>
	    </div>
	</div>
	<!--content str-->
    <div class="container-fluid">
         <div class="row-fluid">
          <!--内容 标题 start-->
          	<div class="operation-button operation-button-order w-pading">
		       <@shiro.hasPermission name="sys:user:list:create"> 
		          <a href="javascript:toAdd();" class="operation-button-a">新增用户</a>        
		       </@shiro.hasPermission>
		       <@shiro.hasPermission name="sys:user:list:update"> 
		          <a href="javascript:toEdit();" class="operation-button-a">修改用户</a>        
		       </@shiro.hasPermission>
		       <@shiro.hasPermission name="sys:user:list:delete"> 
		          <a href="javascript:toDelete();" class="operation-button-a">删除用户</a>        
		       </@shiro.hasPermission>
				<div class="cur_set">
                    <div class="pull-right" id="search">
                         <input type="text" class="" id="searchValue" maxlength="20" placeholder="请输入用户账号或姓名">
                         <button id="search" class="tip-bottom" type="submit">
                              <i class="icon-search icon-white"></i>
                          </button>
                    </div>
                </div>
		    </div>
                <table class="table table-bordered table-textCenter table-striped table-hover">
                    <thead>
                    <tr>
                     	<th width="60"><input id="checkAll" onclick="selectAll()" type="checkbox"></th>
                        <th width="60" >序号</th>
                        <th>用户账号</th>
                        <th>所属组织类型</th>
                        <th>所属商户</th>
                        <th>所属应用</th>
                        <th>状态</th>
                        <th>创建日期</th>
                    </tr>
                    </thead>
                    <tbody class="list-data">
                    
                    </tbody>
                </table>
         </div>
         <div class="pagination pagination-right pagination-style">
         </div>
    </div>

<script type="text/javascript" src="${tradeBasePath}/platform/js/security/user/user.js?v=${js_version}"></script>