<header
		style="position:relative;margin:0 auto;width:100%;">
		<img src="<c:url value='/resources/images/header.jpg'/>"
			class="header-img">
		<p class="header-title">
		<c:choose>
			<c:when test="${ not empty owner}">
				${owner}的小店
			</c:when>
			<c:otherwise>
				筑美的小店
			</c:otherwise>
		</c:choose>
		</p>
		<img src="<c:url value='/resources/images/mall-logo.jpg'/>"
			class="logo-img">
	</header>
	<div class="container" id="nav">
		<div class="row">
			<div class="col-xs-3 ">
				<a class="thumbnail" href="<c:url value='/vmall/uc/index.html?ref=${agentCode}'/>">
					<img data-src="" alt=""
					src="<c:url value='/resources/images/user.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">用户中心</h6>
					</div>
				</a>
			</div>

			<div class="col-xs-3">
				<a class="thumbnail" href="<c:url value='/dmz/pmall/index.html?ref=${agentCode}'/>"> <img data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">积分商城</h6>
					</div>
				</a>
			</div>
			
			<div class="col-xs-3">
				<a class="thumbnail" href="<c:url value='/dmz/tmall/index.html?ref=${agentCode}'/>"> <img data-src="" alt=""
					src="<c:url value='/resources/images/tuiguang.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">特权商城</h6>
					</div>
				</a>
			</div>
			
			<div class="col-xs-3">
				<a class="thumbnail"
					href="<c:url value='/vmall/shopcart/index.html?ref=${agentCode}'/>"> <img
					data-src="" alt=""
					src="<c:url value='/resources/images/shopCart.jpg'/>"
					data-holder-rendered="true">
					<div class="caption">
						<h6 class="text-center">
							购物车<span class="badge" id="shopcartQuantity">${shopcart.quantity}</span>
						</h6>
					</div>
				</a>
			</div>
		</div>
	</div>
	</div>