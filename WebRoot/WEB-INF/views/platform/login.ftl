<html>
<head>
    <meta name="mobile-agent" content="format=html5">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <title>登录</title>
    <script type="text/javascript" src="${tradeBasePath}/platform/js/jquery-1.12.3.min.js?v=${js_version}"></script>
    <link rel="stylesheet" href="${tradeBasePath}/platform/css/login.css?v=${css_version}"/>
</head>
<body> 
<div class="login">
	 <!-- 
     <h1 class="h1-tit"><img src="${tradeBasePath}${application.pfLoginLogoPath}" alt="${application.name}"><span>平台</span></h1>
     -->
     <div class="con ">   
          <form id="loginForm" action="${tradeBasePath}/pf_doLogin" method="post">
          <input type="hidden" name="message" id="message" value="${error}">
          <input type="hidden" name="platFormCode" value="${platFormCode}">
          <p class="p1"><input type="text" id="account" name="account" placeholder="用户名"></p>
          <p class="p2"><input type="password" id="password" name="password" placeholder="密码"></p>
          <#if message != ''>
          	 <p class="p1" style="margin-top:3px;margin-bottom: 3px;text-align:center;color:red">${message}</p>
          </#if>
          <button type="button"  class="button-login" onclick="doLogin()" >立刻登录</button>
          </form>
     </div>
</div>
</body>
</html>
<script type="text/javascript">
var ip='${hostIP}';
$(function() {
   var message = $('#message').val();
   if(message != ""){
      $(".con").addClass("error");
      $(".con").append('<div class="msg-error"><i></i>' + message + '</div>');
   }
   
});

function doLogin(){
	var isValid = true;
   	var account = $('#account').val();
   	var password = $('#password').val();
   	clearError();
   	if(account == ""){
      	$(".con").addClass("error");
      	$(".con").append('<div class="msg-error"><i></i>请输入账户名和密码</div>');
      	isValid =  false;
   	}
   	if(password == ""){
      	$(".con").addClass("error");
      	$(".con").append('<div class="msg-error"><i></i>请输入账户名和密码</div>');
      isValid =  false;
   	}
   	
   	if(isValid){
   		$("#loginForm").submit()
   	}
}
 
function clearError(){
 	$(".con").removeClass("error");
 	var errorDiv = $("div[class='msg-error']");
 	if(errorDiv != null && typeof(id) == "undefined"){
 		errorDiv.remove();
 	}
}
</script>
