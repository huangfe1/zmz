<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<style>
.nav>li>a.menuitem {
	color: #494949;
	background-color: #d4d4d4;
	border-bottom: 1px solid transparent;
	border-bottom-color: #b6b6b6;
	border-top-color: white;
}

.nav-third-level>li>a {
	background-color: #fff;
}

th {
	white-space: nowrap !important;
}
</style>
<nav class="navbar navbar-inverse navbar-static-top" role="navigation"
	style="margin-bottom: 0;">
	<div class="container-fluids">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle animated flip pull-left"
			data-toggle="collapse" data-target=".menu-navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="<c:url value='/index.html'/>"><img
			alt="Brand" style="width:20px;height:20px;"
			src="<c:url value='/resources/images/title-1.png'/>"></a>
		<p class="navbar-text">筑美管理信息系统</p>
	</div>
	<!-- /.navbar-header -->
	<div class="collapse navbar-collapse menu-navbar-collapse" id="menu-navbar-collapse">
			<ul class="nav navbar-nav">
				<li><p class="navbar-text">你好,${user.realName}</p></li>
				<c:choose>
				<c:when test="${user.agent}">
					<%--<li><p class="navbar-text">积分余额:<span id="pointsBalance">${accounts.pointsBalance}</span></p></li>--%>
					<li><p class="navbar-text">代金券:<span id="voucherBalance">${accounts.voucherBalance}</span></p></li>
					<li><p class="navbar-text">预存款:<span id="advanceBalance">${accounts.advanceBalance}</span></p></li>
					<li><a href="<c:url value='/dmz/vmall/index.html'/>">我的商城</a></li>
					<li><a href="<c:url value='/dmz/tmall/index.html'/>">特权商城</a></li>
				<%-- 	<li><a href="<c:url value='/dmz/gmall/index.html'/>">我的官方商城</a></li> --%>
				</c:when>
				<c:when test="${user.admin}">
					<li><p class="navbar-text">公司账户代金券余额:<span id="voucherBalance">${accounts.voucherBalance}</span></p></li>
                    <li><p class="navbar-text">公司账户预存款余额:<span id="advanceBalance">${accounts.advanceBalance}</span></p></li>
                </c:when>
				</c:choose>
			</ul>
			<ul class="nav navbar-top-links navbar-right">
		<li class="dropdown"><a class="dropdown-toggle"
			data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
				<i class="fa fa-caret-down"></i>
		</a>
			<ul class="dropdown-menu dropdown-user">
				<%-- <li><a href="<c:url value='/user/edit.html'/>"><i class="fa fa-user fa-fw"></i>信息修改</a></li>
				<li class="divider"></li> --%>
				<li><a href="#"><i class="fa fa-clock-o fa-fw"></i>最近一次登陆时间：${lastVisitTime}</a></li>
				<li class="divider"></li>
				<li><a href="#"><i class="fa fa-paperclip fa-fw"></i>最近一次登陆IP：${lastVisitIp}</a></li>
				<li class="divider"></li>
				<li><a href="<c:url value='/logout'/>"><i
						class="fa fa-power-off fa-fw"></i> 退出系统</a></li>
			</ul> <!-- /.dropdown-user --></li>
		<!-- /.dropdown -->
			<li class="active"><a href="<c:url value='/logout'/>"><i
						class="fa fa-power-off fa-fw"></i> 退出系统</a></li>
	</ul>
	<!-- /.navbar-top-links -->

	<div class="nav navbar-default sidebar" role="navigation">
		<div class="sidebar-nav navbar-collapse menu-navbar-collapse">
			<ul class="nav" id="side-menu">
				<!-- 
				<li class="sidebar-search">
					<div class="input-group custom-search-form">
						<input type="text" class="form-control" placeholder="Search...">
						<span class="input-group-btn">
							<button class="btn btn-default" type="button">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div> 
				</li>-->
				<c:if test="${user.agent}">
					<li><a href="#" class="menuitem"><i
							class="fa fa-sitemap fa-fw"></i> 账户管理<span class="fa arrow"></span></a>
						<ul class="nav nav-second-level">
							<li><a href="<c:url value='/agent/myAuth.html?from=${user.id}'/>">
							<i class="fa fa-list fa-fw"></i>我的账户<span class="fa arrow"></span></a></li>
							<li><a href="<c:url value='/agent/children.html?from=${user.id}'/>">
							<i class="fa fa-users fa-fw"></i>代理商城客户<span class="fa arrow"></span></a></li>
							<li><a href="<c:url value='/agent/teqchildren.html?from=${user.id}'/>">
								<i class="fa fa-users fa-fw"></i>特权商城客户<span class="fa arrow"></span></a></li>
							<li><a
								href="<c:url value='/transfer/my.html?from=${user.id}'/>"><i
									class="glyphicon glyphicon-retweet fa-fw"></i>我要转货<span
									class="fa arrow"></span></a></li>
							<li><a
								href="<c:url value='/transfer/records.html?from=${user.id}'/>"><i
									class="fa fa-list fa-fw"></i>转货记录<span class="fa arrow"></span></a></li>
							<!-- 转代金券 -->
							<li><a
									href="<c:url value='/voucher/my.html'/>"><i
									class="fa fa-money fa-fw"></i>转代金券记录<span class="fa arrow"></span></a></li>

							<!-- 转代金券 -->
							<li><a
									href="<c:url value='/advance/my.html'/>"><i
									class="fa fa-money fa-fw"></i>转预存款记录<span class="fa arrow"></span></a></li>

									<!-- 代金券记录 -->
										<li><a
								href="<c:url value='/voucher/record.html'/>"><i
									class="fa fa-money fa-fw"></i>代金券详情<span class="fa arrow"></span></a></li>


							<!-- 预存款详情 -->
							<li><a
									href="<c:url value='/advance/record.html'/>"><i
									class="fa fa-money fa-fw"></i>预存款详情<span class="fa arrow"></span></a></li>
								<%-- <li><a
								href="<c:url value='/points/my.html'/>"><i
									class="fa fa-user fa-fw"></i>积分转让<span class="fa arrow"></span></a></li> --%>
						</ul></li>
					<li><a href="#" class="menuitem"><i
							class="fa fa-sitemap fa-fw"></i> 货物管理<span class="fa arrow"></span></a>
						<ul class="nav nav-second-level">
							<li><a href="<c:url value='/delivery/index.html'/>"><i
									class="fa fa-truck fa-fw"></i>请求发货<span
									class="fa arrow"></span></a></li>
							<li><a href="<c:url value='/delivery/agent/confirm.html'/>"><i
									class="fa fa-truck fa-fw"></i>发货确认<span
									class="fa arrow"></span></a></li>
							<li><a href="<c:url value='/securityCode/index.html'/>"><i
									class="fa fa-shield fa-fw"></i>防伪码<span
									class="fa arrow"></span></a></li>
						</ul></li>
				</c:if>
				<c:if test="${user.admin}">
				<c:forEach items="${topModules}" var="tm">
					<li><a href="#" class="menuitem"><i
							class="${tm.cssClass} fa-fw"></i>${tm.name}<span
							class="fa arrow"></span></a>
								<c:if test="${fn:length(tm.children) > 0}">
									<ul class="nav nav-second-level">
									<c:forEach items="${tm.children}" var="m">
									<c:if test="${ not empty myModules[m.id]}">
										<li data-m="${m.id}"><a href="<c:url value='${m.url}'/>"><i
									class="${m.cssClass}"></i>${m.name} <span class="fa arrow"></span></a></li>
									</c:if>
									</c:forEach>
								</ul>
								</c:if>
							</li>
				</c:forEach>
				</c:if>
			</ul>
		</div>
	</div><!-- /.sidebar-collapse -->
	</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
	<!-- /.navbar-static-side -->
</nav>
<div class="modal fade" id="indexModal" tabindex="-1" role="dialog"
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
				<div class="modal-body" id="messageBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
</div>