<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">商户管理</h3>
    </div>
</div>
<!--<input type="hidden" id="merchantNo" value="${merchantNo}">-->
<div class="container-fluid">
    <div class="operation-button operation-button-order w-pading">
        <a href="javascript:void(0);" class="operation-button-a" onclick="addAppAccount();">创建新的支付应用</a>
    </div>
    <div class="widget-content radius-3">
        <!--内容-->
        <form class="form-horizontal">
            <div class="control-group order-group worktime-info">
                <span class="span1">应用名称</span>
	                <span class="span2">
	                <select id="applicationSel">
	                </select>
	            </span>
                <span class="buttondiv ml25"><a href="javascript:void(0)" class="button-a" onclick="searchApp();">查询</a></span>
            </div>
        </form>
    </div>
    <div class="row-fluid radius-3 mt10" id="applicationInfo">
        <div class="app-setting-content">
            <!--tab begin-->
            <ul class="nav nav-tabs clearfix" id="settingTab">
                <li class="active"><a href="#basicInfo">基本信息设置</a></li>
                <li><a href="#payWaysParam">支付渠道参数设置</a></li>
                <li><a href="#waysOrder">渠道排序</a></li>
            </ul>
            <!--tab end-->
            <!--tab content begin-->
            <div class="tab-content">
                <div class="tab-pane active" id="basicInfo">
                    <!--basicInfo begin-->
                    <form class="form-horizontal patient-info" id="basicInfoForm">
                    	<input type="hidden" name="id">
                    	<input type="hidden" name="merchantNo">
                        <div class="control-group order-group">
                            <span class="span1">商户号</span>
                            <span class="span6">
                                <input type="text" readonly="readonly" value="${merchantNo}" id="merchantNo">
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyMerchantNo" data-clipboard-text="">复制</a></span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">应用名称</span>
                            <span class="span6">
                                <input type="text" placeholder="请输入应用名称" name="appName">
                             </span>
                            <!--<span><a href="javascript:void(0);" class="copy-btn">保存</a></span>-->
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">项目备注</span>
                            <span class="span6">
                                <input type="text" placeholder="请输入项目备注" name="comment">
                            </span>
                            <!--<span><a href="javascript:void(0);" class="copy-btn">保存</a></span>-->
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">时间检验</span>
                            <span class="span6">
                                阳光会对每次请求做时间校验，这个开关控制是否开启时间戳校验（默认开启）
                            </span>
                            <span class="icon-off-btn">
                                <a class="iosCheckF" onclick="checkValue()">
                                    <b class="iosCheck"><input id="validateTime" type="hidden" name="validateTime" value="1"><input id="checkValue" type="checkbox"><i class="active"></i></b>
                                </a>
                            </span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">APP ID</span>
                            <span class="span6">
                                <input type="text" readonly="readonly" name="appId" id="appId">
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyAppId" data-clipboard-text="">复制</a></span>
                        </div>
                         <div class="control-group order-group">
                            <span class="span1">商户公钥</span>
                            <span class="span6">
                                <!--<input type="text" readonly="readonly" name="publicKey" id="publicKey">-->
                                <textarea rows="5" class="span12"  name="publicKey" id="publicKey" len="1000" style="margin: 0px; height: 122px;"></textarea>
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyPublicKey" data-clipboard-text="">复制</a></span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">平台公钥</span>
                            <span class="span6">
                                <!--<input type="text" readonly="readonly" name="ygkzPublicKey" id="ygkzPublicKey">-->
                           		 <textarea rows="5" class="span12"  readonly="readonly" name="appPublicKey" id="appPublicKey" len="1000" style="margin: 0px; height: 122px;"></textarea>
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyygkzPublicKey" data-clipboard-text="">复制</a></span>
                        </div>
                        
                        <!-- 
                        <div class="control-group order-group">
                            <span class="span1"><strong>敏感数据操作区</strong></span>
                            <span class="span6">
                                secret是一个非常重要的数据，请您必须小心谨慎的确保此数据保存在足够安全的地方。您从阳光官方获得此数据的同时，即表明您保证不会被用于非法用途和不会在没有得到您授权的情况下被盗用，一旦因此数据保管不善而导致的经济损失及法律责任，均由您独自承担。
                            </span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">APP Secret</span>
                            <span class="span6">
                                <input type="text" readonly="readonly" name="appSecret" id="appSecret">
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyAppSecret" data-clipboard-text="">复制</a></span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1">Master Secret</span>
                            <span class="span6">
                                <input type="text" readonly="readonly" name="masterSecret" id="masterSecret">
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn" id="copyMasterSecret" data-clipboard-text="">复制</a></span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1 line-height-reset">重置secret<br/><i class="color-red">（谨慎操作）</i></span>
                            <span class="span6">
                                如果您担心您的secret已经泄露，或者出现异常，您可以替换secret， 替换后您需要同步替换您应用程序中使用的secret，否则您的应用将无法使用，请谨慎操作
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn btn-danger">重置secret</a></span>
                        </div>
                        <div class="control-group order-group">
                            <span class="span1 line-height-reset">重置Master Secret<br/><i class="color-red">（谨慎操作）</i></span>
                            <span class="span6">
                                如果您是APP类应用的用户，app secret在APP中无法重置，则可以单独重置master secret来确保支付结果的安全性，重置后您需要同步替换您程序中使用的master secret，否则您的程序将无法使用，请谨慎操作
                            </span>
                            <span><a href="javascript:void(0);" class="copy-btn btn-danger">重置Master Secret</a></span>
                        </div>
                         -->
                    </form>
                    <div class="row-fluid mt10">
                        <div class="button-style bgfff radius-3">
                            <button class="btn btn-save" onclick="saveMerchantApplication('saveBasicInfo')">保存</button>
                            <button class="btn btn-remove" onclick="openUrl('merchant/list')">取消</button>
                        </div>
                    </div>
                    <!--basicInfo end-->
                </div>
                <div class="tab-pane" id="payWaysParam">
                    <!--支付渠道参数设置 begin-->
                    <div class="pay-ways-param">
                        <div class="pmbox">
                            <div class="widget-content radius-3">
                                <form class="form-horizontal">
                                    <div class="control-group order-group worktime-info">
                                        <span class="span1">应用场景</span>
                                        <span class="span2">
                                            <select>
                                                <option>全部</option>
                                                <option>pc网页</option>
                                                <option>移动网页</option>
                                                <option>APP</option>
                                                <option>线下收款</option>
                                                <option>微信公共号</option>
                                                <option>境外外卡支付</option>
                                                <option>企业打款</option>
                                            </select>
                                        </span>
                                        <span class="span1">配置状态</span>
                                        <span class="span2">
                                            <select>
                                                <option>全部</option>
                                                <option value="1">已配置</option>
                                                <option value="0">未配置</option>
                                            </select>
                                        </span>
                                        <span class="buttondiv ml25"><a href="javascript:void(0);" class="button-a">查询</a></span>
                                    </div>
                                </form>
                            </div>
                            <hr/>
                            <div class="row-fluid radius-3 mt10">
                                <div class="auto">
                                    <table class="table table-bordered table-textCenter table-striped table-hover" id="tradeChannelTable">
                                        <thead>
                                            <tr>
                                                <th>支付渠道</th>
                                                <th>渠道编码</th>
                                                <th>应用场景</th>
                                                <th>结算周期</th>
                                                <th>配置状态</th>
                                            </tr>
                                        </thead>
                                        <tbody class="list-data">
                                            
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!--支付渠道参数设置 end-->
                </div>
                <div class="tab-pane" id="waysOrder">
                    <!--渠道排序 begin-->
                    <div class="way-order clearfix">
                        <div class="way-order-box">
                            <div class="way-order-config-hd">
                                已配置渠道
                            </div>
                            <div class="way-order-config-content" id="configWays">
                                
                            </div>
                        </div>
                        <div class="img-exchange-box fl">
                            <img class="img-exchange" src="${tradeBasePath}/platform/images/exchange.png">
                        </div>
                        <div class="way-order-box">
                            <div class="way-order-open-hd">
                                已开启渠道
                            </div>
                            <div class="way-order-config-content way-order-open-content" id="openWays">
                                
                            </div>
                        </div>
                    </div>
                    <div class="row-fluid mt30">
                        <div class="button-style bgfff radius-3">
                            <button class="btn btn-save" onclick="updateApplicationChannel()">保存</button>
                            <button class="btn btn-remove" onclick="openUrl('merchant/list')">取消</button>
                        </div>
                    </div>
                    <!--渠道排序 end-->
                </div>
            </div>
            <!--tab content end-->
        </div>
    </div>
