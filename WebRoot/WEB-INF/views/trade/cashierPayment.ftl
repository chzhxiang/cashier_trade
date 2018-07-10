<#include "/trade/common/payCommon.ftl">
<title>支付</title>
</head>
<body>
    <div class="container">
        <div class="wechat-pay-info">
            <div class="wechat-pay-info-logo">
                <#if payIdentity == "wechat">
                    <i class="icon-wpay"></i>
                    <h3>正在尝试打开微信客户端...</h3>
                <#elseif payIdentity == "alipay">
                    <i class="icon-alipay"></i>
                    <h3>正在尝试打开支付宝客户端...</h3>
                </#if>
                <p class="mt15">1.如果未完成付款，请点击“继续支付”&nbsp;&nbsp;&nbsp;&nbsp;</p>
                <p>2.如果已完成付款，请点击“已完成付款”</p>
            </div>
        </div>
    </div>
    <section class="button row wechat-pay-info-btns">
        <a class="but-save col but-cancel" onclick="completedPayment(this)" href="javascript:void(0);">已完成付款</a>
        <a class="but-save col but-down" onclick="continuePayment(this)" href="javascript:void(0);">继续支付</a>
    </section>
    <form id="payForm" name="payForm" method="post">
        <input id="cashierId" name="cashierId" type="hidden" value="${cashierId}" />
        <input id="channelCode" name="channelCode" type="hidden" value="${channelCode}" />
    </form>
    <div id="divHtml"></div>
    <script type="text/javascript" src="${tradeBasePath}/trade/js/cashier.payment.js?v=${trade_js_version}"></script>
</body>
</html>