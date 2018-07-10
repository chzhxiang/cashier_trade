<!DOCTYPE html>
<html lang="en">
<head>
<meta name="renderer" content="webkit" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<title>微信支付</title>
</head>
<body>
<script type="text/javascript">

    function callPay(){
        var isException = '${isException}';
        var isSuccess = '${isSuccess}';
        if(isException){
            alert("网络异常，请稍候再试！");
            window.history.back();
        }else{
            if(isSuccess){
                var appId = '${data['appid']}';
                var prepayId = "prepay_id=" + '${data['prepay_id']}';
                var mchId = '${data['mch_id']}';
                var nonceStr = '${data['nonce_str']}';
                var timeStamp = '${data['timeStamp']}';
                var package = "Sign=WXPay";
                var paySign = '${data['sign']}';
                WeixinJSBridge.invoke('getBrandWCPayRequest', {
                    "appId": '${data['appid']}',
                    "timeStamp": '${data['timeStamp']}',//时间戳
                    "nonceStr": '${data['nonce_str']}',//随机字符串,String(32)
                    "package": "prepay_id=" + '${data['prepay_id']}',  //订单详情扩展字符串,统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***
                    "signType": "MD5",//签名方式
                    "paySign": '${data['sign']}'//签名
                    },function(res){
                        WeixinJSBridge.log(res.err_msg); 
                        if (res.err_msg == 'get_brand_wcpay_request:ok') {
                            location.href = '${returnUrl}';
                        }else if(res.err_msg == 'get_brand_wcpay_request:cancel'){
                            console.log("取消支付");
                            window.history.back();
                        }else{
                            alert(res.err_msg);
                        }
                });
            }else{
                alert('${failMsg}');
                window.history.back();
            }
        }
    }
    
    window.onload = function(){ 
        // 自动提交
        setTimeout(function () {callPay()}, 500);
    };

</script>
</body>
</html>