
    <div id="content-header" >
            <div class="widget-title radius-3 w-pading">
                <h3 class="title">订单管理</h3>
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
	                            <input class="form-control" placeholder="选择时间段" type="text" value="" readonly="" name="startDate">
	                            <span class="input-group-addon">
	                            	<span class="glyphicon glyphicon-calendar"></span>
	                            </span>
	                    	</div>
	                    </span>
                    	<span class="span1">结束时间</span>
                    	<span class="span2">
	                        <div class="fl input-group date form_date col-md-5" id="endDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
	                            <input class="form-control" placeholder="选择时间段" type="text" value="" readonly="" name="endDate">
	                            <span class="input-group-addon">
	                            	<span class="glyphicon glyphicon-calendar"></span>
	                            </span>
	            			</div>
            			</span>
			            <span class="span1">商户名称</span>
			            <span class="span2">
			                <select name="merchantNo" id="merchan">
			                    <option value="">请选择</option>
			                    <#list merchant as m>  
									<option value="${m.merchantNo}">${m.merchantName}</option>
								</#list>
			                </select>
			            </span>
			            <span class="span1">应用名称</span>
			            <span class="span2">
			                <select name="appId" id="appName">
			                    <option  value="">请选择</option>
			                </select>
			            </span>
       				</div>
        			<div class="control-group order-group">
			            <span class="span1">商户订单号</span>
			            <span class="span2">
                        	<input type="text" placeholder="" name="outTradeNo">
                    	</span>
            			<span class="span1">平台订单号</span>
            			<span class="span2">
                        	<input type="text" placeholder="" name="tradeNo">
                    	</span>
			            <span class="span1">第三方订单号</span>
			            <span class="span2">
	                        <input type="text" placeholder="" name="agtTradeNo">
	                    </span>
			            <span class="span1">支付状态</span>
			            <span class="span2">
			                <select name="tradeStatus">
			                    <option value="">请选择</option>
			                    <option value="1">待支付</option>
			                    <option value="2">支付成功</option>
			                    <option value="3">退款成功</option>
			                    <option value="4">支付中</option>
			                    <option value="5">退费中</option>
			                    <option value="7">部分退费</option>
			                    <option value="6">关闭订单</option>
			                </select>
			            </span>
        			</div>
			        <div class="control-group order-group">
			            <span class="span1">支付渠道</span>
			            <span class="span2">
			                <select name="channelCode">
			                    <option value="">请选择</option>
			                    <#list channel as c>  
									<option value="${c.code}">${c.name}</option>
								</#list>
			                </select>
			            </span>
			            <span class="span1">扣费银行卡</span>
			            <span class="span2">
			                <select name="cardId">
			                    <option value="">请选择</option>
			                    <#list bankCard as b>  
									<option value="${b.id}">${b.bankCardNo}&nbsp;&nbsp;${b.userName}</option>
								</#list>
			                </select>
			            </span>
			            <span class="buttondiv ml25"><a href="javascript:search();" class="button-a">查询</a></span>
			            <span class="buttondiv"><a href="javascript:formReset();" class="button-a">重置</a></span>
			            <span class="buttondiv"><a href="javascript:openUrl(order.genExcel);" class="button-a">下载报表</a></span>
			        </div>
		        </form>
		    </div>
		    <div class="row-fluid radius-3 mt10">
		        <div class="auto">
		            <table class="table table-bordered table-textCenter table-striped table-hover">
		                <thead>
		                    <tr>
		                        <th>商户</th>
		                        <th>应用</th>
		                        <th>平台订单号</th>
		                        <th>商户订单号</th>
		                        <th>第三方订单号</th>
		                        <th>支付时间</th>
		                        <th>订单名称</th>
		                        <th>订单金额（元）</th>
		                        <th>实付金额（元）</th>
		                        <th>已退金额（元）</th>
		                        <th>实收金额（元）</th>
		                        <th>支付状态</th>
		                        <th>支付渠道</th>
		                    </tr>
		                </thead>
		                <tbody class="list-data">
		                    
		                </tbody>
		            </table>
		        </div>
		    </div>
    	<div class="pagination pagination-right pagination-style">       
    </div>
    <script type="text/javascript" src="${tradeBasePath}/platform/js/order/order.js?v=${js_version}"></script>
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