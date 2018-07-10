    <#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
    <#include "/platform/common/common.ftl">
    <link rel="stylesheet" href="${tradeBasePath}/platform/css/tree/metroStyle.css" type="text/css">
    <div id="content-header">
        <div class="widget-title radius-3">
        	<h3 class="title">新建账户</h3>
        </div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
                 <form id="formBean" class="form-horizontal">  
                    <input type="hidden" id="id" name="id" value="" />  
                    <input type="hidden" id="roleIds" name="roleIds" value="" /> 
                    <input type="hidden" id="ownedMerchantName" name="ownedMerchantName"/>
                    <input type="hidden" id="ownedApplicationName" name="ownedApplicationName"/>
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>账户名</label>
                         <div class="controls"><input type="text" class="span12" value="" id="account" name="account"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>账户密码</label>
                         <div class="controls"><input type="password" class="span12" id="password" name="password" placeholder="请输入密码" /></div>
                    </div>
					<div class="control-group">
                        <label class="control-label">账号所属类型</label>
                        <div class="controls">
                            <select id="type" name="type" class="mr10 span4" onchange="loadData(this.value)">
                                <option value="" >请选择</option>
                                <option value="1" >平台</option>
                                <option value="2" >商户</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group" >
                        <label class="control-label">所属商户</label>
                        <div class="controls">
                            <select id="ownedMerchant" name="ownedMerchant" class="mr10 span4" onchange="loadAppication(this.value)">
                            	<option value="" >请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group" >
                        <label class="control-label">所属应用</label>
                        <div class="controls">
                            <select id="ownedApplication" name="ownedApplication" class="mr10 span4">
                            	<option value="" >请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >姓名</label>
                         <div class="controls"><input type="text" class="span12" name="name" /></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >电话号码</label>
                         <div class="controls"><input type="text" class="span12" id="mobile" name="mobile" /></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label">选择角色</label>
                         <div class="controls">
                         	<div class="role-select span12">
	                         	<#list roleList as role>
	                         	<#if role.roleCode == 'clinicAdmin'>
	                            	<label class="inline-block mr0">
	                            		<input  type="checkbox" name="checkbox" value="${role.id}" onclick="showClinics(${role.code})"/>${role.roleName}
	                            	</label>
	                            	<#if role.roleCode == 'clinicAdmin'>
	                            		<select  name="clinic">
                                             <option value="440105">请选择门诊</option>
                                        </select>
	                            	</#if>
	                            <#else>
	                            	<label class="inline-block mw120">
	                            		<input  type="checkbox" name="checkbox" value="${role.id}" onclick="showClinics(${role.code})"/>${role.roleName}
	                            	</label>
	                            </#if>
	                            </#list>
                            </div>
                         </div>
                    </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
         <div class="row-fluid mt10">
         	<div class="button-style bgfff radius-3">
         		<@shiro.hasPermission name="sys:user:edit:save"> 
          			<button class="btn btn-save" onClick="saveOrUpdate()">保存</button>    
       			</@shiro.hasPermission>
       			<@shiro.hasPermission name="sys:user:edit:cancel"> 
          			<button class="btn btn-remove" onClick="openUrl('/platform/user/list')">取消</button>
       			</@shiro.hasPermission>
       		</div>
         </div>
    </div>
<script>
$(document).ready(function() {
});

	/**
	*加载商户
	*/
	function loadData(selectValue){
	    $("#ownedOrgId").attr("disabled" , false);
	    if(selectValue != ''){
	        if(selectValue == '1'){
	            $("#ownedMerchant").empty();
	            $("#ownedMerchant").attr("disabled" , true);
	            
	            $("#ownedApplication").empty();
	       		$("#ownedApplication").attr("disabled" , true);
	        }else{
	       		$("#ownedMerchant").attr("disabled" , false);
	            $("#ownedApplication").attr("disabled" , false);
	            var reqUrl = "${tradeBasePath}/merchant/loadAllMerchat";
	            $.ajax({
	               type: "POST",
	               url: reqUrl,
	               error: function(request) {
	                    alert("加载数据失败");
	               },
	               success : function(data) { 
	                  if(data.status == "OK"){
	                      var entityList = data.result.entities;
	                      $("#ownedMerchant").empty();
	                      $("<option/>").val("0")
							            .text("请选择")
							            .appendTo("#ownedMerchant");
	                      $.each(entityList, function(index, item){
	                      	 $("<option/>").val(item.merchantNo)
							               .text(item.merchantName)
							               .appendTo("#ownedMerchant");
	                      });
	                  }
	               }
	            });
	        }
	    }else{
	        $("#ownedMerchant").empty();
	        $("#ownedMerchant").attr("disabled" , true);
	        
	        $("#ownedApplication").empty();
	        $("#ownedApplication").attr("disabled" , true);
	    }
	}

	/**
	*加载应用
	*/
	function loadAppication(merchantNo){
		var reqUrl = "${tradeBasePath}/merchantApplication/getMerchantApplications";
        $.ajax({
           type : "POST",
            url : reqUrl,
    	   data : {merchantNo:merchantNo},
           error: function(request) {
                alert("加载数据失败");
           },
           success : function(data) { 
              if(data.status == "OK"){
                  var entityList = data.result;
                  $("#ownedApplication").empty();
                  $("<option/>").val("0")
					            .text("请选择")
					            .appendTo("#ownedMerchant");
			       $.each(entityList, function(index, item){
	                  	 $("<option/>").val(item.appId)
						               .text(item.appName)
						               .appendTo("#ownedApplication");
                  });
              }
           }
        });
	}


function saveOrUpdate(){  
   var unflag = regBox.regName.test($("#account").val());
   var pwflag = regBox.regName.test($("#password").val());
   if(!unflag){
        $Y.tips("用户账号输入有误");
        return;
   }
   if(!pwflag){
        $Y.tips("用户密码输入有误");
        return;
   }
   var mflag = regBox.regMobile.test($("#mobile").val());
   var tflag = regBox.regTel.test($("#mobile").val());
   if ($("#mobile").val()!=''&&!mflag&&!tflag) {
        $Y.tips("电话号码输入有误");
        return;
   }
   
   var type = $("#type").val();	
   if(type ==  ""){
		alert("请选择对应的账号所属类型!");
        return;
        
   }
   if(type == '2'){
   		var ownedMerchant = $("#ownedMerchant").val();
   		var ownedApplication = $("#ownedApplication").val();
   		if(ownedMerchant == "" || ownedMerchant == "0"){
	   		alert("请选择对应的商户!");
	        return;
   		}
   		if(ownedApplication == "" || ownedApplication == "0"){
	   		alert("请选择对应的应用!");
	        return;
   		}
   }
   
   var roleIdArray = [];
   $('input[name="checkbox"]:checked').each(function(){ 
      roleIdArray.push($(this).val())
   }); 
   $("#roleIds").val(JSON.stringify(roleIdArray));
   $("#ownedMerchantName").val($("#ownedMerchant option:selected").text());
   $("#ownedApplicationName").val($("#ownedApplication option:selected").text());
	   
   $.ajax({
       type: "POST",
       url:"${tradeBasePath}/platform/user/saveOrUpdate",
       data:$('#formBean').serialize(),
       error: function(request) {
            $Y.tips("操作失败");
       },
       success : function(data) { 
          if(data.status == "OK"){
            $Y.tips("操作成功");
            var returnurl = "platform/user/list";
            openUrl(returnurl);
          }else{
            $Y.tips(data.message);
          }
       }
   });
}
</script>
