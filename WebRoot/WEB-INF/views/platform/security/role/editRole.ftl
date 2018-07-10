<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<#include "/platform/common/common.ftl">
		<link rel="stylesheet" href="${tradeBasePath}/platform/css/tree/metroStyle.css" type="text/css">
	<script type="text/javascript" src="${tradeBasePath}/platform/js/tree/jquery.ztree.core.min.js"></script>
	<script type="text/javascript" src="${tradeBasePath}/platform/js/tree/jquery.ztree.excheck.min.js"></script>
    <div id="content-header">
        <div class="widget-title radius-3"><h3 class="title">新增/修改角色</h3></div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
                <form id="formBean" class="form-horizontal">  
                    <input type="hidden" id="id" name="id" value="${role.id}" /> 
                    <input type="hidden" id="status" name="status" value="${role.status}" /> 
                    <input type="hidden" id="resourceIds" name="resourceIds" value="" /> 
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>角色编号</label>
                         <div class="controls"><input type="text" id="roleCode" name="roleCode" value="${role.roleCode}" class="span6"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>角色名称</label>
                         <div class="controls"><input type="text" id="roleName" name="roleName" value="${role.roleName}" class="span6"/></div>
                    </div>
                    <div class="space10"></div>
                    <div class="control-group">
                    	<label class="control-label" >角色授权</label>
                    	<div class="controls span6" style="border:1px solid;float:none;">
                    		<div id="resourceTree" class="ztree" ></div>
                    	</div>
                    </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
         <div class="row-fluid mt10">
         	<div class="button-style bgfff radius-3">
         		<@shiro.hasPermission name="sys:role:edit:save"> 
          			<button class="btn btn-save" onClick="saveOrUpdate()">保存</button>    
       			</@shiro.hasPermission>
       			<@shiro.hasPermission name="sys:role:edit:cancel"> 
          			<button class="btn btn-remove" onClick="openUrl('/platform/role/list')">取消</button>
       			</@shiro.hasPermission>
       		</div>
         </div>
    </div>
    <script>    
        var roleId = '${role.id}';
		var setting = {
		    async: {
				enable: true,
				url: tradeBasePath + "/platform/common/loadResources",
				autoParam:["id=pId", "name", "level"],
				otherParam:{"roleId":roleId,"loadType":"1"},
				dataFilter: null
			},
			check: {
				enable: true,
				chkboxType:{ "Y" : "ps", "N" : "ps" },
				chkStyle:"checkbox"
			},
			data: {
				simpleData: {
					enable: true
				}
			}
		};
	    $(document).ready(function(){
			$.fn.zTree.init($("#resourceTree"), setting);
		});
		
		function getALlCheckedReourceId(){
			var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
			var nodes = treeObj.getCheckedNodes(true);
			var resourceIdArray = [];
			$(nodes).each(function() {
				resourceIdArray.push(this.id);
			});
			return resourceIdArray;
		}

		function saveOrUpdate() {
			var rcflag = regBox.regName.test($("#roleCode").val());
			if (!rcflag) {
				$Y.tips("角色编号输入有误");
				return;
			}
			if (isNull($("#roleName").val())) {
				$Y.tips("角色名称不能为空");
				return;
			}
			var resourceIdArray = getALlCheckedReourceId();
			$("#resourceIds").val(JSON.stringify(resourceIdArray));
			$.ajax({
				type : "POST",
				url : "${tradeBasePath}/platform/role/saveOrUpdate",
				data : $('#formBean').serialize(),
				error : function(request) {
					$Y.tips("操作失败");
				},
				success : function(data) {
					if (data.result.isSuccess) {
						$Y.tips("操作成功");
						var returnurl = "/platform/role/list";
						openUrl(returnurl);
					}else{
						$Y.tips(data.result.msg);
					}
				}
			});
		}
	</script>
    
    
