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
			<h4 class="modal-title" id="myModalLabel">代理信息审核-${action}</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">				
					<div class="row">
					<form action="<c:url value='/agent/audit.json'/>" name="editForm"
					class="form-horizontal" id="editForm" method="post">
					<ul class="nav nav-tabs" role="tablist">
						 <li role="presentation" class="active"><a href="#basicInfo" aria-controls="basicInfo" role="tab" data-toggle="tab">基本信息</a></li>
						 <li role="presentation"><a href="#authedInfo" aria-controls="authedInfo" role="tab" data-toggle="tab">已有授权</a></li>
					</ul>
					<div class="tab-content">
    					<div role="tabpanel" class="tab-pane active" id="basicInfo">
    					<div class="col-md-12 col-xs-12">
    						<div class="form-group">
										<input type="hidden" name="id" value="${parameter.entity.id}">
										<label class="col-sm-2 control-label">代理编号</label>
										<div class="col-sm-10">
											<p class="form-control-static">${parameter.entity.agentCode}</p>
										</div>
									</div>
									<div class="form-group">
										<label for="editName" class="col-sm-2 control-label">姓名</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editName"
												tabIndex="10" required name="realName"
												value="${parameter.entity.realName}" placeholder="输入代理姓名">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editLoginName" class="col-sm-2 control-label">系统登陆名</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editLoginName"
												tabIndex="10" name="loginName"
												value="${parameter.entity.loginName}" placeholder="输入登陆系统账户名">
											<p class="help-block">缺省使用系统生成的代理编码作为系统登陆账号</p>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
										<div class="col-sm-4">
											<input type="tel" class="form-control" id="editMobile"
												tabIndex="11" required name="mobile"
												value="${parameter.entity.mobile}" placeholder="输入代理联系电话">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="editWeixin" class="col-sm-2 control-label">微信号</label>
										<div class="col-sm-4">
											<input type="text" class="form-control" id="editWeixin"
												tabIndex="12" required name="weixin"
												value="${parameter.entity.weixin}" placeholder="输入微信号">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentPoint" class="col-sm-2 control-label">身份证号</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="editIdCard"
												tabIndex="13" required name="idCard"
												value="${parameter.entity.idCard}" placeholder="输入代理身份证号">
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									<div class="form-group">
										<label for="currentBalance" class="col-sm-2 control-label">汇款信息</label>
										<div class="col-sm-6">
											<textarea rows="3" class="form-control" id="editRemittance"
												tabIndex="14" required name="remittance"
												value="${parameter.entity.remittance}" placeholder="请输入汇款信息">${parameter.entity.remittance}</textarea>
										</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
									</div>
									</div>
    					</div>
    					<div role="tabpanel" class="tab-pane" id="authedInfo">
    					<div class="col-md-12">
								<table class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<th>授权类型</th>
											<th>授权产品</th>
											<th>授权状态</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${parameter.entity.authorizations}"
											var="auth">
											<tr>
												<input type="hidden" value="${auth.id}" name="authedIds">
												<td>${auth.authorizationType.name}</td>
												<td>${auth.authorizationType.goods.name}</td>
												<td>${auth.status.desc}</td>
												<td><a class="btn btn-danger removeAuthBtn ajaxLink" 
												href="<c:url value='/agent/auth/remove.json?id=${parameter.entity.id}&authId=${auth.id}'/>">
														<i class="glyphicon glyphicon-trash"></i>取消本授权
													</a></td>
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
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-primary btn-block"
						form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
						value="saveBtn" tabindex="4" data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-save">&nbsp;</span>审核通过
					</button>
				</div>
			</div>
		</div>
	</div>
</div>


<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
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
		var langUrl = "<c:url value='/resources/startbootstrap-admin/bower_components/datatables/media/localisation/Chinese.json'/>";
		var dt = $("#mainTable").DataTable({
			language : {
				url : langUrl
			},
			"processing" : true,
			"serverSide" : true,
			"searching" : false,
			"lengthChange" : false,
			"ordering" : false,
			"pageLength" : 10,
			"ajax" : {
				"url" : "<c:url value='/authType/query.json'/>",
				"data" : function(d) {
					return $.extend({}, d, {
						"entity.name" : $('#authName').val(),"entity.goods.name":$('#goodsName').val(),
						"useDatatables":"true"
					});
				}
			},
			"columns" : [ {
				"data" : "name"
			}, {
				"data" : "goods.name"
			}, {
				"data" : "id"
			} ],
			"columnDefs" : [ {
				"render" : function(data, type, row) {
					return "<input type='checkbox' name='ids' value='"+data + "'/>";
				},
				"targets" : 2
			} ]
		});
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
		$("#searchDT").click(function(e) {
			dt.draw();
		});
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
															alert("保存成功");
															$(".quitBtn")
																	.click();
															$("#search")
																	.click();
														} else {
															alert("保存失败");
														}

													},
													error : function(xhr,
															textStatus,
															errorThrown) {
														var m = $
																.parseJSON(xhr.responseText);
														btn.button("reset");
														alert("保存失败");
													}
												});
							},
							rules : {
								realName : {
									required : true
								},
								loginName : {
									required : true
								},
								mobile:{
									required:true,
									mobile:true
								},
								weixin:{
									required:true
								},
								idCard:{
									required:true
								},
								remittance:{
									required:true
								}
								
							},
							onkeyup : false,
							messages : {
								realName : {
									required : "请输入代理姓名"
								},
								loginName:{
									required: "请输入代理登陆系统账户名"
								},
								mobile : {
									required : "请输入手机号码",
									mobile: "手机号码格式不正确"
								},
								weixin:{
									required:"请输入微信号"
								},
								idCard:{
									required:"请输入身份证号"
								},
								remittance:{
									required:"请输入汇款信息"
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
		$("#removeAuthBtn").click(function(e) {
			$(this).closest("tr").remove();
		});
		$("a.ajaxLink.removeAuthBtn").click(function(e){
			e.preventDefault();
			e.stopPropagation();
			var method="DELETE";
			if(!window.confirm("确定取消本授权?")){
				return false;
			}
			var dataRow=$(this).parents("TR");
			$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
				if(xhr.status>=200 && xhr.status<300){
					$("#alertMessageBody").empty().html("取消产品授权操作成功");
					$("#myAlertModal").modal({backdrop:"static",show:true});
					dataRow.remove();
				}else{
					$("#alertMessageBody").empty().html("取消产品授权操作失败").addClass("text-danger");
					$("#myAlertModal").modal({backdrop:"static",show:true});
				}
			}});
		});
		$('a[data-toggle="tab"]').on("show.bs.tab", function (e) {
			  alert($(e.target).prop("id"));
		});
	}
</script>
