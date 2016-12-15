<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header bg-primary">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="myModalLabel">代理账户信息明细</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">				
					<div class="row">
					<form action="<c:url value='/agent/audit.json'/>" name="editForm"
					class="form-horizontal" id="editForm" method="post">
					<ul class="nav nav-tabs" role="tablist">
						 <li role="presentation" class="active"><a href="#basicInfo" aria-controls="basicInfo" role="tab" data-toggle="tab">基本信息</a></li>
						 <li role="presentation"><a href="#authedInfo" aria-controls="authedInfo" role="tab" data-toggle="tab">账户信息</a></li>
					</ul>
					<div class="tab-content">
    					<div role="tabpanel" class="tab-pane active" id="basicInfo">
    					<div class="col-md-12 col-xs-12">
    						<div class="form-group">
										<input type="hidden" name="id" id="agentId" value="${parameter.entity.id}">
										<label class="col-sm-2 control-label">代理编号</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.agentCode}</p>
										</div>
										<label for="editName" class="col-sm-2 control-label">姓名</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.realName}</p>
										</div>
									</div>
									<div class="form-group">
										<label for="editLoginName" class="col-sm-2 control-label">系统登陆名</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.loginName}</p>
										</div>
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.mobile}</p>
										</div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">微信号</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.weixin}</p>
										</div>
										<label for="currentPoint" class="col-sm-2 control-label">身份证号</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.idCard}</p>
										</div>
									</div>
									<c:if test="${user.admin}">
									<div class="form-group">
										<label for="currentPoint" class="col-sm-2 control-label">登陆密码</label>
										<div class="col-sm-4">
											<p class="form-control-static">${parameter.entity.password}</p>
										</div>
									</div>
									</c:if>
									<!-- 管理员才能查看 -->
										<c:if test="${user.admin}">
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">汇款信息</label>
										<div class="col-sm-10">
											<textarea rows="3" class="form-control" id="editRemittance" readonly="readonly"
												tabIndex="14" required name="remittance"
												value="${parameter.entity.remittance}" placeholder="请输入汇款信息">${parameter.entity.remittance}</textarea>
										</div>
									</div>
										</c:if>
									</div>
    					</div>
    					<div role="tabpanel" class="tab-pane" id="authedInfo">
    					<div class="col-md-12">
								<table class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<th>产品名称</th>
											<th>库存</th>
											<th>累计转货量</th>
											<th>所处等级</th>
										</tr>
									</thead>
									<tbody id="accounts">
										<c:forEach items="${parameter.entity.goodsAccounts}"
											var="accounts">
											<tr>
												<td>${accounts.goods.name}</td>
												<td>${accounts.currentBalance}</td>
												<td>${accounts.cumulative}</td>
												<!-- <td>${accounts.agentLevel.name}</td> -->
												<td><select class="form-control priceLevel" <c:if test="${user.agent}"> readonly="readonly"
												 disabled="disabled"</c:if>
												id="priceLevel" name="priceLevel" data-goods="${accounts.goods.id}">
												<c:forEach items="${accounts.goods.prices}" var="priceLevel">
													<option value="${priceLevel.agentLevel.id}"
													<c:if test="${priceLevel.agentLevel.id  eq accounts.agentLevel.id}">
													 selected="selected" </c:if>>${priceLevel.agentLevel.name}</option>
												</c:forEach>
												</select></td>
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
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn"
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
		init();
	});
	function init() {
		$("#accounts").delegate(".priceLevel","change",function(e){
			var that=$(this);
			var param={
					"id":$("#agentId").val(),
					"levelId":that.val(),
					"goodsId":that.attr("data-goods")
			}
			$.ajax({method:"POST",data:param,url:"<c:url value='/agent/level/change.json'/>",success:function(){
				alert("修改成功");
			},error:function(){alert("修改失败");}})
		});
		var btn = null;
		$('#mainTable tbody').on('click', 'tr', function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
				$(this).find("input[type='checkbox']").prop("checked", false);
			} else {
				dt.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');
				$(this).find("input[type='checkbox']").prop("checked", true);
			}
		});
		$('a[data-toggle="tab"]').on("show.bs.tab", function (e) {
		});
	}
</script>
