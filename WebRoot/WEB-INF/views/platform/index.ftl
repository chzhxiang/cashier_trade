<#include "/platform/common/common.ftl">
<#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${application.name}</title>
</head>
<body>
<div id="header">
    <div class="logo">
         <p class="logo-pic"><img class="img-circle" src="${tradeBasePath}/platform/images/logo.png"></p>
         <p class="logo-name">处方流转平台</p>
    </div>
    <div class="header-tit">
        <div class="location">当前诊所：珠江新城门诊部</div>
        <div class="avatar dropdown">
            <a class="dropdown-toggle" href="javascript:void(0);" data-toggle="dropdown" >
                <span class="avatar-name">chuangxiong</span>
                <i class="icon icon-down"></i>
            </a>
            <ul class="dropdown-menu">
                <li><a href="#"><i class="icon icon-set"></i>系统设置</a></li>
                <li><a href="#"><i class="icon icon-password"></i>修改密码</a></li>
                <li><a href="${tradeBasePath}/pf_logout"><i class="icon icon-quit"></i>退出登录</a></li>
            </ul>
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
<!--content str-->
<div id="content" class="pmbox">
    <div class="msg-list">	
	</div>
</div>
</body>
</html>
<script>
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
    	}
    });
}

</script>