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
<jsp:include page="/WEB-INF/view/common/head_css.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/head_css_fav.jsp"></jsp:include>

<style type="text/css">
.login_logo {
	padding-top: 22px;
	height: 207px;
	color: white;
}
.letter{
display:block;
position:relative;
width:614px;
height:820px;
margin:0px auto;
padding:0px;
}
.left{
display:block;
position:absolute;
left:240px;
}
.name{
top:432px;
}
.wx{
top:458px;
}
.phone{
top:484px;
}
.idcard{
top:509px;
}
.agentcode{
top:535px;
}
.title-body{
display:block;
position:relative;
left:0px;
top:585px;
width:100%;
}
.title{
margin:0px auto;
text-align:center;
}
.date{
top:680px;
}
</style>
</head>
<body style="background:white;">
	<nav class="navbar navbar-default navbar-static-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle animated flip pull-left"
					data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="<c:url value='/login.html'/>"><img
					alt="Brand" style="width:20px;height:20px;"
					src="<c:url value='/resources/images/title-1.png'/>"></a>
				<p class="navbar-text">构筑美好生活</p>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a
						href="<c:url value='/dmz/securityCode/search.html?f=1' />"><span
							class="glyphicon glyphicon-barcode"></span>防伪码查询</a></li>
					<li class="active"><a
						href="<c:url value='/dmz/agent/search.html?f=1' />"><span
							class="glyphicon glyphicon-user"></span>代理信息查询</a></li>
				</ul>

			</div>
		</div>
	</nav>
	<div class="jumbotron"
		style="background-color:white;padding-bottom:0;padding-top:0;">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="login_logo">
						<img src="<c:url value='/resources/images/title-1.png'/>"
							class="center-block img-responsive">
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row" style="margin-bottom:20px;">
			<div
				class="col-md-4 col-md-offset-4 col-sm-8 col-sm-offset-2 col-xs-12">
				<form role="form" class="form-horizontal"
					action="<c:url value='/dmz/agent/search.html' />" method="GET"
					name="searchForm" id="searchForm">
					<div class="form-group input-group">
						<input type="search" name="entity.agentCode" id="agentCode"
							value="${parameter.entity.agentCode}"
							class="form-control input-lg" placeholder="请输入代理编码进行查询"
							autofocus="autofocus" tabindex="1"> <span
							class="input-group-btn">
							<button type="submit" class="btn btn-lg btn-primary">
								<span class="glyphicon glyphicon-search"></span>查询
							</button>
						</span>
					</div>

				</form>
			</div>
		</div>
		<c:if test="${ empty agent and empty f}">
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<div class="alert alert-warning text-center" role="alert">未查询到此代理编码对应的商品信息</div>
				</div>
			</div>
		</c:if>
		<c:if test="${ ! empty agent}">
            <div align="center">
			<c:forEach items="${auths}" var="auth">
				<c:if test="${auth.agent.agentStatus.desc ne '整顿'}">
					<img style="float: left;" class="letter" src="<c:url value='/auth/dmz/letterimg/${agent.agentCode}.html'><c:param name='id' value='${auth.id}'/></c:url>">
				</c:if>


			</c:forEach>
            </div>
			<%--<div class="row">--%>
				<%--<div class="col-md-12   col-sm-12 col-xs-12">--%>
				<%--<div style="width:100%;">--%>
									<%--<div class="media" style="margin:16px auto;width:50%;">--%>
										<%--<div class="media-left">--%>
											<%--<a href="#">--%>
												<%--<img alt="商域二维码" tile="商域二维码" class="media-object"--%>
										<%--src="<c:url value='/dmz/agent/domain.html?id=${agent.id}'/>">--%>
											<%--</a>--%>
										<%--</div>--%>
										<%--<div class="media-body">--%>
											<%--<h3 class="media-heading">代理信息</h3>--%>
											<%--<br>--%>
											<%--<h4>代理编号：${agent.agentCode }</h4>--%>
											<%--<h4>代理姓名：${agent.realName }</h4>--%>
										<%--</div>--%>
									<%--</div>--%>
									<%--</div>--%>
				<%--</div>--%>
			<%--</div>--%>
		</c:if>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			init();
		});
		function init() {
		}
		function checkParent() {
			if (window.parent.length > 0) {
				window.parent.location = "index.html";
			}
		}
	</script>
</body>
</html>
