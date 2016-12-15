<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<style>
<!--
-->
.none {
	display: none;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header bg-primary">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">基础信息修改</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12 col-xs-12">
						<form action="<c:url value='/profile/edit.json'/>" name="editForm"
							class="form-horizontal" id="editForm" method="post">
							<div class="form-group">
								<input type="hidden" name="id" value="${parameter.id}">
								<label class="col-sm-2 control-label">实名</label>
								<div class="col-sm-10">
									<p class="form-control-static">${parameter.realName}</p>
								</div>
							</div>
							<div class="form-group">
								<label for="editName" class="col-sm-2 control-label">昵称</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="editName"
										tabIndex="10" required name="nickName"
										value="${parameter.nickName}" placeholder="输入昵称">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editLoginName" class="col-sm-2 control-label">系统登陆名</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="editLoginName"
										tabIndex="10" name="loginName"
										value="${parameter.loginName}" placeholder="输入登陆系统账户名">
									<p class="help-block">缺省使用系统生成的代理编码作为系统登陆账号</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.mobile}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editWeixin" class="col-sm-2 control-label">微信号</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.weixin}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">身份证号</label>
								<div class="col-sm-6">
									<p class="form-control-static">${parameter.idCard}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">原密码</label>
								<div class="col-sm-4">
									<input type="password" class="form-control"
										name="oldPassword" id="oldPassowrd" placeholder="请输入您原来的密码">
										<p class="help-block">初始密码为您注册时所填写身份证号码的后6位</p>
								</div>
								<div class="col-sm-2">
									<a class="btn btn-warning resetBtn pull-right" 
									href="<c:url value='/profile/resetPassword.json?id=${user.id}'/>">重置密码</a>
								</div>
								
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">新密码</label>
								<div class="col-sm-4">
									<input type="password" class="form-control"
										name="newPassword" id="newPassword" placeholder="请输入您新的密码">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">确认密码</label>
								<div class="col-sm-4">
									<input type="password" class="form-control"
										name="rePassword" id="rePassowrd" placeholder="请再次输入您新的密码">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-primary btn-block"
						form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
						value="saveBtn" tabindex="4" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-save">&nbsp;</span>保存
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="myMessageModal" tabindex="-1" role="dialog"
		aria-labelledby="myMessageModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myMessageModalLabel"></h4>
				</div>
				<div class="modal-body" id="messageBody"></div>
				<div class="modal-footer">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-success btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				</div>
			</div>
		</div>
	</div>

<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		$("#editName").focus().select();
		var btn = null;
		$("#editForm")
				.validate(
						{
							submitHandler : function(form) {
								$(form)
										.ajaxSubmit(
												{
													beforeSubmit : function(
															arr, $form, options) {
														btn.button("loading");
													},
													success : function(
															responseText,
															statusText, xhr,
															$form) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														if (m.flag == "0") {
															alert("保存用户信息成功");
															$(".quitBtn")
																	.click();
														} else {
															alert("保存失败,原因："+m.message);
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														btn.button("reset");
														var m = $
																.parseJSON(xhr.responseText);
														alert("保存失败");
													}
												});
							},
							rules : {
								
								loginName : {
									required : false
								},
								newPassword: {
									minlength: 6
								},
								rePassword: {
									minlength: 6,
									equalTo: "#newPassword"
								},
								
							},
							onkeyup : false,
							messages : {								
								loginName:{
									required: "请输入代理登陆系统账户名"
								},
								newPassword: {
									required: "请输入登陆密码",
									minlength: "密码长度不能低于6个字符"
								},
								rePassword: {
									required: "请输入确认密码",
									minlength: "密码长度不能低于6个字符",
									equalTo: "两次输入的密码不一致"
								}								
							},
							focusInvalid : true,
							errorClass : "text-danger",
							validClass : "valid",
							errorElement : "small",
							errorPlacement : function(error, element) {
								error.appendTo(element
										.closest("div.form-group").children(
												"div.text-error"));
							}
						});
		$("#saveBtn").click(function(e) {
			btn = $(this).button();
			$(document.forms["editForm"]).submit();
		});
		$(".resetBtn").click(function(e){
			e.preventDefault();
			e.stopPropagation();
			var method="POST";
			$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
				if(xhr.status>=200 && xhr.status<300){
					var m=$.parseJSON(xhr.responseText);
					if(m.flag=="0"){
						$("#messageBody").empty().html("<p class='text-danger'>重置密码为您身份证号码后6位,操作成功</p>");
						$("#myMessageModal").modal({backdrop:"static",show:true});
					}else{
						$("#messageBody").empty().html("重置密码操作失败").addClass("text-danger");
						$("#myMessageModal").modal({backdrop:"static",show:true});
					}
					
				}else{
					$("#messageBody").empty().html("重置密码操作失败").addClass("text-danger");
					$("#myMessageModal").modal({backdrop:"static",show:true});
				}
			}});
		});
	}
</script>
