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
<%@include file="/WEB-INF/view/common/head_css_startbootstrap.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_morris.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_treeview.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>我的授权</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<%-- <nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/agent/myAuth.html'/>"
							method="GET">
							<input type="hidden" name="id" value="${user.id}">
							<div class="form-group">
								<label class="">姓名</label> <input type="text"
									value="${parameter.entity.realName}" name="entity.realName" id="realName"
									autofocus class="typeahead form-control" placeholder="代理姓名">
								<label class="">授权状态</label> <select type="text"
									name="entity.agentStatus" id="agentStatus"
									class="form-control">
									<option value="">所有状态</option>
									<c:forEach items="${status}" var="s">
										<option value="${s}" 
										<c:if test="${parameter.entity.agentStatus==s}">selected</c:if> >${s.desc}</option>
									</c:forEach>
									</select>									
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增代理
								</button></li>
							<li><button type="button"
									class="btn btn-primary navbar-btn transBtn">
									<li class="fa fa-exchange fa-fw"></li>代理转让
								</button></li>
						</ul>
					</div>
				</div>
			</nav> --%>
			<br>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="panel panel-yellow" id="basic">
						<div class="panel-heading">
							<h3 class="panel-title">账户信息</h3>
						</div>
						<div class="panel-body">
							<ul class="list-group">
								<%--<li class="list-group-item"><span class="badge">${parameter.entity.accounts.pointsBalance}</span>--%>
								<%--积分余额--%>
								<%--</li>--%>
								<li class="list-group-item"><span class="badge primary">${parameter.entity.accounts.voucherBalance}</span>
								拥有代金券
								</li>
								<li class="list-group-item"><span class="badge primary">${parameter.entity.accounts.advanceBalance}</span>
									拥有预存款
								</li>
								<li class="list-group-item"><span class="badge">${parameter.entity.accounts.benefitPointsBalance}</span>
								拥有福利积分
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
				<div class="panel panel-green id="auths">
						<div class="panel-heading">
							<h3 class="panel-title">我拥有的授权</h3>
						</div>
						<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>授权类型</th>
									<th>产品名称</th>
									<th>等级</th>
									<th>当前状态</th>
									<th>授权证书</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${parameter.entity.authorizations}" var="l">
								<c:set var="account" value="${levels[l.authorizedGoods.id]}" />
									<tr data-row="${l.id}">
										<td>${l.authorizationType.name}</td>
										<td>${l.authorizedGoods.name}</td>
										<td>${account.agentLevel.name }</td>
										<td>${l.status.desc}</td>
										<td><a class="btn btn-default viewBtn" target="letter" href="<c:url value='/auth/letter.html?id=${l.id}'/>" data-id="${l.id}">查看证书</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12 col-md-12">
				<div class="panel panel-red" id="accounts">
						<div class="panel-heading">
							<h3 class="panel-title">产品账户</h3>
						</div>
						<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>产品名称</th>
									<th>当前库存</th>
									<th>累计转货量</th>
									<th>累计转出量</th>
									<th>最大转出量</th>
									<th>当前等级</th>
									<th>团队发货量</th>
								</tr>
							</thead>
							<tbody>
										<%-- <c:forEach items="${parameter.entity.goodsAccounts}"
											var="accounts">
											<tr>
												<td>${accounts.goods.name}</td>
												<td>${accounts.currentBalance}</td>
												<td>${accounts.cumulative}</td>
												<td>${accounts.agentLevel.name}</td>
											</tr>
										</c:forEach> --%>
										<c:forEach items="${gacs}"
											var="accounts">
											<tr>
												<td>${accounts.goods.name}</td>
												<td>${accounts.currentBalance}</td>
												<td>${accounts.cumulative}</td>
												<td>${accounts.tradingCumulative}</td>
												<td>${accounts.maxTradingLimited >=0 ? accounts.maxTradingLimited:'无限制'}</td>
												<td>${accounts.levelName}</td>
												<td>${sum[accounts.goods.id]}</td>
											</tr>
										</c:forEach>
									</tbody>
						</table>
					</div>
					</div>
					</div>
				</div>
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
				<div class="modal-body" id="messageBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myAlertModal" tabindex="-1" role="dialog" style="z-index:999999;"
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
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/agent/edit.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					});
			$("tbody tr[data-row]").each(function(index,row){
				$(row).click(function(event){
					if(event.target.nodeName!="INPUT"){
						rowSelect($(this));
					}
					switchCss($(this));
				});
				$(row).bind("contextmenu",function(){
					return false;
				});
			});
		});
	</script>
</body>
</html>
