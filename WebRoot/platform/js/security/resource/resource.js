$(function() {
//	$.ajax({
//    	url: tradeBasePath + '/platform/resource/findResourceByParentCode',
//    	data:{
//    		'code':'zylb'
//    	},
//        type: 'POST',
//        success: function (data) {
//            $(".operation-button").html(data);
//        }
//    });
	search();
	
	hasButtonAuth();
});

/**
 * 搜索
 */

$("#search").click(function(){  
	search(); 
});  

function search(){
	var searchValue = $("#searchValue").val();
	$(".pagination.pagination-right.pagination-style").pagination({
		url : tradeBasePath + "/platform/resource/findListByPage",
		params : {
			"params['searchValue']" : searchValue
		},
		successCallback : function(data) {
			var k = 0;
			var tbody = $(".list-data");
			var html="";
			// 清空
			tbody.empty();
			var ul = "<ul>";
			$(data.list).each(function(i, n) {
				k++;
				var parentName ="";
				var value = "";
				if(n.parentName != null){
					parentName = n.parentName;
				}
				if(n.value != null){
					value = n.value;
				}
				html+='<tr>';
				html+='<td>' + "<input type='checkbox' name='check' value='"+n.id+"'>" + "</td>";
				html+='<td>' + k + "</td>";
				html+='<td>' + n.code + "</td>";
				html+='<td>' + n.name + "</td>";
				html+='<td>' + parentName+ "</td>";
				if(n.type == "1"){
					html+="<td>菜单</td>";
				}else{
					html+="<td>按钮或其他</td>";
				}
				
				if(n.operationType == 1){
					html+="<td>Url跳转</td>";
				}else if(n.operationType == 2){
					html+="<td>Js方法调用</td>";
				}else{
					html+="<td>其他</td>";
				}
				
				html+='<td>' + value + "</td>";
				html+='</tr>';
			});
			tbody.append(html);
		}
	});
}

function toDelete(){
	var i = 0;
    var ids = ""; 
    $("input[name='check']:checked").each(function(){
       ids+=$(this).val()+",";
       i++;
    })
    if(i == 0){
      alert("请选择选择一条记录");
      return;
    }
    ids = ids.substring(0,ids.length-1);
	if(confirm("删除该资源,将删除该资源下的所有子资源以及资源与角色的关系,是否确定删除"))
	 {
		$.ajax({
			url : tradeBasePath + "/platform/resource/toDelete",
			data: {id:ids},
	        type: 'POST',
	        error: function(request) {
	            alert("操作失败");
	         },
	        success: function (data) {
	            var returnurl = '/platform/resource/list'
	            alert(data.message);
	            openUrl(returnurl);
	        }
	    });
	 }
	
}

function toAdd(){
	$.ajax({
		url : tradeBasePath + "/platform/resource/toAdd",
        type: 'POST',
        error: function(request) {
            alert("操作失败");
         },
        success: function (data) {
           $("#content").html(data);
        }
    });
}

function toEdit(){
	   var i = 0;
       var ids = ""; 
       $("input[name='check']:checked").each(function(){
          ids+=$(this).val()+",";
          i++;
       })
       if(i>1){
         alert("只能选择一条记录");
         return;
       }
       if(i == 0){
         alert("请选择选择一条记录");
         return;
       }
       ids=ids.substring(0,ids.length-1);
       $.ajax({
     	 url: tradeBasePath + "/platform/resource/toEdit?id="+ids,
         type: 'POST',
         error: function(request) {
            alert("操作失败");
         },
         success: function (data) {
              $("#content").html(data);
         }
     });
}
