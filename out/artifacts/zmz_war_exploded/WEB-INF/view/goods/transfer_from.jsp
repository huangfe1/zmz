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
width:50%;
}
td.span4{
width:25%;
}
.quantity{
padding:3px 0px !important;
text-align:center !important;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id="myModalLabel">转货申请</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/transfer/applyFrom.json'/>" name="editForm"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">
				
					<div class="col-md-12 col-xs-12">
					<div class="panel panel-primary">
							<div class="panel-heading">
								申请转入
							</div>
							<div class="panel-body">						
							<input type="hidden" name="id" value="${transfer.entity.id}">
							<%-- <div class="form-group">
								<label for="name" class="col-sm-2 control-label">产品名称</label>
								<div class="col-sm-6">
								<p class="form-control-static">${parameter.entity.name}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">对方当前余额</label>
								<div class="col-sm-4">
									<p class="form-control-static">${currentBalance}</p>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">价格</label>
								<div class="col-sm-4">
									<p class="form-control-static">${price}</p>
									<input type="hidden" id="price" value="${price}">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div> 
							--%>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">转货数量</label>
								<div class="col-sm-4">
									<p class="form-control-static" id="totalQuantity">0.0</p>
								</div>
								
								<label class="col-sm-2 control-label">总金额</label>
								<div class="col-sm-4">
									<p class="form-control-static total" id="totalAmount">0.0</p>
								</div>
							</div>
							<div class="form-group">
							<label class="col-sm-2 control-label">扣除代金券金额</label>
								<div class="col-sm-4">
									<p class="form-control-static total" id="voucherPayable">0.0</p>
								</div>
							<label class="col-sm-2 control-label">实际应付金额</label>
								<div class="col-sm-4">
									<p class="form-control-static text-danger total" id="actualAmount">0.0</p>
								</div>
							</div>
							
							<%-- <div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">使用代金券数额</label>
								<div class="col-sm-2">
									<input type="number" max="${voucher}" class="form-control" required id="editVoucher" tabIndex="12"
										name="voucher" value="${voucher}"
										<c:if test="${voucher<=0}">
											readonly="readonly"
										</c:if>
										placeholder="输入代金券金额">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div> --%>
							<div class="form-group">
								<label for="editRemittance" class="col-sm-2 control-label">汇款信息</label>
								<div class="col-sm-6">
									<textarea class="form-control" required id="editRemittance" tabIndex="14" rows="4"
										name="remittance" value="${transfer.entity.remittance}"
										placeholder="输入汇款信息"></textarea>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
						<div class="col-md-12">
							<div class="panel panel-primary">
								<div class="panel-heading">转出货物信息
								<button type="button" class="btn btn-danger" data-toggle="tooltip" title="点击本按钮,然后选择你所需要的货物"
													id="addItemBtn" name="addItemBtn">
													<span class="glyphicon glyphicon-plus"></span>&nbsp;添加货物
												</button>
								</div>
								<div class="panel-body" style="padding:0px;">

									<div class="table-responsive">
										<table
											class="table table-striped table-bordered table-condensed"
											id="mainTable">
											<thead>
												<tr>
													<th style="width:25%;">货物名称</th>
													<!-- <th>价格</th> -->
													<th>数量</th>
													<!-- <th>金额</th> -->
													<th>操作</th>
												</tr>
											</thead>
											<tbody id="itemsBody">
												<c:forEach items="${transfer.entity.items}"
													var="item">
													<tr data-id="${item.key}">
														<td style="width:25%;">${item.value.goodsName}<input type="hidden"
															name="goodsIds" value="${item.value.goodsId}">
														</td>
														<td class="quantitys"><input type="number" value="${item.value.quantity}" class="form-control quantity"
															required name="quantitys" min="1"></td>
														<%-- <td class="price">${item.value.price}</td> --%>
														<%-- <td class="amount">${item.value.amount}</td> --%>
														<td><a class="btn btn-danger default removeItem btn-block"
															data-role="delete" href="#"><span
																class="fa fa-trash fa-fw" aria-hidden="true"></span>删除</a></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-4 col-xs-4">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign"></span>关闭
					</button>
				</div>
				<div class="col-md-4 col-xs-4">
					<button type="button" class="btn btn-info btn-block calculateBtn" tabIndex="26" data-toggle="tooltip"
						id="calculateBtn" name="calculateBtn" value="login" title="点击核算,填写汇款信息后方能提交"
						tabindex="4" data-loading-text="正在提交核算......">
						<span class="fa fa-rmb"></span>核算
					</button>
				</div>
			<div class="col-md-4 col-xs-4">
					<button type="button" class="btn btn-danger btn-block" disabled="disabled" data-toggle="tooltip"
						form="editForm" tabIndex="27" id="saveBtn" name="saveBtn" title="提交申请,提交前必须先点击核算"
						value="saveBtn" tabindex="4" data-loading-text="正在提交转货申请......">
						<span class="glyphicon glyphicon-save"></span>提交
					</button>
				</div>
		</div>
	</div>
</div>


<script type="text/javascript">
var calcFlag=0;
	$(function() {
		init();
	});
	function init() {
		  $('[data-toggle="tooltip"]').tooltip();
		$("#editName").focus().select();
		$("#addItemBtn").click(function(e){
			e.preventDefault();
			e.stopPropagation();
			
			$("#selectModal").load("<c:url value='/goods/authGoods.html'/>",function(e) {
						$('#selectModal').modal({
							backdrop : "static"
						});
					});
		});
		var btn = null;
		var form=$("#editForm").validate({submitHandler : function(form) {
						$(form).ajaxSubmit({
									beforeSubmit : function(arr, $form, options) {
										btn.button("loading");
									},
									success : function(responseText,
											statusText, xhr, $form) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										if(m.flag=="0"){
											if(m.data=="calculate"){
												$("#winModal").load("<c:url value='/transfer/calculate.html'/>",function(e) {
													$('#winModal').modal({
														backdrop : "static"
													});
												});
												calcFlag=1;
												$("#saveBtn").prop("disabled",false);
											}else{
												alert(m.data);
												$(".quitBtn").click();
											}
											
											//$("#search").click();
										}else{
											alert("操作失败,"+m.message);
										}
										
									},
									error : function(xhr, textStatus,
											errorThrown) {
										btn.button("reset");
										//var m = $.parseJSON(xhr.responseText);
										alert("申请操作失败");
									}
								});
					},
					rules : {
						remittance : {
							required : false
						},
						quantity:{
							required: true,
							digits:true
						}
					},
					onkeyup : false,
					messages : {
						remittance : {
							required : "汇款信息必须填写"
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
		$("#calculateBtn").click(function(e){
			if($("tr[data-id]").size()<1){
				alert("请点击添加货物按钮,添加货物");
				e.preventDefault();
				return;
			}
			btn = $(this).button();
			$(document.forms["editForm"]).attr("action","<c:url value='/transfer/calculate.json'/>");
			$(document.forms["editForm"]).submit();
		});
		$("#saveBtn").click(function(){
			if(calcFlag==0){
				alert("请先点击核算按钮进行价格核算");
				$("#calculateBtn").focus();
				e.preventDefault();
				return false;
			}
			var hkxx=$("#editRemittance").val();
			if($.trim(hkxx).length<1){
				alert("请先根据核算结果填写汇款信息");
				$("#editRemittance").focus();
				e.preventDefault();
				return;
			}
			btn = $(this).button();
			$(document.forms["editForm"]).attr("action","<c:url value='/transfer/applyFrom.json'/>");
			$(document.forms["editForm"]).submit();
		});
		$("#mainTable").delegate(".removeItem","click",function(e) {
			$(this).closest("tr").remove();
			calcFlag=0;
			//calcTotal();
		});
		
		$("#mainTable").delegate(".quantity","change",function(e) {
			calcFlag=0;
		});
		
		$("#editQuantity").change(function(e){
			var q=$(this).val();
			$(".total").empty().text(q*$("#price").val());
		});
		$("tbody#itemsBody").delegate("INPUT[name='quantitys']","change",function(e){
			calcFlag=0;
			var that=$(this),v=that.val();
			if(!v){
				that.val(1);
			}
			v=parseInt(v);
			if(isNaN(v) || v<1){
				that.val(1)
			}
			var tr=that.parents("TR");
			//calcRow(tr);
			//calcTotal();
		});
		
	}
	function calcRow(tr){
		var price=tr.find(".price").text(),quantity=tr.find(".quantity").val(),amount=tr.find(".amount");
		amount.text(Number(parseFloat(price)*parseInt(quantity)).toFixed(2));
	}
	function calcTotal(){
		var qs=$(".quantity"),as=$(".amount"),totalQuantity=0,totalAmount=0.0;
		qs.each(function(i,d){
			totalQuantity+=parseInt($(d).val());
		});
		as.each(function(i,d){
			totalAmount+=parseFloat($(d).text());
		});
		$("#totalQuantity").text(Number(totalQuantity).toFixed(2));
		$("#totalAmount").text(Number(totalAmount).toFixed(2));
	}
</script>
