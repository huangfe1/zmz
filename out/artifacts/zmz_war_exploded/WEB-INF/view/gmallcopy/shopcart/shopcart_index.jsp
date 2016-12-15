<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="${keywords}">
<meta http-equiv="description" content="">
<%@include file="/WEB-INF/view/common/head_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_typeahead.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<style type="text/css">
html {
	background-color: transparent !important;
}

.carousel-inner>.item img {
	width: 100%;
	height: 115px;
	max-height: 240px;
}

.thumbnail {
	border: none !important;
	box-shadow: none;
	-webkit-box-shadow: none;
	background-color: transparent !important;
}

nav .thumbnail {
	margin-bottom: 2px !important;
}

#nav {
	border-bottom: 1px solid #e0e0e0;
	background-color: #fff;
}

#nav .thumbnail {
	margin-bottom: 0px;
	color: black;
	font-family: Tahoma, Helvetica, Arial, "Microsoft Yahei", STXihei,
		sans-serif;
}

#nav .thumbnail .caption {
	padding: 0px;
}

#nav .thumbnail>img {
	width: 60% !important;
	max-width: 72px !important;
}

.product_img {
	width: 100%;
	max-width: 150px !important;
}

a:hover {
	text-decoration: none !important;
}

.goods-title {
	white-space: nowrap;
}
.quantity{
width:2em !important;
text-align:center;
padding:2px;
}
.input-group-addon, .input-group-btn{
width:auto !important;
}
</style>
<title>我的购物车</title>
</head>
<body>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/dmz/vmall/index.html?ref=${agentCode}'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
				<h6 class="text-center">代理商城</h6>

					</div>
				</a>
			</div>
			
				

			<div class="col-xs-4">
				<a class="thumbnail" href="<c:url value='/pmall/index.html?ref=${agentCode}'/>"> <img data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">积分商城</h6>
					</div>
				</a>
			</div>
			<div class="col-xs-4">
				<a class="thumbnail"
					href="<c:url value='/dmz/gmall/index.html'/>"> <img
					data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">
							继续选购
						</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	<c:if test="${ empty gshopcart or gshopcart.quantity<1}">
	
<div class="container">
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<p class="text-primary text-center">购物车里什么也没有</p>
			</div>
		</div>
