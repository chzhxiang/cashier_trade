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
            <span class="span1">商户号</span>
            <span class="span3">
                <input type="text" name="merchantNo" placeholder="请输入商户号" value="${entity.merchantNo}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">支付密钥</span>
            <span class="span3">
                <input type="text" name="paySecret" placeholder="请输入支付密钥" value="${entity.paySecret}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">APPID</span>
            <span class="span3">
                <input type="text" name="appId" placeholder="请输入APPID" value="${entity.appId}"/>
            </span>
        </div>
    </form>
</div>
<!--end-->