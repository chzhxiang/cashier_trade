$(function() {
	//加载应用列表和基本信息
	loadMerchantApplications();

	//加载支付渠道排序
});

//加载应用列表
function loadMerchantApplications() {
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/merchantApplication/getMerchantApplications",
		data: {
			"merchantNo": $("#merchantNo").val()
		},
		success: function(response) {
			if (response.status == "OK" && response.result != null) {
				var optionHTML = "";
				if(response.result.length == 0){ //没有应用
					$("#applicationInfo").hide();
				}else{
					$("#applicationInfo").show();
					$.each(response.result, function(index, value) {
						optionHTML += "<option value='" + value.appId + "'>" + value.appName + "</option>";
					});
				}
				
				$("#applicationSel").html(optionHTML);
				//加载所选择的应用信息
				loadMerchantApplication();

				//加载应用支付渠道
				loadTradeChannel();
			}
		}
	});
}

function searchApp(){
	loadMerchantApplication();
	loadTradeChannel();
}

//加载应用基本信息
function loadMerchantApplication() {
	var applicationSelVal = $("#applicationSel").val();
	
	if (!(applicationSelVal == null || applicationSelVal == "")) {
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/merchantApplication/findByAppId",
			data: {
				"appId": applicationSelVal
			},
			success: function(response) {
				if (response.status == "OK") {
					$("#basicInfoForm")[0].reset();
					$("#basicInfoForm").formEdit(response.result);
					//设置可复制属性
					$("#copyMerchantNo").attr("data-clipboard-text", response.result.merchantNo);
					$("#copyAppId").attr("data-clipboard-text", response.result.appId);
					$("#copyPublicKey").attr("data-clipboard-text", response.result.publicKey);
					$("#copyygkzPublicKey").attr("data-clipboard-text", response.result.appPublicKey);
//					$("#copyAppSecret").attr("data-clipboard-text", response.result.appSecret);
//					$("#copyMasterSecret").attr("data-clipboard-text", response.result.masterSecret);

					//设置是否校验时间戳
					if (response.result.validateTime == 1) {
						$(".iosCheck i").addClass("active");
					} else {
						$(".iosCheck i").removeClass("active");
					}
				}
			}
		});
	}
}

//保存应用基本信息
function saveMerchantApplication(type) {
	if (type == "saveName") {
		if (!validateAppName($("#appName").val())) {
			alert("应用名称为1～26位大小写字母、数字、汉字、下划线，或组合。");
		} else {
			$.ajax({
				type: "POST",
				url: tradeBasePath + "/merchantApplication/save",
				data: {
					"merchantNo": $("#merchantNo").val(),
					"appName": $("#appName").val()
				},
				success: function(response) {
					if (response.status == "OK") {
						$("#addAppModal").modal("hide");
						alert(response.message);
						loadMerchantApplications();
					}else{
						alert("保存失败！");
					}
				}
			});
		}
	} else {
		if ($("#applicationSel").val() == null || $("#applicationSel").val() == "") {
			alert("请选择支付应用");
		} else {
			$.ajax({
				type: "POST",
				url: tradeBasePath + "/merchantApplication/save",
				data: $("#basicInfoForm").serialize(),
				success: function(response) {
					if (response.status == "OK") {
						$("#addAppModal").modal("hide");
						alert(response.message);
						//loadMerchantApplications();
						//加载所选择的应用信息
						loadMerchantApplication();

						//加载应用支付渠道
						loadTradeChannel();
					}else{
						alert("保存失败！");
					}
				}
			});
		}
	}
}

//加载应用支付渠道
function loadTradeChannel() {
	loadOpenTradeChannel();
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/tradeChannel/loadAppTradeChannelList",
		data: {
			"merchantNo": $("#merchantNo").val(),
			"appId": $("#applicationSel").val(),
		},
		success: function(response) {
			if (response.status == "OK") {
				console.log(response.result);
				var tbody = $(".list-data");
				var html = "";
				// 清空
				tbody.empty();
				$(response.result).each(function(index, value) {
					html += "<tr>";
					html += "<td><img class='pay-way-logo' src='" + tradeBasePath + ""+value.icon+"'>" + value.name + "</td>";
					html += "<td>" + value.code + "</td>";
					html += "<td>" + value.scenarios + "</td>";
					html += "<td>" + value.settlementCycle + "</td>";
					var hasConfig = value.hasConfig;
					//是否已配置
					if (hasConfig == "1") {
						html += "<td><a style='color:gray' href='javascript:void(0);' onclick=configEdit('" + value.code + "','" + value.name + "','" + $("#applicationSel").val() + "')>已配置</a>" +
								"<a href='javascript:void(0);' onclick=mesEdit('" + value.code + "','"+$("#applicationSel").val()+"')>修改</a>" +
								"</td>";
					} else {
						html += "<td><a href='javascript:void(0);' onclick=configAdd('" + value.code + "','" + value.name + "')>未配置</a>" +
						"</td>";
					}
					html += "</tr>";
				});
				tbody.append(html);
			}
		}
	});
}


