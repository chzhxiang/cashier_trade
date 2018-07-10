<div class="container-fluid">
     <div class="row-fluid">
        <div class="row-fluid">
     		<div class="span6"  style="font-size: 20px">
     			<strong>当日消费金额上限阈值设置</strong>
     		</div>
     		<div class="span4 offset2">
     			当前阀值：${sysRuleConfig.ruleValue}元
     		</div>
     	</div>
	    <div class="row-fluid mt20">
	        <form id="formBean" class="form-horizontal">
	            <input type="hidden" id="ruleCode" name="ruleCode" value="${sysRuleConfig.ruleCode}">
			 	<input type="hidden" id="id" name="id" value="${sysRuleConfig.id}">
	            <div class="control-group">
                    <div class="input-group-flex">
                        <label class="control-label" style="width:145px">当日消费金额最高阈值</label>
                        <div class="controls" style="margin-left: 157px">
                            <input type="text" id="ruleValue" name="ruleValue" class="span8" placeholder="请输入阀值">元
                        </div>
                    </div>
	            </div>
	        </form>
	    </div>
	    <div class="row-fluid mt20">
	        <div class="alert">
	            <strong>提示：</strong>
	                          当账户当日消费金额大于阈值时，将触发预警
	        </div>
	    </div>
        
     </div>
 </div>
<script>

function saveRule(){
	var ruleValue = $('#ruleValue').val();
	if (ruleValue == "") {
		alert("请输入阀值！");
		return;
	}
	
	$.ajax({
        type: "POST",
        url:"${tradeBasePath}/platform/sysRuleConfig/save",
        data:$('#formBean').serialize(),
        error: function(request) {
             alert('操作失败，请稍后重试');    
        },
        success : function(data) { 
			if(data.status == "OK"){
				 alert('修改成功'); 
				 $('#configAddModal').modal('hide');   
	      	}else{
	      		 alert(data.message);     
	        }
	  }
   });
}

</script>