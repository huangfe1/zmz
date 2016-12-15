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
<title>${user.agentCode}的推广码</title>
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
				<a class="thumbnail" href="#"> <img data-src="" alt=""
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
							购物车<span class="badge">0</span>
						</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	<div class="container">
	<div class="page-header">
  		<h4>我的专属码 <small>微信扫下图推广码加入筑美</small></h4>
	</div>
		<div class="row" id="promoCode">
			<div class="col-md-12 col-xs-12">
				<div class="thumbnail">
					<img data-src="" alt=""
						src="<c:url value='/dmz/agent/domain.html?id=${user.id}'/>"
						data-holder-rendered="true" class="product_img">
					<div class="caption">
						<h6 class="text-center">代理编码：${user.agentCode}</h6>
						
					</div>
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
				<div class="modal-body"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
		});
	</script>
</body>
</html>
