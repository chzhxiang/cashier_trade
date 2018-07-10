<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">商户列表</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="operation-button operation-button-order w-pading">
        <a href="javascript:openUrl('merchant/toAdd')" class="operation-button-a">添加商户</a>
    </div>
    <div class="widget-content radius-3">
        <!--内容-->
        <form class="form-horizontal">
            <div class="control-group order-group worktime-info">
                <span class="span1">商户名称</span>
                <span class="span2">
                <input type="text" placeholder="请输入商户名称" name="merchantName" id="merchantName">
            </span>
                <span class="span1">联系人姓名</span>
                <span class="span2">
                <input type="text" placeholder="请输入联系人姓名" name="linkmanName" id="linkmanName">
            </span>
                <span class="span1">联系人电话</span>
                <span class="span2">
                <input type="text" placeholder="请输入联系人电话" name="mobile" id="mobile">
            </span>
                <span class="buttondiv ml25"><a href="javascript:search()" class="button-a">查询</a></span>
            </div>
        </form>
    </div>
    <div class="row-fluid radius-3 mt10">
        <div class="auto">
            <table class="table table-bordered table-textCenter table-striped table-hover">
                <thead>
                    <tr>
                        <th></th>
                        <th>序号</th>
                        <th>商户</th>
                        <th>企业联系地址</th>
                        <th>联系人姓名</th>
                        <th>联系人电话</th>
                        <th>联系人QQ</th>
                        <th>联系人邮箱</th>
                        <th>状态</th>
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
<script type="text/javascript" src="${tradeBasePath}/platform/js/merchant/merchant.js?v=${js_version}"></script>