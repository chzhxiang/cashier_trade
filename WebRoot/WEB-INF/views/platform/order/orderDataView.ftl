<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">订单总览</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="widget-content radius-3">
        <!--内容-->
        <form class="form-horizontal" id="form_search_id"> 
            <div class="control-group order-group worktime-info">
                <span class="span1">开始时间</span>
                <span class="span2">
                    <div class="fl input-group date form_date col-md-5" id="startDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                        <input class="form-control" placeholder="选择时间段" type="text" value="${params.startDate}" readonly="" name="startDate">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </span>
                <span class="span1">结束时间</span>
                <span class="span2">
                    <div class="fl input-group date form_date col-md-5" id="endDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                        <input class="form-control" placeholder="选择时间段" type="text" value="${params.endDate}" readonly="" name="endDate">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </span>
                <span class="span1">商户名称</span>
                <span class="span2">
                    <select name="merchantNo" id="merchan">
                        <option value="">请选择</option>
                        <#list merchant as m>  
				            <option value="${m.merchantNo}" <#if m.merchantNo==params.merchantNo>selected</#if>>${m.merchantName}</option>
						</#list>
                    </select>
                </span>
                <span class="span1">应用名称</span>
                <span class="span2">
                    <select name="appId" id="appName">
                        <option value="">请选择</option>
                        <#list application as a>  
							<option value="${a.appId}" <#if a.appId==params.appId>selected</#if>>${a.appName}</option>
						</#list>
                    </select>
                </span>
                <span class="buttondiv"><a href="javascript:search();" class="button-a">查询</a></span>
            </div>
            <div class="control-group order-group"></div>
        </form>
    </div>
    <div class="panel-title mt10"><h4>汇总数据</h4></div>
    <div class="row-fluid radius-3">
        <div class="auto">
            <table class="table table-bordered table-textCenter table-striped table-hover">
                <thead>
                    <tr>
                        <th>总交易金额</th>
                        <th>退款金额</th>
                        <th>成功交易金额</th>
                        <th>发起笔数</th>
                        <th>退款笔数</th>
                        <th>成功笔数</th>
                        <th>转化率</th>
                        <th>平均订单金额</th>
                    </tr>
                </thead>
                <tbody class="list-data">
                    <tr>
                        <td>${countTotalFree.total_free}</td>
                        <td>${countTotalFree.refund_free}</td>
                        <td>${countTotalFree.success_free}</td>
                        <td>${countTotalFree.sponsor_frequency}</td>
                        <td>${countTotalFree.refund_frequency}</td>
                        <td>${countTotalFree.success_frequency}</td>
                        <td>${countTotalFree.conversion}%</td>
                        <td>${countTotalFree.average_free}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="panel-title mt10">
        <h4>各商户交易详细数据</h4>
    </div>
    <div class="row-fluid radius-3">
        <div class="auto">
            <table class="table table-bordered table-textCenter table-striped table-hover">
                <thead>
                    <tr>
                        <th>商户</th>
                        <th>总交易金额</th>
                        <th>退款金额</th>
                        <th>成功交易金额</th>
                        <th>发起笔数</th>
                        <th>退款笔数</th>
                        <th>成功笔数</th>
                        <th>转化率</th>
                        <th>平均订单金额</th>
                    </tr>
                </thead>
                <tbody class="list-data">
                	<#list mechanTotalFreeList as mechanTotalFree>
                		<tr>
            				<td>${mechanTotalFree.merchantName}</td>
                         	<td>${mechanTotalFree.total_free}</td>
	                        <td>${mechanTotalFree.refund_free}</td>
	                        <td>${mechanTotalFree.success_free}</td>
	                        <td>${mechanTotalFree.sponsor_frequency}</td>
	                        <td>${mechanTotalFree.refund_frequency}</td>
	                        <td>${mechanTotalFree.success_frequency}</td>
	                        <td>${mechanTotalFree.conversion}%</td>
	                        <td>${mechanTotalFree.average_free}</td>
                		</tr>
                	</#list>
                </tbody>
            </table>
        </div>
    </div>
    <div class="panel-title mt10">
        <h4>各支付应用交易详细数据</h4>
    </div>
    <div class="row-fluid radius-3">
        <div class="auto">
            <table class="table table-bordered table-textCenter table-striped table-hover">
                <thead>
                    <tr>
                        <th>商户</th>
                        <th>应用</th>
                        <th>总交易金额</th>
                        <th>退款金额</th>
                        <th>成功交易金额</th>
                        <th>发起笔数</th>
                        <th>退款笔数</th>
                        <th>成功笔数</th>
                        <th>转化率</th>
                        <th>平均订单金额</th>
                    </tr>
                </thead>
                <tbody class="list-data">
                    <#list mechanApplicationTotalFreeList as mechanApplicationTotalFree>
                		<tr>
            				<td>${mechanApplicationTotalFree.merchantName}</td>
            				<td>${mechanApplicationTotalFree.appName}</td>
                         	<td>${mechanApplicationTotalFree.total_free}</td>
	                        <td>${mechanApplicationTotalFree.refund_free}</td>
	                        <td>${mechanApplicationTotalFree.success_free}</td>
	                        <td>${mechanApplicationTotalFree.sponsor_frequency}</td>
	                        <td>${mechanApplicationTotalFree.refund_frequency}</td>
	                        <td>${mechanApplicationTotalFree.success_frequency}</td>
	                        <td>${mechanApplicationTotalFree.conversion}%</td>
	                        <td>${mechanApplicationTotalFree.average_free}</td>
                		</tr>
                	</#list>
                </tbody>
            </table>
        </div>
    </div>
    <div class="panel-title mt10">
        <h4>各主渠道交易详细数据</h4>
    </div>
    <div class="row-fluid radius-3">
        <div class="auto">
            <table class="table table-bordered table-textCenter table-striped table-hover">
                <thead>
                    <tr>
                        <th>渠道</th>
                        <th>总交易金额</th>
                        <th>退款金额</th>
                        <th>成功交易金额</th>
                        <th>发起笔数</th>
                        <th>退款笔数</th>
                        <th>成功笔数</th>
                        <th>转化率</th>
                        <th>平均订单金额</th>
                    </tr>
                </thead>
                <tbody class="list-data">
                    <#list channelTotalFreeList as channelTotalFree>
                        <tr>
                            <td>${channelTotalFree.channelName}</td>
                            <td>${channelTotalFree.total_free}</td>
                            <td>${channelTotalFree.refund_free}</td>
                            <td>${channelTotalFree.success_free}</td>
                            <td>${channelTotalFree.sponsor_frequency}</td>
                            <td>${channelTotalFree.refund_frequency}</td>
                            <td>${channelTotalFree.success_frequency}</td>
                            <td>${channelTotalFree.conversion}%</td>
                            <td>${channelTotalFree.average_free}</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript" src="${tradeBasePath}/platform/js/order/orderDataView.js?v=${js_version}"></script>
<script>
    $('.form_date').datetimepicker({
        format: "yyyy-mm-dd",
        language: 'fr',
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });
</script>