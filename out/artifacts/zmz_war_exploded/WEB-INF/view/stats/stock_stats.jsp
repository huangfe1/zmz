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
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>货物库存统计</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
				<h4>货物库存及余额实时汇总</h4>
					<%-- <div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/goods/index.html'/>"
							method="GET">
							<input type="hidden" name="action" value="list" /> <input
								type="hidden" name="resourceCode" value="${resourceCode }" />
							<div class="form-group">
								<label class="">产品名称</label> <input type="text"
									value="${parameter.entity.name}" name="entity.name" id="name"
									autofocus class="typeahead form-control" placeholder="产品名称">
								<label class="">积分系数</label> <input type="number"
									value="${parameter.entity.pointFactor}" name="entity.pointFactor" id="pointFactor"
									autofocus class="typeahead form-control" placeholder="产品积分系数">

								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增产品
								</button></li>
						</ul>
					</div> --%>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>产品名称</th>
									<th>总入库数</th>
									<th>当前库存</th>
									<th>当前余额</th>
									<th>代理账户余额</th>
									<th>已发货数</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${result}" var="g">
									<tr data-row="${g[0]}">
										<td>${g[1]}</td>
										<td>${g[4]}</td>
										<td>${g[2]}</td>
										<td>${g[3]}</td>
										<td data-id="${g[0]}"><a class="btn-link agentBalanceDetail" href="<c:url value='/stats/goodsBalance.html?goodsId=${g[0]}'/>">${g[6]}</a></td>
										<td>${g[5]}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<%-- <div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<jsp:include page="/WEB-INF/view/common/pager.jsp"></jsp:include>
				</div>
			</div> --%>
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
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$(".agentBalanceDetail").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("href");
				$("#myModal").load(url,function(e) {
					$('#myModal').modal({
						backdrop : "static"
					});
				});
			});
		});
	</script>
</body>
</html>
