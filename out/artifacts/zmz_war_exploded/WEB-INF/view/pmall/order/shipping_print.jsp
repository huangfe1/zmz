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
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>发货单打印</title>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2 col-xs-2">
				<a href="#" class="thumbnail">
				<img src="<c:url value='/resources/images/mall-logo.jpg'/>"  style="width:80px;">
				</a>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<div class="table-responsive">
					<table class="table">
						<tbody>
							<tr>
								<td class="col-md-6 col-xs-6"><strong>订单编号：</strong>${parameter.entity.orderNo }</td>
								<td class="col-md-6 col-xs-6"><strong>订购时间：</strong>${parameter.entity.orderTime }</td>
							</tr>
						</tbody>
					</table>
					<table class="table">
						<tbody>
							<tr>
								<td class="col-md-6 col-xs-6"><strong>客户姓名：</strong>${parameter.entity.consignee }</td>
								<td class="col-md-6 col-xs-6"><strong>联系方式：</strong>${parameter.entity.mobile }</td>
							</tr>
						</tbody>
					</table>
					<table class="table">
						<tbody>
							<tr>
								<td><strong>配送地址：</strong>${parameter.entity.shippingAddress }</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-md-12 col-xs-12">
				<div class="table-responsive">
					<table class="table table-bordered"  style="border-collapse: collapse;   border: 1px solid #000;">
						<thead>
							<tr>
								<th>商品名称</th>
								<th>数量</th>
								<!-- <th>商品金额</th> -->
								<!-- <th>产生的积分</th> -->
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${parameter.entity.items }" var="item">
								<tr>
									<td>${item.value.goodsName }</td>
									<td>${item.value.quantity }</td>
									<%-- <td></td> --%>
									<%-- <td>${item.point }</td> --%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="col-md-12 col-xs-12">
			<button class="btn btn-link btn-block  hidden-print"  onclick="javascript:window.print();"><span class="glyphicon glyphicon-print"></span>打印单据</button>
			</div>
		</div>
	</div>
</body>
</html>


<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		window.print();
	}
</script>
