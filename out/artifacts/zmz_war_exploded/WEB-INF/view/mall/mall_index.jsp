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
<style type="text/css">
html {
	background-color: transparent !important;
}

#nav-img1, #nav-img2 {
	display: block;
	width: 100%;
	max-height: 200px;
	margin: 0 auto;
}

#nav-img2 {
	margin: 20px auto;
}

.thumbnail {
	border: none !important;
	box-shadow: none;
	-webkit-box-shadow: none;
	background-color: transparent !important;
}
.carousel-inner > .item img {
	width: 100%;
	height: 180px;
	max-height: 350px;
}
.productItem{
padding-right:0px !important;
padding-left:0px !important;
}
.productItem .thumbnail{
	border: solid 1px #eaeae8 !important;
	border-radius: 0 !important;
}

.productItem:first-child>.thumbnail{
	border-right:none !important;
}

.productItem .caption{
	padding:0px 9px !important;
}

nav .thumbnail {
	margin-bottom: 2px !important;
	border: none !important;
}

#nav {
	border-bottom: 1px solid #e0e0e0;
	background-color: #fff;
	margin-bottom:10px;
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
	width: 50% !important;
	max-width: 72px !important;
}

.product_img {
	width: 100%;
	min-height:150px;
	height:150px;
	max-width: 150px !important;
}

a:hover {
	text-decoration: none !important;
}

