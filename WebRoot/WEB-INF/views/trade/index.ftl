<html>
<head>
	<title>收银台接口测试</title>
	<script type="text/javascript" src="${tradeBasePath}/trade/js/common/jquery-1.11.0.min.js"></script>
	
</head>
<body>
	<h1>收银台接口测试(来源：交易测试控制器)</h1>
	<h3>生成聚合支付二维码接口</h3>
		<form  id="getQrCodeForm" >
			<h5>配置：</h5>
			<p>服务方平台公钥sPublicKey：<input type="text" name="sPublicKey"  value="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlLTXHBP9I1a9dkV54j+jb4lB0/VBB7lRi8Yk/FhaEMMNhY7AGLOVNWz3UovxElff0TpBgCuE8cOOtkYFPHyZ5Ng6PKTHgm5Uzt17iagRT+t0hA4igGe+SQH4QaIYDJy+IPSiW9qvHG/olWtbMm1j1l3cqP9Hn1x/WrrNz3gmRxv8OY9MSA76UIuLFCJenU5O6DblGqKOV2WYTCi1rNz2Yv0NskRuH0pRKkA+Kgvx4Yb/UwXcaFHKFhIdqM9bJ1KLKFx0nOYl7cncftQpXYDicc1shD2t3A/IRCkzoGHGpF8NOLMj86QUVZcfqYwmyUP8kjnInwD/MrwZkBbvDqg10QIDAQAB" /></p>
			<p>商户方私钥mPrivateKey：<input type="text" name="mPrivateKey" value="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKVxapmnfi5o5+gdBVoVPPt3N68mrKoscFNWy+S+g8ZV9egKtENLSKYDbtcH868EKggXuhLrYXSQI69ZzkYr4WqiFnBiaXgSCihsG+uE4eCdwXR4bnJTV1uc7ABR+alrg4VXUjI9wYPoMt2bCQWAb3qiZWJ+n31Jj4NKEsJN7U17AgMBAAECgYEAlJpE9/McQa907mZy0jFVb0HsQGTqnof/9Lt3tOTzFAlOMbi7pd5PSN64hmjcFw4UeuRk/G10QfDePLjbsOflNiJo2as8vKp6ijOHGpVJlBt4X6KX81KAgQpt0XetT1RzSnZVHbCPIf8wkeu2TOZYWGIVP3+uET/8oO/MUGSrQyECQQDYYn8LrYsrk1XKu4khA5MbMBaj1C7fbV6S8SLFzFbDV5c8qOjYjbbcQksmrIRueoZOglErwPzkU1aZjpwhrpnjAkEAw7tjPC2dHFvSdMTXr5TRgIfNjGnPEQ51wCZjprXdL8H2OIOA8vaBdkjHj8RTgji7idTz/a+A862bMTPFzD2xiQJBAMx0pN5/6BQQbuvUL0oGeqUh1wKNpoxuZwIo3lZPMVv6E2i5oOmdXb7Y1PGB6NAK7YSZFeFlxI5pQFZvrY8Vz0kCQGZGk3sNhqwb2uPDO8T2qWZVD/YQD/Aksyi9ppKlMzfSTVuIc6gBYPKy4vxfpRdFl9FU4k2sGGC8VXynRfMMRakCQEXa+SBnX3jd5TbIE1VzK8wH74rU8jZxjKikZ8nPev0OmOrDmKxpGq81czk9/RfbOSdOl0JyW5tr2qUkylA7BMU=" /></p>
			<h5>接口入参字段：</h5>
			<p>merchantNo：<input type="text" name="merchantNo" value="9683096567809952" /></p>
			<p>appId：<input type="text" name="appId" value="sun02c4640d1aed483" /></p>
			<p>nonceStr：<input type="text" name="nonceStr" class="nonceStr" value="318540b34cf042d3893d3db105e011fc" /></p>
			<p>signMode：<input type="text" name="signMode" value="RSA" /></p>
			<p>tradeTotalFee：<input type="text" name="tradeTotalFee"value="1" /></p>
			<p>outOrderNo：<input type="text" name="outOrderNo"  class="outOrderNo" value="1513853973535" /></p>
			<p>subject：<input type="text" name="subject" value="聚合二维码测试" /></p>
			<p>attach：<input type="text" name="attach" value="{5=5}" /></p>
			<p>timeStamp：<input type="text" name="timeStamp" value="2017-12-21 18:59:33" /></p>
			<p>outTime：<input type="text" name="outTime" value="" /></p>
			<p>returnUrl：<input type="text" name="returnUrl" value="https://www.baidu.com/" /></p>
			<p>notifyUrl：<input type="text" name="notifyUrl" value="https://xxx/payNotify" /></p>
			<h5>请求入参字符串：</h5>
			<p>paramJson：<span id="paramJson"></span></p>
			<p>paramXml：<xmp id="paramXml"></xmp></p>
			<div>
    			<input type="button" value="生成接口入参" onclick = "getSign();" />  
    			<input type="button" value="生成聚合支付二维码" onclick = "payQRCode();" />
    		</div>    		
    		<div style="padding:20px 20px 20px 20px;">
			<p>responseJson：<span id="responseJson"></span></p>
			<p>responseXml：<xmp id="responseXml"></xmp></p>
    		</div>
    	</form>
    	    	
	<script type="text/javascript">
	$(function(){
		// 初始化参数
		var $outOrderNo = $(".outOrderNo");
		$outOrderNo.each(function(i, domEle) {
			$(domEle).val(Date.parse(new Date()));
		});
	});
	
	function payQRCode() {
		var paramJson = $('#paramJson').html();
		var paramXml = $('#paramXml').html();
		$.ajax({
   		   type: "POST",
		   url:"${tradeBasePath}/test/payQRCode",
		   data:{'paramJson':paramJson,'paramXml':paramXml},
		   success : function(reponse) {  
		    $('#responseJson').html(reponse.result.responseJson);
		    $('#responseXml').html(reponse.result.responseXml);
		     return true;
		   }
		 });
	}
	
	function getSign() {
		    $('#paramJson').html("");
		     $('#paramXml').html("");
		$.ajax({
   		   type: "POST",
		   url:"${tradeBasePath}/test/getSign",
		   data:$('#getQrCodeForm').serialize(),
		   success : function(reponse) {  
		    $('#paramJson').html(reponse.result.paramJson);
		     $('#paramXml').html(reponse.result.paramXml);
		     return true;
		   }
		 });
	}
	</script>
	
	<h3>============================================================================</h3>
	
</body>
</html>