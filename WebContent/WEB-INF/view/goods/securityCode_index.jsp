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
<%@include file="/WEB-INF/view/common/head_css_typeahead.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp"%>
<%@include file="/WEB-INF/view/common/datepicker_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>产品防伪码管理</title>
<style>
.input-daterange {
  width: inherit !important;
}
td{
	word-break:keep-all;
}
</style>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<!-- <div class="collapse navbar-collapse" id="top-navbar-collapse"> -->
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/securityCode/index.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">产品名称</label> <input type="text"
									value="${parameter.entity.goodsName}" name="entity.goodsName" id="goodsName"
									autofocus class="typeahead form-control" placeholder="产品名称">
								<label class="">防伪码</label> <input type="text"
									value="${parameter.entity.code}" name="entity.code" id="code"
									autofocus class="typeahead form-control" placeholder="货物防伪码">
								<label class="">货物持有人</label> <input type="text"
									value="${parameter.entity.owner}" name="entity.owner" id="owner"
									autofocus class="typeahead form-control" placeholder="货物所有人">
								<label>录入时间</label>
								<div class="input-daterange input-group" id="datepicker">
										<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
										<input type="text" class="form-control pointer"
											value="${parameter.startTime }" id="startDate" data-date-format="yyyy-mm-dd"
											name="startTime" placeholder="开始日期" />
										<span class="input-group-addon">到</span> <input type="text"
											class="form-control pointer"
											value="${parameter.endTime }"
											name="endTime" placeholder="截止日期" />
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
								</div>

								<button type="submit" class="btn btn-primary" id="search"  data-toggle="tooltip" data-placement="bottom" title="点击查询防伪码"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
						<li><button type="button" data-toggle="tooltip" data-url="<c:url value='/securityCode/edit_num.html'/>" data-placement="bottom" title="点击录入新的防伪码"
									class="btn btn-info navbar-btn addBtn">
									<li class="fa fa-plus fa-fw"></li>按数量录入
								</button>&nbsp;</li>
							<li><button type="button" data-toggle="tooltip" data-url="<c:url value='/securityCode/edit.html'/>" data-placement="bottom" title="点击录入新的防伪码"
									class="btn btn-info navbar-btn addBtn">
									<li class="fa fa-plus fa-fw"></li>录入防伪码(带删除)
								</button></li>
						</ul>
					<!-- </div> -->
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
									<th>商品名称</th>
									<th>商品所有者</th>
									<th>代理编码</th>
									<th>防伪码</th>
									<th>录入时间</th>
									<th>录入人</th>
									<c:if test="${user.admin}">
									<th>操作</th>
									</c:if>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${codes}" var="g">
									<tr data-row="${g.id}">
										<td><input type="checkbox" value="${g.id}"></td>
										<td><a class="editBtn"
											href="<c:url value='/securityCode/edit.html?id=${g.id}' /> ">${g.goodsName}</a></td>
										<td>${g.owner}</td>
										<td>${g.agentCode}</td>
										<td>${g.code}</td>
										<td>${g.updateTime}</td>
										<td>${g.recorder}</td>
										<c:if test="${user.admin}">
										<td><a class="btn btn-success default editBtn"
											href="<c:url value='/securityCode/edit.html?id=${g.id}' /> "><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>修改</a>
											<a class="btn btn-danger default ajaxLink" data-role="delete"
											href="<c:url value='/securityCode/remove.json?id=${g.id}' /> "><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除</a>
										</td>
										</c:if>
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
				<div class="modal-footer"><div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$("#datepicker.input-daterange").datepicker({
				autoclose : true,
				language : "zh-CN",
				todayHighlight : true,
				todayBtn : "linked",
				format:"yyyy-mm-dd",
				endDate:new Date()
			});
		  $('[data-toggle="tooltip"]').tooltip();
			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						var url=$(this).attr("data-url");
						$("#myModal").load(
								url,
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
				e.stopPropagation();
				var method="POST";
				if($(this).attr("data-role")){
					method="DELETE";
					if(!window.confirm("删除防伪码后无法恢复,是否继续?")){
						return false;
					}
				}
				$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						$("#messageBody").empty().html("操作成功");
						$("#myModal").modal({backdrop:"static",show:true});
						window.setTimeout(function() {
							$("#search").click();
						}, 1000);
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
