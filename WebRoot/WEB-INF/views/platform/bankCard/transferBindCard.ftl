<html>
	<head>
		<title>银联绑卡页面</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	</head>
	<style>
		.am-icon-loading:before { 
			content: ""; 
			display: block; 
			background: url(../../mobileapp/newViewSrc/images/loading.png) no-repeat scroll 0 0; 
			background-size: 100%; 
			height: 20px; 
			width: 20px; 
			-webkit-animation: rotate1 linear 1s infinite;
			animation: rotate1 linear 1s infinite; 
		}
		.am-loading-text { 
			font-size: 1.0rem; 
			position: fixed; 
			top: 40%; 
			background: #000; 
			background-color: rgba(0,0,0,0.5);
			border-radius: 5px; 
			color: #fff; 
			display: inline-block; 
			padding: 30px 20px; 
			z-index: 999; 
			left: 50%; 
			-webkit-transform: translate3d(-50%,0,0); 
			transform: translate3d(-50%,0,0); 
			min-width: 236px; 
		}
		.am-icon-loading { 
			display: inline-block; 
			height: 24px; 
			vertical-align: middle; 
			width: 24px; 
			margin-right: 3px; 
		}
		html { background: #edf3ff; } 
		@-webkit-keyframes rotate1 {0% {-webkit-transform:rotate(0deg);} 100% {-webkit-transform:rotate(360deg);} }
	</style>
	<body>
		<div class="am-loading-text">
			<span class="am-icon-loading"></span>
			<b id="am-loading_txt" style="font-weight: normal">正在进入提交银行卡信息页...</b>
		</div>
		<iframe src="" id="onlinePayContent" width="100%" height="1000" name="main" style="background-color:#e8e8e8;"></iframe>
		<form id ="pay_form" action="${unionBindCardRequestUrl}" method="post" target="main">
			<#if bindCardMap?exists>
	            <#list bindCardMap?keys as key> 
	               <input type="hidden" name="${key}" value="${bindCardMap[key]}" />
	            </#list>
	        </#if>
		</form>
    </body>
    <script type="text/javascript">
        var docH = $(document).height();
        $(function(){
        	var $payConten = $('#onlinePayContent');
             $payConten.attr('height',docH);
             $payConten.css('width', 'calc(100% + 18px)');
             $('body').css('background-color','#e8e8e8');
        });
   		setTimeout(function(){
   			document.all.pay_form.submit();
   			$('.am-loading-text').hide();
   		},800)
   	</script>
</html>