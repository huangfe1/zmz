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
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>用户中心</title>
<style>
.thumbnail {
	border: none !important;
	box-shadow: none;
	-webkit-box-shadow: none;
	background-color: transparent !important;
}

nav .thumbnail {
	margin-bottom: 2px !important;
}

#nav {
	border-bottom: 1px solid #e0e0e0;
	background-color: #fff;
}

#nav .thumbnail {
	margin-bottom: 0px;
	color: black;
	font-family: Tahoma, Helvetica, Arial, "Microsoft Yahei", STXihei,
		sans-serif;
}

#nav .thumbnail .caption {
	padding: 0px;
}

#nav .thumbnail>img {
	width: 60% !important;
	max-width: 72px !important;
}
body{
font-family: Tahoma, Helvetica, Arial, "Microsoft Yahei", STXihei,
		sans-serif;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/view/mall/mall_menu.jsp"></jsp:include>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/pmall/index.html'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/earth_21.png'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">购物首页</h6>
					</div>
				</a>
			</div>
			<div class="col-xs-4">
				<a class="thumbnail"
					href="<c:url value='/dmz/vmall/index.html'/>"> <img
					data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">代理商城</h6>
					</div>
				</a>
			</div>
			<div class="col-xs-4">
				<a class="thumbnail"
					href="<c:url value='/pmall/shopcart/index.html'/>"> <img
					data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">
							购物车<span class="badge">${pmshopcart.quantity}</span>
						</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	<div class="container">
		<br>
		<!-- 我的订单 -->
	<div class="row">
			<div class="col-xs-12">
				<button class="btn btn-info btn-block btn-lg" type="button" id="sw">
					<span class="glyphicon glyphicon-th-list"></span>自己下的订单 (三个月内)
				</button>
				<br>
				<div  class="collapse" id="collapseOrders">

					<c:forEach items="${orders}" var="order">
						<div style="border:1px solid #f00;"  class="panel panel-success">
							<div class="panel-heading">
									<h5>订单编号:&nbsp;${order.orderNo}</h5>
									<h5>下单时间:&nbsp;<fmt:formatDate type="BOTH" pattern="yyyy年MM月dd日 HH:mm:ss" value="${order.orderTime}"/></h5>
									<h5>物流单号:&nbsp;${order.logisticsCode}</h5>
									<h5>物流公司:&nbsp;${order.logistics}</h5>
								</div>
								<div class="table-responsive">
									<table class="table">
										<thead>
											<tr>
												<th>商品</th>
												<th>数量</th>
												<th>金额</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${order.items}" var="o">
												<tr>
													<td>${o.value.goodsName}</td>
													<td class="text-muted">${o.value.quantity}</td>
													<td>
														<%--<p>${o.value.amountPoints}积分</p>--%>
														<p>${o.value.amountMoney}元</p></td>
													
												</tr>
											</c:forEach>
										</tbody>
										<tfoot>
											<tr>
												<td colspan="2">${order.status.name}/${order.paymentStatus.name}</td>
													<td ><c:if test="${order.status eq 'SHIPPED' }">
															<button class="btn btn-success btn-xs receiveBtn" data-url="<c:url value='/pm/order/receive.json?id=${order.id}' />">确认收货</button>
														</c:if>
														<c:if test="${order.paymentStatus eq 'UNPAID' and order.status eq 'NEW' }">
															<a class="btn btn-warning btn-xs" href="<c:url value='/pm/order/onlinepay.html?id=${order.id}'/>"><span class="fa fa-money fa-fw"></span>去支付</a>
														</c:if>
														</td>
											</tr>
										</tfoot>
									</table>
								</div>
						</div>
				
					</c:forEach>

				</div>
			</div>
		</div>
		<br>
		<!-- 下级的订单 -->
			<div class="row">
			<div class="col-xs-12">
				<button class="btn btn-info btn-block btn-lg" type="button" id="other_sw">
					<span class="glyphicon glyphicon-th-list"></span>客户下的订单 (三个月内)
				</button>
				<br>
				<div  class="collapse" id="other_collapseOrders">

					<c:forEach items="${other_orders}" var="order">
						<div style="border:1px solid #f00;"  class="panel panel-success">
							<div class="panel-heading">
									<h5>订单编号:&nbsp;${order.orderNo}</h5>
									<h5>下单时间:&nbsp;<fmt:formatDate type="BOTH" pattern="yyyy年MM月dd日 HH:mm:ss" value="${order.orderTime}"/></h5>
									<h5>客户编码:&nbsp;${order.user.agentCode}</h5>
									<h5>客户姓名:&nbsp;${order.user.realName}</h5>
								</div>
								<div class="table-responsive">
									<table class="table">
										<thead>
											<tr>
												<th>商品</th>
												<th>数量</th>
												<th>金额</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${order.items}" var="o">
												<tr>
													<td>${o.value.goodsName}</td>
													<td class="text-muted">${o.value.quantity}</td>
													<td>
														<%--<p>${o.value.amountPoints}积分</p>--%>
														<p>${o.value.amountMoney}元</p></td>
													
												</tr>
											</c:forEach>
										</tbody>
										<tfoot>
											<tr>
												<td colspan="2">${order.status.name}/${order.paymentStatus.name}</td>
													<td ><c:if test="${order.status eq 'SHIPPED' }">
															<button class="btn btn-success btn-xs receiveBtn" data-url="<c:url value='/pm/order/receive.json?id=${order.id}' />">确认收货</button>
														</c:if>
														<c:if test="${order.paymentStatus eq 'UNPAID' and order.status eq 'NEW' }">
															<a class="btn btn-warning btn-xs" href="<c:url value='/pm/order/onlinepay.html?id=${order.id}'/>"><span class="fa fa-money fa-fw"></span>去支付</a>
														</c:if>
														</td>
											</tr>
										</tfoot>
									</table>
								</div>
						</div>
				
					</c:forEach>

				</div>
			</div>
		</div>
		<br>
<%-- 		<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-warning btn-block btn-lg"
					href="<c:url value='/transfer/my.html?from=${user.id}'/>" id="sw">
					<span class="glyphicon glyphicon-plane"></span>转货管理
				</a>
			</div>
		</div>
		<br> --%>
		<div class="row" id="uc">
			<div class="col-md-12 col-xs-12">
				<div class="thumbnail">
					<img data-src="" alt=""
						src="<c:url value='/dmz/agent/domain.html?id=${user.id}'/>"
						data-holder-rendered="true" class="product_img">
					<div class="caption">
						<h6 class="text-center">代理编码：${user.agentCode}</h6>

					</div>
				</div>
				<ul class="list-group">
					<%-- <li class="list-group-item"><span class="badge">${agent.accounts.benefitPointsBalance}</span>
						待付款订单</li>
					<li class="list-group-item"><span class="badge">${agent.accounts.benefitPointsBalance}</span>
						待收货订单</li> --%>
					<%--<li class="list-group-item"><span class="badge">${agent.accounts.pointsBalance}</span>--%>
						<%--积分余额</li>--%>
					<li class="list-group-item"><span class="badge">${agent.accounts.voucherBalance}</span>
						拥有代金券</li>
					<li class="list-group-item"><span class="badge">${agent.accounts.benefitPointsBalance}</span>
						拥有福利积分</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="modal fade" id="alertModal" tabindex="-1" role="dialog"
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
				<div class="modal-body" id="alertMessageBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$("#sw").click(function(){
				$("#collapseOrders").collapse('toggle');
			});
			
			$("#other_sw").click(function(){
				$("#other_collapseOrders").collapse('toggle');
			});
			
			$("#pwd").click(function(){
				$("#collapsePwd").collapse('toggle');
			});
			$("#auth").click(function(){
				$("#collapseAuth").collapse('toggle');
			});
			$("#backBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				window.history.back();
			});
			$(".receiveBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var method="POST";
				$.ajax({url:$(e.target).attr("data-url"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#alertMessageBody").empty().html("操作成功");
							$("#alertModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								window.location.reload(true);
							}, 2000);
							
						}else{
							$("#alertMessageBody").empty().html("操作失败,"+m.message).addClass("text-danger");
							$("#alertModal").modal({backdrop:"static",show:true});
						}
						
					}else{
						$("#alertMessageBody").empty().html("操作失败").addClass("text-danger");
						$("#alertModal").modal({backdrop:"static",show:true});
					}
				}});
			});
		});
	</script>
</body>
</html>
