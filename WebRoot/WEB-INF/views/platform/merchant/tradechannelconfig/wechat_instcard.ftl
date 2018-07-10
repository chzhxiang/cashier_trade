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
            <span class="span1">公众号ID</span>
            <span class="span3">
                <input type="text" name="appId" placeholder="请输公众号ID" value="${entity.appId}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">商户号</span>
            <span class="span3">
                <input type="text" name="mchId" placeholder="请输入商户号" value="${entity.mchId}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">支付密钥</span>
            <span class="span3">
                <input type="text" name="paySecret" placeholder="请输入支付密钥" value="${entity.paySecret}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">证书位置</span>
             <div class="controls controls-select controls-select-min">
                <div class="btn-save btn-upload fl">
                    <div id="fileBlock">选择文件
                       <input class="fileupload" type="file" id="uploadFile" name="uploadFile" 
                            onchange="uploadTradeFile(this,'<#if channelCode != '' && channelCode != null>add_channelFrom<#else>update_channelFrom</#if>','certificatePath');">
                    </div>
                </div>
                <input type="text" class="span2" name="certificatePath" value="${entity.certificatePath}">
            </div>    
        </div>
        <div class="control-group order-group">
            <span class="span1">appSecret</span>
            <span class="span3">
                <input type="text" name="secret" placeholder="请输入appSecret" value="${entity.secret}"/>
            </span>
        </div>
    </form>
</div>
<!--end-->