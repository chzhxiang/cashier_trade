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
                    <th>订单信息</th>
                    <th>明细内容</th>
                </tr>
                </thead>
                <tbody class="list-data">
                <tr>
                    <td>商品名称</td>
                    <td>${entity.subject}</td>
                </tr>
                <tr>
                    <td>平台订单号</td>
                    <td>${entity.tradeNo}</td>
                </tr>
                <tr>
                    <td>商户订单号</td>
                    <td>${entity.outTradeNo}</td>
                </tr>
                <tr>
                    <td>第三方订单号</td>
                    <td>${entity.agtTradeNo}</td>
                </tr>
                <tr>
                    <td>订单生成时间</td>
                    <td>${entity.createTimeVo}</td>
                </tr>
                <tr>
                    <td>订单支付时间</td>
                    <td>${entity.payTimeVo}</td>
                </tr>
                <tr>
                    <td>订单金额</td>
                    <td>${entity.totalFeeVo}</td>
                </tr>
                <tr>
                    <td>订单状态</td>
                    <td>
                   	 	<#if entity.tradeStatus == '1'>待支付</#if>
                   	 	<#if entity.tradeStatus == '2'>支付成功</#if>
                   	 	<#if entity.tradeStatus == '3'>退款成功</#if>
                   	 	<#if entity.tradeStatus == '4'>支付中</#if>
                   	 	<#if entity.tradeStatus == '5'>退费中</#if>
                   	 	<#if entity.tradeStatus == '6'>支付取消</#if>
                    </td>
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
                    <th>交易信息</th>
                    <th>明细内容</th>
                </tr>
                </thead>
                <tbody class="list-data">
                <tr>
                    <td>第三方订单号</td>
                    <td>${entity.agtTradeNo}</td>
                </tr>
                <tr>
                    <td>订单支付渠道</td>
                    <td>${entity.channelName}</td>
                </tr>
                <tr>
                    <td>系统跟踪号</td>
                    <td>${entity.traceNo}</td>
                </tr>
                <tr>
                    <td>银行卡号</td>
                    <td>${entity.bankCard.bankCardNo}</td>
                </tr>
                <tr>
                    <td>卡姓名</td>
                    <td>${entity.bankCard.userName}</td>
                </tr>
                <tr>
                    <td>订单支付时间</td>
                    <td>${entity.payTimeVo}</td>
                </tr>
                <tr>
                    <td>订单金额</td>
                    <td>${entity.totalFeeVo} 元</td>
                </tr>
                <tr>
                    <td>实付金额</td>
                    <td>${entity.actualPayFreeVo} 元</td>
                </tr>
                <tr>
                    <td>实收金额</td>
                    <td>${entity.actualGetFreeVo} 元</td>
                </tr>
                <tr>
                    <td>退款金额</td>
                    <td>${entity.refundFeeVo} 元</td>
                </tr>
                <tr>
                    <td>退款跟踪号</td>
                    <td>----</td>
                </tr>
                <tr>
                    <td>退款订单号</td>
                    <td>${entity.refundNo}</td>
                </tr>
                <tr>
                    <td>退款时间</td>
                    <td>${entity.refundTimeVo}</td>
                </tr>
                </tbody>
            </table>
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

