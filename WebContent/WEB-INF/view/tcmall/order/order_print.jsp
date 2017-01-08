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
<%@include file="/WEB-INF/view/common/datepicker_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>订单下载</title>
</head>
<body >
<div class="container">
	<h1 class="align-center" > 下载订单总数${orders_size}</h1>
	<c:forEach var="result" items="${results}">
		<div class="row">
			<div class="col-lg-6">${result[0]}</div>
			<div class="col-lg-6">${result[1]}</div>
		</div>
	</c:forEach>
</div>
</body>
</html>
