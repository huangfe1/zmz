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
			<h4 class="modal-title" id="myModalLabel">积分商城订单详情</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/pmall/order/pay.json'/>"
					name="editForm" class="form-horizontal" id="editForm" method="post">
					<input type="hidden" name="id" value="${parameter.entity.id}">
					<div class="row" id="products">
						<div class="col-md-12 col-xs-12">
							<div class="panel panel-info">
								<div class="panel-heading">订单号:${parameter.entity.orderNo}</div>
								<div class="panel-body">
									<div class="col-md-12 col-xs-12">
										<div class="form-group">
											<label class="col-sm-2 control-label">收货人姓名</label>
											<div class="col-sm-4">
												<p class="form-control-static">
													${parameter.entity.consignee}</p>
											</div>
											<label class="col-sm-2 control-label">联系电话</label>
											<div class="col-sm-4">
												<p class="form-control-static">
													${parameter.entity.mobile}</p>
											</div>
										</div>
										<div class="form-group">
											<%--<label class="col-sm-2 control-label">赠送代金券</label>--%>
											<%--<div class="col-sm-4">--%>
												<%--<p class="form-control-static">--%>
													<%--${parameter.entity.voucher}</p>--%>
											<%--</div>--%>
											<label class="col-sm-2 control-label">赠送福利积分</label>
											<div class="col-sm-4">
												<p class="form-control-static">
													${parameter.entity.benefitPoints}</p>
											</div>
										</div>
										<div class="form-group">
											<label for="editWeixin" class="col-sm-2 control-label">订单状态</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.status.name}</p>
											</div>
											<label for="editWeixin" class="col-sm-2 control-label">付款状态</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.paymentStatus.name }</p>
											</div>
										</div>
										<div class="form-group">
											<label for="editWeixin" class="col-sm-2 control-label">订单金额</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.totalMoney}</p>
											</div>
											<label for="editWeixin" class="col-sm-2 control-label">付款方式</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.paymentWay.name}</p>
											</div>
										</div>
										<div class="form-group">
											<label for="editWeixin" class="col-sm-2 control-label">物流单号</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.logisticsCode}</p>
											</div>
											<label for="editWeixin" class="col-sm-2 control-label">物流公司</label>
											<div class="col-sm-4">
												<p class="form-control-static">${parameter.entity.logistics}</p>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>

			<div class="container-fluid">
				<div class="row" id="products">
					<div class="col-md-12 col-xs-12">
						<div class="panel panel-success"> 
							<div class="panel-heading">订单商品列表</div>
							<div class="table-responsive">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>商品</th>
											<th>单价</th>
											<%--<th>折扣价</th>--%>
											<th>数量</th>
											<th>金额</th>
											<%--<th>所需积分</th>--%>
										</tr>
									</thead>
									<tbody id="cartitems">
										<c:forEach items="${parameter.entity.items}" var="item">
											<tr>
												<td>${item.value.goodsName}</td>
												<td>${item.value.moneyPrice}</td>
												<%--<td>${item.value.pointsPrice}</td>--%>
												<td class="quantity">${item.value.quantity}</td>
												<td class="amount">${item.value.amountMoney}</td>
												<%--<td class="pointsAmount">${item.value.amountPoints}</td>--%>
											</tr>
										</c:forEach>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="2">合计</td>
											<td id="totalQuantity"></td>
											<td id="totalAmount"></td>
											<%--<td id="totalPointsAmount"></td>--%>
										</tr>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn btn-lg"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		calcTotal();
		function calcTotal() {
			var totalQuantity = 0, totalAmount = 0.0, totalPointsAmount = 0.0;
			$(".quantity").each(function(i, d) {
				totalQuantity += parseInt($(d).text());
			});
			$(".amount").each(function(i, d) {
				totalAmount += parseFloat($(d).text());
			});
			$(".pointsAmount").each(function(i, d) {
				totalPointsAmount += parseFloat($(d).text());
			});
			$("#totalQuantity").text(totalQuantity);
			$("#totalAmount").text(Number(totalAmount).toFixed(2));
			$("#totalPointsAmount").text(Number(totalPointsAmount).toFixed(0));
		}
	});
</script>
