<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		
		$("#addBtn").click(function(){
			
			
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
    			autoclose: true,
    			todayBtn: true,
    			pickerPosition: "bottom-left"
			});
			
			
			
			$.ajax({
				url:"workbench/activity/getUserList.do",
				data:{},
				type:"get",
				dataType:"json",
				success:function(data){
					
					var html = "<option></option>";
					$.each(data,function(i,e){
						html += "<option value='"+e.id+"'>"+e.name+"</option>"
					})
					$("#create-owner").html(html);
					//在js中用EL表达式一定要在字符串中
					var id = "${user.id}";
					$("#create-owner").val(id);
					
					$("#createActivityModal").modal("show");
				}
				
			})
		
		});
		
		$("#saveBtn").click(function(){
			
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					 "owner":$("#create-owner").val(),
					 "name":$("#create-name").val(),
					 "startDate":$("#create-startDate").val(),
					 "endDate":$("#create-endDate").val(),
					 "cost":$("#create-cost").val(),
					 "description":$("#create-description").val()
				},
				dataType:"json",
				type:"post",
				success:function(data){
					
					if(data.success){
						//刷新市场活动信息列表
						/* 
						$("#activityPage").bs_pagination('getOption', 'currentPage')
							操作后停留在当前页
						$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
							操作后维持已经设置好的每页展示的记录数
						*/
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						
						$("#activityAddForm")[0].reset();
						$("#createActivityModal").modal("hide");
						
						
					}else{
						alert("添加失败");
					}
				}
				
			})
		
		})
		//页面加载默认执行
		pageList(1,2);

		$("#searchBtn").on("click",function(){
			/*
				点击查询按钮时，应该将搜索框中信息保存起来
				保存在隐藏域中
			
			*/
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

			pageList(1,2);
		})
		

		
		$("#qx").click(function(){
			$("input[name=xz]").prop("checked",this.checked);
		})
		
		
		/* 
			$("input[name=xz]").click(function(){
				
			})
			是无效的，动态生成元素不能以普通绑定事件进行操作
			
			只能以on方法的形式触发事件：
				$(需要绑定的元素的有效的外层元素).on(绑定的事件，需要绑定的元素的jQuery对象，回调函数)
		*/
		$("#activityBody").on("click",$("input[name=xz]"),function(){
			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
		})
		
		$("#deleteBtn").click(function(){
			var $xz = $("input[name]:checked")
			
			if($xz.length == 0){
				alert("请选择要删除的记录");
				
			}else{
				
				if(confirm("确定删除？")){
					
					var param = "";
					
					for(var i=0;i<$xz.length;i++){
						param += "id=" + $($xz[i]).val();
						
						
						if(i < $xz.length-1){
							param+="&"
						}
					}
					
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){
							if(data.success){
								//删除后回到第一页，维持每页展示的记录数
								
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除失败");
							}
						}
					})
					
				}
				
				
				
				
			}
			
			
		})
		
		$("#editBtn").click(function(){
			
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length == 0){
				alert("请选择修改记录")
			}else if($xz.length > 1){
				alert("一次只能修改一条记录")
			}else{
				//只有一个元素
				var id = $xz.val();
				
				$.ajax({
					url:"workbench/activity/getAll.do",
					data:{"id":id},
					type:"get",
					dataType:"json",
					success:function(data){
						/* 
							用户列表
							市场活动对象
							
						*/
						var html="<option></option>";
						$.each(data.uList,function(i,e){
							html += "<option value='"+e.id+"'>"+e.name+"</option>"
						})
						$("#edit-owner").html(html);
						
						$("#edit-id").val(data.a.id)
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);
						
						
						$("#editActivityModal").modal("show");
					}
				})	
			}
		})
		
		$("#updateBtn").click(function(){
			
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					 "id":$("#edit-id").val(),
					 "owner":$("#edit-owner").val(),
					 "name":$("#edit-name").val(),
					 "startDate":$("#edit-startDate").val(),
					 "endDate":$("#edit-endDate").val(),
					 "cost":$("#edit-cost").val(),
					 "description":$("#edit-description").val()
				},
				dataType:"json",
				type:"post",
				success:function(data){
					
					if(data.success){
						//刷新市场活动信息列表
						//修改后停留在当前页，维持每页展示的记录数
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#editActivityModal").modal("hide");
					}else{
						alert("修改失败");
					}
				}
				
			})
		})
		
		
	});
	
	
	/*
	所有关系型数据库，做前端的分页相关操作的基础组件
	pageNo：页码
	pageSize：每页展示的记录数
	
	pageList方法：发出Ajax请求，从后台取得最新的市场活动列表数据，
		通过响应回来的数据，局部刷新市场活动信息列表
	1.点击左侧“市场活动”超链接，需要刷新市场活动列表，调用pageList
	2.添加，修改，删除后
	3.点击查询按钮
	3.点击分页组件
	以上为pageList方法制定了六个入口
	
	
	*/
	
	function pageList(pageNo,pageSize){
		//将全选的√去掉
		$("#qx").prop("checked",false);
		
		//查询前，将隐藏域中信息取出来，重新赋予到搜索框
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		
		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$("#search-name").val(),
				"owner":$("#search-owner").val(),
				"startDate":$("#search-startDate").val(),
				"endDate":$("#search-endDate").val()
				
			},
  			type:"get",
			dataType:"json",
			success:function(data){
        		
				/*
        		data
        			市场活动信息列表
        			分页插件需要：总记录数
        		*/
        		
        		var html="";
        		
        		$.each(data.dataList,function(i,e){
        			
        			html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+e.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+e.id+'\';">'+e.name+'</a></td>';
                    html += '<td>'+e.owner+'</td>';
					html += '<td>'+e.startDate+'</td>';
					html += '<td>'+e.endDate+'</td>';
					html += '</tr>';
        		})
        		
        		$("#activityBody").html(html);
        		
        		//计算总页数
				var totalPages = data.total%pageSize == 0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;

        		
        		//数据处理后结合分页查询，展示分页信息
        		$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true, 
					
					
					//该回调函数，在点击分页组件时触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
			   });

        		
			}
		})
		
	}
	
	
	
	
	
	
	
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>
	

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
					<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
					<!-- 
						<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>
					</tbody>
					 -->
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				
				<div id="activityPage"></div>
				
			</div>
			
		</div>
		
	</div>
</body>
</html>