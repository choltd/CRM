<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
	/*
		根据交易表中的不同阶段的数量进行统计，最终形成一个饼图
		
	*/
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="ECharts/echarts.min.js"></script>
<script src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript">

	$(function(){
		
		getChatrs();
	});
	function getChatrs(){
		
		$.ajax({
			url:"workbench/transaction/getCharts.do",
			data:{},
			dataType:"json",
			type:"get",
			success:function(data){
				
				var myChart = echarts.init(document.getElementById('main'));
				
				// 指定图表的配置项和数据
		        var option = {
		        	    legend: {
		        	        top: 'bottom'
		        	    },
		        	    
		        	    series: [
		        	        {
		        	            name: '交易阶段',
		        	            type: 'pie',
		        	            radius: [50, 250],
		        	            center: ['50%', '50%'],
		        	            roseType: 'area',
		        	            itemStyle: {
		        	                borderRadius: 8
		        	            },
		        	            data: data.dataList
		        	            
		        	        }
		        	    ]
		        	};
		     // 使用刚指定的配置项和数据显示图表。
		        myChart.setOption(option);
			}
			
		});
		
	}
	
</script>
</head>
<body>
	<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
	<div id="main" style="width: 900px;height:600px;"></div>
</body>
</html>