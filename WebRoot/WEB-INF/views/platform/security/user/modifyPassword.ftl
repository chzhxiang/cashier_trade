    <#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
    <link rel="stylesheet" href="${tradeBasePath}/platform/css/tree/metroStyle.css" type="text/css">
    <div id="content-header">
        <div class="widget-title radius-3"><h3 class="title">修改密码</h3></div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
                 <form id="formBean" class="form-horizontal">  
                    <input type="hidden" id="id" name="id" value="${user.id}" />  
                    <div class="control-group">
                         <label class="control-label" ><span class="mandatory">*</span>账户名</label>
                         <div class="controls pt7">${user.account}</div>
                    </div>
                    <div class="control-group">
                         <label class="control-label"><span class="mandatory">*</span>原密码</label>
                         <div class="controls"><input type="password" class="span12" id="oldPassword" name="oldPassword" placeholder="请输入原密码6-16位字符(支持数字/字母)" /></div>
                    </div>

                    <div class="control-group">
                         <label class="control-label"><span class="mandatory">*</span>新密码</label>
                         <div class="controls"><input type="password" class="span12" id="newPassword" name="newPassword" placeholder="请输入新密码6-16位字符(支持数字/字母)"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label"><span class="mandatory">*</span>确认新密码</label>
                         <div class="controls"><input type="password" class="span12" class="span12" id="verifyNewPassword" name="verifyNewPassword" placeholder="请确认新密码6-16位字符(支持数字/字母)"/></div>
                    </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
         <div class="row-fluid mt10">
         	<div class="button-style bgfff radius-3">
          		<button class="btn btn-save" onClick="modifyPassword()">保存</button>    
       		</div>
         </div>
    </div>
<script>
$(document).ready(function() {
});


function modifyPassword(){  
   var userId = $("#id").val();
   var oldPassword = $("#oldPassword").val();
   var newPassword = $("#newPassword").val();
   var verifyNewPassword = $("#verifyNewPassword").val();
   var pwflag = regBox.regName.test(oldPassword);
   if(!pwflag){
        $Y.tips("用户原密码输入不合规范");
        return;
   }
   pwflag = regBox.regName.test(newPassword);
   if(!pwflag){
        $Y.tips("用户新密码输入不合规范");
        return;
   }
   pwflag =(verifyNewPassword == newPassword);
   if(!pwflag){
        $Y.tips("用户新密码输入不一致");
        return;
   }
   
   $.ajax({
       type: "POST",
       url:"${tradeBasePath}/platform/user/modifyPassword",
       data:{userId:userId , oldPassword:oldPassword , newPassword:newPassword},
       error: function(request) {
           $Y.tips("修改失败");
       },
       success : function(data) { 
		   if(data.result.isSuccess){
		       $Y.tips(data.result.msg,'success');
		   }else{
           	   $Y.tips(data.result.msg);
           }
       }
   });
}
</script>
