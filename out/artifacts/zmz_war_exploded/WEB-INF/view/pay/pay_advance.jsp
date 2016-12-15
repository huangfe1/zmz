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
<script type="text/javascript">

	//调用微信JS api 支付
	function onBridgeReady(){
		WeixinJSBridge.invoke(
				'getBrandWCPayRequest', ${jsapiParamJson},
				function(res){
					if(res.err_msg == "get_brand_wcpay_request:ok" ) {
						alert("预存款充值功");
						$("#winMessageBody").empty().html("<h4>预存款充值成功,系统将回到商城首页</h4>").addClass("text-primary");
						$("#winModal").modal({backdrop:"static",show:true});
						window.setTimeout(function() {
							window.location.replace("<c:url value='/dmz/vmall/index.html'/>");
						}, 3000);
					}else if(res.err_msg=="get_brand_wcpay_request:fail"){
						alert("支付失败");
					}else if(res.err_msg=="get_brand_wcpay_request:cancel"){
						alert("支付已取消");
					}
				}
		);
	}
	function callpay(){
		if (typeof WeixinJSBridge == "undefined"){
			if( document.addEventListener ){
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		}else{
			onBridgeReady();
		}
	}

</script>
<body>
<div class="jumbotron">
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<h4 class="text-warning">你正在与筑美和之初进行微信支付,请核对订单金额完成支付</h4>
				<h6>订单编号：${order.out_trade_no}</h6>
				<h3>付款金额：${order.advance}元</h3>
			</div>
		</div>
	</div>
</div>
<div class="container">
	<div class="row">
		<div class="col-xs-12">
			<button class="btn btn-lg btn-success btn-block" onclick="callpay();"><span class="fa fa-weixin"></span>确认支付</button>
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
	$(function() {

	});
</script>
</body>
</html>
