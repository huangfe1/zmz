<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<!doctype html>
<html>
<head>
<title>商品详情</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="dreamer">
<meta http-equiv="description" content="dreamer">
<%@include file="/WEB-INF/view/common/head_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<style type="text/css">
html{
font-family: Tahoma, Helvetica, Arial, "Microsoft Yahei", STXihei,
		sans-serif;
}
body{
padding-bottom:10px;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/view/mall/mall_menu.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-xs-12">
				<a href="#" class="thumbnail">
      				<img src="<c:url value='${goods.imgUrl}'/>" alt="产品图片">
    			</a>
			</div>
		</div>
		<div class="row">
					<div class="col-md-12 col-xs-12">
						<h4>${goods.name}</h4>
						<h5>规格:${goods.spec}</h5>
						<h4 class="text-danger"><span class="glyphicon glyphicon-yen"></span>${goods.price}</h4>
					</div>
					<div class="col-xs-5">
					<div style="float:left;padding:6px 2px;">
					<span style="line-height:20px;">数量:</span>					
					</div>
					<div class="input-group">
						<span class="input-group-btn">
							<button class="btn btn-default minsBtn" id="minsBtn" type="button">-</button>
						</span> <input type="number" value="1" style="text-align:center;width:120px"
							class="form-control quantity"
							name="quantity"> <span class="input-group-btn">
							<button class="btn btn-default addBtn" id="addBtn" type="button">+</button>							
						</span>
					</div>
					</div>
		</div>
		<br>
		<div class="row">
		<div class="col-xs-12">
					<button type="button" data-id="${goods.id}" class="btn btn-warning btn-block btn-lg buyBtn">加入购物车</button>
					</div>
		</div>
	</div>
	<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
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
				<div class="modal-body" id='alertMessageBody'></div>
				<div class="modal-footer">
				<button type="button" class="btn btn-info btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在关闭......">
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
		init();
		$(".addBtn,.minsBtn").click(function(e){
			e.stopPropagation();
			e.preventDefault();
			var quantity=$(this).closest(".input-group").find(".quantity");
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
		$(".quantity").change(function(e){
			var $this=$(this);
			if(!parseInt($this.val())){
				$this.val(1);
			}
			if(parseInt($this.val())<1){
				$this.val(1);
			}
		});
		$("#backBtn").click(function(e){
			e.preventDefault();
			e.stopPropagation();
			window.history.back();
		});
	});
	function init() {
		$(".buyBtn").click( function(e) {
			e.preventDefault();
			e.stopPropagation();
			var id = $(this).attr("data-id");
			$.ajax("<c:url value='/gmall/shopcart/add.json'/>", {
				"type" : "POST",
				"data" : {
					"goodsId" : id,
					"quantity": $(".quantity").val()
				},
				"complete":function(xhr, ts){
					if(xhr.status>=200 && xhr.status<300){
						var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#alertMessageBody").empty().html("添加到购物车成功");
							$("#winModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								window.location.href="<c:url value='/gmall/shopcart/index.html?ref=${agentCode}'/>";
							}, 1000);
							/* $("#shopcartQuantity").html(m.data).removeClass(animatedClass)
							.addClass(animatedClass).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend", function(){
							      $(this).removeClass(animatedClass);
						    }); */
						}else{
							$("#alertMessageBody").empty().html(m.message+",添加购物车操作失败").addClass("text-danger");
							$("#winModal").modal({backdrop:"static",show:true});
						}
						
					}else{
						if(xhr.status==401){
							$("#alertMessageBody").empty().html("尚未登陆,转到登陆界面").addClass("text-danger");
							$("#winModal").modal({backdrop:"static",show:true});
							window.location=xhr.getResponseHeader("Location");
						}else{
							$("#alertMessageBody").empty().html("添加购物车操作失败").addClass("text-danger");
							$("#winModal").modal({backdrop:"static",show:true});
						}							
					}
				}
			});
		});
	}
	</script>
</body>
</html>
