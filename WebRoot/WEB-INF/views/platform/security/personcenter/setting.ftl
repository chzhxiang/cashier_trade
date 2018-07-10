<div id="content-header">
        <div class="widget-title radius-3"><h3 class="title">个人设置</h3></div>
    </div>
    <div class="container-fluid">
         <div class="row-fluid">
            <div class="widget-content radius-3">
                <!--内容-->
               <form id="formBean" class="form-horizontal">  
                    <input type="hidden" id="id" name="id" value="${user.id}" /> 
                    <input type="hidden" id="status" name="status" value="${user.status}" />       
                    <input type="hidden" id="roleIds" name="roleIds" value="" />    
                    <div class="control-group">
                         <label class="control-label" >账户</label>
                         <div class="controls"><input type="text" class="span6" readonly="readonly" value="${user.userName}" id="userName" name="userName"/></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >药材名</label>
                         <div class="controls"><input type="text" class="span6" readonly="readonly" value="${user.userRealName}" name="userRealName" /></div>
                    </div>
                     <div class="control-group">
                         <label class="control-label" >手机号码</label>
                         <div class="controls"><input type="text" class="span6" readonly="readonly" value="${user.userNumber}" id="userNumber" name="userNumber" /></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label" >创建日期</label>
                         <div class="controls"><input type="text" class="span6" readonly="readonly" value="${user.ct?string("yyyy-MM-dd")}" id="userNumber" name="userNumber" /></div>
                    </div>
                    <div class="control-group">
                         <label class="control-label">所属角色</label>
                         <div class="controls">
                              <div class="role-select span6">
                               <#list roleList as role>
                                   <label class="inline-block mw120" disabled="disabled">
                                        <#if role.hasFlag=='1'>
                                        
                                        <input class="states" type="checkbox" disabled="disabled" checked="checked" name="checkbox" value="${role.id}" />${role.roleName}
                                     
                                        <#else>
                                   
                                        <input class="states" type="checkbox" disabled="disabled" name="checkbox" value="${role.id}" />${role.roleName}
                                       
                                        </#if>   
                                    </label>
                                </#list>
                              </div>
                         </div>
                    </div>
                </form>
                <!--内容 end-->
            </div>
         </div>
    </div>
