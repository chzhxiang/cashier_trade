<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<#include "/platform/common/common.ftl">
    <link rel="stylesheet" href="${tradeBasePath}/platform/css/tree/metroStyle.css" type="text/css">
    <script type="text/javascript" src="${tradeBasePath}/platform/js/tree/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${tradeBasePath}/platform/js/tree/jquery.ztree.excheck.min.js"></script>
    <div id="content-header">
        <div class="widget-title radius-3"><h3 class="title">新增/修改资源</h3></div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
                 <form id="formBean" class="form-horizontal"> 
                     <input type="hidden" id="id" name="id" value="${resource.id}" /> 
                     <input type="hidden" id="status" name="status" value="${resource.status}" /> 
                     <input type="hidden" id="subResourcesJson" name="subResourcesJson" value="" /> 
                     <input type="hidden" id="parentName" name="parentName" value="${resource.parentName}" />  
                     <input type="hidden" id="hadSub" name="hadSub" value="${resource.hadSub}" />
                     <input type="hidden" id="parentId" name="parentId" value="${resource.parentId}" />
                     <input type="hidden" id="subCount" name="subCount" value="${subCount}" /> 
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>资源名称</label>
                         <div class="controls"><input type="text" id="name" value="${resource.name}" name="name" class="span12"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>资源编码</label>
                         <div class="controls"><input type="text" id="code" value="${resource.code}" name="code" class="span12"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >数据实体类</label>
                         <div class="controls"><input type="text" id="className" name="className"  value="${resource.className}" class="span12"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >数据权限字段</label>
                         <div class="controls"><input type="text" id="dataAuthorityCode" name="dataAuthorityCode" value="${resource.dataAuthorityCode}" class="span12"/></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">上级资源</label>
                        <div class="controls span6" style="border:1px solid;float:none;">
                            <div id="resourceTree" class="ztree" ></div>
                        </div>
                    </div>
	                <div class="control-group">
	                    <label class="control-label" >资源URL</label>
	                    <div class="controls"><input type="text" value="${resource.value}" name="value" class="span12"/></div>
	                </div>
	                <div class="control-group">
	                    <label class="control-label" >资源排序号</label>
	                    <div class="controls"><input type="number" value="${resource.sort}" name="sort" class="form-control"/></div>
	                </div>
	                <div class="control-group">
	                	<label class="control-label" >按钮配置</label>
	                    <div class="controls">
	                    	<div class="button-parameter">
	                        <#list subResources as button>  
	                        	<div class="parameter-li"><i class="par-i"></i><input type="hidden" id="buttonId_${button_index}" value="${button.id}"><input type="text" id="buttonCode_${button_index}" style="width:15%;" class="span2" value="${button.code}"/><span>-</span><input type="text" id="buttonName_${button_index}" class="span2" style="width:15%;" value="${button.name}"/><span>-</span><input type="number" step="1" id="buttonSort_${button_index}"  class="span2" style="width:15%;" value="${button.sort}"/><span>-</span><select id="buttonOperation_${button_index}" class="mr10 span2" style="width:15%;"><option value="">选择操作类型</option><option value="1" <#if button.operationType == 1>selected="selected"</#if>>URL重定向</option><option value="2" <#if button.operationType == 2>selected="selected"</#if>>JS调转</option></select><span>-</span><input type="text" id="buttonUrl_${button_index}" class="span3" style="width:20%;" value="${button.value}"/><a href="#" class="parameter-delete" onclick="delButton(this);">删掉</a></div>
	                        </#list>
	                        </div>
	                        <a class="add-cat add-cat-con" href="#"><span onclick="addButton();"><em>添加按钮</em></span></a>
	                    </div>
	                </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
         <div class="row-fluid mt10">
         	<div class="button-style bgfff radius-3">
         		<@shiro.hasPermission name="sys:resource:edit:save"> 
          			<button class="btn btn-save" onClick="saveOrUpdate()">保存</button>    
       			</@shiro.hasPermission>
       			<@shiro.hasPermission name="sys:resource:edit:cancel"> 
          			<button class="btn btn-remove" onClick="openUrl('/platform/resource/list')">取消</button>
       			</@shiro.hasPermission>
       		</div>
         </div>
    </div>
	<script type="text/javascript">
	var i = $("#subCount").val();
	var g = $("#subCount").val();
	
	var resourceId = '${resource.id}';
	var setting = {
            async: {
                enable: true,
                url: tradeBasePath + "/platform/common/loadMenuResources",
                otherParam:{"resourceId":resourceId},
                dataFilter: null
            },
            check: {
                enable: true,
                chkStyle:"radio",
                radioType: "all"
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
	//添加角色
	function addButton() {
		var html = '';
		html += '<div class="parameter-li">';
		html += '<i class="par-i"></i>';
		html += '<input type="text" class="span2" style="width:15%;" id="buttonCode_'
				+ i
				+ '" placeholder="按钮编号"/><span>-</span><input type="text" class="span2" style="width:15%;" id="buttonName_'
				+ i
				+ '" placeholder="按钮名称"/><span>-</span><input type="number" step="1" id="buttonSort_'
				+ i
				+ '" class="span2" style="width:15%;" placeholder="按钮排序"/><span>-</span><select id="buttonOperation_'
				+ i
				+ '" class="mr10 span2" style="width:15%;"><option value="">选择操作类型</option><option value="1">URL重定向</option><option value="2">JS调转</option></select><span>-</span><input type="text" id="buttonUrl_'
				+ i + '" class="span3" style="width:20%;" placeholder="按钮URL"/>';
		html += '<a href="#" class="parameter-delete" onclick="delButton(this);" style="right:3px;">删掉</a>';
		html += '</div>';
		$('.button-parameter').append(html);
		i++;
		g++;
	}
	//删除角色
	function delButton(obj) {
		$(obj).parents('.parameter-li').remove();
		g--;
	}
	
	function saveOrUpdate() {
		var rcflag = regBox.regCode.test($("#code").val());
		if (!rcflag) {
			$Y.tips("菜单编号输入有误");
			return;
		}
		if (isNull($("#name").val())) {
			$Y.tips("菜单名称不能为空");
			return;
		}
		var subResources = [];
		for (var j = 0; j < i; j++) {
			var resource = new Object();
			var buttonId = $('#buttonId_' + j).val();
			if (buttonId == '' || typeof (buttonId) == "undefined") {
				buttonId = "";
			}
			resource.id = buttonId;
			var buttonCode = $('#buttonCode_' + j).val();
			if (buttonCode == '' || typeof (buttonCode) == "undefined") {
				continue;
			}
			resource.code = buttonCode;
			resource.name = $('#buttonName_' + j).val();
			resource.value = $('#buttonUrl_' + j).val();
			resource.sort = $('#buttonSort_' + j).val();
			resource.operationType = $('#buttonOperation_' + j).val();
			subResources.push(resource);
		}
		if(subResources.length > 0){
			$("hadSub").val(1);
		}
		$("#subResourcesJson").val(JSON.stringify(subResources));
		var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
        var nodes = treeObj.getCheckedNodes(true);
         if(nodes.length>0){
         	$("#parentId").val(nodes[0].id);
			$("#parentName").val(nodes[0].name);
         } else{
         	$("#parentId").val("");
			$("#parentName").val("");
         }
		$.ajax({
			type : "POST",
			url : "${tradeBasePath}/platform/resource/saveOrUpdate",
			data : $('#formBean').serialize(),
			error : function(request) {
				$Y.tips("操作失败");
			},
			success : function(data) {
				if (data.result.isSuccess) {
					$Y.tips('操作成功');
					var returnurl = "/platform/resource/list";
					openUrl(returnurl);
				}else{
					$Y.tips(data.result.msg);
				}
			}
		});
	
	}
	</script>