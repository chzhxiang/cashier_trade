<#include "/trade/common/common.ftl">
<title>支付方式选择页面</title>
<link rel="stylesheet" href="${tradeBasePath}/trade/css/style.css" type="text/css">
</head>
<body>
    <div class="ccontainer">
        <form id="payForm" name="payForm" action="${tradeBasePath}/trade/cashierCenter/" method="post">
            <input name="timeStamp" type="hidden" value="2018-02-27 10:29:28" />
            <input name="appId" type="hidden" value="sun0ba47743282a4cf" />
            <input name="cashierId" type="hidden" value="fa40bed24aea44e8833243794abf00d1" />
            <input name="nonceStr" type="hidden" value="626cb67e18b941e685e343452be02b79" />
            <input name="merchantNo" type="hidden" value="9683097565931577" />
            <input name="signMode" type="hidden" value="RSA" />
            <input name="sign" type="hidden" value="KsvdhY3kzsV+cB67khV6eiIg4nfQw//zDrF/nfiak938W7FZmXgm3p5a19/+RIFjHipFlsapusN+r01YqipOaGxiGrPa8Akpl9q6YsSknVfmqwB94OWjfoBpoL4Ilt3MQSod0b3DBCuAUBaT+DCYrNe3ehEJni5MSxaL6QINg2KDQtsP/jmWFISx4tUQyn/lg61hX/BCxVgQp2B/wZbjjrfo1uq9/P5FDo5SHs5obJoI3M2+TeOdQGki6RVCoNDSaxSGP7sC+qge/ELTOeWAvOTthjn5AnyYtcnD0A8fkIkBYNnjqW27IDetH77zBs/CxhikRjtYT1+yR5VJPD5W9g==" />
        </form>
        <section class="button">
            <a class="but-save" href="javascript:pay();">立即支付</a>
        </section>
        <section class="button">
            <a  class="but-save" href="https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx201710091909109a80f4cb2e0677676344&package=3176955380">微信H5支付</a>
        </section>
    </div>
    <script type="text/javascript">
        function pay(){
            $('#payForm').submit();
        }
    </script>
</body>
</html>