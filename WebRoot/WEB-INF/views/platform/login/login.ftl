<#include "/platform/common/common.ftl">
<html>
	<head>
	    <meta name="mobile-agent" content="format=html5;url=http://m.yx129.com/mobile/">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	    <meta http-equiv="content-type" content="text/html;charset=utf-8">
	    <title>登录</title>
	    <link rel="stylesheet" href="${tradeBasePath}/platform/css/login.css?v=${css_version}" />
	    <script type="text/javascript" src="${tradeBasePath}/platform/js/login/login.js?v=${css_version}"></script>
    </head>

    <body>
        <div class="login">
            <h1 class="h1-tit"><img src="${tradeBasePath}/platform/images/login-logo.png" alt="医程通"><span>收银台2.0</span></h1>
            <div class="con">
            	<form id="loginForm" action="${tradeBasePath}/pf_doLogin" method="post">
	                <div class="msg-error"><i></i>请输入账户名和密码</div>
	                <p class="p1"><input type="text" name="account" id="account" placeholder="用户名"></p>
	                <p class="p2"><input type="password" name="password" id="password" placeholder="密码"></p>
	                <p class="p1" style="margin-top:-15px;margin-bottom: 3px;text-align:center;color:red" id="message">${message}</p>
	                <button type="button" class="button-login" onclick="login()">立刻登录</button>
                </form>
            </div>
        </div>
    </body>
</html>