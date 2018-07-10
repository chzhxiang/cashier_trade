<#include "/trade/common/common.ftl">
<title>支付宝支付</title>
</head>
<body>
    <#if isException == false && isSuccess == true>
        ${data}
    </#if>

<script  type="text/javascript">
    $(function(){ 
        //$("#payForm").submit();
    });
</script>
</body>
</html>