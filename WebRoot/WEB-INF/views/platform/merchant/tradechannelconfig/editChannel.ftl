<!--start-->
<div class="widget-content radius-3">
    <!--内容-->
    <input type="hidden" id="dbId" value="${entity.id}"/>
    <form class="form-horizontal patient-info" id="channelFrom">
        <div class="control-group order-group">
            <span class="span1">前台显示名称</span>
            <span class="span3">
                <input type="text" placeholder="前台显示名称" name="viewName" id="viewName" value="${entity.channelViewName}"/>
            </span>
        </div>
         <div class="control-group order-group label-normal" id="link_url">
            <span class="span1">支付跳转地址</span>
            <span class="span">
               <input type="radio" value="0" name="link_url" <#if entity.urlStatus == 0>checked</#if>/>
               <span>平台地址</span>
            </span>
            <span class="span">
               <input type="radio" value="1" name="link_url" <#if entity.urlStatus == 1>checked</#if>/>
               <span>商户地址</span>
            </span>
        </div>
    </form>
</div>
<!--end-->