</div>

<!--创建应用 begin-->
<div id="addAppModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">创建应用</h3>
    </div>
    <div class="modal-body">
        <!--start-->
        <div class="widget-content radius-3">
            <!--内容-->
            <form class="form-horizontal patient-info">
                <div class="control-group order-group">
                    <span class="span4 ml0">
                        <input type="text" placeholder="请输入应用名称" name="appName" id="appName" />
                    </span>
                </div>
                <p>应用名称为1～16位大小写字母、数字、汉字、下划线，或组合。</p>
            </form>
        </div>
        <!--end-->
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
        <button class="btn btn-primary" onclick="saveMerchantApplication('saveName')">保存</button>
    </div>
</div>
<!--创建应用 end-->

<!--配置状态 新增配置 begin-->
<div id="configAddModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="configAddModalLabel">医保支付渠道参数设置</h3>
    </div>
    <div class="modal-body">
        <!--start-->
        
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
        <button class="btn btn-primary" onclick="saveMerchantApplicationChannel('saveName')">保存</button>
    </div>
</div>
<!--配置状态 新增配置 end-->

<!--配置状态 修改配置 begin-->
<div id="configEditModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="configUpdateModalLabel">医保支付渠道参数设置</h3>
    </div>
    <div class="modal-body">
        <!--start-->
        
        <!--end-->
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
        <button class="btn btn-primary" onclick="updateMerchantApplicationChannel();">保存</button>
    </div>
