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
			<h4 class="modal-title text-primary" id="myModalLabel">增加库存</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/stock/pm/edit.json'/>" name="editForm"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">
				
					<div class="col-md-12 col-xs-12">
					<div class="panel panel-primary">
							<div class="panel-heading">
								增加库存
							</div>
							<div class="panel-body">						
							<input type="hidden" name="goods.id" value="${stockBlotter.entity.goods.id}">
							<input type="hidden" name="id" value="${stockBlotter.entity.id}">
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">产品名称</label>
								<div class="col-sm-6">
								<p class="form-control-static">${stockBlotter.entity.goods.name}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">当前库存</label>
								<div class="col-sm-4">
									<p class="form-control-static">${stockBlotter.entity.goods.stockQuantity}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">增加库存</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editCurrentStock" tabIndex="12"
										name="change" value="${stockBlotter.entity.change}"
										placeholder="输入产品库存">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">备注</label>
								<div class="col-sm-6">
									<textarea class="form-control" id="editRemark" tabIndex="14" rows="4"
										name="remark" value="${stockBlotter.entity.remark}"
										placeholder="输入库存备注"></textarea>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
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
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-success btn-block" form="editForm" tabIndex="27"
						id="saveBtn" name="saveBtn" value="saveBtn" tabindex="4"
						data-loading-text="正在提交......">
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
		$("#editForm").validate({submitHandler : function(form) {
						$(form).ajaxSubmit({
									beforeSubmit : function(arr, $form, options) {
										btn.button("loading");
									},
									success : function(responseText,
											statusText, xhr, $form) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										if(m.flag=="0"){
											alert("保存成功");
											$(".quitBtn").click();
											$("#search").click();
										}else{
											alert("保存失败");
										}
										
									},
									error : function(xhr, textStatus,
											errorThrown) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										alert("保存失败");
									}
								});
					},
					rules : {
						point : {
							required : true,
							number: true
						},
						change:{
							required: true,
							digits:true
						}
					},
					onkeyup : false,
					messages : {
						point : {
							required : "积分变更必须填写"
						},
						change:{
							required:"库存变更数必须填写",
							digits:"库存数必须为整数"
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
		$("#editForm").find("input[type='checkbox']").change(function(e){
			var $t=$(this);
			var next=$t.next("input[type='hidden']");
			$t.prop("checked")?next.val(1):next.val(0);
		});
		$("#saveBtn").click(function(e) {
			btn = $(this).button();
			$(document.forms["editForm"]).submit();
		});
	}
</script>