function loadOpenTradeChannel(){
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/applicationChannel/findTradeChannelByAppId",
		data: {
			"appId": $("#applicationSel").val(),
		},
		success: function(response) {
			if (response.status == "OK") {
				orderArr = [];  //查询前先清空
				//渠道排序
				var confightmltbody = $("#configWays");
				var opentbody = $("#openWays");
				var confightml = "";
				var openhtml = "";
				// 清空
				confightmltbody.empty();
				opentbody.empty();
				var count = 0 ;
				$(response.result).each(function(index, value) {
					//渠道排序
					if (value.isOpen == "0") {  //已配置未开启
						confightml += "<li id='"+value.id+"'>" + value.channelName + "</li>";
					}else if(value.isOpen == "1"){  //已配置已开启
						openhtml += "<li attr-index='"+count+"' id='"+value.id+"'><span>" + value.channelName +"</span>";
						openhtml += "<img class='first' src='" + tradeBasePath + "/platform/images/up.png' onclick='dealWithOrder(this,1);'>"
						openhtml += "<img class='last' src='" + tradeBasePath + "/platform/images/down.png' onclick='dealWithOrder(this,-1);'>"+ "</li>"	
						count ++;
					}
				});
				confightml = "<ul>" + confightml + "</ul>";
				openhtml = "<ul>" + openhtml + "</ul>";
				confightmltbody.append(confightml);
				opentbody.append(openhtml);
				
				delWithConfig();
		        delWithOpen();
		        var orderItems = $('#openWays').find('li');
		        $.each(orderItems, function(index, item) {
		            pesoItem = $.trim($(item).find('span').text());
		            orderArr.push(pesoItem+";"+$(item).attr('id'));
		        });
			}
		}
	});
}

//修改支付渠道显示名称
function editApplicationChannel(){
	var url_status = $('#link_url input[name="link_url"]:checked ').val();
	//alert(url);
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/applicationChannel/updateChannelView",
		data: {
			channelViewName:$("#viewName").val(),
			urlStatus:url_status,
			id:$("#dbId").val(),
		},
		success: function(response) {
			if (response.status == "OK") {
				$("#editChannelModal").modal("hide");
				alert(response.message);
				loadTradeChannel();
			}else{
				alert("保存失败！");
			}
		}
	});
}

//保存应用的支付渠道配置信息
function saveMerchantApplicationChannel(type){
	var map = {};
	var flag = true;
	$("#add_channelFrom").find(":input ").each(function(){
		var name = $(this).attr("name");
		var intType = $(this).attr("type");
		var mesg = $(this).attr("placeholder");
		var value = $(this).val();
		if(name!=undefined && value!="" && intType!="file")
		 {	
			map[name] = $.trim(value);
		 }else if(intType=="file"){
			 
		 }else{
			 alert(mesg);
			 flag = false;
			 return false;
		 }
	});
	if(!flag){
		return;
	}
	$("#add_channelFrom").find("textarea").each(function(){
		var name = $(this).attr("name");
		var mesg = $(this).attr("placeholder");
		var value = $(this).val();
		if(name!=undefined && value!="" )
		 {	
			map[name] = $.trim(value);
		 }else{
			 alert(mesg);
			 flag = false;
			 return false;
		 }
	});
	if(!flag){
		return;
	}
	var PARAMS_JSON = JSON.stringify(map);
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/applicationChannel/save",
		data: {
			appId:$("#applicationSel").val(),
			channelCode:$("#channelCode").val(),
			merchantNo:$("#merchantNo").val(),
			paramsJson:PARAMS_JSON,
		},
		success: function(response) {
			if (response.status == "OK") {
				$("#configAddModal").modal("hide");
				alert(response.message);
				loadTradeChannel();
			}else{
				alert("保存失败！");
			}
		}
	});
}

