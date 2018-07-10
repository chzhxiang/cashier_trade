$(function() {
	search();
});

//加载数据
function search() {
	var merchantName = $("#merchantName").val();
	var linkmanName = $("#linkmanName").val();
	var mobile = $("#mobile").val();

	$(".pagination.pagination-right.pagination-style").pagination({
		url: tradeBasePath + "/merchant/findListByPage",
		params: {
			"params['merchantName']": merchantName,
			"params['linkmanName']": linkmanName,
			"params['mobile']": mobile
		},
		successCallback: function(data) {
			var count = 0;
			var tbody = $(".list-data");
			var html = "";
			// 清空
			tbody.empty();
			var ul = "<ul>";
			$(data.list).each(function(index, value) {
				var status = value.status;
				var statusButtonText = "启用";
				if (status == 1) {
					status = "启用";
					statusButtonText = "停用";
				} else {
					status = "停用";
					statusButtonText = "启用";
				}
				count++;
				html += "<tr>";
				html += "<td></td>";
				html += "<td>" + count + "</td>";
				html += "<td>" + value.merchantName + "</td>";
				html += "<td>" + value.merchantAddress + "</td>";
				html += "<td>" + value.linkmanName + "</td>";
				html += "<td>" + value.mobile + "</td>";
				html += "<td>" + (value.qq == null ? "" : value.qq) + "</td>";
				html += "<td>" + (value.email == null ? "" : value.email) + "</td>";
				html += "<td>" + status + "</td>";
				html += "<td><button class='btn btn-small' onclick=openUrl('merchant/toSetting?merchantNo=" + value.merchantNo + "')>设置</button>" +
					"<button class='btn btn-primary btn-small' onclick=toEdit('" + value.merchantNo + "')>修改</button>" +
					"<button class='btn btn-warning btn-small' onclick=changeStatus(" + (value.status == 1 ? 0 : 1) + ",'" + value.merchantNo + "','" + value.merchantName + "','" + statusButtonText + "')>" + statusButtonText + "</button></td>";
				html += "</tr>";
			});
			tbody.append(html);
		}
	});
}

//修改状态(0停用、1启用)
function changeStatus(status, merchantNo, merchantName, statusButtonText) {
	if (confirm("确定" + statusButtonText + "【" + merchantName + "】?")) {
		$.ajax({
			type: "POST",
			url: tradeBasePath + "/merchant/changeStatus",
			data: {
				"merchantNo": merchantNo,
				"status": status
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

//跳转修改商户
function toEdit(merchantNo) {
	openUrl("merchant/toEdit?merchantNo=" + merchantNo);
}

//保存商户基本信息
function save() {
	var id = $("#id").val();
	var merchantNo = $("#merchantNo").val();
	var merchantName = $("#merchantName").val();
	var merchantAddress = $("#merchantAddress").val();
	var linkmanName = $("#linkmanName").val();
	var mobile = $("#mobile").val();
	var qq = $("#qq").val();
	var email = $("#email").val();
	var status = $("#status").val();

	if (merchantName == "") {
		alert("企业名称不能为空");
		return;
	}
	if (merchantAddress == "") {
		alert("企业联系地址不能为空");
		return;
	}
	if (linkmanName == "") {
		alert("联系人姓名不能为空");
		return;
	}
	if (mobile == "") {
		alert("联系人电话不能为空");
		return;
	}

	$.ajax({
		type: "POST",
		url: tradeBasePath + "/merchant/save",
		data: {
			"id": id,
			"merchantNo": merchantNo,
			"merchantName": merchantName,
			"merchantAddress": merchantAddress,
			"linkmanName": linkmanName,
			"mobile": mobile,
			"qq": qq,
			"email": email,
			"status": status
		},
		success: function(response) {
			alert(response.message);
			if (response.status == "OK") {
				openUrl("merchant/list");
			}
		}
	});
}
