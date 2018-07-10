var order = {};
var formate = "";
$(function() {
	$("#merchan").on("change",function(){
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/bankCard/getApplicationByMerchan",
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
					$("#appName").html(html);
				}else{
					$("#appName").html("<option  value=''>请选择</option>");
				}
			}
		});
	});
	search();
});


//查询以及分页
function search(){
	var datas="";
	var map = {};
	$("#form_search_id").find(":input ").each(function(){
		var name=$(this).attr("name");
		var value=$(this).val();
		if(name!=undefined && value!="" )
		 {	
			if(name=="startDate" ||name=="endDate" || name=="isException")
			{
				map["params['"+name+"']"] = $.trim(value);
			}else{
				map["params['"+name+"']"] = ""+$.trim(value)+"";
			}		
		 }
	});
	$("#form_search_id").find(".select_class").each(function(){
		var name=$(this).attr("name");
		var value=$("select[name="+name+"]").find("option:selected").val();
		if(name!=undefined && value!="" )
		 {			
			map["params['"+name+"']"] = $.trim(value);
		 }
	});
	$(".pagination.pagination-right.pagination-style").pagination({
		url :tradeBasePath + "/bankCard/findBankCardList",
		params : map,
		successCallback : function(data) {
			var tbody = $(".list-data");
			var html="";
			// 清空
			tbody.empty();
			var ul = "<ul>";
			var k = 0;
			$.each(data.list,function(i, n) {
				var count = ++k;
				html+=register(n,count);
			});
			tbody.append(html);
		}
	});
}

//分页表格填充
function register(n,count){
	var html= "";
	html += '<tr ondblclick=\"clinicView(\'' +  n.id + "')\">";  //ondblclick=\"clinicView(\'clinic_' +  n.id + "')\"
	html += '<td>' + "<input type='checkbox' name='check' value='" + n.id + "'>" + "</td>";
	html += '<td>' + count + "</td>";
	html += '<td>' + resultFormat(n.merchantName) + "</td>";
	html += '<td>' + n.appName + "</td>";
	html += '<td>' + n.bankCardName+ "</td>";
	html += '<td>' + n.bankCardNoStr+ "</td>";
	html += '<td>' + n.userName+ "</td>";
	html += '<td>' + n.cardPhone+ "</td>";
	html += '<td>' + n.cardBalanceByYuan+ "</td>";
	html += '<td>' + n.activateStatusVo+ "</td>";
	html += '<td>' + n.createTimeVo + "</td>";
	html += "<td><button class='btn btn-warning btn-small' onclick=changeStatus('"+n.appId+"','"+ n.bankCardNo +"','"+n.id+"','"+(n.activateStatus == 1 ? 4 : 1 )+"')>" + (n.activateStatus == 1 ? "停止使用" :  "开始使用" ) + "</button>";
	html += "<button class='btn btn-primary btn-small' onclick=toEdit('" + n.id + "')>修改</button>";
	html += "<button class='btn btn-danger btn-small' onclick=toDel('" + n.id + "','"+ n.bankCardNo +"','"+n.appId+"')>删除</button>";
	html += "<button class='btn btn-info btn-small' onclick=rechargeRecord('" + n.id + "')>充值记录</button></td>";
	html += '</tr>';
	return html;
}

//点击进入详情页面
function clinicView(clinicId){
	openDetailUrl('bankCard/bankCardDetail?id='+clinicId);
}

//修改状态
function changeStatus(appId,bankCardNo,id,status) {
	if (confirm("确定" + (status == 1 ? "使用" : "停止使用" ) + "这张卡【" + bankCardNo + "】?")) {
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/bankCard/changeStatus",
			data: {
				"id": id,
				"status": status,
				"bankCardNo" : bankCardNo,
				"appId":appId
			},
			success: function(response) {
				alert(response.message);
				if (response.status == "OK") {
					search();
				}
			}
		});
	}
}


//修改状态
function toDel(id,bankCardNo,appId) {
	var status = 6;
	if (confirm("确定删除这张卡【" + bankCardNo + "】?")) {
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/bankCard/changeStatus",
			data: {
				"id": id,
				"status": status,
				"bankCardNo" : bankCardNo,
				"appId":appId
			},
			success: function(response) {
				alert(response.message);
				if (response.status == "OK") {
					search();
				}
			}
		});
	}
}

//点击进入编辑页面
function toEdit(id){
	openBankUrl('bankCard/updateBankCard?id='+id);
}



