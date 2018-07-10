<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no" />
<title>登录</title>
<script type="text/javascript" src="${tradeBasePath}/mobileapp/js/jquery-1.9.1.min.js?v=${js_version}"></script>
<link rel="stylesheet" href="${tradeBasePath}/mobileapp/css/style.css?v=${css_version}"/>
<script type="text/javascript" src="${tradeBasePath}/platform/js/common.js?v=${js_version}"></script>
<style>
html,body { background: #fff;  }
</style>
</head>

<body>
<div class="container" style="padding-top: 10px">
     <section class="login">
         <div class="login-enter">
         	<form id="loginForm" action="" method="post">
	            <p id="pnumber"><i class="icon icon-number middle"></i><span><input type="text" id="useraccount" name="account"  value="" class="login-input" placeholder="请输入手用户账号"></span></p>
	            <p><i class="icon icon-password middle"></i><input type="password" id="userpassword" name="password"  class="login-input"></p>         
	            <p class="p-button">
	                <button onclick="login()" class="loginbutton">登录</button>
	            </p>
	            <#if message != ''>
		         	<p class="p1" style="margin-top:3px;margin-bottom: 3px;text-align:center;color:red">${message}</p>
		        </#if>
	            <p class="p-link">
	                <a class="fl" onclick="register()">注册账号</a>
	                <a class="fr" onclick="forgotPs()">忘记密码？</a>
	            </p>
	         </form>
        </div>
     </section>
</div>
</body>
</html>
<script type="text/javascript">
var deviceInfo = "";
 //注册
 function register(){
	 
 }
 
 //忘记密码
function forgotPs(){
	 
 }
 
 function login(){
      var useraccount = $('#useraccount').val();
      if(useraccount == ''){
        alert('账号名不能为空'); 
        return false;
      }
      var password = $('#userpassword').val();
      if(password==''){
    	  alert('密码不能为空'); 
         return false;   
      }
      $("#loginForm").attr("action","${tradeBasePath}/mobileapp/doLogin");
      $("#loginForm").submit();
     /*  $.ajax({
                type: "POST",
                url:"",
                data:$('#loginForm').serialize(),
                error: function(request) {
					alert('登录失败，请稍后重试');
                },
                success : function(data) { 
			    //IOS隐藏底部状态栏
			   /*  if(IS.iOS){
			        try{
				        deviceInfo =  appDeviceInfo();
				    }catch(e){}
			    }
			    if(IS.android){
			         try{
			            deviceInfo =  nativeMethod.appDevice();
			         }catch(e){}
			    } */
				
                 /*  if(data.status == "OK"){
                	  debugger;
                	  data;
                	  $("#loginForm").attr("src","");
                	  //$("#loginForm").submit();
                  }else{
                    
                  }
              }
         }); */ 
 }
  
 
</script>
