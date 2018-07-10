<!--start-->
<div class="widget-content radius-3">
    <!--内容-->
    <#if channelCode != '' && channelCode != null>
     	<input type="hidden" id="channelCode" name="channelCode" value="${channelCode}"/>
    </#if>
    <#if applicationId != '' && applicationId != null>
     	<input type="hidden" id="applicationId" name="applicationId" value="${applicationId}"/>
    </#if>
    <form class="form-horizontal patient-info" id="<#if channelCode != '' && channelCode != null>add_channelFrom<#else>update_channelFrom</#if>">
        <div class="control-group order-group">
            <span class="span1">合作者ID</span>
            <span class="span3">
                <input type="text" name="mchId" placeholder="请输入支付宝合作者ID" value="${entity.mchId}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">卖家账号</span>
            <span class="span3">
                <input type="text" name="mchAccount" placeholder="请输入支付宝卖家账号" value="${entity.mchAccount}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">支付公钥</span>
            <span class="span3">
             	<textarea rows="5" class="span4" placeholder="请输入支付公钥" name="payPublicKey" style="margin: 0px; height: 122px;">${entity.payPublicKey}</textarea>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">支付私钥</span>
            <span class="span3">
             	<textarea rows="5" class="span4" placeholder="请输入支付私钥" name="payPrivateKey" style="margin: 0px; height: 122px;">${entity.payPrivateKey}</textarea>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">支付密钥</span>
            <span class="span3">
                <input type="text" name="payKey" placeholder="请输入支付密钥" value="${entity.payKey}"/>
            </span>
        </div>
    </form>
</div>
<!--end-->