//保存商户基本信息
function save() {
	var certificateNo = $("#certificateNo").val();
	var userName = $("#userName").val();
	var deviceId = $("#deviceId").val();
	var accountId = $("#accountId").val();
	var bankCardNo = $("#bankCardNo").val();
	var appId = $("#appId").val();
	var merchantNo = $("#merchantNo").val();
	var channelCode = $("#channelCode").val();
	var certificateType = $("#certificateType").val();
	var cardBalance = $("#cardBalance").val();
	var singleQuota = $("#singleQuota").val();
	var dayQuota = $("#dayQuota").val();
	var monthlyQuota = $("#monthlyQuota").val();
	var bankCardName = $("#bankCardName").val();
	
	if (merchantNo == "") {
		alert("请选择商户名称");
		return;
	}
	if (appId == "") {
		alert("请选择应用名称");
		return;
	}
	if (certificateType == "") {
		alert("请选择证件类型");
		return;
	}
	if (certificateNo == "") {
		alert("证件号码不能为空");
		return;
	}
	if (userName == "") {
		alert("用户姓名不能为空");
		return;
	}
	if (deviceId == "") {
		alert("设备ID不能为空");
		return;
	}
	if (accountId == "") {
		alert("用户id不能为空");
		return;
	}
	if (bankCardNo == "") {
		alert("银行卡号不能为空");
		return;
	}
	if (cardBalance == "") {
		alert("银行卡余额不能为空");
		return;
	}
	if (singleQuota == "") {
		alert("单笔限额不能为空");
		return;
	}
	if (dayQuota == "") {
		alert("单日限额不能为空");
		return;
	}
	if (monthlyQuota == "") {
		alert("单月限额不能为空");
		return;
	}
	if (bankCardName == "") {
		alert("银行名称不能为空");
		return;
	}
	var url = "/bindCard/applyBindCard";
	var urlEnCode = "?certificateNo="+certificateNo+"&userName="+userName+"&certificateType="+certificateType+"&deviceId="+deviceId+"&accountId="+accountId+"&bankCardNo="+bankCardNo+"&channelCode="+channelCode+"&merchantNo="+merchantNo+"&appId="+appId+"&cardBalance="+cardBalance+"&singleQuota="+singleQuota+"&monthlyQuota="+monthlyQuota+"&bankCardName="+bankCardName+"&dayQuota="+dayQuota;
	openEnCodeUrl(url,urlEnCode);
}


//修改金额
function editBankCard() {
	var id = $("#id").val();
	var appId = $("#appId").val();
	var bankCardNo = $("#bankCardNo").val();
	var singleQuota = $("#singleQuota").val();
	var dayQuota = $("#dayQuota").val();
	var monthlyQuota = $("#monthlyQuota").val();
	var bankCardName = $("#bankCardName").val();
	
	if (singleQuota == "") {
		alert("单笔限额不能为空");
		return;
	}
	if (dayQuota == "") {
		alert("单日限额不能为空");
		return;
	}
	if (monthlyQuota == "") {
		alert("单月限额不能为空");
		return;
	}
	
	if(parseInt(singleQuota) > 10000000){
		alert("单笔限额不能超过10000000！");
		return;
	}
	if(parseInt(dayQuota) > 10000000){
		alert("单日限额不能超过10000000！");
		return;
	}
	if(parseInt(monthlyQuota) > 10000000){
		alert("单月限额不能超过10000000！");
		return;
	}
	
	$.ajax({
		type: "POST",
		url: tradeBasePath + "/bankCard/updateCardBalance",
		data: {
			"id": id,
			"appId": appId,
			"bankCardNo": bankCardNo,
			"singleQuota" : singleQuota,
			"dayQuota" : dayQuota,
			"bankCardName" : bankCardName,
			"monthlyQuota" : monthlyQuota
		},
		success: function(response) {
			alert(response.message);
			if (response.status == "OK") {
				openBankUrl('bankCard/bankCardList');
			}
		}
	});
}


//导出报表
order.genExcel = function() {
	var map = {};
	$("#form_search_id").find(":input ").each(function(){
		var name=$(this).attr("name");
		var value=$(this).val();
		if (name != undefined && value != "") {
			map[name] = $.trim(value);
		}
	});
	$("#form_search_id").find(".select_class").each(function(){
		var name=$(this).attr("name");
		var value=$(this).val();	
		if (name != undefined && value != "") {
			map[name] = $.trim(value);
		}
	});
	var params = encodeURI(encodeURI(JSON.stringify(map)));
	var url = tradeBasePath +'/order/dowmExcel?pageParams='+params; 
	
	window.open(url,'_parent');  

}


function backList() {
	$("#content_Detail").hide();
	$("#content").show();				
}

function openDetailUrl(url) {
	$.ajax({
		url : tradeBasePath + "/" + url,
		type : 'POST',
		error : function(request) {
			alert("操作失败");
		},
		success : function(data) {
			if (data != null && data.status == "OK") {
				var returnurl = tradeBasePath + "/" + data.message
				alert("操作成功");
				openDetailUrl(returnurl);
			} else {
				$("#content_Detail").html(data);
				$("#content").hide();
				$("#content_Detail").show();
				
			}
		}
	});
}

function openBankUrl(url) {
	if (typeof url == 'function') {
		url();
	} else {
		$.ajax({
			url : tradeBasePath + "/" + url,
			type : 'POST',
			error : function(request) {
				alert("操作失败");
			},
			success : function(data) {
				if (data != null && data.status == "OK") {
					var returnurl = tradeBasePath + "/" + data.message
					alert("操作成功");
					openUrl(returnurl);
				} else {
					$("#content-list").html(data);
				}
			}
		});
	}

}

//显示格式化
function resultFormat(result){
	if(result == null || result == "null" || result == "undefined" || result == undefined){
		return formate;
	}else{
		return result;
	}
}

//重置表单
function formReset(){
	$("#form_search_id")[0].reset();
}

//充值余额
function rechargeBalance() {
	var i = 0;
	var ids = "";
	$("input[name='check']:checked").each(function() {
		ids += $(this).val() + ",";
		i++;
	})
	if (i == 0) {
		alert("请选择选择一条记录");
		return;
	}
	ids = ids.substring(0, ids.length - 1);
	$.ajax({
		url : tradeBasePath + "/bankCard/rechargeBalance?ids=" + ids,
		type : 'POST',
		error : function(request) {
			alert("操作失败");
		},
		success : function(data) {
			$("#content-list").html(data);
		}
	});
}

function rechargeRecord(id){
	openDetailUrl('bankCard/rechargeRecord?id='+id);
}


