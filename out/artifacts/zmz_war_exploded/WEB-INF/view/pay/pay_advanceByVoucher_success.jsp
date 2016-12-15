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
        $("#winMessageBody").empty().html("<h4>预存款充值成功,系统将回到商城首页</h4>").addClass("text-primary");
        $("#winModal").modal({backdrop:"static",show:true});
        window.setTimeout(function() {
            window.location.replace("<c:url value='/dmz/vmall/index.html'/>");
        }, 2000);
    </script>
</body>
</html>
