<#include "/platform/common/common.ftl">
<div id="content-list">
	<div id="content-header">
	    <div class="widget-title radius-3 w-pading">
	        <h3 class="title">银行卡管理</h3>
	    </div>
	</div>
	<div class="container-fluid">
		<div class="operation-button operation-button-order w-pading">
	        <a href="javascript:openBankUrl('bindCard/toCard')" class="operation-button-a">添加银行卡</a>
	        <a href='javascript:void(0);' onclick=balanceSetting() class="operation-button-a">余额阀值设置</a>
	        <a href='javascript:void(0);' onclick=amountLimit() class="operation-button-a">当日消费金额上限阀值设置</a>
	        <a href="javascript:rechargeBalance();" class="operation-button-a">充值</a>   
	       	 银行卡余额：${allCardBalance}元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;今日消费金额：  ${totalPrice}元
	    </div>
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
	    			<span class="span1">银行卡号</span>
		            <span class="span2">
                    	<input type="text" placeholder="请输入银行卡号" name="bankCardNo">
                	</span>
                	<span class="span1">持卡人姓名</span>
		            <span class="span2">
                    	<input type="text" placeholder="请输入持卡人姓名" name="userName">
                	</span>
				</div>
		        <div class="control-group order-group">
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
		            <span class="span1">开通状态</span>
		            <span class="span2">
		                <select name="activateStatus">
		                    <option value="">请选择</option>
		                    <option value="1">使用中</option>
		                    <option value="2">余额不足</option>
		                    <option value="3">支付受限</option>
		                    <option value="4">停止使用</option>
		                    <option value="5">待开通</option>
		                </select>
					</span>
                	<span class="buttondiv ml25"><a href="javascript:search();" class="button-a">查询</a></span>
			        <span class="buttondiv"><a href="javascript:formReset();" class="button-a">重置</a></span>
		        </div>
			</form>
		</div>
	    <div class="row-fluid radius-3 mt10">
	        <div class="auto">
	            <table class="table table-bordered table-textCenter table-striped table-hover">
	                <thead>
	                    <tr>
	                    	<th width="60"><input id="checkAll" onclick="selectAll()" type="checkbox"></th>
	                    	<th width="60" >序号</th>
	                        <th>商户</th>
	                        <th>应用</th>
	                        <th>银行名称</th>
	                        <th>银行卡号</th>
	                        <th>持卡人姓名</th>
	                        <th>银行预留手机号</th>
	                        <th>银行卡余额</th>
	                        <th>当前状态</th>
	                        <th>开通时间</th>
	                        <th>操作</th>
	                    </tr>
	                </thead>
	                <tbody class="list-data">
	                </tbody>
	            </table>
	        </div>
	    </div>
		<div class="pagination pagination-right pagination-style">
	</div>
</div>

 <!--阀值设置 begin-->
<div id="configAddModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    
    <div class="modal-body">
        <!--start-->
        
        <!--end-->
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
        <button class="btn btn-primary" onclick="saveRule();">保存</button>
    </div>
</div>
<!--阀值设置 end-->


<script type="text/javascript" src="${tradeBasePath}/platform/js/bankCard/bankCard.js?v=${js_version}"></script>
<script>
	//余额阀值设置
    function balanceSetting() {
    	$("#configAddModal").modal({
			remote: tradeBasePath + "/bankCard/thresholdSetting"
		});
        $('#configAddModal').modal('show');
    }
    
    //当日消费金额上限设置
    function amountLimit() {
    	$("#configAddModal").modal({
			remote: tradeBasePath + "/bankCard/amountLimitSetting"
		});
        $('#configAddModal').modal('show');
    }
    
	$("#configAddModal").on("hidden", function() {  
	    $(this).removeData("modal"); 
	});
	
    $(function(){
    	$('body').css('background-color','#e8e8e8');
    	$('body').css('overflow-x','hidden');
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
    });
</script>