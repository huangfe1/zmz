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
<title>授权证书</title>
<style type="text/css">
.letter{
display:block;
position:relative;
width:614px;
height:820px;
margin:0px auto;
padding:0px;
background:url("<c:url value='/resources/images/letter.jpg'/>");
background-size:614px 820px;
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
<body>
	<div class="letter">
	<h4 class="left name">${name}</h4>
	<h4 class="left wx">${wx}</h4>
	<h4 class="left phone">${mobile}</h4>
	<h4 class="left idcard">${idcard}</h4>
	<h4 class="left agentcode">${agentCode}</h4>
	<div class="title-body">
		<h3 class="title">${goods}</h3>
		<h3 class="title">${level}</h3>
	</div>
	<h4 class="left date">${date}</h4>
	</div>
</body>
</html>
