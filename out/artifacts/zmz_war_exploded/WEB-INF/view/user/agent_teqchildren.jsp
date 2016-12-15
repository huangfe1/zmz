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
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<title>代理管理</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<!-- <div class="collapse navbar-collapse" id="top-navbar-collapse"> -->
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/agent/children.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">姓名</label> <input type="text"
									value="${parameter.entity.realName}" name="entity.realName" id="realName"
									autofocus class="typeahead form-control" placeholder="代理姓名">
								<label class="">代理编号</label> <input type="text"
									value="${parameter.entity.agentCode}" name="entity.agentCode" id="agentCode"
									autofocus class="typeahead form-control" placeholder="代理编码">
								<label class="">手机号码</label> <input type="text"
									value="${parameter.entity.mobile}" name="entity.mobile" id="mobile"
									 class="typeahead form-control" placeholder="联系电话">
								<label class="">微信号</label> <input type="text"
									value="${parameter.entity.weixin}" name="entity.weixin" id="weixin"
									 class="typeahead form-control" placeholder="微信号码">
								<label class="">代理状态</label> <select type="text"
									value="${parameter.entity.agentStatus}" name="entity.agentStatus" id="agentStatus"
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
						<!-- <ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增代理
								</button></li>
							<li><button type="button"
									class="btn btn-primary navbar-btn transBtn">
									<li class="fa fa-exchange fa-fw"></li>代理转让
								</button></li>
						</ul> -->
					<!-- </div> -->
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>代理编号</th>
									<th>姓名</th>
									<th>电话</th>
									<th>微信号</th>
									<%--<th>积分</th>--%>
									<th>代金券余额</th>
									<th>预存款余额</th>
									<th>福利积分</th>
									<th>代理状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${agents}" var="l">
									<tr data-row="${l.id}">
										<td><a class="editBtn"
											href="<c:url value='/agent/accounts_detail.html?id=${l.id}' /> ">${l.agentCode}</a></td>
										<td>${l.realName}</td>
										<td>${l.mobile}</td>
										<td>${l.weixin}</td>
										<%--<td>${l.accounts.pointsBalance}</td>--%>
										<td>${l.accounts.voucherBalance}</td>
										<td>${l.accounts.advanceBalance}</td>
										<td>${l.accounts.benefitPointsBalance}</td>
										<td>${l.agentStatus.desc}</td>
										<td style="word-break:keep-all;">
										<a class="btn btn-warning editBtn" href="<c:url value='/agent/accounts_detail.html?id=${l.id}' /> ">
										<span class="glyphicon glyphicon-new-window ">&nbsp;</span>账户明细</a>
											<a class="btn btn-warning " href="<c:url value='/securityCode/scan_num.html?owner=${l.realName}&&agentCode=${l.agentCode}' /> ">录入防伪</a>
											<c:if test="${user.admin}">
										<a class="btn btn-success default editBtn"
											href="<c:url value='/agent/edit.html?id=${l.id}' /> "><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>修改</a>
											<a class="btn btn-info default transBtn"
											href="<c:url value='/agent/transfer.html?id=${l.id}' /> "><span class="glyphicon glyphicon-retweet" aria-hidden="true"></span>&nbsp;转让</a>
											<c:if test="${l.inactive or l.reorganize}">
											<a class="btn btn-success default ajaxLink"
											href="<c:url value='/agent/active.json?id=${l.id}' /> "><span class="fa fa-unlock fa-fw" aria-hidden="true"></span>激活</a>
											</c:if>
											<c:if test="${l.active}">
											<a class="btn btn-warning default ajaxLink"
											href="<c:url value='/agent/reorganize.json?id=${l.id}' /> "><span class="fa fa-lock fa-fw" aria-hidden="true"></span>整顿</a>
											</c:if>
											<a class="ajaxLink btn btn-danger default" data-role="delete"
											href="<c:url value='/agent/remove.json?id=${l.id}' /> "><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>
										</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<jsp:include page="/WEB-INF/view/common/pager.jsp"></jsp:include>
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
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
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
			/* $(".transBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/agent/transfer.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					}); */
			
			$(".editBtn,.transBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$("#myModal").load($(this).attr("href"),function(e) {
							$('#myModal').modal({
								backdrop : "static"
							});
						});
			});
			$("#dataList a.ajaxLink").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var method="POST";
				if($(this).attr("data-role")=="delete"){
					if(!window.confirm("代理数据删除将不能恢复,是否继续?")){
						return false;
					}
					method="DELETE";
				}
				$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#messageBody").empty().html("操作成功");
							$("#myModal").modal({backdrop:"static",show:true});
							$("#search").click();
						}else{
							$("#messageBody").empty().html("操作失败").addClass("text-danger");
							$("#myModal").modal({backdrop:"static",show:true});
						}
						
					}else{
						$("#messageBody").empty().html("操作失败").addClass("text-danger");
						$("#myModal").modal({backdrop:"static",show:true});
					}
				}});
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