.goods-title {
	white-space: nowrap;
}
.header-img{
display:block;
width:100%;
max-height:350px;
margin:0 auto;
}
.header-title{
position:absolute;
font-size:1.2em;
bottom:0px;
left:20px;
font-family: Tahoma, Helvetica, Arial, "Microsoft Yahei", STXihei,
		sans-serif;
color:#fff;
}
.logo-img{
position:absolute;
bottom:0px;
left:20px;
height:80px;
}
.detailBtn{
cursor:pointer;
}
</style>
</head>
<body>
	<input type="hidden" name="agentCode" id="agentCode"
		value="${agentCode}">
	<input type="hidden" name="start" id="start" value="0">
	<input type="hidden" name="legth" id="length" value="8">
	<%-- <form class="form-horizontal" role="search"
		style="margin:2px auto;padding:0px;border-bottom: 1px solid #e0e0e0;">
		<div class="container">
			<div class="form-group">
				<div class="col-xs-2" style="padding-right:0px;padding-top:5px;">
					<img alt="Brand"
						style="width:70%;height:20px;display:block;margin:0px auto;"
						src="<c:url value='/resources/images/title-1.png'/>">
				</div>
				<div class="col-xs-10" style="padding-left:0px;">
					<div class="input-group">
						<input type="search" class="form-control input-lg" name="entity.name" value="${parameter.entity.name}"
							id="keywords" placeholder="搜索您感兴趣的商品"> <span
							class="input-group-btn">
							<button class="btn btn-default btn-lg" type="button" id="searchBtn">
								<span class="glyphicon glyphicon-search"></span>搜索
							</button>
						</span>
					</div>
				</div>
			</div>
		</div>
 	</form> --%>
	<header
		style="position:relative;margin:0 auto;width:100%;">
		<%--<img src="<c:url value='/resources/images/header.jpg'/>"--%>
			<%--class="header-img">--%>
			<div id="myCarousel" class="carousel slide ">
				<%--<ol class="carousel-indicators" >--%>
				<%--<li data-target="#myCarousel" data-slide-to="0" class=""></li>--%>
				<%--<li data-target="#myCarousel" data-slide-to="1" class=""></li>--%>
				<%--<li data-target="#myCarousel" data-slide-to="2" class=""></li>--%>
				<%--</ol>--%>
				<div class="carousel-inner">
					<div class="item">
						<img src="<c:url value='/resources/images/header.jpg'/>" alt="">
					</div>
					<div class="item active">
						<img src="<c:url value='/resources/images/header1.jpg'/>" alt="广告2">
					</div>
					<div class="item">
						<img src="<c:url value='/resources/images/header2.jpg'/>" alt="广告3">
					</div>
				</div>

			</div>

		<p class="header-title">
		<%--<c:choose>--%>
			<%--<c:when test="${ not empty owner}">--%>
				<%--${owner}的小店--%>
			<%--</c:when>--%>
			<%--<c:otherwise>--%>
				<%--筑美的小店--%>
			<%--</c:otherwise>--%>
		<%--</c:choose>--%>
			<c:choose>
				<c:when test="${ not empty user}">
					${owner}的小店
				</c:when>
				<c:otherwise>
					筑美的小店
				</c:otherwise>
			</c:choose>

		</p>
		<%--<img src="<c:url value='/resources/images/mall-logo.jpg'/>"--%>
			<%--class="logo-img">--%>
	</header>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-6 ">
				<a class="thumbnail" href="<c:url value='/vmall/uc/index.html'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/user.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">用户中心</h6>
					</div>
				</a>
			</div>

			<%--<div class="col-xs-3">--%>
				<%--<a class="thumbnail" href="<c:url value='/dmz/pmall/index.html'/>"> <img data-src="" alt=""--%>
					<%--src="<c:url value='/resources/images/tuiguang.jpg'/>"--%>
					<%--data-holder-rendered="true">--%>
					<%--<div class="caption">--%>
						<%--<h6 class="text-center">积分商城</h6>--%>
					<%--</div>--%>
				<%--</a>--%>
			<%--</div>--%>
			<%----%>
			<%--<div class="col-xs-3">--%>
				<%--<a class="thumbnail" href="<c:url value='/dmz/tmall/index.html'/>"> <img data-src="" alt=""--%>
					<%--src="<c:url value='/resources/images/tuiguang.jpg'/>"--%>
					<%--data-holder-rendered="true">--%>
					<%--<div class="caption">--%>
						<%--<h6 class="text-center">特权商城</h6>--%>
					<%--</div>--%>
				<%--</a>--%>
			<%--</div>--%>
			
			<div class="col-xs-6">
				<a class="thumbnail"
					href="<c:url value='/vmall/shopcart/index.html'/>"> <img
					data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">
							购物车<span class="badge" id="shopcartQuantity">${shopcart.quantity}</span>
						</h6>
					</div>
				</a>
			</div>
		</div>
		<span style="color:red;font-size: 1.2em">精彩推荐:</span><span
			style="color: #00aba9;margin-left: 2em">筑美一站式购物平台,赶紧抢购!!!</span>
	</div>


	<div class="container">
		<a href="">
			<img id="nav-img1" src="<c:url value='/resources/images/d1.png'/>"/>
		</a>
		<a href="">
			<img id="nav-img2" src="<c:url value='/resources/images/d2.png'/>"/>
		</a>
	</div>
	<div class="container" id="products">
			<%-- <div class="col-md-3 col-xs-6">
				<div class="thumbnail">
					<img data-src="" alt=""
						src="<c:url value='/dmz/img/goods/example1.jpg'/>"
						data-holder-rendered="true" class="product_img">
					<div class="caption">
						<h6 class="text-center">金龙鱼 阳光葵花子油</h6>
						<p class="text-danger">
							<span class="glyphicon glyphicon-yen"></span>23.88
						</p>
						<p>
							<a href="#" class="btn btn-info pull-right btn-block btn-xs"
								role="button"><span
								class="glyphicon glyphicon-shopping-cart"></span>购买</a>
						</p>
					</div>
				</div>
			</div> --%>
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
			//启动幻灯片
			$('.carousel').carousel({
				interval: 4000
			});
			$("#start").val(0);
			$("#length").val(8);
			var query = function() {
				var param = {
					"entity.name" : $("#keywords").val(),
					"start" : $("#start").val(),
					"length" : $("#length").val(),
					"useDatatables" : true
				};
				var agentCode="${agentCode}";
				if(agentCode){
					window.localStorage.setItem("agentCode", agentCode);
				}else{
					agentCode=window.localStorage.getItem("agentCode");
				}
				param["agentCode"]=agentCode;
				var url="<c:url value='/dmz/vmall/goods/query.json'/>";
				$.getJSON("<c:url value='/dmz/vmall/goods/query.json'/>",param,
								function(datas) {
					var row=null;
					datas.forEach(function(data,i) {
								if(i%2==0){
									row=$("<div>").addClass("row");
									row.appendTo($("#products"));
								}				
										var template = '<div class="col-md-6 col-xs-6 productItem">'
														+ '<div class="thumbnail detailBtn" data-id='+data.id+'><img data-src="" alt=""	src="'+data.imgUrl+'"'+
						' data-holder-rendered="true" class="product_img"><div class="caption">'
														+ '<h6 class="text-center">'
														+ data.name
														+ '</h6><p class="text-danger text-center">'
														+ '<span class="glyphicon glyphicon-yen"></span>'
														+ data.price
														+ '</p>'
														+ '<p style="padding-left:80%;margin-bottom:0px;"><a href="#" class="btn btn-link buyBtn" role="button" data-id='+data.id+'>'
														+ '<span class="glyphicon glyphicon-shopping-cart"></span></a></p> </div>	</div></div>';
												$(template).appendTo(row);
											});
									var start = $("#start");
									;
									start.val(parseInt(start.val())
											+ datas.length);
								});
			};
			query();
			var names = new Bloodhound({
				name : "names",
				datumTokenizer : Bloodhound.tokenizers.obj.whitespace("value"),
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				limit : 10,
				prefetch : {
					url : "<c:url value='/dmz/goods/names.json'/>",
					filter : function(l) {
						return $.map(l, function(d) {
							return {
								value : d
							};
						});
					}
				}
			});
			names.initialize();
			$("#keywords.typeahead").typeahead({
				hint : true,
				highlight : true,
				minLength : 2
			}, {
				name : "names",
				displayKey : "value",
				source : names.ttAdapter()
			});
			var animatedClass="shake animated";
			$("#products").delegate(".buyBtn", "click", function(e) {
				e.preventDefault();
				e.stopPropagation();
				var id = $(this).attr("data-id");
				$.ajax("<c:url value='/vmall/shopcart/add.json'/>", {
					"type" : "POST",
					"data" : {
						"goodsId" : id
					},
					"complete":function(xhr, ts){
						if(xhr.status>=200 && xhr.status<300){
							var m=$.parseJSON(xhr.responseText);
							if(m.flag=="0"){
							/* 	$("#alertMessageBody").empty().html("添加到购物车成功");
								$("#myModal").modal({backdrop:"static",show:true}); */
								$("#shopcartQuantity").html(m.data).removeClass(animatedClass)
								.addClass(animatedClass).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){
								      $(this).removeClass(animatedClass);
							    });
							}else{
								$("#alertMessageBody").empty().html(m.message+",添加购物车操作失败").addClass("text-danger");
								$("#myModal").modal({backdrop:"static",show:true});
							}
							
						}else{
							if(xhr.status==401){
								$("#alertMessageBody").empty().html("尚未登陆,转到登陆界面").addClass("text-danger");
								$("#myModal").modal({backdrop:"static",show:true});
								window.location=xhr.getResponseHeader("Location");
							}else{
								$("#alertMessageBody").empty().html("添加购物车操作失败").addClass("text-danger");
								$("#myModal").modal({backdrop:"static",show:true});
							}							
						}
					}
				});
			});
			$("#products").delegate(".detailBtn", "click", function(e) {
				e.preventDefault();
				e.stopPropagation();
				var id=$(this).attr("data-id");
				window.location="<c:url value='/dmz/vmall/detail.html'/>"+"?id="+id;
			});
			$("#searchBtn").click(function(e){
				e.preventDefault();
				$("#products").empty();
				$("#start").val(0);
				query();
			});
			$(window).scroll(
					function(e) {
						var mayLoadContent = $(window).scrollTop() >= $(
								document).height()
								- $(window).height();
						if (mayLoadContent) {
							query();
						}
					});
		});
	</script>

	<%@ include file="cs.jsp" %>
	<%CS cs = new CS(1259551985);cs.setHttpServlet(request,response);
		String imgurl = cs.trackPageView();%>
	<img src="<%= imgurl %>" width="0" height="0"  />

</body>
</html>
