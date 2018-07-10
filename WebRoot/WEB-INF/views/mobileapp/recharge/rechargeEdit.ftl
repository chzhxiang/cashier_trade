<#include "/trade/common/common.ftl">
<title>押金充值</title>
<link rel="stylesheet" href="${tradeBasePath}/trade/css/style.css" type="text/css">
</head>
<body>
    <div class="ccontainer">
        <form id="applyRechargeForm" name="applyRechargeForm" action="${tradeBasePath}/recharge/applyRecharge/" method="post">
            
        </form>
        <section class="button">
            <a class="but-save" href="javascript:applyRecharge();">充值</a>
        </section>
    </div>
    <script type="text/javascript">
        function applyRecharge(){
            $('#applyRechargeForm').submit();
        }
    </script>
</body>
</html>