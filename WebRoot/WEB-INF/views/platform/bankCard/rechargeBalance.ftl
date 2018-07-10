<div id="content-list">
	<div id="content-header">
	    <div class="widget-title radius-3 w-pading">
	        <h3 class="title">银行卡充值</h3>
	    </div>
	</div>
	<div class="container-fluid">
	    <div class="row-fluid radius-3 mt10">
	        <div class="auto">
	            <table class="table table-bordered table-textCenter table-striped table-hover" id="myTable">
	                <thead>
	                    <tr>
	                        <th>商户</th>
	                        <th>应用</th>
	                        <th>银行名称</th>
	                        <th>银行卡号</th>
	                        <th>持卡人姓名</th>
	                        <th>银行卡余额</th>
	                        <th>当前状态</th>
	                        <th>充值金额（元）</th>
	                        <th>记账日期</th>
	                    </tr>
	                </thead>
	                <tbody class="list-data">
	                	<#list bankCards as bankCard>
	                		<tr>
	                			<input type="hidden" name="id" value="${bankCard.id}"/>
	                			<td class="merchantName">${bankCard.merchantName}</td>
	                			<td class="appName">${bankCard.appName}</td>
	                			<td class="bankCardName">${bankCard.bankCardName}</td>
	                			<td class="bankCardNo">${bankCard.bankCardNoStr}</td>
	                			<td class="userName">${bankCard.userName}</td>
	                			<td class="cardBalanceByYuan">${bankCard.cardBalanceByYuan}</td>
	                			<td class="activateStatusVo">${bankCard.activateStatusVo}</td>
	                			<td>
	                				<input style="height:34px;width:80px" type="text" name="rechargeBalance" value="0" id="rechargeBalance" />
	                			</td>
	                			<td style="width: 210px">
		                			<div class="fl input-group date form_date col-md-5" id="recordDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
				                        <input class="form-control input-small" placeholder="选择时间段" type="text" value="${bankCard.recordDate}" readonly="" name="recordDate">
				                        <span class="input-group-addon" style="left:50px;">
				                        	<span class="glyphicon glyphicon-calendar"></span>
				                		</span>
				        			</div>
			        			</td>
	                		</tr>
	                	</#list>
	                </tbody>
	            </table>
	        </div>
	    </div>
	</div>
	<div class="row-fluid mt10">
	 	<div class="button-style bgfff radius-3">
  			<button class="btn btn-save" onClick="saveRechargeBalance()">保存</button>    
  			<button class="btn btn-remove" onClick="openBankUrl('bankCard/bankCardList')">取消</button>
		</div>
	 </div>
</div>
<script>
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
    
    function saveRechargeBalance(){
    	var array = "";
		$('.list-data tr').each(function() {
		    var id = $(this).find("input[name='id']").val();
		    var rechargeBalance = $(this).find("input[name='rechargeBalance']").val();
		    var json = "{_id:'" + id + "',rechargeBalance:'"+rechargeBalance+"'}";
		    array += "," + json;
		});
		array = "[" + array.substring(1) + "]";
    	
    	$.ajax({
    		type: "POST",
    		url: tradeBasePath + "/bankCard/saveRechargeBalance",
    		data: {
				"array": array
			},
			success: function(data) {
				if (data.status == "OK") {
					$Y.tips("操作成功");
		            var returnurl = "bankCard/bankCardList";
		            openBankUrl(returnurl);
				}else{
					$Y.tips(data.message);
				}
			}
    	});
    }
</script>