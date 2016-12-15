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
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp"%>
<%@include file="/WEB-INF/view/common/datepicker_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>转货确认</title>
<style>
.input-daterange {
  width: inherit !important;
}
</style>
</head>
<body style="overflow:hidden;">
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<%--<nav class="navbar navbar-default" role="navigation">--%>
				<div class="container-fluid">
					<%--<div class="collapse navbar-collapse" id="top-navbar-collapse">--%>
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/transfer/confirm.html'/>"
							method="GET">
							<div class="form-group">
							<label>申请人</label>
							<input type="text" name="applyAgent" value="${applyAgent}" class="form-control" id="applyAgent">
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
							<li><button type="button" data-url="<c:url value='/transfer/to.html?from=${user.id}' />"
									class="btn btn-primary navbar-btn toBtn">
									<li class="fa fa-mail-forward fa-fw"></li>转出货物
								</button></li>
						</ul>
					</div>
				<%--</div>--%>
			<%--</nav>--%>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>选择</th>
									<th>转货数量</th>
									<th>总金额</th>
									<th>产生积分</th>
									<th>使用的代金券</th>
									<th>申请人</th>
									<th>申请时间</th>
									<th>申请来源</th>
									<th>汇款信息</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${trans}" var="g">
									<tr data-row="${g.id}">
										<td><input type="checkbox" value="${g.id}"></td>
										<td>${g.quantity}</td>
										<td>${g.amount}</td>
										<td>${g.points}</td>
										<td>${g.voucher}</td>
										<td>${g.userByToAgent.realName}(${g.userByToAgent.agentCode})</td>
										<td>${g.applyTime}</td>
										<td>${g.applyOrigin.desc}</td>
										<td>${g.remittance}</td>
										<td>${g.status.desc}</td>
										<td>
										<a class="btn btn-info default detailBtn" href="<c:url value='/transfer/detail.html?id=${g.id}'/>">
										<span class="glyphicon glyphicon-list"></span>转货明细</a>
											<a class="btn btn-success default ajaxLink"
											href="<c:url value='/transfer/confirm.json?id=${g.id}' /> "><span class="fa fa-mail-forward" aria-hidden="true"></span>确认转货</a>
											<a class="btn btn-warning default ajaxLink"
											href="<c:url value='/transfer/refuse.json?id=${g.id}' /> "><span class="fa fa-times-circle" aria-hidden="true"></span>拒绝转货</a>
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
	<div class="modal fade" id="winModal" style="overflow-x:hidden;overflow-y:auto;" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header bg-primary">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h3 class="modal-title" id="myModalLabel">操作提示</h3>
				</div>
				<div class="modal-body" id="winMessageBody"></div>
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
	<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
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
			$(".toBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						var url=$(this).attr("data-url");
						$("#winModal").load(
								url,
								function(e) {
									$("#winModal").modal({
										backdrop : "static"
									});
								});
					});
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
				}
				if(!window.confirm("确定执行本次操作吗?")){
					return false;
				}
				var url=$(e.target).attr("href");
				$.ajax({url:url,method:method,complete:function(xhr,ts){
					/* if(xhr.status>=200 && xhr.status<300){ */

						var m=$.parseJSON(xhr.responseText);

						if(xhr.status>=200 && xhr.status<300){//黄小飞修改
						if(m.flag=="0"){
							$("#winMessageBody").empty().html("操作成功");
							$("#winModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								$("#search").click();
							}, 1500);							
						}else{
							$("#winMessageBody").empty().html("操作失败,"+m.message).addClass("text-danger");
							$("#winModal").modal({backdrop:"static",show:true});
						}
						
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
