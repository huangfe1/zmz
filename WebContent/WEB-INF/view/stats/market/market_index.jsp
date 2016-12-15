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
<title>市场详情统计</title>
<style type="text/css">
.agent{
word-break:keep-all;             /* 不换行 */
white-space:nowrap;
font-size:0.5em;
}
.self,.toChildren{
cursor:pointer;
}
</style>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/stats/market/index.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">代理编码</label> <input type="text"
									value="${agentCode}" name="agentCode" id="agentCode"
									autofocus class="typeahead form-control" placeholder="代理编码">
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
								<button type="button" class="btn btn-warning" id="back" onclick="window.history.back();"
									name="back">
									<span class="fa fa-arrow-left"></span>&nbsp;返回上级
								</button>
							</div>
						</form>
					</div> 
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<caption><h4>${table.owner} 的市场情况&nbsp;<small style="font-size:0.5em !important;"><span class="text-danger" >红色字体为整顿状态代理</span>
							<span>点击姓名查看账户信息，点击代理编码进入代理的市场详情</span>
							</small>
							</h4></caption>
							<thead>
							<tr>
							<c:forEach items="${table.columns}" var="col">
								
									<th>${col.name}</th>
							</c:forEach>
							</tr>
							</thead>
							<tbody id="dataList">
								<tr>
								<c:forEach items="${table.columns}" var="g">
										<td>${fn:length(g.agents)}</td>
								</c:forEach>
								</tr>
								<c:if test="${table.maxRow>0 }">
								<c:forEach var="i" begin="0" end="${table.maxRow-1}" step="1">
								<tr>
									<c:forEach items="${table.columns}" var="s">
										<c:choose>
										<c:when test="${fn:length(s.agents) > i}">
										<td class="agent"><span <c:if test="${s.agents[i].status eq 'REORGANIZE'}"> class="text-danger"</c:if>>
										<span class="self" data-href="<c:url value='/agent/accounts_detail.html?id=${s.agents[i].id}'/>"
										title="点击查看账户基本信息"  data-id="${s.agents[i].id}">${s.agents[i].agentName}</span>
										(<span class="toChildren" title="点击查看我的市场情况" data-href="<c:url value='/stats/market/index.html?agentCode=${s.agents[i].agentCode}'/>">${s.agents[i].agentCode}</span>)</span></td>
										</c:when>
										<c:otherwise>
										<td>&nbsp;</td>
										</c:otherwise>
										</c:choose>
									</c:forEach>
								
								</tr>
								</c:forEach>
								</c:if>
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
			$(".toChildren").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("data-href");
				window.location=url;
			});
			$(".self").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("data-href");
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
