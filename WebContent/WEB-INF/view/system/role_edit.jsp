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
	<form action="<c:url value='/system/role/edit.json'/>"
		class="form-horizontal" name="editForm" id="editForm" method="post">
		<input type="hidden" name="id" value="${parameter.entity.id}">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title text-primary" id="myModalLabel">角色编辑</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<div class="panel panel-primary">
								<div class="panel-heading">角色基本信息</div>
								<div class="panel-body">
									<div class="form-group">
										<label for="editName" class="col-sm-2 control-label">角色名称</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" id="editName"
												required name="name" value="${parameter.entity.name}"
												placeholder="输入角色名称">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editRemark" class="col-sm-2 control-label">参数说明</label>
										<div class="col-sm-6">
											<textarea rows="4" class="form-control" id="editRemark"
												name="remark" value="${parameter.entity.remark}"
												placeholder="输入系统参数说明">${parameter.entity.remark}</textarea>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12 col-xs-12">
							<div class="panel panel-primary">
								<div class="panel-heading">角色权限信息</div>
								<div class="panel-body">
									<ul class="nav nav-tabs" id="tabs" role="tablist">
										<c:forEach items="${topModules}" var="tm" varStatus="st">
											<li role="presentation" ${st.index==0 ? 'class=active':'' }><a
												href="#tab${st.index}" aria-controls="tab1" role="tab"
												data-toggle="tab">${tm.name}</a></li>
										</c:forEach>
									</ul>
									<div class="tab-content">
										<c:forEach items="${topModules}" var="tm" varStatus="st">
											<div role="tabpanel" class="tab-pane fade ${st.index== 0 ? 'active in':'' }" 
												id="tab${st.index}">
												<c:forEach items="${tm.children}" var="sm">
													<div class="checkbox">
														<label> <input type="checkbox" value="${sm.id }"
														<c:if test="${oldIds[sm.id]}"> checked="checked" </c:if>
														 name="modulesIds">
															${sm.name}
														</label>
													</div>
												</c:forEach>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<div class="form-group">
					<div class="col-md-6 col-xs-12">
						<button type="button" class="btn btn-default btn-block quitBtn"
							id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
							tabindex="4" data-loading-text="正在返回......">
							<span class="glyphicon glyphicon-off">&nbsp;</span>关闭
						</button>
					</div>
					<div class="col-md-6 col-xs-12">
						<button type="submit" class="btn btn-success btn-block"
							form="editForm" id="saveBtn" name="saveBtn" value="saveBtn"
							tabindex="4" data-loading-text="正在提交......">
							<span class="glyphicon glyphicon-saved">&nbsp;</span>保存
						</button>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>


<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		$("#editCode").focus().select();
		var btn = null;
		$("#editForm").validate(
				{
					submitHandler : function(form) {
						$(form).ajaxSubmit(
								{
									beforeSubmit : function() {
										btn.button("loading");
									},
									success : function(responseText,
											statusText, xhr, $form) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										if (m.flag == "0") {
											alert("角色保存成功");
											$(".quitBtn").click();
											$("#search").click();
										} else {
											alert("角色保存失败," + m.message);
										}

									},
									error : function(xhr, textStatus,
											errorThrown) {
										btn.button("reset");
										//var m = $.parseJSON(xhr.responseText);
										alert("角色保存失败");
									}
								});
					},
					rules : {
						name : {
							required : true,
							maxlength : 64
						},
						remark : {
							maxlength : 128
						}
					},
					onkeyup : false,
					messages : {
						name : {
							required : "请输入角色名称"
						},
						remark : {
							maxlength : "参数说明不能超过128字符"
						}
					},
					focusInvalid : true,
					errorClass : "text-danger",
					validClass : "valid",
					errorElement : "small",
					errorPlacement : function(error, element) {
						error.appendTo(element.closest("div.form-group")
								.children("div.text-error"));
					}
				});
		$("#editForm").find("input[type='checkbox']").change(function(e) {
			var $t = $(this);
			var next = $t.next("input[type='hidden']");
			$t.prop("checked") ? next.val(1) : next.val(0);
		});
		$("#saveBtn").click(function() {
			btn = $(this).button();
		});
		$("#tabs a").click(function(e) {
			e.preventDefault()
			$(this).tab('show')
		});
	}
</script>
