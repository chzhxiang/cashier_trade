<#include "/trade/common/common.ftl">
<title>支付宝支付</title>
</head>
<body>
<script  type="text/javascript">
    function iosEvent(){
        var isException = '${isException}';
        var isSuccess = '${isSuccess}';
        if(isException){
            alert("网络异常，请稍候再试！");
            window.history.back();
        }else{
            if(isSuccess){
                var data = '${data}';
                var returnUrl = '${returnUrl}';
                if(IS.iOS){
                    appAliPay(data,returnUrl);
                }else if(IS.android){
                    nativeMethod.appAliPay(data,returnUrl);
                }else{
                    alert('系统错误,请稍后再试');
                }
            }else{
                alert('${failMsg}');
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