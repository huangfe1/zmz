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
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>发货管理</title>
<style>
.input-daterange {
  width: inherit !important;
}
td{
	word-break:keep-all;
}
.fromBtn,.toBtn,.backBtn{
	cursor:pointer;
}
</style>
</head>
<body  style="overflow:hidden;">
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
		<div class="row">
				<div class="col-lg-12">
					<h4 class="page-header">发货管理</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-xs-12">
					<div class="panel panel-yellow toBtn" data-url="<c:url value='/delivery/edit.html'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-truck fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>申请发货</div>
								</div>
							</div>
						</div>
						<a href="#">
							<div class="panel-footer">
								<span class="pull-left">向公司申请把自己库存余额的货物发出</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-6 col-md-6 col-xs-12">
					<div class="panel panel-green fromBtn" data-url="<c:url value='/delivery/agent/confirm.html'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-money fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>店铺订单</div>
								</div>
							</div>
						</div>
						<a href="#">
							<div class="panel-footer">
								<span class="pull-left">确认客户在您商城下的单、确认后公司将尽快发货</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<div class="modal fade" id="editModal" style="overflow-x:hidden;overflow-y:auto;" tabindex="-1" role="dialog"
		aria-labelledby="editModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="editModalLabel"></h4>
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
	<div class="modal fade" id="winModal" tabindex="-2" role="dialog" style="z-index:999999;"
		aria-labelledby="winModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="winModalLabel"></h4>
				</div>
				<div class="modal-body" id="winBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
	$(function() {
		
		$(".toBtn").click(
				function(e) {
					e.preventDefault();
					e.stopPropagation();
					var url=$(this).attr("data-url");
					$("#editModal").load(
							url,
							function(e) {
								$('#editModal').modal({
									backdrop : "static"
								});
							});
		});
		$(".fromBtn").click(function(e){
			e.preventDefault();
			e.stopPropagation();
			var url=$(this).attr("data-url");
			window.location.href=url;
		});
	});
	</script>
</body>
</html>
