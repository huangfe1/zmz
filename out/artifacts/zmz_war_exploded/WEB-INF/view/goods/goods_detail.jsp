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
			<h4 class="modal-title text-primary" id="myModalLabel">产品信息详情</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/goods/edit.json'/>" name="editForm"
					class="form-horizontal" id="editForm" method="post">
					<div class="row">

						<div class="col-md-12 col-xs-12">
							<div class="panel panel-primary">
								<div class="panel-heading">基本信息</div>
								<div class="panel-body">
									<div class="form-group">
										<label for="name" class="col-sm-2 control-label">产品名称</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.name}</p>
										</div>
										
								<label for="pointFactor" class="col-sm-2 control-label">积分系数</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.pointFactor}</p>
								</div>
								</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">当前库存</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.currentStock}</p>
								</div>
								<label for="currentPoint" class="col-sm-2 control-label">当前积分</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.currentPoint}</p>
								</div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">当前余额</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.currentBalance}</p>
								</div>
								<label for="benchmark" class="col-sm-2 control-label">是主打产品</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.benchmark?"是":"否"}</p>
								</div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">排列顺序</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.order}</p>
								</div>
							</div>
						<!-- </form>-->
						</div>
						</div>
					</div>
					<div class="col-md-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								产品授权信息
							</div>							
							<input type="hidden" name="authorizationType.id" value="${parameter.entity.authorizationType.id}">
							<div class="panel-body">
							<div class="form-group">
								<label for="authName" class="col-sm-2 control-label">授权类型</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.authorizationType.name}</p>
								</div>
								<label for="editOrder" class="col-sm-2 control-label">授权排列顺序</label>
								<div class="col-sm-4">
									<p class="form-control-static">${parameter.entity.authorizationType.order}</p>
								</div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">授权说明</label>
								<div class="col-sm-10">
									<textarea class="form-control" name="authorizationType.remark" readonly="readonly"
									 rows="3" tabIndex="17" placeholder="授权类型的说明">${parameter.entity.authorizationType.remark}
									 </textarea>
								</div>
							</div>
							</div>
						</div>
					</div>
					<div class="col-md-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								价格设置
							</div>							
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
											</td>
											<td><p class="form-control-static">${levelP.price}</p></td>
											<td><p class="form-control-static">${levelP.threshold}</p></td>
										</tr>
									</c:forEach>
								</tbody>
  							</table>
  							
						</div>
					</div>
				</div>
				
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-primary btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭详情
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
	}
</script>
