<#include "/trade/common/common.ftl">
<title>支付方式选择页面</title>
</head>
<body>
    <div class="ccontainer">
        <div id="chooseTradeMode">
            <h3 class="h3-tit">选择支付方式</h3>
            <div class="modal-pay m-pay" id="tradeModes"></div>
        </div>
        <form id="payForm" name="payForm" action="${tradeBasePath}/trade/cashierPayment/" method="post">
            <input id="cashierId" name="cashierId" type="hidden" value="${cashierId}" />
            <input id="channelCode" name="channelCode" type="hidden" value="" />
        </form>
        <section class="button">
            <a class="but-save" onclick="payFormSubmit(this)" href="javascript:void();">立即支付</a>
        </section>
    </div>
    <script type="text/javascript" src="${tradeBasePath}/trade/js/cashier.trade.js?v=${trade_js_version}"></script>
</body>
</html>