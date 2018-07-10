$(function() {
	search();
//	$.ajax({
//		url : tradeBasePath + '/platform/user/findResourceByParentCode',
//		data : 'code=zhlb',
//		type : 'POST',
//		success : function(data) {
//			$(".operation-button").html(data);
//		}
//	});
	hasButtonAuth();
});

/**
 * 搜索
 */

$("#search").click(function() {
	search();
});

function search(){
	var searchValue = $("#searchValue").val();
	$(".pagination.pagination-right.pagination-style").pagination({
		url : tradeBasePath + "/platform/user/findListByPage",
		params : {
			"params['searchValue']" : searchValue
		},
		successCallback : function(data) {
			var k = 0;
			var tbody = $(".list-data");
			var html = "";
			// 清空
			tbody.empty();
			var ul = "<ul>";
			$(data.list).each(function(i, n) {
				var type = (n.type == 1 ? "平台" : "商户");
				var status = n.status;
				var statusButtonText = "启用";
				if (status == 1) {
					status = "启用";
					statusButtonText = "停用";
				} else {
					status = "停用";
					statusButtonText = "启用";
				}
				var ownedMerchantName ="";
				if(n.ownedMerchantName != null){
					ownedMerchantName = n.ownedMerchantName;
				}
				
				var ownedApplicationName ="";
				if(n.ownedApplicationName != null){
					ownedApplicationName = n.ownedApplicationName;
				}
				
				k++;
				var d = new Date(n.ct); //根据时间戳生成的时间对象
				var date = (d.getFullYear()) + "-" +
				(d.getMonth() + 1) + "-" +
				(d.getDate()) + " " +
				(d.getHours()) + ":" +
				(d.getMinutes()) + ":" +
				(d.getSeconds());
				html += '<tr>';
				html += '<td>' + "<input type='checkbox' name='check' value='" + n.id + "'>" + "</td>";
				html += '<td>' + k + "</td>";
				html += '<td>' + n.account + "</td>";
				html += "<td>" + type + "</td>";
				html += "<td>" + ownedMerchantName + "</td>";
				html += "<td>" + ownedApplicationName + "</td>";
				html += "<td>" + status + "</td>";
				html += '<td>' + date + "</td>";
				html += '</tr>';
			});
			tbody.append(html);
		}
	});
}

function toDelete() {
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
	if (confirm("确定删除")) {
		$.ajax({
			url : tradeBasePath + "/platform/user/toDelete?ids=" + ids,
			type : 'POST',
			error : function(request) {
				alert("操作失败");
			},
			success : function(data) {
				var url = "/platform/user/list";
				var returnurl = tradeBasePath + url;
				alert(data.message);
				openUrl(url); 
			}
		});
	}

}

function toAdd() {
	$.ajax({
		url : tradeBasePath + "/platform/user/toAdd",
		type : 'POST',
		error : function(request) {
			alert("操作失败");
		},
		success : function(data) {
			$("#content").html(data);
		}
	});
}

function toEdit() {
	var i = 0;
	var ids = "";
	$("input[name='check']:checked").each(function() {
		ids += $(this).val() + ",";
		i++;
	})
	if (i > 1) {
		alert("只能选择一条记录");
		return;
	}
	if (i == 0) {
		alert("请选择选择一条记录");
		return;
	}
	ids = ids.substring(0, ids.length - 1);
	$.ajax({
		url : tradeBasePath + "/platform/user/toEdit?id=" + ids,
		type : 'POST',
		error : function(request) {
			alert("操作失败");
		},
		success : function(data) {
			$("#content").html(data);
		}
	});
}