<#include "/trade/common/common.ftl">
<title>银联绑卡页面</title>
<link rel="stylesheet" href="${tradeBasePath}/trade/css/style.css" type="text/css">
</head>
<body>
    <div class="ccontainer">
        <form id="payForm" name="payForm" action="${tradeBasePath}/bindCard/applyBindCard/" method="post">
            <input name="certificateNo" type="hidden" value="341126197709218366" />
            <input name="deviceId" type="hidden" value="1111" />
            <input name="accountId" type="hidden" value="1111" />
            <input name="certificateType" type="hidden" value="01" />
            <input name="userName" type="hidden" value="全渠道" />
            <input name="bankCardNo" type="hidden" value="6216261000000000018" />
            <input name="channelCode" type="hidden" value="unionpay_small_quick" />
            <input name="timeStamp" type="hidden" value="2017-10-09 17:18:52" />
            <input name="appId" type="hidden" value="sun90f99466b08c4c7" />
            <input name="nonceStr" type="hidden" value="bafdc9ca26364506a1538ea72fac5c31" />
            <input name="merchantNo" type="hidden" value="9683091070339795" />
            <input name="signMode" type="hidden" value="RSA" />
            <input name="sign" type="hidden" value="KsvdhY3kzsV+cB67khV6eiIg4nfQw//zDrF/nfiak938W7FZmXgm3p5a19/+RIFjHipFlsapusN+r01YqipOaGxiGrPa8Akpl9q6YsSknVfmqwB94OWjfoBpoL4Ilt3MQSod0b3DBCuAUBaT+DCYrNe3ehEJni5MSxaL6QINg2KDQtsP/jmWFISx4tUQyn/lg61hX/BCxVgQp2B/wZbjjrfo1uq9/P5FDo5SHs5obJoI3M2+TeOdQGki6RVCoNDSaxSGP7sC+qge/ELTOeWAvOTthjn5AnyYtcnD0A8fkIkBYNnjqW27IDetH77zBs/CxhikRjtYT1+yR5VJPD5W9g==" />
        </form>
        <section class="button">
            <a class="but-save" href="javascript:pay();">绑卡跳转</a>
        </section>
    </div>
    <script type="text/javascript">
        function pay(){
            $('#payForm').submit();
        }
    </script>
</body>
</html>