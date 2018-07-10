 $(function () {
	 
	 initDate();
	     
});

 /**
  * 初始化日期
  * @returns
  */
function initDate() {
	 var currYear = (new Date()).getFullYear();  
     var opt={};
     opt.date = {preset : 'date'};
     opt.datetime = {preset : 'datetime'};
     opt.time = {preset : 'time'};
     opt.default = {
       theme: 'android-ics light', //皮肤样式
       display: 'modal', //显示方式 
       mode: 'scroller', //日期选择模式
       lang:'zh',
       showNow: true,
       nowText: "今天",
       startYear:currYear - 100, //开始年份
       endYear:currYear + 0 //结束年份
     };
     
     $(".isdataControls").val('').scroller('destroy').scroller($.extend(opt['date'], opt['default']));
 }

$(document).on('click','.radio.inline',function(){
    $(this).addClass("check").siblings().removeClass("check"); 
})

$(document).on('click','.checkbox',function(){
    if($(this).hasClass('check')) {
        $(this).removeClass('check')
    }else {
        $(this).addClass('check')
    }
})
function checkValue(call) {
    if ($(".iosCheck i").hasClass("active")) {
        $(".iosCheck i").removeClass("active");
        $("#guarder").css("display","none");
        clearGuarder();
    } else {
        $(".iosCheck i").addClass("active");
        $("#guarder").css("display","block");
    }
}

/**
 * 下拉框选择关系
 * @returns
 */
function turnRelations(obj){
	if($(obj).val() == 4 || $(obj).val() == 3 ) {
		$("#underAge").css("display","block");
		$(".iosCheck i").removeClass("active");
		$("#guarder").css("display","none");
	}else{
		$("#underAge").css("display","none");
		$("#guarder").css("display","none");
	}
	clearGuarder();
}

function clearGuarder() {
	$("#guarderName").val("");
	$("#guarderCardtype").val("");
	$("#guarderCardno").val("");
	$("#guarderMobile").val("");
}

/**
 * 验证输入信息
 * @returns
 */
function numberValidate(){
	var map={};
	var flag=true;
	var msg="";
    if($("#fullname").val().trim() == ""){
    	map.flag=false;
    	map.msg= "<div>姓名不能为空</div>";
		return map;   
    }
	
    if($("#idType").val().trim() == ""){
    	map.flag=false;
    	map.msg= "<div>证件类型不能为空</div>";
		return map;  
    }
    
    if($("#idCard").val().trim() == ""){
    	map.flag=false;
    	map.msg= "<div>证件号码不能为空</div>";
		return map; 
    }
    
    if($("#idType").val().trim() == 1){
    	 var mflag = IdCardValidate($("#idCard").val().trim());
    	 if (!mflag) {
    	    	map.flag=false;
    			map.msg= "<div>身份证格式错误</div>";
    			return map;
    	    }
    }
    
    if($("#birthdays").val().trim() == ""){
    	map.flag=false;
    	map.msg= "<div>出生日期不能为空</div>";
		return map; 
    }else{
    	var age=getAge($("#birthdays").val().trim());
    	if(age != -1){
    		$("#age").val(age);
    	}
    }
    
	if($("#sexChoose").find(".radio.inline.check").length>0) {
		$("#sex").val($("#sexChoose").find(".radio.inline.check").find("input").val());
	}else{
		map.flag=false;
		map.msg= "<div>必须选择性别</div>";
		return map;
	}
	
    var mobile = $('#mobile').val().trim();
    if(mobile == ""){
    	map.flag=false;
		map.msg= "<div>手机号码不能为空</div>";
		return map;   
    }
    var mflag = validateTelephone(mobile,"mobile");
    if (!mflag) {
    	map.flag=false;
		map.msg= "<div>手机号码格式错误</div>";
		return map;
    }

    
    if($("#guarder").css("display") == "block"){
    	if($("#guarderName").val().trim() == ""){
    		map.flag=false;
        	map.msg= "<div>监护人姓名不能为空</div>";
    		return map; 
    	}
    	if($("#guarderCardtype").val().trim() == ""){
    		map.flag=false;
        	map.msg= "<div>监护人证件类不能为空</div>";
    		return map; 
    	}
    	if($("#guarderCardno").val().trim() == ""){
    		map.flag=false;
        	map.msg= "<div>监护人证件号不能为空</div>";
    		return map; 
    	}
    	if($("#guarderCardtype").val().trim() == 1){
       	 var mflag = IdCardValidate($("#guarderCardno").val().trim());
       	 if (!mflag) {
       	    	map.flag=false;
       			map.msg= "<div>监护人身份证格式错误</div>";
       			return map;
       	    }
       }
    	if($("#guarderMobile").val().trim() == ""){
    		map.flag=false;
        	map.msg= "<div>监护人手机号码不能为空</div>";
    		return map; 
    	}
    	var mflag = validateTelephone($("#guarderMobile").val().trim(),"guarderMobile");
        if (!mflag) {
        	map.flag=false;
    		map.msg= "<div>监护人手机号码格式错误</div>";
    		return map;
        }
    }
    
    if($("#defaultpatientCh").find(".checkbox.check").length>0){
    	$("#defaultpatient").val(1);
    }else{
    	$("#defaultpatient").val(0);
    }
    
    map.flag=true;
	return map;
}

/**
 * 校验手机格式
 * @param tel
 * @param obj
 * @returns
 */
function validateTelephone(tel,obj){
	var mflag = regBox.regMobile.test(tel);
    if (!mflag) {
      $("#"+obj).val("");
      return false;
    }
    return true;
}


/**
 * 输入电话号码失焦事件
 * @param obj
 * @returns
 */
function  validateMobile(obj){
	var mobile = $(obj).val();
	var mflag = regBox.regMobile.test(mobile);
    if (!mflag) {
      $(obj).val("");
      $Y.tips('<div>手机号码格式错误</div>');  
    }
}

/**
 * 保存家人信息
 * @returns
 */
function save () {
	var map=numberValidate();
	if(!map.flag){
		$Y.tips('<div>'+map.msg+'</div>');
		return false;
	}
	
	var birthday=$("#birthdays").val();
	$.ajax({
        url : tradeBasePath+'/mobileapp/family/saveOrUpdate?myBirthday='+birthday,
        type: 'POST',
        data : $('#formBeanFamily').serialize(),
        error: function(request) {
        	$Y.tips('<div>操作失败</div>');  
         },
        success: function (data) {
           if(data.status == "OK"){
               $Y.tips('<div>添加成功</div>');
               $.ajax({
                   url : tradeBasePath+'/mobileapp/toHomePage',
                   type: 'POST',
                   error: function(request) {
                   	$Y.tips('<div>操作失败</div>');  
                    },
                   success: function (data) {
                  	 setTimeout(function(){createPage(data)},1200);
                   }
               });
          }else{
               $Y.tips('<div>' + data.message + '</div>');     
          }
        }
    });
	
}







