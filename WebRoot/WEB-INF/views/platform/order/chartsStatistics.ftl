<div id="content-header">
    <div class="widget-title radius-3 w-pading">
        <h3 class="title">图表统计分析</h3>
    </div>
</div>
<div class="container-fluid">
    <div class="widget-content radius-3">
        <!--内容-->
        <form class="form-horizontal chart-form-items" id="searchForm" name="searchForm" action="${tradeBasePath}/order/orderStatistics"> 
            <div class="control-group order-group worktime-info">
           	       <span class="span1">统计时间</span>
                       <span class="span2">
                        <div class="fl input-group date form_date col-md-5" id="startDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                            <input class="form-control" placeholder="选择时间段" type="text" value="" readonly="" name="startDate">
                            <span class="input-group-addon">
                            	<span class="glyphicon glyphicon-calendar"></span>
                            </span>
                    	</div>
                    </span>
                   	<span class="ml-persent-1 fl">--</span>
                   	<span class="span2">
                        <div class="fl input-group date form_date col-md-5" id="endDate" data-date="" data-date-format="dd MM yyyy" data-link-format="yyyy-mm-dd">
                            <input class="form-control" placeholder="选择时间段" type="text" value="" readonly="" name="endDate">
                            <span class="input-group-addon">
                            	<span class="glyphicon glyphicon-calendar"></span>
                            </span>
            			</div>
           			</span>
           			<span class="span1">商户名称</span>
		            <span class="span2">
		                <select id="merchan" onchange="merchantsChange(this)">
		                    <option value="">请选择</option>
		                    <#list merchants as m>  
                                <option value="${m.merchantNo}">${m.merchantName}</option>
                            </#list>
		                </select>
		                <input type="hidden" name="merchantNo" value="" >
		            </span>
            </div>
            <!--<div id="appsCheckboxDiv" class="control-group order-group">
           		<span class="span1">应用</span>
            </div>
            <input type="hidden" id="appIds" name="appIds" value="" >-->
            <div class="control-group order-group">
                <span class="span1"></span>
            	<span class="buttondiv ml-persent-1"><a href="javascript:search();" class="button-a">查询</a></span>
            </div>
        </form>
    </div>
    <div class="widget-content container-fluid mt10 m10">
        <div class="panel-title mt10"><h4>健康钱包数据汇总</h4></div>
        <div class="row-fluid">
            <!--echarts begin-->
            <div id="lineBarCharts" style="width: 100%;height:600px;padding:15px 0;margin-top:25px;"></div>
            <!--echarts end-->
        </div>
    </div>
    
    
</div>
<script type="text/javascript" src="${tradeBasePath}/platform/lib/echarts/echarts.min.js?v=${js_version}"></script>
<script type="text/javascript">
    //初始化日期控件
	$('.form_date').datetimepicker({
	    format: "yyyy-mm-dd",
	    language: 'fr',
	    weekStart: 1,
	    todayBtn: 1,
	    autoclose: 1,
	    todayHighlight: 1,
	    startView: 2,
	    minView: 2,
	    forceParse: 0
	});
 // 基于准备好的dom，初始化echarts实例
    
    search();
    var lineCharts;
    
    function search(){
        var url = tradeBasePath + "/order/chartsStatisticsData/";
        var datas = $("#searchForm").serializeArray();
        // 异步加载数据
        $.post(url,datas).done(function(data) {
            lineCharts = echarts.init(document.getElementById('lineBarCharts'));
            var i = Math.floor(data.dayArray.length/30) < 0?0:Math.floor(data.dayArray.length/30);
            var option = {
                title: {
                    text: '健康钱包充值金额'
                },
                tooltip: {},
                legend: {
                    data:[{name:'金额',icon: 'circle'}]
                },
                xAxis: {
                    data: data.dayArray,
                    axisLabel:{  
                         interval:i,
                         rotate:-30,//-30度角倾斜显示  
                    }  
                },
                yAxis: {
                    
                }
                /*,
                series: [{
                    name: '净额',
                    type: 'line',
                    data: data.priceArray
                }]*/
            };
            
            
            var series = [];
            var legend = {};
            /*if(data.dataList.length >= 1){
                var legendData = [];
                for(var i = 0; i < data.dataList.length; i++){
                    var seriesData = {};
                    seriesData.name= "金额" + i;
                    seriesData.type= "line";
                    seriesData.data = data.dataList[i];
                    series.push(seriesData);
                    legendData[i] = seriesData.name;
                }
                legend.data=legendData;
                option.legend = legend;
            }*/
            var k = 0;
            var legendData = [];
            for (var key in data.dataList) {  
                var seriesData = {};
                seriesData.name= key;
                seriesData.type= "line";
                seriesData.data = data.dataList[key];
                series.push(seriesData);
                legendData[k] = seriesData.name;
                k++;
            }  
            legend.data=legendData;
                option.legend = legend;
            option.series = series;
           
            lineCharts.setOption(option);
        });
    }
    
    /**
    * 商户选择联动
    * @param obj
    */
    function merchantsChange(obj){
        var merchantNo = $(obj).find("option:selected").val();
        $(obj).parent().find(":hidden[name='merchantNo']").val(merchantNo);
        $.ajax({
            type: "POST",
            url: tradeBasePath + "/order/getApplicationByMerchan?merchantNo=" + merchantNo,
            success: function(response) {
                var html = "<span class='span1'>应用</span>";
                if (response.status == "OK") {
                    var list = response.result;
                    for(var i = 0;i<list.length;i++){
                        html += "<span class='span2'><input type='checkbox' value='" + list[i].appId + "' onclick='checkApps(this)'/><label>" + list[i].appName + "</label></span>";
                    }
                }
                $("#appsCheckboxDiv").html(html);
            }
        });
    }
    
    //选择应用
    function checkApps(obj){
        var appIds = $('#appIds').val();
        if($(obj).is(':checked')){
            if(appIds == ''){
                appIds = $(obj).val();
            }else{
                appIds += "," + $(obj).val();
            }
        }else{
            if(appIds != ''){
                var item = appIds.split(",");
                if(item.length > 0){
                    appIds = "";
                    for(var i = 0; i < item.length; i++){
                        if(item[i] != $(obj).val()){
                            appIds += item[i] + ",";
                        }
                    }
                    appIds = appIds.substring(0, appIds.length - 1);
                }
            }
        }
        $('#appIds').val(appIds);
    }
</script>