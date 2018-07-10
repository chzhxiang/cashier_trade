<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">修改密码</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="widget-content radius-3">
        <form class="form-horizontal patient-info" id="setPasswordForm">
            <div class="control-group order-group">
                <span class="span1">原密码</span>
                <span class="span3">
                    <input type="password" name="oldPassword" placeholder="请输入原始密码">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">设置新密码</span>
                <span class="span3">
                    <input type="password" name="newPassword" placeholder="请输入6-16位新密码">
                </span>
            </div>
            <div class="control-group order-group">
                <span class="span1">确认新密码</span>
                <span class="span3">
                    <input type="password" name="confirmPassword" placeholder="请重新输入6-16位新密码">
                </span>
            </div>
        </form>
    </div>
    <div class="row-fluid mt10">
        <div class="button-style bgfff radius-3">
            <button class="btn btn-save" onclick="changePassword();">保存</button>
            <button class="btn btn-remove">取消</button>
        </div>
    </div>
</div>
 <script type="text/javascript" src="${tradeBasePath}/platform/js/security/user/user.js?v=${js_version}"></script>