<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<!doctype html>
<html>
<head>
<title>用户注册</title>
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
							<h2><img style="width:15%;" src="<c:url value='/resources/images/mall-logo.jpg'/>">&nbsp;新用户注册</h2>
						</div>
					</div>
				</div>
				<form role="form" action="<c:url value='/vmall/uc/dmz/${agentCode}/register.json' />"
					method="POST" name="loginForm" id="loginForm">
					<div class="form-group">
						<label for="accountName" class="control-label">您的姓名</label>
						<div class="row">
							<div class="col-md-8 col-xs-12 input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
								<input type="text" class="form-control input-lg" id="editName"
												tabIndex="10" required name="realName" autofocus="autofocus"
												value="${parameter.entity.realName}" placeholder="输入您的姓名">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<div class="form-group">
						<label for="editMobile">手机号码</label>
						<div class="row">
							<div class="col-md-8 col-xs-12 input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-phone"></span></span>
								<input type="tel" class="form-control input-lg" id="editMobile"
												tabIndex="11" required name="mobile"
												value="${parameter.entity.mobile}" placeholder="联系电话">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<div class="form-group">
						<label for="editWeixin">微信号</label>
						<div class="row">
							<div class="col-md-8 col-xs-12 input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-comment"></span></span>
								<input type="text" class="form-control input-lg" id="editWeixin"
												tabIndex="12" required name="weixin"
												value="${parameter.entity.weixin}" placeholder="输入微信号">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<div class="form-group">
						<label for="editIdCard">身份证号</label>
						<div class="row">
							<div class="col-md-8 col-xs-12 input-group">
							<span class="input-group-addon"><span class="glyphicon glyphicon-credit-card"></span></span>
							<input type="text" class="form-control input-lg" id="editIdCard"
												tabIndex="13" required name="idCard"
												value="${parameter.entity.idCard}" placeholder="输入代理身份证号">
							</div>
							<div class="col-md-4 col-xs-4 text-error"></div>
						</div>
					</div>
					<div class="form-group">
						<div class="row">
							<div class="col-md-12 col-xs-12">
								<button type="submit" class="btn btn-danger btn-block btn-lg"
									id="regBtn" name="regBtn" value="register" tabindex="14" data-loading-text="正在提交资料......">
									<span class="glyphicon glyphicon-check"
										>&nbsp;</span>提交注册
								</button>
							</div>
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
				<div class="modal-body" id="alertMessageBody"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在关闭......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
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
			search = search.substr(5);
		}
		checkParent();
		$("#accountName").keydown(keydownHandler);
		var btn = null;
		$("#loginForm")
				.validate(
						{
							submitHandler : function(form) {
								$(form).ajaxSubmit(
												{beforeSubmit : function() {
														btn.button("loading");
													},
													success : function(
															responseText,
															statusText, xhr,
															$form) {
														var m = $.parseJSON(xhr.responseText);
														if (m.flag == "0") {
															$("#alertMessageBody").empty().html("注册成功,正在进入购物中心.....");
															$("#myModal").modal({backdrop:"static",show:true});
															window.setTimeout(function() {
																window.location=xhr.getResponseHeader("Location");
															}, 2000);															
														} else {
															btn.button("reset"); 
															$("#alertMessageBody").empty().html(m.message+",注册失败").addClass("text-danger");
															$("#myModal").modal({backdrop:"static",show:true});
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														$("#alertMessageBody").empty().html(m.message+",注册失败").addClass("text-danger");
														$("#myModal").modal({backdrop:"static",show:true});
													}
												});
							},
							rules : {
								realName : {
									required : true
								},
								mobile:{
									required:true,
									mobile:true
								},
								weixin:{
									required:true
								},
								idCard:{
									required:true
								}								
							},
							onkeyup : false,
							messages : {
								realName : {
									required : "请输入代理姓名"
								},
								mobile : {
									required : "请输入手机号码",
									mobile: "手机号码格式不正确"
								},
								weixin:{
									required:"请输入微信号"
								},
								idCard:{
									required:"请输入身份证号"
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
		$("#regBtn").click(function() {
			btn = $(this).button();
		});
		$("#backBtn").click(function(){
			window.history.back();
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
