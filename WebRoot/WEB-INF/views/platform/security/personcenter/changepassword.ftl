<!--<div id="content-header">
	<div class="widget-title radius-3"><h3 class="title">修改密码</h3></div>
</div>-->
<div class="container-fluid">
     <div class="row-fluid">
        <div class="widget-content radius-3">
			 <form id="formBean" class="form-horizontal">  
				<input type="hidden" id="id" name="id" value="${user.id}"/>
	     	    <div class="control-group">
                     <label class="control-label" >原密码</label>
                     <div class="controls"><input type="password" id="oldPassword" name="oldPassword" class="span6" placeholder="请输入原密码"></div>
                </div>
                <div class="control-group">
                     <label class="control-label" >设置新密码</label>
                     <div class="controls"><input type="password" id="newPassword" name="newPassword" class="span6" placeholder="请输入6-16位新密码"></div>
                </div>
                 <div class="control-group">
                     <label class="control-label" >确认新密码</label>
                     <div class="controls"><input type="password" id="confirmNewPassword" name="confirmNewPassword" class="span6" placeholder="请重新输入6-16位确认新密码"></div>
                </div>
		     </form>
        </div>
     </div>
     <!--<div class="row-fluid mt10">
        <div class="button-style bgfff radius-3"><button class="btn btn-save" onClick="save()">保存</button></div>
     </div>-->
 </div>
<script>

var regBox = {
    regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,// 邮箱
    regPassword : /^[a-zA-Z0-9_-]{0,100}$/,// 用户名
    regName : /^[a-zA-Z0-9_-]{3,16}$/,// 用户名
    regMobile : /^0?1[3|4|5|7|8][0-9]\d{8}$/,// 手机
    regTel : /^0[\d]{2,3}-[\d]{7,8}$/,
    regPostcode : /^[1-9][0-9]{5}$/  // 邮政编码
};

function psValidate(password,title,tag){
    if(password==''){
        alert(title + '不能为空');
        return false;
    }
    var mflag = regBox.regPassword.test(password);
    if (!mflag) {
        alert( title + '不能包含特殊字符');
        return false;
    }
    else
    {
	    if(password.length<6){
	       alert( title + '位数不够');
	       return false;
	    }else if(password.length>16){
	       alert( title + '过长');
	       return false;
	    }else{
	       return true;
	    }
    }
}

function psSameValidation(oldPassword,newPassword,confirmNewPassword)
{
	if(newPassword != confirmNewPassword)
	{
	    alert('设置新密码和确认新密码不一致');
        return false;
	}
	if(oldPassword == confirmNewPassword)
	{
	    alert('原密码和新密码一致');
        return false;
	}
	return true;
}

function save(){
	var oldPassword = $('#oldPassword').val();
	var newPassword = $('#newPassword').val();
	var confirmNewPassword = $('#confirmNewPassword').val();
	
   	if(psValidate(oldPassword,'原密码','pOldPassword') && psValidate(newPassword,'新密码','pNewPassword') && psValidate(confirmNewPassword,'确认新密码','pConfirmNewPassword') && psSameValidation(oldPassword,newPassword,confirmNewPassword))
   	{
	   $.ajax({
	        type: "POST",
	        url:"${tradeBasePath}/platform/user/updatepassword",
	        data:$('#formBean').serialize(),
	        error: function(request) {
	             alert('操作失败，请稍后重试');    
	        },
	        success : function(data) { 
				if(data.status == "OK"){
					 alert('密码修改成功,下次登录请使用新密码');    
		      	}else{
		      		 alert(data.message);     
		        }
		  }
	   });
	}
}

</script>