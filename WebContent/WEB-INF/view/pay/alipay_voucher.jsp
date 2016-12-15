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
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js">
</script>
<style type="text/css">
html {
	background-color: transparent !important;
}

.carousel-inner>.item img {
	width: 100%;
	height: 115px;
	max-height: 240px;
}

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

.product_img {
	width: 100%;
	max-width: 150px !important;
}

a:hover {
	text-decoration: none !important;
}

.goods-title {
	white-space: nowrap;
}
.panel-red{
border-color: #d9534f;
}
.panel-red .panel-heading {
    border-color: #d9534f;
    color: #fff;
    background-color: #d9534f;
}
</style>
<title>结算</title>
</head>
<body>
<div class="jumbotron">
	<h2 align="center">第二步,向下面指定支付宝转账</h2>
		<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<%--<h6>订单编号：${order.out_trade_no}</h6>--%>
				<h3 style="color: red;">收款账号:18802083025 </h3>
				<h3 style="color: blueviolet;">收款姓名:龙成</h3>
			<h3>付款金额：${order.voucher}元</h3>
				<h3>备注编号：${user.agentCode}</h3>
			</div>
			</div>
		</div>
	</div>
	<div class="container" >
		<div class="row">
			<div class="col-xs-12">
				<button class="btn btn-lg btn-success btn-block" onclick="callpay();"><span class="fa fa-weixin"></span>(请先打开您的支付宝转账)转账后,点我进行查看</button>
			</div>
		</div>
	</div>
<br>

<div class="container">
	<div class="row">
		<!--详情页-->
		<div class="col-md-12 col-xs-12">
			<a href="#" class="thumbnail" style="margin-bottom: 0px;border: none;padding: 0px">
				<img src="<c:url value='/resources/images/t1.jpg'/>" alt="教程">
			</a>
		</div>
	</div>
</div>

<div class="container">
	<div class="row">
		<!--详情页-->
		<div class="col-md-12 col-xs-12">
			<a href="#" class="thumbnail" style="margin-bottom: 0px;border: none;padding: 0px">
				<img src="<c:url value='/resources/images/t2.jpg'/>" alt="教程">
			</a>
		</div>
	</div>
</div>

<div class="container">
	<div class="row">
		<!--详情页-->
		<div class="col-md-12 col-xs-12">
			<a href="#" class="thumbnail" style="margin-bottom: 0px;border: none;padding: 0px">
				<img src="<c:url value='/resources/images/t3.jpg'/>" alt="教程">
			</a>
		</div>
	</div>
</div>

<div class="container">
	<div class="row">
		<!--详情页-->
		<div class="col-md-12 col-xs-12">
			<a href="#" class="thumbnail" style="margin-bottom: 0px;border: none;padding: 0px">
				<img src="<c:url value='/resources/images/t4.jpg'/>" alt="教程">
			</a>
		</div>
	</div>
</div>

	<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
		aria-labelledby="winModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="winModalLabel">和之初预存款充值</h4>
				</div>
				<div class="modal-body" id="winMessageBody"></div>
			</div>
		</div>
	</div>
<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
        function callpay(){
            window.location.href="<c:url value="/voucher/my.html"/>" ;
        }
		$(function() {

		});
	</script>
</body>
</html>
