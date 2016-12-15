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
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js">
</script>
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
</style>
<title>结算</title>
</head>
 <script type="text/javascript">
          //获取共享地址
          function editAddress()
          {
             WeixinJSBridge.invoke(
                 'editAddress',
                 ${jsapiParamJson},//josn串
                   function (res)
                   {
                	 if(res.err_msg=="edit_address:ok"){
                	 document.getElementById("editConsigneeName").value=res.userName;
                	 document.getElementById("editMobile").value=res.telNumber;
                     var addr1 = res.proviceFirstStageName;
                     var addr2 = res.addressCitySecondStageName;
                     var addr3 = res.addressCountiesThirdStageName;
                     var addr4 = res.addressDetailInfo;
                     var addr = addr1 + addr2 + addr3 + addr4;
                     document.getElementById("editAddress").value=addr;
                     document.getElementById("editPostCode").value=res.addressPostalCode;
                	 }
                   }
               );
         }

function callEditAddress()
           {
               if (typeof WeixinJSBridge == "undefined"){
                   if (document.addEventListener){
                       document.addEventListener('WeixinJSBridgeReady', editAddress, false);
                   }
                   else if (document.attachEvent){
                       document.attachEvent('WeixinJSBridgeReady', editAddress);
                       document.attachEvent('onWeixinJSBridgeReady', editAddress);
                   }
               }else{
                   editAddress();
               }
};
</script>
<body>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-4">
				<a class="thumbnail"
					href="<c:url value='/vmall/uc/index.html?ref=${agentCode}'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/user.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">用户中心</h6>
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
				<a class="thumbnail" href="<c:url value='/dmz/vmall/index.html'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">继续选购</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	<br>
	<div class="container">
		<div class="row" id="products">
			<div class="col-md-12 col-xs-12">
				<div class="panel panel-info">
					<div class="panel-heading"><button type="button" class="btn btn-info" id="editAddrBtn" onclick="callEditAddress();"><span class="fa fa-home"></span>编辑收货信息</button></div>
					<div class="panel-body">
						<div class="col-md-12 col-xs-12">
							<form action="<c:url value='/vmall/shopcart/pay.json'/>"
								name="editForm" class="form-horizontal" id="editForm"
								method="post">
								<div class="form-group">
									<label for="editLoginName" class="col-sm-2 control-label">收货人姓名</label>
									<div class="col-sm-4">
										<input type="text" class="form-control" id="editConsigneeName"
										onclick="callEditAddress();" readonly="readonly"
											tabIndex="10" name="consigneeName" value=""
											placeholder="输入收货人姓名">
									</div>
									<div class="col-md-4 col-xs-4 text-error"></div>
								</div>

								<div class="form-group">
									<label for="editMobile" class="col-sm-2 control-label">手机号码</label>
									<div class="col-sm-4">
										<input type="tel" class="form-control" id="editMobile"
										onclick="callEditAddress();" readonly="readonly"
											tabIndex="11" required name="mobile" value=""
											placeholder="输入收货人联系电话">
									</div>
									<div class="col-md-4 col-xs-4 text-error"></div>
								</div>
								<div class="form-group">
									<label for="editWeixin" class="col-sm-2 control-label">收货地址</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="editAddress"
											tabIndex="12" required name="address" value=""
											onclick="callEditAddress();" readonly="readonly"
											placeholder="输入收货地址">
									</div>
									<div class="col-md-4 col-xs-4 text-error"></div>
								</div>
								<div class="form-group">
									<label for="editWeixin" class="col-sm-2 control-label">邮政编码</label>
									<div class="col-sm-6">
										<input type="text" class="form-control" id="editPostCode" onclick="callEditAddress();"
											tabIndex="12" name="postCode" value="" readonly="readonly"
											placeholder="输入邮政编码">
									</div>
									<div class="col-md-4 col-xs-4 text-error"></div>
								</div>
								<div class="form-group">
									<label for="currentBalance" class="col-sm-2 control-label">备注</label>
									<div class="col-sm-10">
										<textarea rows="3" class="form-control" id="editRemark"
											tabIndex="14" name="remark" value="" placeholder="请输入发货备注信息"></textarea>
										<span class="help-block">发货注意事项可以在此说明</span>
									</div>
								</div>
								<div class="form-group">
									<div class="col-xs-12">
										<button type="button" class="btn btn-warning btn-block btn-lg"
											form="editForm" tabIndex="27" id="saveBtn" name="saveBtn"
											value="saveBtn" tabindex="4" data-loading-text="正在提交......">
											<span class="glyphicon glyphicon-credit-card">&nbsp;</span>提交结算
										</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row" id="products">
			<div class="col-md-12 col-xs-12">
				<div class="table-responsive">
					<table class="table">
						<thead>
							<tr>
								<th>商品</th>
								<th>单价</th>
								<th>价格等级</th>
								<th>数量</th>
								<th>金额</th>
							</tr>
						</thead>
						<tbody id="cartitems">
							<c:forEach items="${shopcart.items}" var="item">
								<tr>
									<td><img
										src="<c:url value='${imgPath}${item.value.goods.imgFile}'/>"
										class="img-responsive img-thumbnail" style="max-width:100px;"
										alt="Responsive image"></td>
									<td>${item.value.price}</td>
									<td>${item.value.priceLevelName}</td>
									<td class="quantity">${item.value.quantity}</td>
									<td class="amount">${item.value.amount}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="3">合计</td>
								<td id="totalQuantity"></td>
								<td id="totalAmount"></td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<a class="btn btn-info btn-lg btn-block"
					href="<c:url value='/dmz/vmall/index.html?ref=${agentCode}'/>">
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
					<button type="button" class="btn btn-success btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在关闭......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
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
				<div class="modal-body" id="winMessageBody"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger btn-block quitBtn"
						tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
						value="login" tabindex="4" data-loading-text="正在关闭......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$("#editForm")
					.validate(
							{
								submitHandler : function(form) {
									$(form)
											.ajaxSubmit(
													{
														beforeSubmit : function(
																arr, $form,
																options) {
															btn
																	.button("loading");
														},
														success : function(
																responseText,
																statusText,
																xhr, $form) {
															
															var m = $
																	.parseJSON(xhr.responseText);
															if (m.flag == "0") {
																$("#winModal").load("<c:url value='/delivery/success.html'/>"+"?id="+m.data,function(e) {
																	$('#winModal').modal({
																		backdrop : "static"
																	});
																});
																window.setTimeout(function() {
																	window.location=xhr.getResponseHeader("Location");
																}, 6000);
															} else {
																btn.button("reset");
																$("#alertMessageBody").empty().html("商品订单提交失败,"+m.message);
																$("#myModal").modal({backdrop:"static",show:true});
															}

														},
														error : function(xhr,
																textStatus,
																errorThrown) {
															btn.button("reset");
															alert("商品订单提交失败"+xhr.responseText);
														}
													});
								},
								rules : {
									consigneeName : {
										required : false
									},
									mobile : {
										required : true,
										mobile : true
									},
									address : {
										required : true
									}
								},
								onkeyup : false,
								messages : {
									consigneeName : {
										required : "请输入收货人姓名"
									},
									mobile : {
										required : "请输入手机号码",
										mobile : "手机号码格式不正确"
									},
									address : {
										required : "请输入收货地址"
									}

								},
								focusInvalid : true,
								errorClass : "text-danger",
								validClass : "valid",
								errorElement : "small",
								errorPlacement : function(error, element) {
									error.appendTo(element.closest(
											"div.form-group").children(
											"div.text-error"));
								}
							});
			$("#saveBtn").click(function(e) {
				
				btn = $(this).button();
				if ($("#cartitems").find("TR").size() < 1) {
					alert("还未添加选购货物");
					return false;
				}
				$(document.forms["editForm"]).submit();
			});
			calcTotal();
			function calcTotal() {
				var qs = $(".quantity"), as = $(".amount"), totalQuantity = 0, totalAmount = 0.0;
				qs.each(function(i, d) {
					totalQuantity += parseInt($(d).text());
				});
				as.each(function(i, d) {
					totalAmount += parseFloat($(d).text());
				});
				$("#totalQuantity").text(totalQuantity);
				$("#totalAmount").text(totalAmount);
			}
		});
	</script>
</body>
</html>
