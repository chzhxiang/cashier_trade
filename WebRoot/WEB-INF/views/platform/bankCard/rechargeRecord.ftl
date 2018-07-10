<div id="content" class="pmbox">
    <div id="content-header">
        <div class="widget-title radius-3 w-pading">
            <h3 class="title">银行卡号：${entity.bankCardNoStr}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	用户名：${entity.userName} </h3>
            <div class="w-back">
		         <a href="javascript:backList();" class="a-back" style="margin-right: 10px;">返回</a>
		    </div>
        </div>
        
    </div>
    <div class="container-fluid mt10">
        <div class="widget-content radius-3">
            <h4>详细日志</h4>
            <div class="order-detail-log mt10">
	    		<#list entity.logs as s>
	               <p>IP：${s.ip}，HandleDate：${s.date?string('yyyy-MM-dd HH:mm:ss')}，HandleMsg：${s.log}</p>
	            </#list>
            </div>
        </div>
     </div>
  </div>

