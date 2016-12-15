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
		.panel-red{
			border-color: #d9534f;
		}
		.panel-red .panel-heading {
			border-color: #d9534f;
			color: #fff;
			background-color: #d9534f;
		}
	</style>
	<title>充值代金券</title>
</head>

<body>
<br>
<h2 align="center">第一步:填写充值金额,提交结算</h2>
	<%--<form action="<c:url value='/voucher/pay/commit.json'/>"--%>
	<form action="<c:url value='/voucher/pay/alicommit.json'/>"
		  name="editForm" class="form-horizontal" id="editForm" method="post">
        <div class="row" >
            <div class="col-md-12 col-xs-12">
                <div class="panel panel-red">
                    <div class="panel-heading">填写代金券充值数量</div>
                    <div class="panel-body">
                        <div class="col-md-12 col-xs-12">
                                    <!-- <li class="list-group-item"><div class="radio">
                                            <label> <input type="radio" name="paymentWay"
                                                id="paymentWay1" value="COD" checked> 货到付款
                                            </label>
                                        </div></li> -->   <!--hf  修改  将radio修改成了checkbox  -->
                                <div class="control-group">

                                <div class="controls" align="center">
                                    <input style="line-height: 2em"  type="text" id="voucher" name="voucher" placeholder="代金券数量">
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
	</form>
		<div class="row" >
			<div class="col-md-12 col-xs-12">
				<div class="panel panel-red">
					<div class="panel-heading">选择支付方式</div>
					<div class="panel-body">
						<div class="col-md-12 col-xs-12">
							<div class="form-group">
								<ul class="list-group">
									<%--<li class="list-group-item"><div class="radio">--%>
									<%--<label> <input type="checkbox" name="paymentWay" checked="true"--%>
									<%--id="paymentWay2" value="WX"> 微信支付--%>
									<%--</label>--%>
									<%--</div></li>--%>
									<li class="list-group-item"><div class="radio">
										<label> <input type="checkbox" name="paymentWay" checked="true"
													   id="paymentWay2" value="WX"> 支付宝支付
										</label>
									</div></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
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
														btn.button("loading");
													},
													success : function(
															responseText,
															statusText,
															xhr, $form) {
														btn.button("reset");
														var m = $
																.parseJSON(xhr.responseText);
														if (m.flag == "0") {
															$("#alertMessageBody").empty().html("商品订单提交成功,前往订单支付页面完成支付");
															$("#myModal").modal({backdrop:"static",show:true});
															window.setTimeout(function() {
																window.location=xhr.getResponseHeader("Location");
															}, 1000);
														} else {
															$("#alertMessageBody").empty().html(m.message+",商品订单提交失败");
															$("#myModal").modal({backdrop:"static",show:true});
														}

													},
													error : function(xhr,
																	 textStatus,
																	 errorThrown) {
                                                        alert("错误"+xhr.responseText);
														btn.button("reset");
														var m = $
																.parseJSON(xhr.responseText);
														alert("商品订单提交失败失败");
													}
												});
							},
							rules : {
								voucher : {
									required : true,
                                    number:true
								}
							},
							onkeyup : false,
							messages : {
                                voucher : {
                                    required : "请填写规范",
                                    number:"请填写规范"
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
            btn=$("#saveBtn");
			$(document.forms["editForm"]).submit();
		});

	});
</script>
</body>
</html>
