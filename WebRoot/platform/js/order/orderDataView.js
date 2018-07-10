var order = {};
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
	//initalAppclication();
});


function search(){
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
	var url = '/order/searchOrderDataView?pageParams='+params; 
	openMenu(5,url);
}

/**
 * 统计搜索
 */
function searchStatistics(){
	var datas = $("#searchForm").serializeArray();
	$.ajax({
        url: tradeBasePath + '/order/orderStatistics',
        data:datas,
        type:'POST',
        success: function (data) {
        	$("#content").html(data);
    	}
    });
}

/**
 * 商户选择联动
 * @param obj
 */
function merchantsChange(obj){
	var merchantNo = $(obj).find("option:selected").val();
	$(obj).parent().find(":hidden[name='merchantNo']").val(merchantNo);
	/*$.ajax({
		type: "POST",
		url: tradeBasePath + "/order/getApplicationByMerchan?merchantNo=" + merchantNo,
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
	});*/
}
/**
 * 应用选择联动
 * @param obj
 */
function applicationChange(obj){
	$(obj).parent().find(":hidden[name='appId']").val($(obj).find("option:selected").val());
}

