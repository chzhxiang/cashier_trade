<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">添加银行卡</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="panel-title">
        <h4>银行卡信息</h4>
    </div>
    <div class="widget-content radius-3">
        <form class="form-horizontal patient-info" id="cardForm">
        	<div class="control-group order-group">
	    	    <span class="span1">
	        		<span style="color:red;">*</span>商户名称
	        	</span>
	            <span class="span3">
	                <select name="merchantNo" id="merchantNo">
	                    <option value="">请选择</option>
	                    <#list merchant as m>  
							<option value="${m.merchantNo}">${m.merchantName}</option>
						</#list>
	                </select>
	            </span>
	        </div>
	        <div class="control-group order-group">
	            <span class="span1">
	                <span style="color:red;">*</span>应用名称
	            </span>
	            <span class="span3">
	                <select name="appId" id="appId">
	                    <option  value="">请选择</option>
	                </select>
	            </span>
	        </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>证件类型
                </span>
            	<span class="span3">
                    <select name="certificateType" id="certificateType">
                        <option value="01">身份证</option>
                        <option value="02">军官证</option>
                        <option value="03">护照</option>
                        <option value="04">回乡证</option>
                        <option value="05">台胞证</option>
                        <option value="06">警官证</option>
                        <option value="07">士兵证</option>
                        <option value="08">其它证</option>
                    </select>
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>证件号码
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入证件号码" name="certificateNo" id="certificateNo">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>用户姓名
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入用户姓名" name="userName" id="userName">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>设备ID
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入设备ID" name="deviceId" id="deviceId" value="000000">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>用户ID
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入用户ID" name="accountId" id="accountId" value="default">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>银行卡号
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入银行卡号" name="bankCardNo" id="bankCardNo">
                </span>
            </div>
             <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>银行名称
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入银行名称" name="bankCardName" id="bankCardName">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>银行卡余额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入银行卡余额" name="cardBalance" id="cardBalance">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单笔限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单笔限额" name="singleQuota" id="singleQuota">
                </span>
            </div>
             <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单日限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单日限额" name="dayQuota" id="dayQuota">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单月限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单月限额" name="monthlyQuota" id="monthlyQuota">
                </span>
            </div>
             <input name="channelCode" id="channelCode" type="hidden" value="unionpay_small_quick" />
        </form>
    </div>
    <div class="row-fluid mt10">
        <div class="button-style bgfff radius-3">
            <button class="btn btn-save" onclick="save()">保存</button>
            <button class="btn btn-remove" onclick="openBankUrl('bankCard/bankCardList')">取消</button>
        </div>
    </div>
</div>
    <script>
        $(function(){
			$("#merchantNo").on("change",function(){
				$.ajax({
					type: "POST",
					url:  "${tradeBasePath}/bankCard/getApplicationByMerchan",
					data: {
						"merchantNo": $(this).val()
					},
					success: function(response) {
						if (response.status == "OK") {
							//alert(response.result.length);
							var list = response.result;
							var html = "<option  value=''>请选择</option>";
							for(var i = 0;i<list.length;i++){
								html = html + "<option value='"+list[i].appId+"'>"+list[i].appName+"</option>"
							}
							$("#appId").html(html);
						}else{
							$("#appId").html("<option  value=''>请选择</option>");
						}
					}
				});
			});
		});
    </script>