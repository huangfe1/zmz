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
			<h4 class="modal-title" id="myModalLabel">完成发货</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<form action="<c:url value='/delivery/delivery.json'/>"
						name="editForm" class="form-horizontal" id="editForm"
						method="post">
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
											<input type="number" readonly class="form-control" tabIndex="12"
												value="${parameter.entity.logisticsFee}" name="logisticsFee"
												placeholder="已经收取物流费用">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>

									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">实际物流费用</label>
										<div class="col-sm-6">
											<input type="number"  class="form-control" tabIndex="12"
												   value="${parameter.entity.logisticsFee}" name="actual_logisticsFee"
												   placeholder="修改实际物流费">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
								</div>
							</div>
						</div>
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
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>

									<div class="form-group">
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.mobile}</p>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">收货地址</label>
										<div class="col-sm-6">
											<p class="form-control-static">${parameter.entity.province}${parameter.entity.city}${parameter.entity.county}${parameter.entity.address}</p>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">备注</label>
										<div class="col-sm-10">
											<textarea rows="3" class="form-control" id="editRemark"
												readonly="readonly" tabIndex="14" name="remark"
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
										<table
											class="table table-striped table-bordered table-condensed"
											id="mainTable">
											<thead>
												<tr>
													<th>货物名称</th>
													<th>数量</th>
												</tr>
											</thead>
											<tbody id="itemsBody">
												<c:forEach items="${parameter.entity.deliveryItems}"
													var="item">
													<tr data-id="${item.goods.id}">
														<td>${item.goods.name}<input type="hidden"
															name="goodsIds" value="${item.goods.id}">
														</td>
														<td><input type="number" value="${item.quantity}"
															readonly="readonly" required name="quantitys" min="0"></td>
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
						tabIndex="27" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-danger btn-block"
						form="editForm" tabIndex="26" id="saveBtn" name="saveBtn"
						value="saveBtn" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-ok">&nbsp;</span>发货
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
																	"<c:url value='/delivery/print.html?id=${parameter.entity.id}'/>",
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
								consigneeName : {
									required : false
								},
								mobile : {
									required : true,
									mobile : true
								},
								address : {
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
