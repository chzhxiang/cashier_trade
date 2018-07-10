<#include "/trade/common/common.ftl">
<title>错误提示</title>
<style>
    .page-tips {
        padding-top: 26px;
    }
    i.tips-error {
        display: block;
        width: 163px;
        height: 167.5px;
        margin: 0 auto;
        background: url(${tradeBasePath}/trade/images/error.png) no-repeat;
        background-size: 100% auto;
    }
    .page-tips p {
        font-size: 0.9rem;
        color: #989cab;
        padding: 15px 35px 0;
        text-align: center;
    }
</style>
</head>
<body>
    <div class="ccontainer">
        <section class="page-tips">
            <i class="tips-error"></i>
            <p class="tips-word">${returnCode}:${returnMsg}</p>
        </section>
    </div>
    <section class="button">
        <a class="but-save" href="javascript:window.history.back();">返回</a>
    </section>
</body>
</html>