</div>
		</c:if>
	<div class="container">
		<div class="row" id="products">
			<div class="col-md-12 col-xs-12">
				<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th>商品</th><!-- <th>单价</th> --><th>数量</th><!-- <th>金额</th> -->
							<th>操作</th>
						</tr>
					</thead>
						<tbody id="cartitems">
							<c:forEach items="${gshopcart.items}" var="item">
								<tr>
									<td><img
										src="<c:url value='${imgPath}${item.value.goods.imgFile}'/>"
										class="img-responsive img-thumbnail" style="max-width:80px;"
										alt="Responsive image"></td>
									<%-- <td class="price">${item.value.price}</td> --%>
									<td>
										<div class="input-group" >
											<span class="input-group-btn">
												<button class="btn btn-default minsBtn" type="button">-</button>
											</span> 
											
											<input size="20"  type="number" value="${item.value.quantity}" data-id="${item.value.goods.id }"
												class="form-control  quantity" name="quantity"> 
												
												<span class="input-group-btn">
												<button class="btn btn-default addBtn"  type="button" >+</button>
												</span>
										</div>
									</td>
									<%-- <td class="amount">${item.value.amount}</td> --%>
									<td><button class="btn btn-danger btn-xs removeBtn"
											data-id="${item.value.goods.id}">删除</button></td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
						<tr>
							<td >合计</td>
							<td id="totalQuantity"></td>
							<td></td>
						</tr>
					</tfoot>
				</table>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 col-xs-12">
			<a class="btn btn-warning btn-lg btn-block" id="btn-calcuate" href="#"  <c:if test="${ empty gshopcart or gshopcart.quantity<1}">disabled='disabled'</c:if> >
				<span class="glyphicon glyphicon-credit-card"></span>计算费用</a>
			</div>
		</div>
		<br>
		
			<div class="row">
			<div class="col-md-12 col-xs-12" >
			<a style="display: none" class="btn btn-warning btn-lg btn-block" id="btn-transfer" href="#"  <c:if test="${ empty gshopcart or gshopcart.quantity<1}">disabled='disabled'</c:if> >
				<span class="glyphicon glyphicon-credit-card"></span>去结算（转入系统库存）</a>
			</div>
		</div>
		<br>
		
		
				<div class="row">
			<div class="col-md-12 col-xs-12" style="display: none;">
				<a style="display: none" class="btn btn-warning btn-lg btn-block" href="<c:url value='/gmall/gshopcart/pay.html?ref=${agentCode}'/>" id="payBtn" <c:if test="${ empty gshopcart or gshopcart.quantity<1}">disabled='disabled'</c:if> >
				<span class="glyphicon glyphicon-credit-card"></span>去结算（直接公司发货）</a>
			</div>
		</div>
		<br>
		
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<a  class="btn btn-info btn-lg btn-block" href="<c:url value='/dmz/gmall/index.html?ref=${agentCode}'/>">
				<span class="glyphicon glyphicon-shopping-cart"></span>继续购物</a>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body" id="alertMessageBody"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在关闭......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
	
		<div class="modal fade" id="winModal" tabindex="-2" role="dialog" style="z-index:999999;"
		aria-labelledby="winModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="winModalLabel"></h4>
				</div>
				<div class="modal-body" id="winBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			//hf add
			//结算
			$("#btn-calcuate").click(function(){
				
				

				
				
				$.ajax("<c:url value='/transfer/hf_gmallCalculate.json'/>",
						{"type":"POST","success":function(xhr){
							var  m = xhr;
							if(m.flag=="0"){
								//弹出审核窗口
								$("#winModal").load("<c:url value='/transfer/calculate.html'/>",function(e) {
									$('#winModal').modal({
										backdrop : "static"
									});
								});
								$("#btn-calcuate").hide();
								
							}else{
								alert("计算失败:"+m.message);
							} 
				},"error":function(xhr){
					alert("申请操作失败");
				}});
				
			
				
			});
			//进货提交
			
					$("#btn-transfer").click(function(){
					
						 var str=prompt("请填写汇款信息");
						 if(str==null||str==""){
							 alert("汇款信息必填");
						 }else{
							 
							
									
									$.ajax("<c:url value='/transfer/hf_gmallApplyFrom.json'/>",
											{"type":"POST","data":{"remittance":str},"success":function(xhr){
												var  m = xhr;
												if(m.flag=="0"){
												alert("申请成功！");
											
												  location.href="<c:url value='/dmz/gmall/index.html?ref=${agentCode}'/>";
												}else{
													
													alert("进货失败:"+m.message);
													
												}

												
									},"error":function(xhr){
										alert("申请操作失败");
									}});
									
														 
							
						 }
				
			
			
				
			});
			
			var names = new Bloodhound({
				name : "names",
				datumTokenizer : Bloodhound.tokenizers.obj.whitespace("value"),
				queryTokenizer : Bloodhound.tokenizers.whitespace,
				limit : 10,
				prefetch : {
					url : "<c:url value='/dmz/goods/names.json'/>",
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
			$("#keywords.typeahead").typeahead({
				hint : true,
				highlight : true,
				minLength : 2
			}, {
				name : "names",
				displayKey : "value",
				source : names.ttAdapter()
			});
			$("#products").delegate(".buyBtn", "click", function(e) {
				var id = $(this).attr("data-id");
				$.ajax("<c:url value='/gmall/gshopcart/add'/>", {
					"type" : "POST",
					"data" : {
						"goodsId" : id
					}
				})
			});
			calcTotal();
			$("#cartitems").delegate(".removeBtn","click",function(e){
				e.preventDefault();
				var goodsId=$(this).attr("data-id"),$this=$(this);
				removeItem($this,goodsId);
			});
			$("#cartitems").delegate(".quantity","change",function(e){
				var $this=$(this), val=$this.val(),goodsId=$this.attr("data-id");
				if(isNaN(val) || val<=0){
					removeItem($this,goodsId);
					return false;
				}
				$.ajax("<c:url value='/gmall/gshopcart/put.json'/>",{"type":"POST","data":{"goodsId":goodsId,"quantity":val},"success":function(xhr){
					var tr=$this.parents("tr");
					calcRow(tr);
					calcTotal();
				},"error":function(xhr){
					$("#alertMessageBody").empty().html("修改购物车商品数量失败").addClass("text-danger");
					$("#myModal").modal({backdrop:"static",show:true});
				}})
			});
			$("#cartitems").delegate(".addBtn,.minsBtn","click",function(e){
				var quantity=$(this).closest("td").find(".quantity");
				var v=parseInt(quantity.val());
				if(isNaN(v)){
					v=1;
				}
				if($(this).hasClass("addBtn")){
					quantity.val(v+1).change();
				}else{
					quantity.val(v-1).change();
				}
				
			});
			function removeItem($this,goodsId){
				$.ajax("<c:url value='/gmall/shopcart/remove.json'/>",
						{"type":"POST","data":{"goodsId":goodsId},"success":function(xhr){
							$("#alertMessageBody").empty().html("删除购物车商品成功");
							$("#myModal").modal({backdrop:"static",show:true});
							if($this.closest("tbody").find("tr").size()<=1){
								$("#payBtn").attr("disabled","disabled");
							}		
							$this.closest("tr").remove();
							calcTotal();
				},"error":function(xhr){
					if(xhr.status==401){
						$("#alertMessageBody").empty().html("尚未登陆,转到登陆界面").addClass("text-danger");
						$("#myModal").modal({backdrop:"static",show:true});
						window.location=xhr.getResponseHeader("Location");
					}else{
						$("#alertMessageBody").empty().html("删除购物车条目失败").addClass("text-danger");
						$("#myModal").modal({backdrop:"static",show:true});
					}
				}});
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
				$("#totalQuantity").text(totalQuantity);
				$("#totalAmount").text(totalAmount);
			}
		});
	</script>
</body>
</html>
