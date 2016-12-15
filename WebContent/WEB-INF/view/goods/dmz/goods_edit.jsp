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
			<h4 class="modal-title text-primary" id="myModalLabel">产品信息编辑</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/goods/edit.json'/>" name="editForm" enctype="multipart/form-data"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">				
					<div class="col-md-12 col-xs-12">
					<div class="panel panel-primary">
							<div class="panel-heading">
								基本信息
							</div>
							<div class="panel-body">	
						
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">产品名称</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="editName" tabIndex="10"
										name="name" value="${parameter.entity.name}"
										placeholder="输入产品名称">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="spec" class="col-sm-2 control-label">产品规格</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="editSpec" tabIndex="11"
										name="spec" value="${parameter.entity.spec}"
										placeholder="输入产品规格">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="img" class="col-sm-2 control-label">产品图片</label>
								<div class="col-sm-4">
									<input type="file" class="form-control" id="img" tabIndex="12"
										name="img" accept="image/png,image/jpeg,image/gif" placeholder="产品图片">
									<span class="help-block">图片尺寸:200*200px 体积小于50kb</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="pointFactor" class="col-sm-2 control-label">零售价</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editRetailPrice" tabIndex="13" required
										name="retailPrice" value="${parameter.entity.retailPrice}"
										placeholder="输入产品零售价">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="pointFactor" class="col-sm-2 control-label">积分系数</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editPointFactor" tabIndex="13"
										name="pointFactor" value="${parameter.entity.pointFactor}"
										placeholder="输入产品积分系数">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">当前库存</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editCurrentStock" tabIndex="14" readonly="readonly"
										name="currentStock" value="${parameter.entity.currentStock}"
										placeholder="输入产品库存">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentPoint" class="col-sm-2 control-label">当前积分</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editCurrentPoint" tabIndex="15" readonly="readonly"
										name="currentPoint" value='<fmt:formatNumber value="${parameter.entity.currentPoint}" groupingUsed="false"/>'
										placeholder="输入产品当前总积分">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">当前余额</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editCurrentBalance" tabIndex="16" readonly="readonly"
										name="currentBalance" value='<fmt:formatNumber value="${parameter.entity.currentBalance}" groupingUsed="false"/>'
										placeholder="输入产品当前余额">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
										<div class="col-sm-offset-2 col-sm-6">
											<div class="checkbox">
												<label> <input type="checkbox" id="benchmark"
													<c:if test="${parameter.entity.benchmark == true }">
														checked="checked"
													</c:if>
													name="benchmarkCheckBox">是主打产品
													<input type="hidden" name="benchmark" 
													value="${parameter.entity.benchmark ? 1 : 0}"> 
												</label>
											</div>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">排列顺序</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editOrder"
										name="order" value="${parameter.entity.order}" tabIndex="17"
										placeholder="输入显示顺序">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
						<!-- </form>-->
						</div>
						</div>
					</div>
					<div class="col-md-12 col-xs-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								产品授权信息
							</div>							
							<input type="hidden" name="authorizationType.id" value="${parameter.entity.authorizationType.id}">
							<div class="panel-body">
							<div class="form-group">
								<label for="authName" class="col-sm-2 control-label">授权类型</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="editAuthName"
										name="authorizationType.name" value="${parameter.entity.authorizationType.name}" tabIndex="17"
										placeholder="授权类型名称">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">授权说明</label>
								<div class="col-sm-4">
									<textarea class="form-control" name="authorizationType.remark" 
									 rows="3" tabIndex="17" placeholder="请输入授权类型的说明">${parameter.entity.authorizationType.remark}</textarea>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
  							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">授权排列顺序</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editOrder"
										name="authorizationType.order" value="${parameter.entity.authorizationType.order}" tabIndex="19"
										placeholder="输入显示顺序">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							</div>
						</div>
					</div>
					<div class="col-md-12 col-xs-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								价格设置
							</div>
							<div class="table-responsive">				
							<table class="table table-condensed">
								<thead>
									<tr>
										<th>等级名称</th>
										<th>单价</th>
										<th>等级阈值</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="l" items="${levels}" varStatus="s">
										<c:set var="levelP" value="${prices[l.id]}" />
										<tr>
											<td>${l.name}
												<input type="hidden" name="levelId"  value="${l.id}">
												<input type="hidden" name="priceId" value="${levelP.id}">
											</td>
											<td><input type="number" name="levelPrice" tabindex="${s.count+18}" required value="${levelP.price}" ></td>
											<td><input type="number" name="levelThreshold" tabindex="${s.count+19}" value="${levelP.threshold}"></td>
										</tr>
									</c:forEach>
								</tbody>
  							</table>
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


<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
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
						name : {
							required : true
						},
						levelPrice:{
							required: true,
							number: true
						},
						currentStock:{
							digits:true,
							min:0
						},
						retailPrice:{
							required:true,
							number:true,
							min:0
						},
						currentBalance:{
							digits:true
						}
					},
					onkeyup : false,
					messages : {
						name : {
							required : "产品名称必须填写"
						},
						levelPrice:{
							required:"价格必须填写",
							number:"价格必须为数字"
						},
						currentStock:{
							digits:"当前库存余额必须为整数",
							min:"库存不能为负数"
						},
						currentBalance:{
							digits:"当前帐户余额必须为整数"
						},
						retailPrice:{
							required:"填写零售价",
							number:"零售价必须为有效数字",
							min:"零售价不能小于0"
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
