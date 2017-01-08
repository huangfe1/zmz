<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<!doctype html>
<html>
<head>
<title>
高品质${goods.name}优惠价${goods.moneyPrice}元/${goods.spec}

</title>
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
	<div class="container-fluid">
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
						<h6>市场价:<del><small class="text-muted"><span class="glyphicon glyphicon-yen"></span>${goods.price}</small></del></h6>
						<h5 class="text-danger">折扣价:<span class="fa fa-rmb"></span><strong>${goods.moneyPrice}元</strong></h5>
						<%--<h5 class="text-danger">分享可获得:<span class="fa fa-rmb"></span><strong>${fn:replace(goods.vouchers, , )}元</strong></h5>--%>
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
			<c:if test="${goods.id!=40}"><!--如果不是胸膜-->
				<button type="button" data-id="${goods.id}" class="btn btn-warning btn-block btn-lg buyBtn">加入购物车</button>
			</c:if>

					</div>
		</div>
		<%-- <div class="row">
					<div class="col-md-12 col-xs-12">
						<button data-id="${goods.id}" class="btn btn-block btn-lg btn-warning buyBtn">
						<span class="fa fa-shopping-cart fa-fw pull-left">&nbsp;</span>加入购物车</button>
					</div>
		</div> --%>
	</div>
	<%--${ticket}----%>
	<%--${url}----%>
	<%--${appId}--%>
	<%--${timestamp}--%>
	<%--${nonceStr}--%>
	<%--${signature}--%>
    <%--${jsApiList}--%>
	<!--详情页-->
	<%--${jsapiParamJson.appId}--%>
	<c:forEach items="${goods.detailImgUrls}" var="xqimg">
		<c:if test="${xqimg ne ''}">

			<div class="container">
				<div class="row">
					<!--详情页-->
					<div class="col-md-12 col-xs-12">
						<a href="#" class="thumbnail" style="margin-bottom: 0px;border: none;padding: 0px">
							<img src="<c:url value='${xqimg}'/>" alt="详情页">
						</a>
					</div>
				</div>
			</div>
		</c:if>

	</c:forEach>

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
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
	$(function() {
		var jsapiParamJson=${jsapiParamJson};
//		tt.replace('false',true);
//		alert(tt.appId);
//		alert(json);
        var url="";
        var shareName="";
        var shareDetail="";
		<c:if test="${myCode==null||myCode==''}">
        alert("您尚未登陆,登陆之后才能分享链接,点击购物车登陆!!");
		</c:if>
        <c:if test="${myCode!=null&&myCode!=''}">
                url=location.href.split('#')[0]+"&&myCode=${myCode}";//分享链接地址
                shareName="${goods.shareName}";
                shareDetail="${goods.shareDetail}";
        </c:if>

////获取随机数
//		function getRandomStringByLength(len){
//			var base = "abcdefghijklmnopqrstuvwxyz0123456789";
//			var sb = "";
//			for (var i = 0; i < length; i++) {
//				var number= Math.floor(Math.random() * len);
//				sb+=base.charAt(number);
//			}
//			return sb;
//		}

		//微信分享链接
		<%--wx.config(${jsapiParamJson});--%>
		wx.config({
			debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			appId: jsapiParamJson.appId, // 必填，公众号的唯一标识
			timestamp: jsapiParamJson.timestamp, // 必填，生成签名的时间戳
			nonceStr: jsapiParamJson.nonceStr, // 必填，生成签名的随机串
			signature: jsapiParamJson.signature,// 必填，签名，见附录1
			jsApiList: jsapiParamJson.jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});

        /*
         * 注意：
         * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
         * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
         * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
         *
         * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
         * 邮箱地址：weixin-open@qq.com
         * 邮件主题：【微信JS-SDK反馈】具体问题
         * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
         */
        <%--wx.config({--%>
            <%--debug: true,--%>
            <%--appId: '${appId}',--%>
            <%--timestamp: ${timestamp},--%>
            <%--nonceStr: '${nonceStr}',--%>
            <%--signature: '${signature}',--%>
            <%--jsApiList: [--%>
                <%--'checkJsApi',--%>
                <%--'onMenuShareTimeline',--%>
                <%--'onMenuShareAppMessage',--%>
                <%--'onMenuShareQQ',--%>
                <%--'onMenuShareWeibo',--%>
                <%--'onMenuShareQZone',--%>
                <%--'hideMenuItems',--%>
                <%--'showMenuItems',--%>
                <%--'hideAllNonBaseMenuItem',--%>
                <%--'showAllNonBaseMenuItem',--%>
                <%--'translateVoice',--%>
                <%--'startRecord',--%>
                <%--'stopRecord',--%>
                <%--'onVoiceRecordEnd',--%>
                <%--'playVoice',--%>
                <%--'onVoicePlayEnd',--%>
                <%--'pauseVoice',--%>
                <%--'stopVoice',--%>
                <%--'uploadVoice',--%>
                <%--'downloadVoice',--%>
                <%--'chooseImage',--%>
                <%--'previewImage',--%>
                <%--'uploadImage',--%>
                <%--'downloadImage',--%>
                <%--'getNetworkType',--%>
                <%--'openLocation',--%>
                <%--'getLocation',--%>
                <%--'hideOptionMenu',--%>
                <%--'showOptionMenu',--%>
                <%--'closeWindow',--%>
                <%--'scanQRCode',--%>
                <%--'chooseWXPay',--%>
                <%--'openProductSpecificView',--%>
                <%--'addCard',--%>
                <%--'chooseCard',--%>
                <%--'openCard'--%>
            <%--]--%>
        <%--});--%>

        wx.ready(function(){
            // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。

           //朋友圈分享
        wx.onMenuShareTimeline({
            title: shareName, // 分享标题
            link: url, // 分享链接
            imgUrl: "${goods.imgUrl}", // 分享图标
                success: function () {
                    // 用户确认分享后执行的回调函数
                    alert("分享成功");
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                }
            });
            //用户分享
            wx.onMenuShareAppMessage({
                title: shareName, // 分享标题
                desc: shareDetail, // 分享描述
                link: url, // 分享链接
                imgUrl: "${goods.imgUrl}", // 分享图标
                type: '', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function () {
                    // 用户确认分享后执行的回调函数
                    alert("分享成功");
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                }
            });


        });




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
			$.ajax("<c:url value='/dmz/pmall/shopcart/add.json'/>", {
				"type" : "POST",
				"data" : {
					"goodsId" : id,
					"quantity": $(".quantity").val()
				},
				"complete":function(xhr, ts){
					if(xhr.status>=200 && xhr.status<300){
                        var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
                            if(m.data=="noLogin"){
                                window.location.href="<c:url value='/vmall/uc//dmz/login.html?myCode=${myCode}&&gid=${goods.id}'/>";
                            }
							$("#alertMessageBody").empty().html("添加到购物车成功");
							$("#winModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								window.location.href="<c:url value='/pmall/shopcart/index.html'/>";
							}, 1000);

							/* $("#alertMessageBody").empty().html("添加到购物车成功");
							$("#winModal").modal({backdrop:"static",show:true});
							$("#shopcartQuantity").html(m.data).removeClass(animatedClass)
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
                            var m=$.parseJSON(xhr.responseText);
							$("#alertMessageBody").empty().html(m.message+"添加购物车操作失败").addClass("text-danger");
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
