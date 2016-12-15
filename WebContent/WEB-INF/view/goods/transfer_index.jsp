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
<title>我要转货</title>
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
					<h4 class="page-header">进货操作</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-xs-12">
					<div class="panel panel-yellow toBtn" data-url="<c:url value='/transfer/to.html?from=${user.id}' />">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-truck fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>转给客户</div>
								</div>
							</div>
						</div>
						<a href="#">
							<div class="panel-footer">
								<span class="pull-left">把自己的货物直接转给别人(不会返利)</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<%-- <div class="col-lg-6 col-md-6 col-xs-12">
					<div class="panel panel-green fromBtn" data-url="<c:url value='/transfer/from.html?from=${user.id}' />">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-money fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>申请进货</div>
								</div>
							</div>
						</div>
						<a href="#">
							<div class="panel-footer">
								<span class="pull-left">填写向上级进货的申请订单</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div> --%>
                <c:if test="${teqVip}">
                    <div class="col-lg-6 col-md-6 col-xs-12">
                        <div class="panel panel-red backBtn" data-url="<c:url value='/transfer/to.html?back=1&from=${user.id}' />">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-cloud-upload fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div>退回上级</div>
                                    </div>
                                </div>
                            </div>
                            <a href="#">
                                <div class="panel-footer">
                                    <span class="pull-left">大区把货物退给公司</span> <span class="pull-right"><i
                                        class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>


                </c:if>


				<%--<div class="col-lg-6 col-md-6 col-xs-12">--%>
					<%--<div class="panel panel-primary recordBtn" data-url="<c:url value='/transfer/records.html?from=${user.id}' />">--%>
						<%--<div class="panel-heading">--%>
							<%--<div class="row">--%>
								<%--<div class="col-xs-3">--%>
									<%--<i class="fa fa-cloud-upload fa-5x"></i>--%>
								<%--</div>--%>
								<%--<div class="col-xs-9 text-right">--%>
									<%--<div>业务明细</div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<a href="#">--%>
							<%--<div class="panel-footer">--%>
								<%--<span class="pull-left">进货、退货的操作明细</span> <span class="pull-right"><i--%>
									<%--class="fa fa-arrow-circle-right"></i></span>--%>
								<%--<div class="clearfix"></div>--%>
							<%--</div>--%>
						<%--</a>--%>
					<%--</div>--%>
				<%--</div>--%>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal" style="overflow-x:hidden;overflow-y:auto;" tabindex="-1" role="dialog"
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
			$(".fromBtn,.toBtn,.backBtn").click(
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
			$(".recordBtn").click(function (e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("data-url");
				window.location.href=url;
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
		});
	</script>
</body>
</html>
