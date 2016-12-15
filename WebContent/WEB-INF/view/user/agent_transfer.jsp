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
			<h4 class="modal-title text-primary" id="myModalLabel">信息编辑</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/agent/transfer.json'/>" name="editForm"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">
				
					<div class="col-md-12 col-xs-12">
					<div class="panel panel-primary">
							<div class="panel-heading">
								出让代理基本信息
							</div>
							<div class="panel-body">	
						
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">代理编码</label>
								<div class="col-sm-6">
									<p class="form-control-static">${parameter.entity.agentCode}</p>
								</div>
							</div>
							<div class="form-group">
								<label for="pointFactor" class="col-sm-2 control-label">姓名</label>
								<div class="col-sm-6">
									<p class="form-control-static">${parameter.entity.realName}</p>
								</div>
							</div>
						<!-- </form>-->
						</div>
						</div>
					</div>
					<div class="col-md-12">
						<div class="panel panel-primary">
							<div class="panel-heading">
								选择接收方代理
							</div>							
							<table class="table table-condensed" id='mainTable'>
							<caption>
												<label>代理编码</label> <input type="text" value=""
													name="searchAgentCode" id="searchAgentCode" placeholder="代理编码"> 
												<button type="button" class="btn btn-primary navbar-btn"
													id="searchDT" name="searchDT">
													<span class="glyphicon glyphicon-search"></span>&nbsp;查询
												</button>
											</caption>
								<thead>
									<tr>
										<th>编码</th>
										<th>实名</th>
										<th>电话</th>
										<th>选择</th>
									</tr>
								</thead>
								<tbody id="dataList">									
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
						toId : {
							required : true
						}
					},
					onkeyup : false,
					messages : {
						toId : {
							required : "请选择一位接收代理"
						}
					},
					focusInvalid : true,
					errorClass : "text-danger",
					validClass : "valid",
					errorElement : "small"
				});
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
			"pageLength" : 5,
			"ajax" : {
				"url" : "<c:url value='/agent/transfer/query.json'/>",
				"data" : function(d) {
					return $.extend({}, d, {
						"entity.agentCode" : $('#searchAgentCode').val(),"useDatatables":"true"
					});
				}
			},
			"columns" : [ {
				"data" : "agentCode"
			}, {
				"data" : "realName"
			},{
				"data" : "mobile"
			},
			{
				"data" : "id"
			} ],
			"columnDefs" : [ {
				"render" : function(data, type, row) {
					return "<input type='radio' name='toId' value='"+data + "'/>";
				},
				"targets" : 3
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
