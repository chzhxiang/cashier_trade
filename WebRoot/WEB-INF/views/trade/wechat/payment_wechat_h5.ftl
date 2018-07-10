<#include "/trade/common/common.ftl">
<title>微信支付</title>
</head>
<body>
<a id="pay" href="${data['mweb_url']}"></a>
<script type="text/javascript">
    var isException = '${isException}';
    var isSuccess = '${isSuccess}';
    if(isException){
        alert("网络异常，请稍候再试！");
        window.history.back();
    }else{
        if(isSuccess){
            document.getElementById("pay").click();
        }else{
            alert('${failMsg}');
            window.history.back();
        }
    }
</script>
</body>
</html>