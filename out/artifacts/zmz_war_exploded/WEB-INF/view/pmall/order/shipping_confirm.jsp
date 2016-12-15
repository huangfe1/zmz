<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">发货操作&nbsp;订单号:${parameter.entity.orderNo}</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<form action="<c:url value='/pm/order/shipping/confirm.json'/>" name="editForm"
						class="form-horizontal" id="editForm" method="post">
						<div class="panel panel-primary">
							<div class="panel-heading">收货信息</div>
							<div class="panel-body">
								<div class="col-md-12 col-xs-12">
									<input type="hidden" name="id" value="${parameter.entity.id}">
									<div class="form-group">
										<label for="editAgentCode" class="col-sm-2 control-label">代理编号</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.user.agentCode}</p>
										</div>
										<label for="editLoginName" class="col-sm-2 control-label">收货人姓名</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.consignee}</p>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									
									<div class="form-group">
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.mobile}</p>
										</div>
										<label for="editMobile" class="col-sm-2 control-label">邮政编码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.postCode}</p>
										</div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">收货地址</label>
										<div class="col-sm-6">
											<p class="form-control-static">${parameter.entity.shippingAddress}</p>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">备注</label>
										<div class="col-sm-10">
											<p class="form-control-static">${parameter.entity.remark}</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="panel panel-primary">
							<div class="panel-heading">物流信息</div>
							<div class="panel-body">
								<div class="col-md-12 col-xs-12">
									<div class="form-group">
										<label for="editLoginName" class="col-sm-2 control-label">物流单号</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" autofocus="autofocus"
												id="editLogisticsCode" tabIndex="10" placeholder="请填写物流单号"
												value="${parameter.entity.logisticsCode}"
												name="logisticsCode">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>

									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">物流公司</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" tabIndex="11"
												value="${parameter.entity.logistics}"
												placeholder="填写配送的物流公司" name="logistics">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">物流费用</label>
										<div class="col-sm-6">
											<input type="number" class="form-control" tabIndex="12"
												value="${parameter.entity.logisticsFee}" name="logisticsFee"
												placeholder="请填写物流费用">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="panel panel-primary">
							<div class="panel-heading">商品信息</div>
							<div class="panel-body">
								<div class="col-md-12">
									<div class="table-responsive">
										<table
											class="table table-striped table-bordered table-condensed"
											id="mainTable">
											<thead>
												<tr>
													<th>名称</th>
													<th>数量</th>
												</tr>
											</thead>
											<tbody id="itemsBody">
												<c:forEach items="${parameter.entity.items}" var="item">
													<tr data-id="${item.value.goodsId}">
														<td>${item.value.goodsName}
														</td>
														<td>${item.value.quantity}</td>
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
				<div class="col-md-6 col-xs-6">
					<button type="button" class="btn btn-default btn-block quitBtn btn-lg"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-6">
					<button type="button" class="btn btn-danger btn-block btn-lg"
						form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
						value="saveBtn" tabindex="4" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-ok">&nbsp;</span>发货确认
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
		var btn = null;
		$("#editLogisticsCode").focus();
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
															alert("发货操作成功");
															$("#search").click();
															$(".quitBtn").click();
															window.open(
																	"<c:url value='/pm/order/shipping/print.html?id=${parameter.entity.id}'/>",
																	"print",
																	"toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
														} else {
															alert("发货操作失败,"
																	+ m.message);
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														alert("发货操作失败");
													}
												});
							},
							rules : {
								logisticsCode : {
									required : true
								},
								logistics: {
									required : true
								},
								address : {
									required : true
								}
							},
							onkeyup : false,
							messages : {
								logisticsCode : {
									required : "请输入物流单号"
								},
								logistics : {
									required : "请填写物流公司名称"
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