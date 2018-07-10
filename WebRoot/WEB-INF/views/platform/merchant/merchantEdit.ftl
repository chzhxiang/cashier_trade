<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">修改商户</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="panel-title">
        <h4>企业主体信息</h4>
    </div>
    <div class="widget-content radius-3">
        <form class="form-horizontal patient-info">
        	<input type="hidden" id="id" value="${entity.id}">
        	<input type="hidden" id="merchantNo" value="${entity.merchantNo}">
        	<input type="hidden" id="status" value="${entity.status}">
            <div class="control-group order-group">
                <span class="span1"><span style="color:red;">*</span>企业名称</span>
                <span class="span3">
                    <input type="text" placeholder="请输入企业名称" id="merchantName" value="${entity.merchantName}">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1"><span style="color:red;">*</span>企业联系地址</span>
                <span class="span3">
                    <input type="text" placeholder="请输入企业联系地址" id="merchantAddress" value="${entity.merchantAddress}">
                </span>
            </div>
        </form>
    </div>
    <div class="panel-title mt10">
        <h4>联系人信息</h4>
    </div>
    <div class="widget-content radius-3">
        <form class="form-horizontal patient-info">
            <div class="control-group order-group">
                <span class="span1"><span style="color:red;">*</span>联系人姓名</span>
                <span class="span3">
                    <input type="text" placeholder="请输入联系人姓名" id="linkmanName" value="${entity.linkmanName}">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1"><span style="color:red;">*</span>联系人电话</span>
                <span class="span3">
                    <input type="text" placeholder="请输入联系人电话" id="mobile" value="${entity.mobile}">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">联系人QQ</span>
                <span class="span3">
                    <input type="text" placeholder="请输入联系人QQ" id="qq" value="${entity.qq}">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">联系人邮箱</span>
                <span class="span3">
                    <input type="text" placeholder="请输入联系人邮箱" id="email" value="${entity.email}">
                </span>
            </div>
        </form>
    </div>
    <div class="row-fluid mt10">
        <div class="button-style bgfff radius-3">
            <button class="btn btn-save" onclick="save()">保存</button>
            <button class="btn btn-remove" onclick="openUrl('merchant/list')">取消</button>
        </div>
    </div>
</div>