    <#assign shiro=JspTaglibs["/WEB-INF/tlds/shiro.tld"]/>
    <link rel="stylesheet" href="${tradeBasePath}/platform/css/tree/metroStyle.css" type="text/css">
    <div id="content-header">
        <div class="widget-title radius-3"><h3 class="title">个人资料</h3></div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
            	<form id="curUserInfo">
            		<input type="hidden" value="${user.id}" name="id" id="id">
            		<input type="hidden" value="" name="roleIds" id="roleIds">
            		<input type="hidden" value="${user.roleIdStr}" name="roleIdStr" id="roleIdStr">
            		<input type="hidden" value="${user.account}" name="account" id="account">
                    <div class="row">
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label"><span class="asterisk">*</span>姓名</label>
                                <input type="text" class="form-control" value="${user.name}" name="name" id="name">
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label"><span class="asterisk">*</span>身份证号</label>
                                <input type="text" class="form-control" value="${user.idCardNo}" name="idCardNo" id="idCardNo">
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label"><span class="asterisk">*</span>性别</label>
                                <div class="rdio rdio-default mt6 ml10">
                                    <label class="radio-inline on">
                                         <input type="radio" name="sex" id="inlineRadio1" <#if user.sex == 1>checked="checked"</#if> value="1">男
                                    </label>
                                    <label class="radio-inline">
                                         <input type="radio" name="sex" id="inlineRadio2" <#if user.sex == 0>checked="checked"</#if> value="0">女
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                       <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label">出生年月</label>
                                <input type="text" class="form-control appDate" name="birthDay" value="${user.birthDay}" id="birthDay">
                            </div>
                       </div>
                       <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label"><span class="asterisk">*</span>手机号</label>
                                <input type="text" class="form-control" name="mobile" value="${user.mobile}" id="mobile">
                            </div>
                       </div>
                       <div class="col-sm-4">
                            <div class="form-group">
                                <label class="control-label">职称</label>
                                <input type="text" class="form-control" name="title" value="${user.title}" id="title">
                            </div>
                       </div>
                    </div>
                    <div class="row">
                       <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label">当值门诊</label>
                                <div class="clinic-list-choose radius" style="padding:10px;">
                                    <div class="row">
                                        <div class="col-sm-11">
                                             <span>珠江新城门诊部</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                       </div>
                    </div>
                    <div class="row">
                       <div class="col-sm-12">
                            <div class="form-group">
                                <label class="control-label">介绍</label>
                                <textarea class="form-control" rows="4" name="introduction" value="${user.introduction}" id="introduction"></textarea>
                            </div>
                       </div>
                    </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
         <div class="row-fluid mt10">
         	<div class="button-style bgfff radius-3">
          		<button class="btn btn-save" onClick="saveOrUpdate()">保存</button>    
       		</div>
         </div>
    </div>
<script>
$(document).ready(function() {
});


function saveOrUpdate(){   
   var name = $("#name").val();
   if(isNull(name)){
     	$Y.tips("姓名不能为空");
    	return;  
   }
   
   var iflag = regBox.regIdNumber.test($("#idCardNo").val());
   if(!iflag){
        $Y.tips("身份证号码输入有误");
        return;
   }

   var mflag = regBox.regMobile.test($("#mobile").val());
   var tflag = regBox.regTel.test($("#mobile").val());
   if ($("#mobile").val()!=''&&!mflag&&!tflag) {
    	$Y.tips("电话号码输入有误");
    	return;
   }
   
   var roleIdStr = $("#roleIdStr").val();
   if(!isNull(roleIdStr)){
   	   var roleIdArray = roleIdStr.split(",");
	   $("#roleIds").val(JSON.stringify(roleIdArray));
   }
   $.ajax({
       type: "POST",
       url:"${tradeBasePath}/platform/user/updateUserInfo",
       data:$('#curUserInfo').serialize(),
       error: function(request) {
            $Y.tips("操作失败");
       },
       success : function(data) { 
          if(data.status == "OK"){
            $Y.tips("操作成功",'success');
          }else{
            $Y.tips(data.message);
          }
       }
   });
}
</script>
