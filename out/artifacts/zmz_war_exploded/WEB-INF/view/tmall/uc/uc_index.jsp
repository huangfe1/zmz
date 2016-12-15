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

</style>
</head>
<body>
<jsp:include page="/WEB-INF/view/mall/mall_menu.jsp"></jsp:include>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/dmz/vmall/index.html'/>"> <img data-src="" alt=""
					src="<c:url value='/resources/images/user.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">购物首页</h6>
					</div>
				</a>
			</div>
			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/vmall/uc/promoCode.html'/>" > <img data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">推广码</h6>
					</div>
				</a>
			</div>
			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/vmall/shopcart/index.html'/>"> <img data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">
							购物车<span class="badge">${shopcart.quantity}</span>
						</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	<div class="container">
	<%-- 	<div class="row">
			<div class="col-xs-12">
			<button class="btn btn-danger btn-block btn-lg" type="button" style="margin-top:10px;"  id="auth"><span class="glyphicon glyphicon-leaf"></span>我的授权</button>
					<div class="collapse" id="collapseAuth">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>授权类型</th>
									<th>产品名称</th>
									<th>当前状态</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${my.authorizations}" var="l">
									<tr data-row="${l.id}">
										<td>${l.authorizationType.name}</td>
										<td>${l.authorizedGoods.name}</td>
										<td>${l.status.desc}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
			</div>
		</div>--%>
		<br> 

	<%-- 	<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-warning btn-block btn-lg" href="<c:url value='/transfer/my.html?from=${user.id}'/>" id="sw">
					<span class="glyphicon glyphicon-plane"></span>转货管理
				</a>
			</div>
		</div> --%>
		
		<!--我的账户 -->
				<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-info btn-block btn-lg" href="<c:url value='/agent/myAuth.html?from=${user.id}'/>" id="sw">
					<span class="glyphicon glyphicon-user"></span>&nbsp我的账户
				</a>
			</div>
		</div>
		<br>
		
		
		<!-- 向上家进货 -->
		<%-- 	<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-warning btn-block btn-lg" href="<c:url value='/transfer/my.html?from=${user.id}'/>" id="sw">
					<span class="glyphicon glyphicon-plane"></span>&nbsp进货操作
				</a>
			</div>
		</div>
		<br>
		--%>
			<!-- 下级的请求 -->
			<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-warning btn-block btn-lg" href="<c:url value='/transfer/records.html?from=${user.id}'/>" id="sw">
					<span class="glyphicon glyphicon-plane"></span>&nbsp下级转货订单
				</a>
			</div>
		</div>
		<br> 
		<!-- 店铺订单 -->
			<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-warning btn-block btn-lg" href="<c:url value='/delivery/agent/confirm.html'/>" id="sw">
					<span class="glyphicon glyphicon-plane"></span>&nbsp直接发货订单
				</a>
			</div>
		</div>
		<br> 
		
		
		
		<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-success btn-block btn-lg" href="<c:url value='/delivery/mobile.html?from=${user.id}'/>" id="sw">
					<span class="fa fa-truck"></span>我要发货
				</a>
			</div>
		</div>
		<br>
		<!-- 我的客户 -->
				<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-success btn-block btn-lg" href="<c:url value='/agent/children.html?from=${user.id}'/>" id="sw">
					<span class="fa fa-truck"></span>我的客户
				</a>
			</div>
		</div>
		<br>
		
		
				<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-success btn-block btn-lg" href="<c:url value='/securityCode/index.html'/>" id="sw">
					<span class="fa fa-truck"></span>防伪录入
				</a>
			</div>
		</div>
		<br>
		
		
		<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-primary btn-block btn-lg" href="<c:url value='/index.html'/>" id="sw">
					<span class="fa fa-desktop"></span>管理后台
				</a>
			</div>
		</div>
		
		<br>
		<div class="row">
			<div class="col-xs-12">
				<a class="btn btn-default btn-block btn-lg" href="<c:url value='/agent/logout'/>" id="sw">
					<span class="fa fa-power-off"></span>退出系统
				</a>
			</div>
		</div>
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
								<%--<li class="list-group-item"><span class="badge">${my.accounts.pointsBalance}</span>--%>
								<%--积分余额--%>
								<%--</li>--%>
								<li class="list-group-item"><span class="badge">${my.accounts.voucherBalance}</span>
								拥有代金券
								</li>
								<li class="list-group-item"><span class="badge">${my.accounts.benefitPointsBalance}</span>
								拥有福利积分
								</li>
							</ul>
			</div>
		</div>
		<!-- <div class="row">
			<div class="col-xs-12">
			<button class="btn btn-warning btn-block btn-lg" type="button"  id="pwd"><span class="glyphicon glyphicon-eye-close"></span>修改密码</button>
					<div class="collapse" id="collapsePwd">
					<div class="well">
						<form class="form">
							<div class="form-group">
							<label>原密码</label>
							<input type="password" class="form-control input-lg" name="oldPassword" id="oldPassowrd" placeholder="请输入您原来的密码">
							<label>新密码</label>
							<input type="password" class="form-control input-lg" name="password" id="passowrd" placeholder="请输入新密码">
							<label>确认密码</label>
							<input type="password" class="form-control input-lg" name="rePassword" id="rePassowrd" placeholder="请再次输入新密码">
							
							</div>
							<button type="submit" class="btn btn-lg btn-block btn-lg btn-primary"><span class="glyphicon glyphicon-save"></span>修改</button>
						</form>
					</div>
					</div>
			</div>
		</div> -->
		
		<!-- 我的订单 -->
		<div class="row">
			<div class="col-xs-12">
		<button class="btn btn-info btn-block btn-lg" type="button"  id="sw"><span class="glyphicon glyphicon-th-list"></span>&nbsp我的订单</button>
		<div class="collapse" id="collapseOrders">
		<c:if test="${empty orders }">
			<h5 class="text-center">没有订单信息!</h5>
		</c:if>
		<c:forEach items="${orders}" var="order">
						<div class="panel panel-success">
							<div class="panel-heading">订单号:${order.orderNo}&nbsp;
							<h4><small>下单时间:<fmt:formatDate type="BOTH" pattern="yyyy年MM月dd日 HH:mm:ss" value="${order.applyTime}"/></small></h4>
							<h4><small>物流单号:${order.logisticsCode}</small></h4>
							<h4><small>物流公司:${order.logistics}</small></h4></div>
								<div class="table-responsive">
									<table class="table">
										<thead>
											<tr>
												<th>商品</th>
												<th>数量</th>
												<th>状态</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${order.deliveryItems}" var="o">
												<tr>
													<td>${o.goods.name}</td>
													<td class="text-muted">${o.quantity}</td>
													<td>${order.status.desc}</td>													
												</tr>
											</c:forEach>
										</tbody>
										<%-- <tfoot>
											<tr>
												<td colspan="2">${order.status.name}/${order.paymentStatus.name}</td>
													<td ><c:if test="${order.status eq 'SHIPPED' }">
															<button class="btn btn-success btn-xs receiveBtn" data-url="<c:url value='/pm/order/receive.json?id=${order.id}' />">确认收货</button>
														</c:if>
														<c:if test="${order.paymentStatus eq 'UNPAID' and order.status eq 'NEW' }">
															<button class="btn btn-warning btn-xs"><span class="fa fa-money fa-fw"></span>去支付</button>
														</c:if>
														</td>
											</tr>
										</tfoot> --%>
									</table>
								</div>
								<c:if test="${order.status eq 'NEW' }">
								<div class="panel-footer"><button class="btn btn-warning btn-xs revokeBtn"
								 data-id="${order.id}">撤销订单</button></div></c:if>
						</div>
					</c:forEach>
		</div>
		</div>
		</div>
		<br>

	</div>
	<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
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
				<div class="modal-body" id='alertMessageBody'></div>
				<div class="modal-footer">
				<button type="button" class="btn btn-info btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在关闭......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$("#sw").click(function(){
				$("#collapseOrders").collapse('toggle');
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
			$(".revokeBtn").click(function(e){
				var id=$(this).attr("data-id");
				$.ajax("<c:url value='/delivery/remove.json'/>"+"?id="+id, {
					"type" : "DELETE",
					"data" : {
						"id" : id
					},
					"complete":function(xhr, ts){
						if(xhr.status>=200 && xhr.status<300){
							var m=$.parseJSON(xhr.responseText);
							if(m.flag=="0"){
								$("#alertMessageBody").empty().html("撤销订单成功");
								$("#winModal").modal({backdrop:"static",show:true});
								window.setTimeout(function() {
									window.location.reload(true);
								}, 1000);
							}else{
								$("#alertMessageBody").empty().html(m.message+",撤销订单操作失败").addClass("text-danger");
								$("#winModal").modal({backdrop:"static",show:true});
							}
							
						}else{
							if(xhr.status==401){
								$("#alertMessageBody").empty().html("尚未登陆,转到登陆界面").addClass("text-danger");
								$("#winModal").modal({backdrop:"static",show:true});
								window.location=xhr.getResponseHeader("Location");
							}else{
								$("#alertMessageBody").empty().html("添加购物车操作失败").addClass("text-danger");
								$("#winModal").modal({backdrop:"static",show:true});
							}							
						}
					}
				});
			});
		});
	</script>
</body>
</html>
