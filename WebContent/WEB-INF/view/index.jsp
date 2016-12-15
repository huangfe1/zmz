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
<%@include file="/WEB-INF/view/common/head_css_morris.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_treeview.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h4 class="page-header">我的工作台<small><a href="<c:url value='/profile/edit.html?id=${user.id}'/>" class="btn btn-link pull-right editBtn">
					<span class="glyphicon glyphicon-edit"></span>&nbsp;修改资料</a></small></h4>
					 
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<c:if test="${user.agent}">
			<div class="row">
				<div class="col-lg-3 col-md-6 col-xs-12 recordBtn" data-url="<c:url value='/agent/myAuth.html?from=${user.id}#auths'/>">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-users fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge" id="newer">${fn:length(user.authorizations)}</div>
									<div>我的授权</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/agent/myAuth.html?from=${user.id}#auths'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12">
					<div class="panel panel-green recordBtn" data-url="<c:url value='/transfer/records.html?from=${user.id}'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-tasks fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${transferNewer}</div>
									<div>转货请求</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/transfer/records.html?from=${user.id}'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12 recordBtn" data-url="<c:url value='/agent/myAuth.html?from=${user.id}#basic'/>">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-support fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${user.accounts.voucherBalance}</div>
									<div>我的代金券</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/agent/myAuth.html?from=${user.id}#basic'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12">
					<div class="panel panel-yellow recordBtn" data-url="<c:url value='/delivery/agent/confirm.html'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-comments fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${deliveryNewer}</div>
									<div>发货申请</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/delivery/agent/confirm.html'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12">
					<div class="panel panel-primary recordBtn" data-url="<c:url value='/transfer/my.html?from=${user.id}'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-truck fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>转货管理</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/transfer/my.html?from=${user.id}'/>">
							<div class="panel-footer">
								<span class="pull-left">主动转出货物给客户</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12">
					<div class="panel panel-success recordBtn" data-url="<c:url value='/agent/myAuth.html?from=${user.id}'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-list fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>我的账户</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/agent/myAuth.html?from=${user.id}'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				
				<div class="col-lg-3 col-md-6 col-xs-12" >
					<div class="panel panel-red recordBtn" data-url="<c:url value='/agent/children.html?from=${user.id}'/>">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-users fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>我的客户</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/agent/children.html?from=${user.id}'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12 recordBtn" data-url="<c:url value='/delivery/mobile.html?from=${user.id}'/>">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-truck fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>发货管理</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/delivery/mobile.html?from=${user.id}'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6 col-xs-12 recordBtn" data-url="<c:url value='/securityCode/index.html'/>">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-shield fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div>防伪码</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/securityCode/index.html'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
			</div>
			</c:if>
<c:if test="${user.admin and (user.loginName eq 'zmzcaohai')}">
			<div class="row">
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-users fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge" id="newer">${newer}</div>
									<div>新的注册</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/agent/audit/index.html'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>

				<div class="col-lg-3 col-md-6">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<%--<i class="fa fa-tasks fa-5x"></i>--%>
								</div>
								<div class="col-xs-12 ">
									<div class="huge">${sumVoucher}</div>
									<div>市场代金券总数</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='#'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-tasks fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${transferNewer}</div>
									<div>转货请求</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/transfer/confirm.html'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-yellow">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-comments fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${deliveryNewer}</div>
									<div>发货申请</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/delivery/index.html'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
				<div class="col-lg-3 col-md-6">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-3">
									<i class="fa fa-shopping-cart fa-5x"></i>
								</div>
								<div class="col-xs-9 text-right">
									<div class="huge">${ empty newOrder ? 0 : newOrder}</div>
									<div>积分订单</div>
								</div>
							</div>
						</div>
						<a href="<c:url value='/pm/order/index.html?entity.status=NEW'/>">
							<div class="panel-footer">
								<span class="pull-left">查看详细</span> <span class="pull-right"><i
									class="fa fa-arrow-circle-right"></i></span>
								<div class="clearfix"></div>
							</div>
						</a>
					</div>
				</div>
			</div>
			</c:if>
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
				<div class="modal-body"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<%-- <jsp:include page="/WEB-INF/view/common/morris.jsp"></jsp:include> --%>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<%-- <jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/treeview.jsp"></jsp:include>
	 --%>
	 <script type="text/javascript">
		$(function() {
			$(".editBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$("#indexModal").load($(this).attr("href"),function(e) {
							$(this).modal({
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
		});
	</script>
</body>
</html>
