<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="${keywords}">
<meta http-equiv="description" content="">
<%@include file="/WEB-INF/view/common/head_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>授权类型管理</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/authType/index.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">类型名称</label> <input type="text"
									value="${parameter.entity.name}" name="entity.name" id="name"
									autofocus class="typeahead form-control" placeholder="授权类型名称">
								<label class="">产品名称</label> <input type="text"
									value="${parameter.entity.goods.name}" name="entity.goods.name" id="goodsName"
									autofocus class="typeahead form-control" placeholder="授权产品名称">
								
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<!-- <ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增授权类型
								</button></li>
						</ul> -->
					</div>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr class="bg-success">
									<th>选择</th>
									<th>授权类型</th>
									<th>说明</th>
									<th>授权产品</th>
									<th>显示顺序</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${authTypes}" var="g">
									<tr data-row="${g.id}">
										<td><input type="checkbox" value="${g.id}"></td>
										<td><a class="editBtn"
											href="<c:url value='/authType/edit.html?id=${g.id}' /> ">${g.name}</a></td>
										<td>${g.remark}</td>
										<td>
											<a href="<c:url value='/goods/detail.html?id=${g.goods.id}' />" class="btn detailBtn"><span class="label label-primary">${g.goods.name}</span>
											</a>
										</td>
										<td>${g.order}</td>
										<td><a class="btn btn-success default editBtn"
											href="<c:url value='/authType/edit.html?id=${g.id}' /> "><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>修改</a>
											<a class="ajaxLink btn btn-danger default" data-role="delete"
											href="<c:url value='/authType/remove.json?id=${g.id}' /> "><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<jsp:include page="/WEB-INF/view/common/pager.jsp"></jsp:include>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body" id="messageBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/authType/edit.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					});
			$(".editBtn,.detailBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$("#myModal").load($(this).attr("href"),function(e) {
							$('#myModal').modal({
								backdrop : "static"
							});
						});
			});
			$("#dataList a.ajaxLink").click(function(e){
				e.preventDefault();
				var method="POST";
				if($(this).attr("data-role")){
					method="DELETE";
				}
				$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						$("#messageBody").empty().html("操作成功");
						$("#myModal").modal({backdrop:"static",show:true});
						$("#search").click();
					}else{
						$("#messageBody").empty().html("操作失败").addClass("text-danger");
						$("#myModal").modal({backdrop:"static",show:true});
					}
				}});
			});
			$("tbody tr[data-row]").each(function(index,row){
				$(row).click(function(event){
					if(event.target.nodeName!="INPUT"){
						rowSelect($(this));
					}
					switchCss($(this));
				});
				$(row).bind("contextmenu",function(){
					return false;
				});
			});
		});
	</script>
</body>
</html>
