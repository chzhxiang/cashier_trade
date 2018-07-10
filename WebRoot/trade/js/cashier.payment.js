$(document).ready(function() {
	loading();
	payment();
});

/**
 * 加载框
 */
function loading() {
    var $loadingState = $('.loading-mask');
    var loadingHtml = '<div class="loading-mask"><div class="loading-container"><span class="hoadhd"></span><p class="color-main">加载中</p></div></div>';
    if ($loadingState) {
        $('.loading-mask').remove();
    }
    $('body').append(loadingHtml);
}

/**
 * 调起支付
 */
function payment(){
    var datas = $("#payForm").serializeArray();
    $.ajax({  
        type : 'POST',  
        url : tradeBasePath + "/trade/paymentJson/",  
        data : datas,  
        dataType : 'json',  
        timeout:120000,
        error : function(data) {
        	$Y.tips("系统内部错误,请联系管理员。");
        },
        success : function(data) { 
            if(!data.isException && data.isSuccess){
                var channelCode = data.channelCode;
                if(channelCode == "alipay_h5"){
                    $("#divHtml").html(data.data);
                    $("form[name='punchout_form']").submit();
                }else if(channelCode == "alipay_app"){
                    if(IS.iOS){
                        appAliPay(data.data,data.returnUrl);
                    }else if(IS.android){
                        nativeMethod.appAliPay(data.data,data.returnUrl);
                    }else{
                    	$Y.tips('系统错误,请稍后再试');
                    }
                }else if(channelCode == "wechat_app"){
                	var package = "Sign=WXPay";
                    var wechat = data.data;
                    if(IS.iOS){
                        appWXPay(wechat.appid,wechat.prepay_id,wechat.mch_id,wechat.nonce_str,wechat.timeStamp,package,wechat.sign,data.returnUrl);
                    }else if(IS.android){
                        nativeMethod.appWXPay(wechat.appid,wechat.prepay_id,wechat.mch_id,wechat.nonce_str,wechat.timeStamp,package,wechat.sign,data.returnUrl);
                    }else{
                    	$Y.tips('系统错误,请稍后再试');
                    }
                }else if(channelCode == "wechat_jsapi"){
                	var wechat = data.data;
                    var prepayId = "prepay_id=" + wechat.prepay_id;
                    var appId = wechat.appid;
        			var nonceStr = wechat.nonce_str;
        			var timeStamp = wechat.timeStamp + "";
        			var paySign = wechat.sign;
        			var successUrl = data.returnUrl;
        			//alert(prepayId + "\n" + appId + "\n" + nonceStr + "\n" + timeStamp + "\n" + paySign);
     				WeixinJSBridge.invoke('getBrandWCPayRequest', {
 	    				"appId": appId,
 	    				"timeStamp": timeStamp,//时间戳
 	    				"nonceStr": nonceStr,//随机字符串,String(32)
 	    				"package": prepayId,  //订单详情扩展字符串,统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***
 	    				"signType": "MD5",//签名方式
 	    				"paySign": paySign//签名
 	    				},function(res){
 	    					WeixinJSBridge.log(res.err_msg); 
 	    					if (res.err_msg == 'get_brand_wcpay_request:ok') {
 	    						location.href = successUrl;
 	    					}else if(res.err_msg == 'get_brand_wcpay_request:cancel'){
 	    						console.log("取消支付");
 	    					}else{
 	    						$Y.tips('系统错误,请稍后再试');
 	    					}
     				});
                }else if(channelCode == "unionpay_h5"){
                	$("#divHtml").html(data.data);
                	$("form[name='punchout_form']").submit();
                }else if(channelCode == "unionpay_sdk"){
                	var tn = data.data.tn;
                	var returnUrl = data.returnUrl;
                	if(IS.iOS){
						appUnionPay(tn,returnUrl);
					}else if(IS.android){
						nativeMethod.appUnionPay(tn,returnUrl);
					}else{
						$Y.tips('系统错误,请稍后再试');
					}
                }else if(channelCode == "unionpay_apple_pay"){
                	var tn = data.data.tn;
                	var returnUrl = data.returnUrl;
                	var applePayMerchantID = data.applePayMerchantID;
                	var totalFee = data.unionPayTotalFee;
                	if(IS.iOS){
    					appUnionApplePay(tn,returnUrl,applePayMerchantID,returnUrl,totalFee);
    				}else{
    					$Y.tips('系统错误,仅支持IOS平台。');
    				}
                	
                }
                $('.loading-mask').remove();
            }else{
            	var tipsBox = new $Y.confirm({
                    content: '<p class="center">' + data.failMsg + '</p>',
                    ok: {title: '确认',click: function() {tipsBox.close();window.history.back();}}
                })
            }
        }
    });
}

