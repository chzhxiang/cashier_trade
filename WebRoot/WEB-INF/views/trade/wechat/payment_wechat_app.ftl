<#include "/trade/common/common.ftl">
<title>微信支付</title>
</head>
<body>
<script type="text/javascript">
    function iosEvent(){
        var isException = '${isException}';
        var isSuccess = '${isSuccess}';
        if(isException){
            $Y.tips("网络异常，请稍候再试！");
            window.history.back();
        }else{
            if(isSuccess){
                var appId = '${data['appid']}';
                var prepayId = '${data['prepay_id']}';
                var mchId = '${data['mch_id']}';
                var nonceStr = '${data['nonce_str']}';
                var timeStamp = '${data['timeStamp']}';
                var package = "Sign=WXPay";
                var paySign = '${data['sign']}';
                var returnUrl = '${returnUrl}';
                if(IS.iOS){
                    appWXPay(appId,prepayId,mchId,nonceStr,timeStamp,package,paySign,returnUrl);
                }else if(IS.android){
                    nativeMethod.appWXPay(appId,prepayId,mchId,nonceStr,timeStamp,package,paySign,returnUrl);
                }else{
                    $Y.tips('系统错误,请稍后再试');
                    window.history.back();
                }
            }else{
                $Y.tips('${failMsg}');
                window.history.back();
            }
        }
    }
    if(IS.android){
        iosEvent();
    }
</script>
</body>
</html>