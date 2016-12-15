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
			<h4 class="modal-title " id="myModalLabel"><span class="fa fa-edit fa-fw"></span>积分商城信息编辑</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/mall/goods/edit.json'/>" name="editForm" enctype="multipart/form-data"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">				
					<div class="col-md-12 col-xs-12">						
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">商品名称</label>
								<div class="col-sm-6">
									<input type="text" class="form-control" id="editName" tabIndex="10"
										name="name" value="${parameter.entity.name}"
										placeholder="输入商品名称">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="pointFactor" class="col-sm-2 control-label">商品规格</label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="editSpec" tabIndex="11"
										name="spec" value="${parameter.entity.spec}"
										placeholder="输入商品规格">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="img" class="col-sm-2 control-label">产品图片</label>
								<div class="col-sm-4">
									<input type="file" class="form-control" id="img" tabIndex="12"
										name="img" accept="image/png,image/jpeg,image/gif" placeholder="产品图片">
									<span class="help-block">图片尺寸:200*200px 体积小于50kb</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentStock" class="col-sm-2 control-label">价格</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editPrice" tabIndex="12" required
										name="price" value="${parameter.entity.price}"
										placeholder="输入商品价格">
										<span class="help-block">非积分+现金消费时的价格</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentPoint" class="col-sm-2 control-label">所需积分</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editPointPrice" tabIndex="13"
										name="pointPrice" value="${parameter.entity.pointPrice}"
										placeholder="输入购买本商品所需积分">
										<span class="help-block">使用积分+现金消费时的积分数</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">积分价格</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editMoneyPrice" tabIndex="14"
										name="moneyPrice" value="${parameter.entity.moneyPrice}"
										placeholder="输入商品积分价格">
										<span class="help-block">使用积分+现金消费时的价格</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">返券额度</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editVoucher" tabIndex="15"
										name="voucher" value="${parameter.entity.voucher}" required
										placeholder="输入商品返券额">
										<span class="help-block">购买此商品的返券额度</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">返福利积分数</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editBenefitPoints" tabIndex="16"
										name="BenefitPoints" value="${parameter.entity.benefitPoints}" required
										placeholder="输入商品返券额">
										<span class="help-block">购买此商品的返福利积分数</span>
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="currentBalance" class="col-sm-2 control-label">当前库存</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editStockQuantity" tabIndex="17" readonly="readonly"
										name="stockQuantity" value="${parameter.entity.stockQuantity}" required
										placeholder="输入商品当前库存">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
							<label for="" class="col-sm-2 control-label">是否上架商品</label>
										<div class="col-sm-6">
											<%-- <div class="checkbox">
												<label> <input type="checkbox" id="shelf" tabIndex="16"
													<c:if test="${parameter.entity.shelf == true }">
														checked="checked"
													</c:if>
													name="shelf">是否上架
												</label>
											</div> --%>
									
									<label class="radio-inline"> <input type="radio" <c:if test="${parameter.entity.shelf}">
														checked="checked"
													</c:if>
										name="shelf" id="inlineRadio1" value="1">
										上架
									</label> <label class="radio-inline"> <input
										type="radio" name="shelf" id="inlineRadio2" <c:if test="${not parameter.entity.shelf}">
														checked="checked"
													</c:if>
										value="0"> 下架
									</label>
								</div>
										<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							<div class="form-group">
								<label for="editOrder" class="col-sm-2 control-label">排列顺序</label>
								<div class="col-sm-4">
									<input type="number" class="form-control" id="editSequence"
										name="sequence" value="${parameter.entity.sequence}" tabIndex="17"
										placeholder="输入显示顺序">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
						<!-- </form>-->
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
											$("#searchDT").click();
										}else{
											alert("保存失败"+m.message);
										}
										
									},
									error : function(xhr, textStatus,
											errorThrown) {
										btn.button("reset");
										alert("积分商品保存失败");
									}
								});
					},
					rules : {
						name : {
							required : true
						},
						spec:{
							required: true
						},
						price:{
							number:true,
							min:true,
							required:true
						},
						pointPrice:{
							required: true,
							number:true
						},
						moneyPrice:{
							required: true,
							number:true
						},
						voucher:{
							required:true,
							number:true,
							min:true
						},
						benefitPoints:{
							required:true,
							number:true,
							min:true
						},
						stockQuantity:{
							number:true
						}
					},
					onkeyup : false,
					messages : {
						name : {
							required : "商品名必须填写"
						},
						spec:{
							required:"商品规格必须填写"
						},
						price:{
							number:"价格必须为数字",
							min:"价格不能为负数",
							required:"价格需填写"
						},
						pointPrice:{
							number:"积分必须为数字",
							min:"积分不能为负数",
							required:"所需积分不能为空"
						},
						moneyPrice:{
							number:"积分价格必须为数字",
							min:"积分价格不能为负数",
							required:"积分价格必须填写"
						},
						voucher:{
							required:"返券额度需填写",
							min:"返券额度不能为负数",
							number:"返券额度必须为数字"
						},
						benefitPoints:{
							required:"返福利积分数需填写",
							number:"福利积分必须为数字",
							min:"福利积分数不能为负"
						},
						stockQuantity:{
							number:"当前库存必须为数字"
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
	}
</script>
