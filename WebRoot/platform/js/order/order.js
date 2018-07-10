var order = {};
var formate = "";
$(function() {
	$("#merchan").on("change",function(){
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/order/getApplicationByMerchan",
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
		url :tradeBasePath + "/order/findOrderList",
		params : map,
		successCallback : function(data) {
			var tbody = $(".list-data");
			var html="";
			// 清空
			tbody.empty();
			var ul = "<ul>";
			$.each(data.list,function(i, n) {
				html+=register(n);
			});
			tbody.append(html);
		}
	});
}

//分页表格填充
function register(n){
	var html="";
	html+='<tr ondblclick=\"clinicView(\'' +  n.id + "')\">";  //ondblclick=\"clinicView(\'clinic_' +  n.id + "')\"
	html+='<td>' + resultFormat(n.merchantName) + "</td>";
	html+='<td>' + n.appName + "</td>";
	html+='<td>' + n.tradeNo+ "</td>";
	html+='<td>' + n.outTradeNo+ "</td>";
	html+='<td>' + resultFormat(n.agtTradeNo) + "</td>";
	html+='<td>' + n.payTimeVo + "</td>";
	html+='<td>' + n.subject + "</td>";
	html+='<td>' + n.totalFeeVo+ "</td>";
	html+='<td>' + n.actualPayFreeVo + "</td>"; //实付金额
	html+='<td>' + n.refundFeeVo + "</td>";
	html+='<td>' + n.actualGetFreeVo + "</td>"; //实收金额
	html+='<td>' + n.tradeStatusVo + "</td>";
	html+='<td>' + resultFormat(n.channelName) + "</td>";
	html+='</tr>';
	return html;
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

//点击进入详情页面
function clinicView(clinicId){
//	openMenu(5,'/order/orderDetail?id='+clinicId);
	openDetailUrl('/order/orderDetail?id='+clinicId);
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


