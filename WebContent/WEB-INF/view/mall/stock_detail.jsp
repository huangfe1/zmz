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
			<h4 class="modal-title text-primary" id="myModalLabel">库存流水</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<input type="hidden" name="goodsId" value="${stockBlotter.entity.goods.id}"  id="goodsId">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								库存流水
							</div>
							<div class="table-responsive">					
							<table class="table table-striped table-bordered table-hover table-condensed" id="mainTable">
								<thead>
									<tr>
										<th>商品名</th>
										<th>库存变更数</th>
										<th>变更后库存</th>
										<th>操作员</th>
										<th>操作时间</th>
									</tr>
								</thead>
								<tbody>
									<%-- <c:forEach var="st" items="${stocks}" varStatus="s">
										<tr>
											<td>${st.goods.name}</td>
											<td>${st.change}</td>
											<td>${st.currentStock}</td>
											<td>${st.point}</td>
											<td>${st.currentPoint}</td>
											<td>${st.user.realName}</td>
											<td>${st.operateTime}</td>
										</tr>
									</c:forEach> --%>
								</tbody>
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


<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		var langUrl = "<c:url value='/resources/js/datatables-plugins/i18n/Chinese.json'/>";
		var dt = $("#mainTable").DataTable({
			language : {
				url : langUrl
			},
			"processing" : true,
			"serverSide" : true,
			"searching" : false,
			"lengthChange" : false,
			"ordering" : false,
			"pageLength" : 15,
			"ajax" : {
				"url" : "<c:url value='/stock/pm/detail/query.json'/>",
				"data" : function(d) {
					return $.extend({}, d, {
						"goodsId" : $('#goodsId').val(),
						"useDatatables":"true"
					});
				}
			},
			"columns" : [ {
				"data" : "goods.name"
			}, {
				"data" : "change"
			}, {
				"data" : "currentStock"
			},{
				"data":"user.realName"
			},{
				"data":"operateTime"
			} ]
		});
	}
</script>
