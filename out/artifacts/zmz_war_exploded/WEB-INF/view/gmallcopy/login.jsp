<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<!doctype html>
<html>
<head>
<title>用户登录</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="dreamer">
<meta http-equiv="description" content="dreamer">
<%@include file="/WEB-INF/view/common/head_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
</head>
<body>
	<jsp:include page="/WEB-INF/view/mall/mall_menu.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2 col-xs-12">
				<div class="row">
					<div class="col-md-12 col-sm-12 col-xs-12">
						<div class="page-header">
							<h3>系统登陆<small>您还没有登陆系统</small></h3>
						</div>
					</div>
				</div>
				
				<form role="form" action="<c:url value='/agent/login.json' />"
					method="POST" name="loginForm" id="loginForm">
					<div class="form-group">
						<label for="accountName" class="control-label">帐户名</label>
						<div class="row">
							<div class="col-md-8 col-xs-12">
								<input type="text" name="accountName" id="accountName"
									value="${accountName}" class="form-control input-lg"
									placeholder="您的账户名" autofocus="autofocus" tabindex="1">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<div class="form-group">
						<label for="password">登录口令</label>
						<div class="row">
							<div class="col-md-8 col-xs-12">
								<input type="password" class="form-control input-lg" autocomplete="off"
									id="password" name="password" required placeholder="您的密码"
									tabindex="2">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<!-- <div class="form-group">
						<div class="row">
							<div class="col-md-12 col-xs-12">
								<div class="checkbox">
									<label> <input type="checkbox" value="1"
										name="remeberMe" id="remeberMe"> 记住我的用户名
									</label>
								</div>
							</div>
						</div>
					</div> -->
					<div class="form-group">
						<div class="row">
						<div class="col-md-6 col-xs-6">
								<a class="btn btn-danger btn-block btn-lg" tabindex="4"
								href="<c:url value='/vmall/uc/dmz/register.html?url=${url}'/>">
									<span class="glyphicon glyphicon-edit "
										>&nbsp;</span>注册新用户
								</a>
							</div>
							<div class="col-md-6 col-xs-6">
								<button type="submit" class="btn btn-info btn-block btn-lg"
									id="loginBtn" name="loginBtn" value="login" tabindex="3" data-loading-text="正在登录系统......">
									<span class="glyphicon glyphicon-log-in"
										>&nbsp;</span>系统登陆
								</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<label class="col-md-12 col-xs-12"><a class="pull-left"
								href="<c:url value='/vmall/uc/dmz/register.html?url=${url}'/>"> <span
									class="text-muted">还没有自己的账户?</span> <span class="text-primary">&nbsp;快速注册</span></a></label>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="row">
					<div class="col-md-8 col-xs-12 col-md-8">
						<div class="alert alert-danger invisible" role="alert" id="alert">
							&nbsp;</div>
					</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
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
			//search = search.substr(5);
			search="${url}";
			console.log(search);
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
														var l = xhr.getResponseHeader("Location");
														if (search) {
															l = decodeURIComponent(search);
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
														$(".captchaImg")
																.click();
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
										"div.row").children(
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
			window.parent.location = "dmz/mall/index.html";
		}
	}
	</script>
</body>
</html>
