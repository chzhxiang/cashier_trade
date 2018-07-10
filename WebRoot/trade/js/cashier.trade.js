$(document).ready(function() {
	getChoicePayment();
	
});

/**
 * 支付方式选择控制
 * 当支付方式大于三种时需要加载
 */
function choicePaymentController(){
	if($("#tradeModes a").length > 3){
		$('#tradeModes').css('height', '144px');
	}
	$("#tradeModes a").click(function() {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass('active');
        if (index > 2) {
            $('.pay-more-ways').hide();
        } else {
            $('.pay-more-ways').show();
        }
        $("#channelCode").val($(this).attr("tradename"));
    });
    $('.pay-more-ways').bind('click', function() {
        var $state = $(this).hasClass('active');
        if ($state) {
            $(this).removeClass('active').find('span').text('更多支付方式');
            $('#tradeModes').css('height', '144px');
        } else {
            $(this).addClass('active').find('span').text('收起');
            $('#tradeModes').css('height', 'auto');
        }
    });
}

/**
 * 加载支付方式
 */
function getChoicePayment(){
	var datas = $("#payForm").serializeArray();
	$.ajax( {  
		type : 'POST',  
		url : tradeBasePath + "/trade/getChoicePayment/",  
		data : datas,  
		dataType : 'json',  
		timeout:120000,
		error : function(data) {
			alert("系统内部错误,请联系管理员。");
		},
		success : function(data) { 
			if(data.isSuccess == true){
				var entities = data.entities;
				if(entities.length > 0){
					var html = "";
					for(var i = 0; i < entities.length; i++){
						var channelCode = entities[i].channelCode;
						var channelViewName = entities[i].channelViewName;
						if(IS.iOS){
							html += genertorTradeModes(i,channelCode,channelViewName);
						}else{
							if(channelCode != "unionpay_apple_pay"){
								html += genertorTradeModes(i,channelCode,channelViewName);
							}
						}
					}
					$("#tradeModes").html(html);
					$("#tradeModes").find("a").eq(0).addClass("active");
					$("#channelCode").val($("#tradeModes").find("a").eq(0).attr("tradename"));
					if(entities.length > 3){
						$("#chooseTradeMode").append('<a href="javascript:void(0);" class="item item-pay pay-more-ways"><span>更多支付方式</span></a>');
					}
					choicePaymentController();
				}else{
					alert("商户配置缺失，请联系管理员!");
				}
			}else{
				alert("系统异常，请稍候再试!");
			}
		}
	});
}


function genertorTradeModes(idx, channelCode,channelViewName){
	return "<a class='item item-pay' tradename='" + channelCode +"'><i class='icon-" + channelCode +"'><b class='icon-pay-pass'></b></i>" + channelViewName +"<i class='icon icon-select middle'></i></a>";
}

/**
 * 调起支付
 */
function payment(){
	var datas = $("#payForm").serializeArray();
	if($("#channelCode").val() != null && $("#channelCode").val() != ''){
		$.ajax( {  
			type : 'POST',  
			url : tradeBasePath + "/trade/payment/",  
			data : datas,  
			dataType : 'json',  
			timeout:120000,
			error : function(data) {
				alert("系统内部错误,请联系管理员。");
			},
			success : function(data) { 
				if(data.isSuccess){
					if(!data.isException && data.isSuccess){
						if(data.channelCode == "wechat_jsapi"){
							var retuenUrl = "http://www.baidu.com/";
							wechatJsapi(data.appid,data.prepay_id,data.mch_id,data.nonce_str,data.timeStamp,data.sign,retuenUrl);
						}else{
							
						}
					}
				}else{
					alert("系统异常，请稍候再试!");
				}
			}
		});
	}else{
		alert("请选择支付方式!");
	}
}

/**
 * 支付提交
 */
function payFormSubmit(obj){
	document.getElementById("payForm").submit();
	obj.classList.add('btn-disabled');
	obj.removeAttribute('onclick');
}