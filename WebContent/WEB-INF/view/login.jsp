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
<jsp:include page="/WEB-INF/view/common/head_css_startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/head_css_fav.jsp"></jsp:include>
<style type="text/css">
.login_top {
	width: 100%;
	height: 257px;
	background: url(<c:url value='/resources/images/reg-bj_02.jpg'/>) repeat-x;
	padding-bottom: 0px;
	margin-bottom: 0px;
}

.login_logo {
	padding-top: 22px;
	height: 207px;
	margin: 0px auto;
	color: white;
}

@media (max-width: 767px) {
.login_top {
	height: 157px;
	padding-top:5px !important;
}
.login_logo {
	height: 107px;
}
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
					<li><a
						href="<c:url value='/dmz/agent/search.html?f=1' />"><span
							class="glyphicon glyphicon-user"></span>代理信息查询</a></li>
				</ul>

			</div>
		</div>
	</nav>
	<div class="jumbotron login_top hidden-xs">
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
		<div class="row">
			<div class="col-md-8 col-md-offset-4 col-sm-8 col-xs-12">

				<div class="row">
					<div class="col-md-5 col-xs-12 col-md-5">
						<div class="alert alert-danger invisible" role="alert" id="alert">
							&nbsp;</div>
					</div>
				</div>

				<form role="form" class="form-horizontal"
					action="<c:url value='/login.json' />" method="POST"
					name="loginForm" id="loginForm">
					<div class="form-group">
						<!-- <label for="accountName" class="col-sm-1 control-label">用户名</label> -->
						<div class="col-md-5 col-xs-12">
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1"> <span
									class="glyphicon glyphicon-user"></span>
								</span> <input type="text" name="accountName" id="accountName"
									value="${accountName}" class="form-control input-lg"
									placeholder="您的账户名" autofocus="autofocus" tabindex="1">
							</div>
						</div>
						<div class="col-md-4 col-xs-4 text-error"></div>
					</div>
					<div class="form-group">
						<!-- <label for="password" class="col-sm-1 control-label">密码</label>-->
						<div class="col-md-5 col-xs-12">
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1"> <span
									class="glyphicon glyphicon-lock"></span>
								</span> <input type="password" class="form-control input-lg"
									id="password" name="password" required placeholder="您的密码"
									tabindex="2">
							</div>
						</div>
						<div class="col-md-4 col-xs-4 text-error"></div>
					</div>
					<div class="form-group" >
                        <div id="captcha" align="center" class="col-md-5 col-xs-5"> </div>
						<!-- <label for="password" class="col-sm-1 control-label">验证码</label> -->
						<%--<div class="col-md-3 col-xs-6">--%>
							<%--<div class="input-group">--%>
								<%--<span class="input-group-addon" id="basic-addon1"> <span--%>
									<%--class="glyphicon glyphicon-picture"></span>--%>
								<%--</span> <input type="text" name="captcha" placeholder="请输入右图验证码"--%>
									<%--class="form-control input-lg" tabindex="3">--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="col-md-2 col-xs-4" style="padding: 0;">--%>
							<%--<img src="<c:url value='/captcha.jpg'/>" title="点我换一张"--%>
								<%--class="captcha-img captchaImg">--%>
								<%--<span class="help-block captcha-text hidden-xs">看不清,换一张</span>--%>
						<%--</div>--%>
						<%--<div class="col-md-4 col-xs-4 text-error"></div>--%>
					</div>
					<div class="form-group">
						<div class="col-md-5 col-xs-12">
							<button type="submit" class="btn btn-danger btn-block btn-lg"
								id="loginBtn" name="loginBtn" value="login" tabindex="4"
								data-loading-text="正在登录......">
								<span class="glyphicon glyphicon-log-in">&nbsp;</span>系统登录
							</button>
						</div>
					</div>
				</form>
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
	<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>

	<script src="http://static.geetest.com/static/tools/gt.js"></script>
	<script type="text/javascript">
        var t;
		<!--验证码-->
		var handler = function (captchaObj) {
			// 将验证码加到id为captcha的元素里
			captchaObj.appendTo("#captcha");
            t=captchaObj;
		};
		$.ajax({
			// 获取id，challenge，success（是否启用failback）
			url: "<c:url value="/StartCaptchaServlet"/>",
			type: "get",
			dataType: "json", // 使用jsonp格式
			success: function (data) {
				// 使用initGeetest接口
				// 参数1：配置参数，与创建Geetest实例时接受的参数一致
				// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它做appendTo之类的事件
				initGeetest({
					gt: data.gt,
					challenge: data.challenge,
					product: "float", // 产品形式
					offline: !data.success
				}, handler);
			}
		});

		//这里只是最简单的前端调用
		//要了解更多前端使用请访问 http://www.geetest.com/install/sections/idx-client-sdk.html#web
		$(function() {
			init();
		});
		function keydownHandler(event) {
			if (event.keyCode == 13) {
				$("#password").trigger("focus");
				return false;
			}
		}
		function init() {
			var search = location.search;
			if (search && search.length > 5) {
				search = search.substr(5);
			}
			checkParent();
			$("#accountName").keydown(keydownHandler);
			var btn = null;
			$("#loginForm")
					.validate(
							{
								submitHandler : function(form) {
									$(form)
											.ajaxSubmit(
													{
														beforeSubmit : function() {
															btn
																	.button("loading");
														},
														success : function(
																responseText,
																statusText,
																xhr, $form) {
															//btn.button("reset");
															var l = null;
															if (search) {
																l = decodeURIComponent(search);
															} else {
																l = xhr.getResponseHeader("Location");
															}
															window.location = l;
														},
														error : function(xhr,
																textStatus,
																errorThrown) {
															var m = $
																	.parseJSON(xhr.responseText);
															$("#alert")
																	.empty()
																	.html(
																			m.message)
																	.removeClass(
																			"invisible");
															btn.button("reset");
                                                            t.refresh();//刷新验证码
															$("#accountName")
																	.focus();
														}
													});
								},
								rules : {
									accountName : {
										required : true
									},
									password : {
										required : true
									},
									captcha : {
										required : true
									}
								},
								onkeyup : false,
								messages : {
									accountName : {
										required : "帐户名必须填写"
									},
									password : {
										required : "登录密码必须填写"
									},
									captcha : {
										required : "请输入左侧验证码"
									}
								},
								focusInvalid : true,
								errorClass : "text-danger",
								validClass : "valid",
								errorElement : "small",
								errorPlacement : function(error, element) {
									error.appendTo(element.closest(
											"div.form-group").children(
											"div.text-error"));
								}
							});
			$("#loginBtn").click(function() {
				btn = $(this).button();
			});
			$('.carousel').carousel({
				interval : 2000
			});
			$(".captcha-text").click(function() {
				$(".captchaImg").click();
			});
		}
		function checkParent() {
			if (window.parent.length > 0) {
				window.parent.location = "index.html";
			}
		}
	</script>
</body>
</html>
