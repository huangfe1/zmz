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
<title>系统管理员管理</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/user/admin/index.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">姓名</label> <input type="text"
									value="${parameter.entity.realName}" name="entity.realName" id="realName"
									autofocus class="typeahead form-control" placeholder="管理员姓名">
								<label class="">手机号码</label> <input type="text"
									value="${parameter.entity.mobile}" name="entity.mobile" id="mobile"
									 class="typeahead form-control" placeholder="联系电话">
								<label class="">用户状态</label> <select type="text"
									value="${parameter.entity.userStatus}" name="entity.userStatus" id="userStatus"
									class="form-control">
									<option value="">所有状态</option>
									<c:forEach items="${userStatus}" var="s">
										<option value="${s}" 
										<c:if test="${parameter.entity.userStatus==s}">selected</c:if> >${s.desc}</option>
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
									<li class="fa fa-plus-square fa-fw"></li>新增管理员
								</button></li>
						</ul>
					</div>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>选择</th>
									<th>登陆名</th>
									<th>姓名</th>
									<th>电话</th>
									<th>微信号</th>
									<th>身份证号</th>
									<th>用户状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${admins}" var="l">
									<tr data-row="${l.id}">
										<td><input type="checkbox" value="${l.id}"></td>
										<td>${l.loginName}</td>
										<td>${l.realName}</td>
										<td>${l.mobile}</td>
										<td>${l.weixin}</td>
										<td>${l.idCard}</td>
										<td>${l.userStatus.desc}</td>
										<td>
										<c:if test="${l.admin}">
										<a class="btn btn-success default editBtn"
											href="<c:url value='/user/admin/edit.html?id=${l.id}' /> "><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>修改</a>
											<c:if test="${l.normal}">
											<a class="btn btn-warning default ajaxLink" data-role="lock"
											href="<c:url value='/user/admin/lock.json?id=${l.id}' /> "><span class="fa fa-unlock fa-fw" aria-hidden="true"></span>锁定</a>
											</c:if>
											<c:if test="${l.locked}">
											<a class="btn btn-success default ajaxLink" data-role="unlock"
											href="<c:url value='/user/admin/unlock.json?id=${l.id}' /> "><span class="fa fa-lock fa-fw" aria-hidden="true"></span>解锁</a>
											</c:if>
											<a class="ajaxLink btn btn-danger default" data-role="delete"
											href="<c:url value='/user/admin/remove.json?id=${l.id}' /> "><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>
										</c:if>
										<c:if test="${l.mutedUser}">
										系统内置用户,无法进行编辑操作
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
<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/user/admin/edit.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					});
			$(".editBtn").click(function(e){
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
					if(!window.confirm("用户数据删除将不能恢复,是否继续?")){
						return false;
					}
					method="DELETE";
				}else if($(this).attr("data-role")=="lock"){
					if(!window.confirm("将锁定此用户账户,是否继续?")){
						return false;
					}
				}else if($(this).attr("data-role")=="unlock"){
					if(!window.confirm("将解锁此用户账户,是否继续?")){
						return false;
					}
				}
				
				var that=$(this);
				$.ajax({url:that.attr("href"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#alertMessageBody").empty().html("操作成功");
							$("#myAlertModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								$("#search").click();
							}, 1500);
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