/**
 * 继续支付
 */
function continuePayment(){
    var datas = $("#payForm").serializeArray();
    $.ajax({  
        type : 'POST',  
        url : tradeBasePath + "/trade/continuePayment/",  
        data : datas,  
        dataType : 'json',  
        timeout:120000,
        error : function(data) {
            $Y.tips("系统内部错误,请联系管理员。");
        },
        success : function(data) { 
            if(!data.isException && data.isSuccess){
            	paymentTips(data.tradeState,data.returnUrl);
            }else{
            	var tipsBox = new $Y.confirm({
                    content: '<p class="center">' + data.failMsg + '</p>',
                    ok: {title: '确认',click: function() {tipsBox.close();window.history.back();}}
                })
            }
        }
    });
}



/**
 * 已完成支付
 */
function completedPayment(){
    var datas = $("#payForm").serializeArray();
    $.ajax({  
        type : 'POST',  
        url : tradeBasePath + "/trade/continuePayment/",  
        data : datas,  
        dataType : 'json',  
        timeout:120000,
        error : function(data) {
        	$Y.tips("系统内部错误,请联系管理员。");
        },
        success : function(data) { 
            if(!data.isException && data.isSuccess){
            	if(data.tradeState == "NO_PAYMENT"){
            		var tipsBox = new $Y.confirm({
                        title: '未支付',
                        content: '<p>订单未支付，是否继续支付！</p>',
                        ok: {
                            title: '是',
                            click: function() {
                            	payment();
                            	tipsBox.close();
                            }
                        },
                        cancel: {
                            title: "否",
                            click: function() {
                            	tipsBox.close();
                            }
                        },
                    })
            	}else{
            		 paymentTips(data.tradeState,data.returnUrl);
            	}
            }else{
            	var tipsBox = new $Y.confirm({
                    content: '<p class="center">' + data.failMsg + '</p>',
                    ok: {title: '确认',click: function() {tipsBox.close();window.history.back();}}
                })
            }
        }
    });
}

/**
 * 支付状态提示
 * @param tradeState
 * @param returnUrl
 */
function paymentTips(tradeState, returnUrl){
	if(tradeState == "NO_PAYMENT"){
        //订单未支付，可以执行支付
        payment();
    }else if(tradeState == "PAYMENTING"){
        //订单支付中，提示用户，且提供跳转至商户页面
    	var tipsBox = new $Y.confirm({
            title: '支付中',
            content: '<p>订单正在支付中，请稍等，是否跳转至订单详情！</p>',
            ok: {
                title: '订单详情',
                click: function() {
                	window.location.href=returnUrl;
                }
            },
            cancel: {
                title: "否",
                click: function() {
                	tipsBox.close();
                }
            },
        })
    }else if(tradeState == "PAYMENT"){
        //订单已经支付跳转至商户页面
    	var tipsBox = new $Y.confirm({
            title: '已支付',
            content: '<p>订单已支付，跳转至订单详情！</p>',
            ok: {
                title: '订单详情',
                click: function() {
                	window.location.href=returnUrl;
                }
            }
        })
    }else if(tradeState == "CLOSE"){
        //订单关闭跳转至商户页面
    	var tipsBox = new $Y.confirm({
            title: '支付中',
            content: '<p>订单正在支付中，请稍等，是否跳转至订单详情！</p>',
            ok: {
                title: '订单详情',
                click: function() {
                	window.location.href=returnUrl;
                }
            }
        })
    }
}
