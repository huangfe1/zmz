<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header bg-danger">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title bg-danger" id="myModalLabel">代理商城订单-收款确认</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<form action="<c:url value='/delivery/confirm.json'/>" name="editForm"
						class="form-horizontal" id="editForm" method="post">
						<div class="panel panel-primary">
							<div class="panel-heading">收货信息</div>
							<div class="panel-body">
								<div class="col-md-12 col-xs-12">
									<input type="hidden" name="id" value="${parameter.entity.id}">
									<div class="form-group">
										<label for="editAgentCode" class="col-sm-2 control-label">收货人代理编号</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.userByConsignee.agentCode}</p>
										</div>
										<label for="editLoginName" class="col-sm-2 control-label">收货人姓名</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.consigneeName}</p>
										</div>
									</div>
									
									<div class="form-group">
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.mobile}</p>
										</div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">收货地址</label>
										<div class="col-sm-6">
											<p class="form-control-static">${parameter.entity.address}</p>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">金额+物流费</label>
										<div class="col-sm-6">
                                                <c:set var="logisticsFee" value="${parameter.entity.logisticsFee==null?0:parameter.entity.logisticsFee}"/>
											<p class="form-control-static"><strong>${parameter.entity.amount}+${logisticsFee}=${parameter.entity.amount+logisticsFee}</strong></p>
										</div>
									</div>
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">备注</label>
										<div class="col-sm-10">
											<textarea rows="3" class="form-control" id="editRemark" readonly="readonly"
												tabIndex="14" name="remark"
												value="${parameter.entity.remark}" placeholder="请输入发货备注信息">${parameter.entity.remark}</textarea>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="panel panel-primary">
							<div class="panel-heading">货物信息</div>
							<div class="panel-body">
								<div class="col-md-12">
									<div class="table-responsive">
										<table style="width: 200%;max-width: 200%"
											class="table table-striped table-bordered table-condensed"
											id="mainTable">
											<thead>
												<tr>
													<th>货物名称</th>
													<th>数量</th>
													<c:if test="${parameter.entity.applyOrigin eq 'MALL' }">
													<th>价格</th>
													<th>金额</th>
													</c:if>
												</tr>
											</thead>
											<tbody id="itemsBody">
												<c:forEach items="${parameter.entity.deliveryItems}" var="item">
													<tr data-id="${item.goods.id}">
														<td>${item.goods.name}
														<input type="hidden" name="goodsIds" value="${item.goods.id}">
														</td>
														<td><input type="number" class="form-control" value="${item.quantity}" readonly="readonly" required
															name="quantitys" min="0"></td>
														<c:if test="${parameter.entity.applyOrigin eq 'MALL' }">
															<td>${item.price}</td>
															<td>${item.amount}</td>
														</c:if>
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
				<c:if test="${parameter.entity.status=='NEW' }">
				<div class="col-md-6 col-xs-12">
					<button type="button"  class="btn btn-danger btn-block"
						form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
						value="saveBtn" tabindex="4" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-ok">&nbsp;</span>确认收款
					</button>
				</div>
				</c:if>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
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
															alert("确认收款操作成功");
															$(".quitBtn")
																	.click();
															$("#search")
																	.click();
														} else {
															alert("确认收款操作失败,"+m.message);
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														alert("收款确认失败");
													}
												});
							},
							rules : {
								consigneeName : {
									required : false
								},
								mobile : {
									required : true,
									mobile : true
								},
								address: {
									required : true
								}
							},
							onkeyup : false,
							messages : {
								consigneeName : {
									required : "请输入收货人姓名"
								},
								mobile : {
									required : "请输入手机号码",
									mobile : "手机号码格式不正确"
								},
								address : {
									required : "请输入收货地址"
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
