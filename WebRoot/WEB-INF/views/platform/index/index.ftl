<#include "/platform/common/common.ftl">
<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<html>
	<head>
	    <title>收银台</title>
    </head>

    <body>
	    <!--Header str-->
	    <div id="header">
	        <h1>
	            <a href="javascript:void(0);"><img src="${tradeBasePath}/platform/images/logo.png"></a>
	        </h1>
	        <div id="content-top">
	            <div class="container-fluid">
	                <div class="box">
	                    <div id="avatar">
	                        <div class="set-msg dropdown">
	                            <a class="dropdown-toggle" href="javascript:void(0);" data-toggle="dropdown">
	                                <img src="${tradeBasePath}/platform/images/avatar.jpg">
	                                <span class="text">${entity.fullname}</span>
	                                <i class="icons_settings icons-down"></i>
	                            </a>
	                            <ul class="dropdown-menu pmbox">
	                                <li>
	                                	<a href="#">
	                                		<span class="text">系统设置</span>
	                                		<i class="icons_settings icons-set"></i>
	                                	</a>
	                                </li>
	                                <li>
	                                	<a href='javascript:void(0);' onclick=configEdit('${user.account}')>
	                                		<span class="text">修改密码</span>
	                                		<i class="icons_settings icons-password"></i>
	                                	</a>
	                                </li>
	                                <li>
	                                	<a href="${tradeBasePath}/logout">
	                                		<span class="text">退出登录</span>
	                                		<i class="icons_settings icons-loginout"></i>
	                                	</a>
	                                </li>
	                            </ul>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	    <!--Header end-->
	    <!--sidebar-menu str-->
		<div id="sidebar" class="sidebar" style="display:block;">
		 <div class="sidebar-height">
		 <ul>
		   <#list menuList as resource>
		       <@shiro.hasPermission name="${resource.code}">  
		          	<li id="${resource.id}">
		          		<a href="javascript:void(0)" onclick="childMenu(this,'${resource.id}')">
		          			<i class="icon icon-clinic"></i>
		          			<span>${resource.name}</span>
		          			<i class="icon icon-down"></i>
		          		</a>
		          	</li>
		       </@shiro.hasPermission>
		   </#list>
		 </ul>
		    </div>
		</div>
		<!--sidebar-menu end-->
	    
	    <!--content start-->
	    <div id="content" class="pmbox">
	        
	    </div>
	    <!--content start-->
	    <div id="content_Detail" style="display:none;"></div>
	    
	    
	    <!--配置状态 修改配置 begin-->
		<div id="configEditModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        <h3 id="configUpdateModalLabel">修改密码</h3>
		    </div>
		    <div class="modal-body">
		        <!--start-->
		        
		        <!--end-->
		    </div>
		    <div class="modal-footer">
		        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
		        <button class="btn btn-primary" onclick="changepassword();">保存</button>
		    </div>
		</div>
		<!--配置状态 修改配置 end-->
	</body>
	<script>
			
		 //修改：configEditModal
	    function configEdit(account) {
	    	$("#configEditModal").modal({
				remote: tradeBasePath + "/platform/user/changepassword?account=" + account
			});
	        $('#configEditModal').modal('show');
	    }
		$("#configEditModal").on("hidden", function() {  
		    $(this).removeData("modal"); 
		});
	
		function childMenu(obj,id){
		   //$("#"+id).find("ul").empty();
		   if(!$(obj).hasClass("active")) {
			   $.ajax({
			        url: '${tradeBasePath}/platform/resource/findMenuListByParentId',
			        data:'resourceId='+id,
			        type: 'POST',
			        success: function (data) {
			            $(".sidebar-height li ul").slideUp(200,function(){
			            	  $(this).remove();
			            });
			     	    $(".sidebar-height > ul>li>a,.sidebar-height ul ul>li>a").removeClass('active').removeClass('rotateRight');
			     	    $("#"+id).append(data);
			     	    $(obj).addClass("active")
			     	    if(data != '') {
			     	    	$(obj).parent("li").find("ul").slideDown(200);
			     	    }else {
			     	    	$(obj).addClass('active rotateRight');
			     	    }
		
			   		}
			   });
		   }
		}
		function openMenu(menuId,menuUrl){
		   $(".sidebar-height ul ul li").removeClass("on");
		   if(menuUrl == ''){
		      menuUrl = tradeBasePath + "/pf_building";
		   }
		   $("#" + menuId).addClass("on");
		   $.ajax({
		        url: tradeBasePath + menuUrl,
		        data:'resourceId=' + menuId,
		        type:'POST',
		        success: function (data) {
		        	$("#content").html(data);
		        	$("#content_Detail").hide();
		   			$("#content").show();
		    	}
		    });
		}
		
		var regBox = {
		    regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,// 邮箱
		    regPassword : /^[a-zA-Z0-9_-]{0,100}$/,// 用户名
		    regName : /^[a-zA-Z0-9_-]{3,16}$/,// 用户名
		    regMobile : /^0?1[3|4|5|7|8][0-9]\d{8}$/,// 手机
		    regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
		    regPostcode : /^[1-9][0-9]{5}$/  // 邮政编码
		};
		
		function psValidate(password,title,tag){
		    if(password==''){
		        alert(title + '不能为空');
		        return false;
		    }
		    var mflag = regBox.regPassword.test(password);
		    if (!mflag) {
		        alert( title + '不能包含特殊字符');
		        return false;
		    }
		    else
		    {
			    if(password.length<6){
			       alert( title + '位数不够');
			       return false;
			    }else if(password.length>16){
			       alert( title + '过长');
			       return false;
			    }else{
			       return true;
			    }
		    }
		}
		
		function psSameValidation(oldPassword,newPassword,confirmNewPassword)
		{
			if(newPassword != confirmNewPassword)
			{
			    alert('设置新密码和确认新密码不一致');
		        return false;
			}
			if(oldPassword == confirmNewPassword)
			{
			    alert('原密码和新密码一致');
		        return false;
			}
			return true;
		}
		
		//修改密码
		function changepassword(){
			var oldPassword = $('#oldPassword').val();
			var newPassword = $('#newPassword').val();
			var confirmNewPassword = $('#confirmNewPassword').val();
			var id = $("#id").val();
			
		   	if(psValidate(oldPassword,'原密码','pOldPassword') && psValidate(newPassword,'新密码','pNewPassword') && psValidate(confirmNewPassword,'确认新密码','pConfirmNewPassword') && psSameValidation(oldPassword,newPassword,confirmNewPassword))
		   	{
			   $.ajax({
			        type: "POST",
			        url:"${tradeBasePath}/platform/user/modifyPassword",
			        data:{
						"oldPassword" : oldPassword,
						"newPassword" : newPassword,
						"confirmNewPassword" : confirmNewPassword,
						"userId" : id
					},
			        error: function(request) {
			             alert('操作失败，请稍后重试');    
			        },
			        success : function(data) { 
						if(data.status == "OK"){
							 alert('密码修改成功,下次登录请使用新密码'); 
							 $('#configEditModal').modal('hide');
				      	}else{
				      		 alert(data.message);     
				        }
				  }
			   });
			}
		}
	</script>
</html>