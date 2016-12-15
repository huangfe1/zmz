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
			<h4 class="modal-title" id="myModalLabel"><span class="glyphicon glyphicon-pencil"></span>防伪码录入</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/securityCode/editWithNum.json'/>" name="editForm"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">
				
					<div class="col-md-12 col-xs-12">
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">货物名称</label>
								<div class="col-sm-4">
									<input type="text" class="form-control typeahead" id="editGoodsName" tabIndex="12"
										name="goodsName" value="${parameter.entity.goodsName}"
										<c:if test="${not empty parameter.entity.id}">
										readonly="readonly"
										</c:if>
										placeholder="输入产品名称">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">货物所有人</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="editOwner" tabIndex="13"
										name="owner" value="${parameter.entity.owner}"
										placeholder="输入本货物所有人">
									<span class="help-block">所有人是指此防伪码对应的货物发给了哪个代理</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editAgentCode" class="col-sm-2 control-label">代理编号</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" required id="editAgentCode" tabIndex="14"
										name="agentCode" value="${parameter.entity.agentCode}"
										placeholder="输入货物所有人代理编号">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">防伪码</label>
								<div class="col-sm-6">
									<textarea class="form-control" id="editCode" tabIndex="15" rows="4"
										name="codeSegment" value="${parameter.entity.code}"
										placeholder="输入防伪码">${parameter.entity.code}</textarea>
									<p class="text-danger">防伪码格式：录入头编号比如11，33  下面填写数量20   将录入11-30 &nbsp;  33-52</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="quantity" class="col-sm-2 control-label">数量</label>
								<div class="col-sm-6">
									<input type="number" name="quantity" value="1" id="quantity" class="form-control" required min="1"
									 tabindex="16" placeholder="防伪码连号数量">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
					</div>
				</div>
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-4 col-xs-6">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<c:if test="${user.admin}">
				<div class="col-md-4 col-xs-6">
					<button type="button" class="btn btn-danger btn-block" form="editForm" tabIndex="27"
						id="removeBtn" name="removeBtn" value="removeBtn" tabindex="5"
						<c:if test="${user.agent}"> disabled="disabled"</c:if>
						data-loading-text="正在删除......">
						<span class="glyphicon glyphicon-trash">&nbsp;</span>删除
					</button>
				</div>
				</c:if>
				<div class="col-md-4 col-xs-6">
					<button type="button" class="btn btn-success btn-block" form="editForm" tabIndex="27"
						id="saveBtn" name="saveBtn" value="saveBtn" tabindex="3"
						data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-floppy-save">&nbsp;</span>保存
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
		$("#editGoodsName").focus().select();
		$('#editCode').popover({"animation":true,"placement":"right"});
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
											alert("防伪码保存成功");
											$(".quitBtn").click();
											$("#search").click();
										}else{
											alert("防伪码保存失败,"+m.message);
										}
										
									},
									error : function(xhr, textStatus,
											errorThrown) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										alert("防伪码录入失败");
									}
								});
					},
					rules : {
						goodsName : {
							required : true
						},
						agentCode:{
							required:true,
							remote:"<c:url value='/dmz/agent/exists.json'/>"
						},
						owner:{
							required: true
						},
						codeSegment:{
							required: true
						}
					},
					onkeyup : false,
					messages : {
						goodsName : {
							required : "请填写货物名称"
						},
						agentCode:{
							required:"请填写货物所有者代理编号",
							remote: "代理编码对应的代理不存在"
						},
						owner:{
							required:"请填写货物所有者"
						},
						codeSegment:{
							required:"请填写防伪码"
						}
					},
					focusInvalid : true,
					errorClass : "text-danger",
					validClass : "valid",
					errorElement : "small",
					errorPlacement : function(error, element) {
						error.appendTo(element.closest("div.form-group")
								.children("div.text-error"));
					}
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
		
		$("#removeBtn").click(function(e) {
			var agent=$("#editAgentCode"),code=$("#editCode"),quantity=$("#quantity");
			if($.trim(agent.val()).length==0){
				alert("请输入防伪码所属代理编码");
				agent.focus();
				return false;
			}
			if($.trim(code.val()).length==0){
				alert("请输入需要删除的防伪码");
				code.focus();
				return false;
			}
			if(!window.parseInt(quantity.val())){
				alert("请正确输入数量");
				quantity.focus();
				return false;
			}
			if(parseInt(quantity.val())<1){
				alert("数量不能小于1");
				quantity.focus();
				return false;
			}
			var param={"agent":agent.val(),"code":code.val(),"quantity":quantity.val()};
			var url="<c:url value='/securityCode/rang-rm-num.json'/>";
			$.ajax({url:url,method:"POST",data:param,complete:function(xhr,ts){
				if(xhr.status>=200 && xhr.status<300){
					var m = $.parseJSON(xhr.responseText);
					if (m.flag == "0") {
						window.setTimeout(function() {
							$("#search").click();
						}, 1100);
						alert("删除成功");				
					}else{
						alert("删除失败,"+m.message);
					}
						
				}else{
					alert("删除失败");
				}
			}});
		});
		
		var names = new Bloodhound({
			name : "names",
			datumTokenizer : Bloodhound.tokenizers.obj.whitespace("value"),
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			limit : 15,
			prefetch : {
				url : "<c:url value='/dmz/goods/names.json'/>",
				ttl:86400000,
				filter : function(l) {
					return $.map(l, function(d) {
						return {
							value : d
						};
					});
				}
			}
		});
		names.initialize();
		$("#editGoodsName.typeahead").typeahead({
			hint : true,
			highlight : true,
			minLength : 2
		}, {
			name : "names",
			displayKey : "value",
			source : names.ttAdapter()
		});
	}
</script>
