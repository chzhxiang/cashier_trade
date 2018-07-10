<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">订单总览</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="widget-content radius-3">
        <!--内容-->
        <form class="form-horizontal" id="searchForm" name="searchForm" action="${tradeBasePath}/order/orderStatistics"> 
            <div class="control-group order-group worktime-info">
                <span class="span1">开始时间</span>
                <span class="span2">
                    <div class="fl input-group date form_date col-md-5" id="startDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                        <input class="form-control" placeholder="选择时间段" type="text" value="${startDate}" readonly="" name="startDate">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </span>
                <span class="span1">结束时间</span>
                <span class="span2">
                    <div class="fl input-group date form_date col-md-5" id="endDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                        <input class="form-control" placeholder="选择时间段" type="text" value="${endDate}" readonly="" name="endDate">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </span>
                <span class="span1">商户名称</span>
                <span class="span2">
                    <select id="merchan" onchange="merchantsChange(this)">
                        <option value="">请选择</option>
                        <#list merchants as m>  
                            <option value='${m.merchantNo}' <#if m.merchantNo==merchantNo>selected</#if>>${m.merchantName}</option>
                        </#list>
                    </select>
                    <input type="hidden" value="${merchantNo}" name="merchantNo">
                </span>
                <span class="span1">应用名称</span>
                <span class="span2">
                    <select id="appName" onchange="applicationChange(this)">
                        <option value="">请选择</option>
                        <#list application as a>  
                            <option value="${a.appId}" <#if a.appId==appId>selected</#if>>${a.appName}</option>
                        </#list>
                    </select>
                    <input type="hidden" value="${appId}" name="appId">
                </span>
                <span class="buttondiv"><a href="javascript:searchStatistics();" class="button-a">查询</a></span>
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
                        <td>${statistics.totalPrice}</td>
                        <td>${statistics.refundPrice}</td>
                        <td>${statistics.payPrice}</td>
                        <td>${statistics.launchCount}</td>
                        <td>${statistics.refundCount}</td>
                        <td>${statistics.payCount}</td>
                        <td>${statistics.conversion}%</td>
                        <td>${statistics.averagePrice}</td>
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
                    <#list merchantStatistics as ms>
                        <tr>
                            <td>${ms.merchantName}</td>
                            <td>${ms.totalPrice}</td>
                            <td>${ms.refundPrice}</td>
                            <td>${ms.payPrice}</td>
                            <td>${ms.launchCount}</td>
                            <td>${ms.refundCount}</td>
                            <td>${ms.payCount}</td>
                            <td>${ms.conversion}%</td>
                            <td>${ms.averagePrice}</td>
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
                    <#list applicationsStatistics as acs>
                        <tr>
                            <td>${acs.merchantName}</td>
                            <td>${acs.appName}</td>
                            <td>${acs.totalPrice}</td>
                            <td>${acs.refundPrice}</td>
                            <td>${acs.payPrice}</td>
                            <td>${acs.launchCount}</td>
                            <td>${acs.refundCount}</td>
                            <td>${acs.payCount}</td>
                            <td>${acs.conversion}%</td>
                            <td>${acs.averagePrice}</td>
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
                    <#list channelStatistics as cs>
                        <tr>
                            <td>${cs.channelName}</td>
                            <td>${cs.totalPrice}</td>
                            <td>${cs.refundPrice}</td>
                            <td>${cs.payPrice}</td>
                            <td>${cs.launchCount}</td>
                            <td>${cs.refundCount}</td>
                            <td>${cs.payCount}</td>
                            <td>${cs.conversion}%</td>
                            <td>${cs.averagePrice}</td>
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