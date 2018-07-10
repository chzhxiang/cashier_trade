<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">商户：${entity.merchantName}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	应用：${entity.appName} </h3>
    </div>
</div>
<div class="container-fluid">
	<div class="panel-title">
        <h4>银行卡信息</h4>
    </div>
    <div class="widget-content radius-3">
    	<form class="form-horizontal patient-info">
    		<input type="hidden" id="id" value="${entity.id}">
        	<input type="hidden" id="appId" value="${entity.appId}">
        	<input type="hidden" id="bankCardNo" value="${entity.bankCardNo}">
            <div class="control-group order-group">
                <span class="span1">银行卡余额</span>
                <span class="span3">
                    ${entity.cardBalanceByYuan}
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单笔限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单笔限额" name="singleQuota" id="singleQuota" value="${entity.singleQuotaByYuan}">
                </span>
            </div>
             <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单日限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单日限额" name="dayQuota" id="dayQuota" value="${entity.dayQuotaByYuan}">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">
                	<span style="color:red;">*</span>单月限额
                </span>
                <span class="span3">
                    <input type="text" placeholder="请输入单月限额" name="monthlyQuota" id="monthlyQuota" value="${entity.monthlyQuotaByYuan}">
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
        </form>
    </div>
    <div class="row-fluid mt10">
        <div class="button-style bgfff radius-3">
            <button class="btn btn-save" onclick="editBankCard()">保存</button>
            <button class="btn btn-remove" onclick="openBankUrl('bankCard/bankCardList')">取消</button>
        </div>
    </div>
</div>

