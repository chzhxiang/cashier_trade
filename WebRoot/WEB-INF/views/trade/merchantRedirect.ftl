<#include "/trade/common/common.ftl">
<title>支付成功</title>
<style type="text/css">
    * {
        margin: 0;
        padding: 0;
        font-style: normal;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
        vertical-align: baseline;
    }
    
    html {
        font-size: 100%;
        -webkit-user-select: none;
        user-select: none;
        -webkit-text-size-adjust: 100%;
        -webkit-text-size-adjust: 100%;
        background: #edf3ff;
    }
    
    .button {
        padding: 14px 14px;
        margin: 10px 0;
    }
    
    .but-save {
        display: block;
        height: 50px;
        line-height: 50px;
        text-align: center;
        text-decoration: none;
        color: #fff;
        background: #7ba1fe;
        font-size: 1.2rem;
        border-radius: 25px;
        -moz-border-radius: 25px;
        background: linear-gradient(145deg, #7ca5ff, #7a9ffe, #6f84f7);
    }
    
    .but-save,
    .but-save:active {
        -webkit-box-shadow: 0 4px 12px rgba(118, 141, 251, .35);
        box-shadow: 0 4px 12px rgba(118, 141, 251, .35);
        opacity: 1;
    }
    
    .page-tips {
        padding: 20px 0;
        text-align: center;
    }
    
    .tips-icon,
    .tips-icon-hcons {
        width: 206px;
        height: 206px;
        overflow: hidden;
        display: inline-block;
        background: url(${tradeBasePath}/trade/images/sprites-new.png) no-repeat;
        background-size: 100% auto;
    }
    
    .tips-word h3 {
        font-size: 2.0rem;
    }
    
    .pay-tip-container {
        background-color: #ffffff;
    }
    
    .pay-tip-container .button {
        margin-top: 0;
        padding-bottom: 27px;
    }
</style>
</head>
<body>
    <div class="pay-tip-container">
        <section class="page-tips">
            <i class="tips-icon payment-wanting"></i>
            <div class="tips-word">
                <p>${merchantName}</p>
                <h3>¥ ${totalFee}</h3>
            </div>
        </section>
        <section class="button">
            <a class="but-save" href="${returnUrl}">${returnText}</a>
        </section>
    </div>
</body>
</html>