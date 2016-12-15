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
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id="myModalLabel">授权类型编辑</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/authType/edit.json'/>" name="editForm"
					class="form-horizontal" id="editForm" method="post">
					<div class="row">

						<div class="col-md-12 col-xs-12">
							<div class="panel panel-primary">
								<div class="panel-heading">基本信息</div>
								<div class="panel-body">

									<input type="hidden" name="id" value="${parameter.entity.id}">
									<div class="form-group">
										<label for="name" class="col-sm-2 control-label">授权名称</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editName"
												tabIndex="10" name="name" value="${parameter.entity.name}"
												placeholder="输入授权名称">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">说明</label>
										<div class="col-sm-6">
											<textarea rows="3" placeholder="输入授权类型说明"
												class="form-control" name="remark" id="editRemark">${parameter.entity.remark}</textarea>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editOrder" class="col-sm-2 control-label">排列顺序</label>
										<div class="col-sm-4">
											<input type="number" class="form-control" id="editOrder"
												name="order" value="${parameter.entity.order}" tabIndex="15"
												placeholder="输入显示顺序">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<!-- </form>-->
								</div>
							</div>
						</div>
						<div class="col-md-12">
							<div class="panel panel-primary">
								<div class="panel-heading">所授权产品</div>
								<div class="panel-body">
									<div class="form-group">
										<label for="editOrder" class="col-sm-2 control-label">产品名称</label>
										<div class="col-sm-6">
											<p class="form-control-static">${parameter.entity.goods.name}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn"
						tabIndex="16" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-success btn-block"
						form="editForm" tabIndex="17" id="saveBtn" name="saveBtn"
						value="saveBtn" tabindex="4" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-save">&nbsp;</span>保存
					</button>
				</div>
			</div>
		</div>
	</div>
</div>


<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
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
															alert("保存成功");
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
								name : {
									required : true
								}
							},
							onkeyup : false,
							messages : {
								name : {
									required : "授权名称必须填写"
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