</div>
<!--配置状态 修改配置 end-->

<!-- 修改支付渠道信息-->
<div id="editChannelModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="editChannelModalLabel">创建账号</h3>
    </div>
    <div class="modal-body">
        <!--start-->
        
        <!--end-->
    </div>
    <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
        <button class="btn btn-primary" onclick="editApplicationChannel();">保存</button>
    </div>
</div>
<!-- Modal end-->
<script type="text/javascript" src="${tradeBasePath}/platform/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="${tradeBasePath}/platform/lib/clipboard/clipboard.min.js"></script>
<script type="text/javascript" src="${tradeBasePath}/platform/js/merchant/merchantSetting.js?v=${js_version}"></script>
<script>
    var $configLi, $openLi, configLiAccount, openLiAccount;
    var orderArr = []; //排序结果数组
    var orderJsonArr = []; //排序结果对应json
    $(function() {
        //tab
        $('#settingTab a').click(function(e) {
            e.preventDefault();
            $(this).tab('show');
        });
    });


    //创建新应用弹窗
    function addAppAccount() {
        $('#addAppModal').modal('show');
    }

    //参数设置弹窗操作：新增
    //configAddModal
    function configAdd(channelCode,channelName) {
    	$("#configAddModalLabel").text(channelName + " 参数配置");
	    $("#configAddModal").modal({
			remote: tradeBasePath + "/tradeChannel/toConfig?channelCode=" + channelCode
		});
        $('#configAddModal').modal('show');
    }
    $("#configAddModal").on("hidden", function() {  
	    $(this).removeData("modal"); 
	});  

    //修改：configEditModal
    function configEdit(channelCode,channelName,appId) {
    	$("#configUpdateModalLabel").text(channelName + " 参数修改");
    	$("#configEditModal").modal({
			remote: tradeBasePath + "/tradeChannel/toUpdateConfig?channelCode=" + channelCode+"&appId="+appId
		});
        $('#configEditModal').modal('show');
    }
	$("#configEditModal").on("hidden", function() {  
	    $(this).removeData("modal"); 
	});  
	
	
	//修改支付渠道显示名称：editChannelModal
    function mesEdit(channelCode,appId) {
    	$("#editChannelModalLabel").text("支付渠道信息修改");
    	$("#editChannelModal").modal({
			remote: tradeBasePath + "/tradeChannel/toChange?channelCode=" + channelCode+"&appId="+appId
		});
        $('#editChannelModal').modal('show');
    }
	$("#editChannelModal").on("hidden", function() {  
	    $(this).removeData("modal"); 
	});  


    function delWithConfig() {
        //exchange: 已配置操作
        $configLi = $('#configWays').find('li');
        configLiAccount = $configLi.length;
        $configLi.unbind('dblclick');
        $configLi.bind('dblclick', function() {
            var $curValue = $(this).text();
            var connect_id = $(this).attr("id");
            var configHtmlTempl = '';
            configHtmlTempl = '<li attr-index=' + openLiAccount + ' id='+connect_id+'><span>' + $curValue + '</span><img class="first" src="${tradeBasePath}/platform/images/up.png" onclick="dealWithOrder(this,1);"><img class="last" src="${tradeBasePath}/platform/images/down.png" onclick="dealWithOrder(this,-1);"></li>';
            $(this).remove();
            //orderArr[openLiAccount] = $curValue;
            orderArr[openLiAccount] = $curValue + ";" + connect_id;
            //resetIndex();
            console.log(orderArr);
            //数组转json
            arrToJson(orderArr);
            console.log(arrToJson(orderArr));

            configLiAccount = $('#configWays').find('li').length;
            if (configLiAccount == 0) {
                $('#configWays ul').append('<li class="li-op-disabled">已全部开启</li>');
            }
            if (openLiAccount == 0) {
                $('#openWays').find('ul').empty().append(configHtmlTempl);
            } else {
                $('#openWays').find('ul').append(configHtmlTempl);
            }
            delWithOpen();
        });
    }

    function delWithOpen() {
        //exchange: 已开启操作
        $openLi = $('#openWays').find('li');
        openLiAccount = $openLi.length;
        $openLi.unbind('dblclick');
        $openLi.bind('dblclick', function(e) {
            var $curValue = $(this).find('span').text();
            var connect_id = $(this).attr("id");
            var openHtmlTempl = '<li id='+connect_id+'>' + $curValue + '</li>';
            var curIndex = Number($(this).attr('attr-index'));
            $(this).remove();
            orderArr.splice(curIndex, 1);
            resetIndex();
            console.log(orderArr);
            //将数组转为json
            arrToJson(orderArr);
            console.log(arrToJson(orderArr));
            openLiAccount = $('#openWays').find('li').length;
            if (openLiAccount == 0) {
                $('#openWays ul').append('<li class="li-op-disabled">已全部关闭</li>');
            }
            if (configLiAccount == 0) {
                $('#configWays').find('ul').empty().append(openHtmlTempl);
            } else {
                $('#configWays').find('ul').append(openHtmlTempl);
            }
            delWithConfig();
        });
    }
    /*
     * 支付方式排序
     * @param obj:指代当前对象，此处赋值为this
     * @param type:排序类型，1:上移,-1:下移;
     */
    function dealWithOrder(obj, type) {
        var orderItems = $('#openWays').find('li');
        var curIndex = Number($(obj).parent('li').attr('attr-index'));
        var pesoItem, transItem;
        var newHtmlTempl = '';
        orderArr = [];
        $.each(orderItems, function(index, item) {
            pesoItem = $.trim($(item).find('span').text());
            //orderArr.push(pesoItem);
            orderArr.push(pesoItem+";"+$(item).attr('id'));
        });
        if (type === 1) {
            //升序
            if (curIndex == 0) {
                alert('已经是第一个了');
                return;
            } else {
                transItem = orderArr[curIndex];
                orderArr[curIndex] = orderArr[curIndex - 1];
                orderArr[curIndex - 1] = transItem;
            }
        } else if (type === -1) {
            //降序
            if (curIndex == orderArr.length - 1) {
                alert('已经是最后一个了');
                return;
            } else {
                transItem = orderArr[curIndex];
                orderArr[curIndex] = orderArr[curIndex + 1];
                orderArr[curIndex + 1] = transItem;
            }
        }
        console.log(orderArr);
        //数组转json
        arrToJson(orderArr);
        console.log(arrToJson(orderArr));
        //针对新的数据进行处理
        $.each(orderArr, function(index, item) {
        	var content = item.split(";")[0];
        	var id = item.split(";")[1];
            newHtmlTempl += '<li attr-index=' + index + ' id="'+id+'"><span>' + content + '</span><img class="first" src="${tradeBasePath}/platform/images/up.png" onclick="dealWithOrder(this,1);"><img class="last" src="${tradeBasePath}/platform/images/down.png" onclick="dealWithOrder(this,-1);"></li>';
        });
        $('#openWays').find('ul').html(newHtmlTempl);
        delWithOpen();
    }
    //li reset index
    function resetIndex() {
        var htmlStr = '';
        $.each(orderArr, function(index, item) {
         	var content = item.split(";")[0];
        	var id = item.split(";")[1];
            htmlStr += '<li attr-index="' + index + '" id="'+id+'">\
                       <span>' + content + '</span>\
                       <img class="first" src="${tradeBasePath}/platform/images/up.png" onclick="dealWithOrder(this,1);">\
                       <img class="last" src="${tradeBasePath}/platform/images/down.png" onclick="dealWithOrder(this,-1);">\
                       </li>';
        });
        $('#openWays').find('ul').html(htmlStr);
        delWithOpen();
    }
    //arrToJson
    function arrToJson(arr) {
        var subItemsJson;
        orderJsonArr = [];
        $.each(arr, function(index, item) {
            subItemsJson = {};
            subItemsJson.key = index;
            subItemsJson.value = item;
            orderJsonArr[index] = subItemsJson;
        });
        return JSON.stringify(orderJsonArr);
    }
    //onOff
    function checkValue(call) {
        if ($(".iosCheck i").hasClass("active")) {
            $(".iosCheck i").removeClass("active");
            $("#validateTime").val(0);
        } else {
            $(".iosCheck i").addClass("active");
            $("#validateTime").val(1);
        }
    }
</script>