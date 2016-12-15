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
			<h4 class="modal-title" id="myModalLabel">管理员信息编辑-${action}</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">				
					<div class="row">
					<form action="<c:url value='/user/admin/edit.json'/>" name="editForm"
					class="form-horizontal" id="editForm" method="post">
					<ul class="nav nav-tabs" role="tablist">
						 <li role="presentation" class="active"><a href="#basicInfo" aria-controls="basicInfo" role="tab" data-toggle="tab">基本信息</a></li>
						 <li role="presentation"><a href="#roleInfo" aria-controls="roleInfo" role="tab" data-toggle="tab">拥有角色</a></li>
					</ul>
					<div class="tab-content">
    					<div role="tabpanel" class="tab-pane active fade in" id="basicInfo">
    					<div class="col-md-12 col-xs-12">
    					<input type="hidden" name="id" value="${parameter.entity.id}">
									<div class="form-group">
										<label for="editName" class="col-sm-2 control-label">姓名</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editName"
												tabIndex="10" required name="realName"
												value="${parameter.entity.realName}" placeholder="输入用户实名">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editLoginName" class="col-sm-2 control-label">系统登陆名</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editLoginName"
												tabIndex="10" name="loginName"
												<c:if test="${parameter.entity.loginName eq 'zmzcaohai' }">
												readonly='readonly'
												</c:if>
												value="${parameter.entity.loginName}" placeholder="输入登陆系统账户名">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editPassword" class="col-sm-2 control-label">登陆密码</label>
										<div class="col-sm-6">
											<input type="password" class="form-control" id="editPassword"
												tabIndex="10" name="password"
												value="${parameter.entity.password}" placeholder="输入登陆密码">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editRePassword" class="col-sm-2 control-label">确认密码</label>
										<div class="col-sm-6">
											<input type="password" class="form-control" id="editRePassword"
												tabIndex="10" name="confirm_password"
												value="${parameter.entity.password}" placeholder="输入登陆确认密码">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<input type="tel" class="form-control" id="editMobile"
												tabIndex="11" required name="mobile"
												value="${parameter.entity.mobile}" placeholder="输入联系电话">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">微信号</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" id="editWeixin"
												tabIndex="12" required name="weixin"
												value="${parameter.entity.weixin}" placeholder="输入微信号">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentPoint" class="col-sm-2 control-label">身份证号</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editIdCard"
												tabIndex="13" required name="idCard"
												value="${parameter.entity.idCard}" placeholder="输入代理身份证号">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									</div>
    					</div>
    					<div role="tabpanel" class="tab-pane fade" id="roleInfo">
    						<div class="row">
    						<c:forEach items="${roles}" var="role">
    						<div class="col-md-3 col-xs-6">
    						<div class="checkbox">
														<label> <input type="checkbox" value="${role.id }"
														<c:if test="${oldRoles[role.id]}"> checked="checked" </c:if>
														 name="roleIds">
															${role.name}
														</label>
													</div>
							</div>
    						</c:forEach>
    						</div>
    					</div>
    				</div>
						</form>
						
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
															alert("保存管理用户信息成功");
															$(".quitBtn")
																	.click();
															$("#search")
																	.click();
														} else {
															alert("保存失败");
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														alert("保存失败");
													}
												});
							},
							rules : {
								realName : {
									required : true
								},
								loginName : {
									required : false
								},
								password: {
									required: true,
									minlength: 6
								},
								confirm_password: {
									required: true,
									minlength: 6,
									equalTo: "#editPassword"
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
									required : "请输入用户实名"
								},
								loginName:{
									required: "请输入登陆系统账户名"
								},
								password: {
									required: "请输入登陆密码",
									minlength: "密码长度不能低于6个字符"
								},
								confirm_password: {
									required: "请输入确认密码",
									minlength: "密码长度不能低于6个字符",
									equalTo: "两次输入的密码不一致"
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
								error.appendTo(element
										.closest("div.form-group").children(
												"div.text-error"));
							}
						});
		$("#editForm").find("input[type='checkbox']").change(function(e) {
			var $t = $(this);
			var next = $t.next("input[type='hidden']");
			$t.prop("checked") ? next.val(1) : next.val(0);
		});
		$("#saveBtn").click(function(e) {
			btn = $(this).button();
			$(document.forms["editForm"]).submit();
		});
	}
</script>
