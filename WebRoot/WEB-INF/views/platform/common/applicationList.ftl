<div class="modal-body">
    <div class="modal-body-checkbox">
        <table class="table table-bordered table-textCenter table-striped table-hover">
            <thead>
            <tr>
                <th width="30" ><input type="checkbox" onclick="selectAll(this)"></th>
                <th>平台名称</th>
                <th>平台编码</th>
                <th>是否是省/城市app</th>
            </tr>
            </thead>
            <tbody>
            <#list entityList as entity>
            <tr>
                <td style="padding: 5px 5px;"><input type="checkbox" value="${entity.id}" name="applicationKey"></td>
                <td style="padding: 5px 5px;">${entity.name}</td>
                <td style="padding: 5px 5px;">${entity.code}</td>
                <td style="padding: 5px 5px;">
                    <#if entity.isCityApp == '0'>
                                                                    否
                    <#else>
                                                                    是
                    </#if>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
     </div>
 </div>
<!-- 模态框（Modal） -->
<script type="text/javascript">
function selectAll(obj){
    $("[name=applicationKey]:checkbox").each(function() {
          $(this).attr('checked' , $(obj).is(':checked'))
     });
}

function getSelectKey(){
    var applicationIds ="";
     $("[name=applicationKey]:checkbox").each(function() {
           if($(this).is(':checked')){
                if(applicationIds.length > 0){
                    applicationIds += ",";
                }
                applicationIds += $(this).val();
           }
     });
     
     return applicationIds;
}
</script>