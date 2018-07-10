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
                <input type="text" name="mchId" placeholder="请输入商户号" value="${entity.mchId}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">商户名称</span>
            <span class="span3">
                <input type="text" name="mchAccount" placeholder="请输入商户名称" value="${entity.mchAccount}"/>
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
            <span class="span1">商户密钥</span>
            <span class="span3">
                <input type="text" name="payPrivateKey" placeholder="请输入商户密钥" value="${entity.payPrivateKey}"/>
            </span>
        </div>
        <div class="control-group order-group">
            <span class="span1">商户密码</span>
            <span class="span3">
                <input type="text" name="payKey" placeholder="请输入商户密码" value="${entity.payKey}"/>
            </span>
        </div>
    </form>
</div>
<!--end-->