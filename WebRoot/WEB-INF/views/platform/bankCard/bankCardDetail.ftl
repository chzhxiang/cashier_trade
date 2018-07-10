<div id="content" class="pmbox">
    <div id="content-header">
        <div class="widget-title radius-3 w-pading">
            <h3 class="title">商户：${entity.merchantName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            	应用：${entity.appName} </h3>
            <div class="w-back">
		         <a href="javascript:backList();" class="a-back" style="margin-right: 10px;">返回</a>
		    </div>
        </div>
        
    </div>
    <div class="container-fluid">
        <div class="widget-content radius-3">
            <table class="table table-hover table-bordered table-striped order-detail">
                <thead>
                <tr>
                    <th>消费信息</th>
                    <th>明细内容</th>
                </tr>
                </thead>
                <tbody class="list-data">
                	<tr>
	                    <td>当月消费金额上限（元）</td>
	                    <td>${entity.monthlyQuotaByYuan}</td>
	                </tr>
                	<tr>
	                    <td>当日消费金额上限（元）</td>
	                    <td>${entity.dayQuotaByYuan}</td>
	                </tr>
                	<tr>
	                    <td>当笔消费金额上限（元）</td>
	                    <td>${entity.singleQuotaByYuan}</td>
	                </tr>
                	<tr>
	                    <td>今日消费总额</td>
	                    <td>${entity.payMoneyByCurDay}</td>
	                </tr>
                	<tr>
	                    <td>今日退款总额</td>
	                    <td>${entity.refundMoneyByCurDay}</td>
	                </tr>
                	<tr>
	                    <td>本月消费总额</td>
	                    <td>${entity.payMoneyByCurMonth}</td>
	                </tr>
                	<tr>
	                    <td>本月退款总额</td>
	                    <td>${entity.refundMoneyByCurMonth}</td>
	                </tr>
                	<tr>
	                    <td>银行卡余额（元）</td>
	                    <td>${entity.cardBalanceByYuan}</td>
	                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="container-fluid mt10">
        <div class="widget-content radius-3">
            <table class="table table-hover table-bordered table-striped order-detail">
                <thead>
	                <tr>
	                    <th>银行卡信息</th>
	                    <th>明细内容</th>
	                </tr>
                </thead>
                <tbody class="list-data">
                	<tr>
	                    <td>银行卡流水号</td>
	                    <td>${entity.cardSerialNo}</td>
	                </tr>
	                <tr>
	                    <td>银行卡号</td>
	                    <td>${entity.bankCardNoStr}</td>
	                </tr>
	                <tr>
	                    <td>发卡行</td>
	                    <td>${entity.bankCardName}</td>
	                </tr>
	                <tr>
	                    <td>开通状态</td>
	                    <td>
	                   	 	<#if entity.activateStatus == '0'>未开通</#if>
	                   	 	<#if entity.activateStatus == '1'>已开通</#if>
	                   	 	<#if entity.activateStatus == '2'>已解绑</#if>
	                   	 	<#if entity.activateStatus == '3'>余额不足</#if>
	                   	 	<#if entity.activateStatus == '4'>支付受限</#if>
	                    </td>
	                </tr>
	                <tr>
	                    <td>银行卡号预留手机号码</td>
	                    <td>${entity.cardPhone}</td>
	                </tr>
	                <tr>
	                    <td>证件类型</td>
	                    <td>
	                   	 	<#if entity.certificateType == '01'>二代身份证</#if>
	                   	 	<#if entity.certificateType == '02'>军官证</#if>
	                   	 	<#if entity.certificateType == '03'>护照</#if>
	                   	 	<#if entity.certificateType == '04'>回乡证</#if>
	                   	 	<#if entity.certificateType == '05'>台胞证</#if>
	                   	 	<#if entity.certificateType == '06'>警官证</#if>
	                   	 	<#if entity.certificateType == '07'>士兵证</#if>
	                   	 	<#if entity.certificateType == '08'>其它证</#if>
	                    </td>
	                </tr>
	                <tr>
	                    <td>证件号码</td>
	                    <td>${entity.certificateNoStr}</td>
	                </tr>
	                <tr>
	                    <td>绑卡姓名</td>
	                    <td>${entity.userName}</td>
	                </tr>
	                <tr>
	                    <td>绑卡时间</td>
	                    <td>${entity.createTimeVo}</td>
	                </tr>
	                <tr>
	                    <td>银行卡分类</td>
	                    <td>
	                   	 	<#if entity.bankCardType == '01'>借记卡</#if>
	                   	 	<#if entity.bankCardType == '02'>贷记卡</#if>
	                   	 	<#if entity.bankCardType == '03'>准贷记卡</#if>
	                   	 	<#if entity.bankCardType == '04'>社保卡</#if>
	                    </td>
	                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!--<div class="container-fluid mt10">
        <div class="widget-content radius-3">
            <h4>详细日志</h4>
            <div class="order-detail-log mt10">
	    		<#list entity.logs as s>
	               <p>IP：${s.ip}，HandleDate：${s.date?string('yyyy-MM-dd hh:mm:ss')}，HandleMsg：${s.log}</p>
	            </#list>
            </div>
        </div>
     </div>-->
  </div>

