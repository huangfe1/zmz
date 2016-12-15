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
	<form action="<c:url value='/agentLevel/edit.json'/>"
		class="form-horizontal" name="editForm" id="editForm" method="post">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title text-primary" id="myModalLabel">代理等级编辑</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 col-xs-12">

							<input type="hidden" name="id" value="${parameter.entity.id}">
							<input type="hidden" name="parent.id"
								value="${parameter.entity.parent.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label">上一等级</label>
								<div class="col-sm-10">
									<p class="form-control-static">${parameter.entity.parent==null?'无':parameter.entity.parent.name}</p>
									<input type="hidden" name="parent.id"
										value="${parameter.entity.parent==null?'':parameter.entity.parent.name}">
								</div>
							</div>

							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">等级名称</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="editName"
										name="name" value="${parameter.entity.name}"
										placeholder="输入等级名称">
								</div>
							</div>
							<!-- 等级类型 -->
							<!-- 产品类型 -->
						<div class="form-group">
								<label for="spec" class="col-sm-2 control-label">对应产品类型</label>
								<div class="col-sm-6">
								<c:forEach items="${types}" var="type">
									 <label class="radio-inline">
    			  						<input required  type="radio" name="goodsType" id="goodsType" value="${type}"  ${parameter.entity.goodsType eq  type ? 'checked' : ''} > 
        					 				 ${type.desc}
        							 </label>
   								</c:forEach>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">允许自动升级</label>
								<div class="col-sm-10">
									<label class="radio-inline"> <input type="radio"
										name="auto_promotion" id="inlineRadio1" 
										<c:if test="${parameter.entity.auto_promotion}">
										 checked="checked"</c:if> value="1">
										允许
									</label> <label class="radio-inline"> <input
										type="radio" name="auto_promotion" id="inlineRadio2"
										<c:if test="${not parameter.entity.auto_promotion}">
										 checked="checked"</c:if>
										value="0">不允许
									</label>
								</div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">排列顺序</label>
								<div class="col-sm-10">
									<input type="number" class="form-control" id="editOrder"
										name="order" value="${parameter.entity.order}"
										placeholder="输入显示顺序">
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
							<span class="glyphicon glyphicon-close">&nbsp;</span>关闭
						</button>
					</div>
					<div class="col-md-6 col-xs-12">
						<button type="submit" class="btn btn-success btn-block"
							form="editForm" id="saveBtn" name="saveBtn" value="saveBtn"
							tabindex="4" data-loading-text="正在提交......">
							<span class="glyphicon glyphicon-save">&nbsp;</span>保存
						</button>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>


<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
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
											alert("保存成功");
											$(".quitBtn").click();
											$("#search").click();
										} else {
											alert("保存失败");
										}

									},
									error : function(xhr, textStatus,
											errorThrown) {
										var m = $.parseJSON(xhr.responseText);
										$("#alert").empty().html(m.message)
												.removeClass("invisible");
										btn.button("reset");
									}
								});
					},
					rules : {
						"entity.name" : {
							required : true
						}
					},
					onkeyup : false,
					messages : {
						"entity.name" : {
							required : "代理等级必须填写"
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
		$("#saveBtn").click(function() {
			btn = $(this).button();
		});
	}
</script>