//更新应用的支付渠道配置信息
function updateMerchantApplicationChannel(){
	var map = {};
	var flag = true;
	$("#update_channelFrom").find(":input ").each(function(){
		var name=$(this).attr("name");
		var intType = $(this).attr("type");
		var value=$(this).val();
		var mesg = $(this).attr("placeholder");
		if(name!=undefined && value!="" && intType!="file")
		 {	
			map[name] = $.trim(value);
		 }else if(intType=="file"){
			 
		 }else{
			 alert(mesg);
			 flag = false;
			 return false;
		 }
	});
	if(!flag){
		return;
	}
	$("#add_channelFrom").find("textarea").each(function(){
		var name = $(this).attr("name");
		var mesg = $(this).attr("placeholder");
		var value = $(this).val();
		if(name!=undefined && value!="" )
		 {	
			map[name] = $.trim(value);
		 }else{
			 alert(mesg);
			 flag = false;
			 return false;
		 }
	});
	if(!flag){
		return;
	}
	var PARAMS_JSON = JSON.stringify(map);
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/applicationChannel/save",
		data: {
			id: $("#applicationId").val(),
			paramsJson:PARAMS_JSON,
		},
		success: function(response) {
			if (response.status == "OK") {
				$("#configEditModal").modal("hide");
				alert(response.message);
				loadTradeChannel();
			}else{
				alert("保存失败！");
			}
		}
	});
}

//更新应用配置的支付渠道信息（排序跟是否开启）
function updateApplicationChannel(){
	 var configItems = $('#configWays').find('li');
	 configJsonArr = [];
	 $.each(configItems, function(index, item) {
		 configItemsJson = {};
		 configItemsJson.sort = $(item).attr("attr-index");
		 configItemsJson.id = $(item).attr("id");
		 if(configItemsJson.id !="" && configItemsJson.id != undefined){
			 configJsonArr[index] = configItemsJson;
		 }
     });
	 //alert(JSON.stringify(configJsonArr));
     var config_params = JSON.stringify(configJsonArr);
     
	 var openItems = $('#openWays').find('li');
     openJsonArr = [];
     $.each(openItems, function(index, item) {
         openItemsJson = {};
         openItemsJson.sort = $(item).attr("attr-index");
         openItemsJson.id = $(item).attr("id");
         if(openItemsJson.id !="" && openItemsJson.id != undefined){
        	 openJsonArr[index] = openItemsJson;
         }
     });
     //alert(JSON.stringify(openJsonArr));
     var open_params = JSON.stringify(openJsonArr);
     $.ajax({
 		type: "POST",
 		url: tradeBasePath + "/applicationChannel/updateAppChannel",
 		data: {
 			configParams:config_params,
 			openParams:open_params,
 			appId:$("#applicationSel").val(),
 		},
 		success: function(response) {
 			if (response.status == "OK") {
 				$("#configEditModal").modal("hide");
 				alert(response.message);
 				orderArr = [];
 				loadTradeChannel();
 			}
 		}
 	});
}


/**
 * 上传文件
 */
function uploadTradeFile(obj,formId,name){
    $(":file[name='uploadFile']").attr("id","uploadFiles");
    $(obj).attr("id","uploadFile");
    $.ajaxFileUpload({
        secureuri:false,
        fileElementId: $(obj).attr("id"),
        dataType: 'json',     
        type:'POST',
        url: tradeBasePath + "/applicationChannel/upload",
        error : function(data) {
            alert("系统内部错误,请联系管理员。");
        },
        success:function(response){
            console.log(response);
            if(response.status == "OK"){
            	alert("证书文件上传成功！");
            	$("#" + formId).find(":text[name='" + name + "']").val(response.result);
                //$("#" + formId).find(":text[name='" + name + "']").val(resp.result);
            }else{
                alert(response.message);
            }
        }                                                                                                              
    });
}

//校验应用名称
function validateAppName(appName) {
	var patt = /^[a-z|A-Z|0-9|_|\u4e00-\u9fa5]{1,}$/;
	if (patt.test(appName)) {
		return true;
	} else {
		return false;
	}
}

//复制
var btns = document.querySelectorAll(".copy-btn");
var clipboard = new Clipboard(btns);

clipboard.on("success", function(e) {
	alert("复制成功");
});
