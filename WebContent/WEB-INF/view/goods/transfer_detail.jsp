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
td.quantitys{
width:20%;
}
td.span4{
width:25%;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id="myModalLabel">转货清单</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="table-responsive">
						<table class="table table-striped table-condensed table-bordered">
							<thead>
								<tr>
									<th>货物名称</th>
									<th>价格</th>
									<th>数量</th>
									<th>金额</th>
								</tr>
							</thead>
							<tbody>
							<c:set var="amount" value="0"/>
							<c:set var="quantity" value="0"/>
							<c:forEach items="${items}" var="item">
								<tr>
									<td>${item.goodsName}</td>
									<td>${item.price}</td>
									<td>${item.quantity}</td>
									<td>${item.amount}</td>
								</tr>
								<c:set value="${item.amount+amount}" var="amount"/>
								<c:set value="${item.quantity+quantity}" var="quantity"/> 
							</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th colspan="2">合计</th>
									<th>${quantity}</th>
									<th>${amount}</th>
								</tr>
							</tfoot>
						</table>
					</div>
				</div>
			</div>
			</div>	
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</div>