<div class="modal-body">
    <div class="modal-body-checkbox">
        <table class="table table-bordered table-textCenter table-striped table-hover">
            <thead>
            <tr>
                <th width="30" ><input type="checkbox" onclick="selectAll(this)"></th>
                <th>交易方式名称</th>
                <th>交易方式编码</th>
                <th>交易方式描述</th>
            </tr>
            </thead>
            <tbody>
            <#list entityList as entity>
            <tr>
                <td style="padding: 5px 5px;"><input type="checkbox" value="${entity.id}" name="tradeModeKey"></td>
                <td style="padding: 5px 5px;">${entity.name}</td>
                <td style="padding: 5px 5px;">${entity.code}</td>
                <td style="padding: 5px 5px;">${entity.tradeDesc}</td>
            </tr>
            </#list>
            </tbody>
        </table>
     </div>
 </div>
<!-- 模态框（Modal） -->
<script type="text/javascript">
function selectAll(obj){
    $("[name=tradeModeKey]:checkbox").each(function() {
          $(this).attr('checked' , $(obj).is(':checked'))
     });
}

function getSelectKey(){
    var tradeModeIds ="";
     $("[name=tradeModeKey]:checkbox").each(function() {
           if($(this).is(':checked')){
                if(tradeModeIds.length > 0){
                    tradeModeIds += ",";
                }
                tradeModeIds += $(this).val();
           }
     });
     
     return tradeModeIds;
}
</script>