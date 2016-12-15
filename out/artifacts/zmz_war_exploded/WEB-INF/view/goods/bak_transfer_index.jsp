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
<title>我要转货</title>
<style>
.input-daterange {
  width: inherit !important;
}
td{
	word-break:keep-all;
}
</style>
</head>
<body  style="overflow:hidden;">
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/transfer/my.html'/>"
							method="GET">
							<input type="hidden" name="from" value="${user.id}">
							<div class="form-group">
								<label class="">产品名称</label> <input type="text"
									value="${parameter.entity.name}" name="entity.name" id="name"
									autofocus class="typeahead form-control" placeholder="产品名称">
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<!-- <ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增产品
								</button></li>
						</ul> -->
					</div>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>选择</th>
									<th>产品名称</th>
									<th>当前余额</th>
									<th>价格</th>
									<th>是否主打产品</th>
									<th>授权状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${goods}" var="g">
									<tr data-row="${g.goods.id}" <c:if test="${g.goods.mainGoods}"> class="danger"</c:if>>
										<td><input type="checkbox" value="${g.goods.id}"></td>
										<td>${g.goods.name}</a></td>
										<td>${g.currentBalance}</td>
										<td>${g.price}</td>
										<td>${g.goods.benchmark ? "是" : "否" }</td>
										<td>${g.status.desc}</td>
										<td>
										
										<a class="btn btn-success default editBtn" 
											<c:if test="${g.status!='ACTIVE' }">disabled="disabled"</c:if>
											href="<c:url value='/transfer/from.html?id=${g.goods.id}' /> "><span class="fa fa-cloud-download" aria-hidden="true"></span>申请转货</a>
											<a class="btn btn-warning default editBtn"
											<c:if test="${g.status!='ACTIVE' }">disabled="disabled"</c:if>
											href="<c:url value='/transfer/to.html?id=${g.goods.id}' /> "><span class="fa fa-mail-forward" aria-hidden="true"></span>转出货物</a>
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
	<div class="modal fade" id="selectModal" tabindex="-2" role="dialog" style="z-index:999999;"
		aria-labelledby="selectModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="selectModalLabel"></h4>
				</div>
				<div class="modal-body" id="selectBody"></div>
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
								"<c:url value='/transfer/from.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					});
			$(".editBtn").click(function(e){
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
