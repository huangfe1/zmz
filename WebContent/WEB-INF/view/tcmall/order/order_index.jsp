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
<%@include file="/WEB-INF/view/common/datepicker_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_datatables.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<style>
.input-daterange {
  width: inherit !important;
}
td{
	word-break:keep-all;
}
</style>
<title>订单管理</title>
</head>
<body >
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/pm/order/index.html'/>"
							method="GET">
							<input type="hidden" name="from" value="${user.id}">
							<div class="form-group">
							<label class="">订单号</label> <input type="text"
									value="${parameter.entity.orderNo}" name="entity.orderNo" id="orderNo"
									autofocus class="typeahead form-control" placeholder="订单号查询">
								<label class="">收货人</label> <input type="text"
									value="${parameter.entity.consignee}" name="entity.consignee" id="consignee"
									autofocus class="typeahead form-control" placeholder="收货人姓名">
								<label>下单时间</label>
										<div class="input-daterange input-group" id="datepicker">
										<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
										<input type="text" class="form-control pointer"
											value="${parameter.startTime }" id="startDate" data-date-format="yyyy-mm-dd"
											name="startTime" placeholder="开始日期" />
										<span class="input-group-addon">到</span> <input type="text"
											class="form-control pointer"
											value="${parameter.endTime }"
											name="endTime" placeholder="截止日期" />
											<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
									</div>
								
								<label class="">状态</label>
								<select name="entity.status" id="status" class="form-control">
									<option value="">所有</option>
									<c:forEach items="${status}" var="s">
										<option value="${s}" <c:if test="${parameter.entity.status==s}">selected</c:if>>
										${s.name}</option>
									</c:forEach>
								</select>
								<label class="">付款状态</label>
								<select name="entity.paymentStatus" id="paymentStatus" class="form-control">
									<option value="">所有</option>
									<c:forEach items="${paymentStatus}" var="s">
										<option value="${s}" <c:if test="${parameter.entity.paymentStatus==s}">selected</c:if>>
										${s.name}</option>
									</c:forEach>
								</select> 
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
								<button type="submit" class="btn btn-primary" id="download"
										name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;下载
								</button>
							</div>
						</form>
						<!-- <ul class="nav navbar-nav navbar-right">
								<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>
								</button></li>
						</ul> -->
					</div>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>选择</th>
									<th>订单编号</th>
									<th>数量</th>
									<th>金额</th>
									<th>下单人</th>
									<th>代理编号</th>
									<th>收货人</th>
									<th>联系电话</th>
									<th>下单时间</th>
									<th>物流单号</th>
									<th>物流公司</th>
									<th>订单状态</th>
									<th>付款状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
							<c:set var="amounts" value="0"/>
								<c:forEach items="${orders}" var="g">
									<%-- <c:forEach items="${g.items}" var="i">
										<c:set var="amounts" value="${amounts+g.quantity}"/>
									</c:forEach> --%>
									<tr data-row="${g.id}" 
									<c:choose>
										<c:when test="${g.status=='REVOKED'}">
											<c:out value="class=danger"/>
										</c:when>
										<c:when test="${g.paymentStatus=='UNPAID'}">
										<c:out value="class=warning"/>
										</c:when>
										<c:when test="${g.status=='RECEIVED'}">
										<c:out value="class=success"/>
										</c:when>
									</c:choose>
									>
										<td><input type="checkbox" value="${g.id}"></td>
										<td>${g.orderNo}</td>
										<td>${g.quantity}</td>
										<td>${g.totalMoney}</td>
										<td>${g.user.realName}</td>
										<td>${g.user.agentCode}</td>
										<td>${g.consignee}</td>
										<td>${g.mobile}</td>
										<td>${g.orderTime}</td>
										<td>${g.logisticsCode}</td>
										<td>${g.logistics}</td>
										<td>${g.status.name}</td>
										<td>${g.paymentStatus.name}</td>
										<td>
										<c:if test="${user.admin}">
											<c:if test="${g.paymentStatus=='UNPAID' }">
											<a class="btn btn-success default editBtn" 
											href="<c:url value='/pm/order/pay.html?id=${g.id}' /> "><span class="glyphicon glyphicon-ok fa-fw" aria-hidden="true"></span>付款</a>
											<a class="btn btn-danger default editBtn"
											href="<c:url value='/pm/order/revoke.html?id=${g.id}' /> "><span class="fa fa-trash fa-fw" aria-hidden="true"></span>撤销</a>
											</c:if>
											<c:if test="${g.status=='SHIPPED' }">
											<a class="btn btn-info default ajaxLink" 
											href="<c:url value='/pm/order/receive.json?id=${g.id}' /> "><span class="glyphicon glyphicon-ok fa-fw" aria-hidden="true"></span>收货</a>
											</c:if>
											<c:if test="${g.paymentStatus=='PAID' and (g.status=='RETURNED' or g.status=='REVOKED') }">
											<a class="btn btn-danger default editBtn"
											href="<c:url value='/pm/order/refund.html?id=${g.id}' /> "><span class="fa fa-reply-all fa-fw" aria-hidden="true"></span>退款</a>
											</c:if>
											<c:if test="${g.status=='RECEIVED' }">
											<a class="btn btn-warning default editBtn" 
											href="<c:url value='/pm/order/return.html?id=${g.id}' /> "><span class="fa fa-reply fa-fw" aria-hidden="true"></span>退货</a>
											</c:if>
											<a class="btn btn-success default editBtn" 
											href="<c:url value='/pm/order/detail.html?id=${g.id}' /> "><span class="glyphicon glyphicon-list fa-fw" aria-hidden="true"></span>详情</a>
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
	<div class="modal fade" id="editModal" style="overflow-x:hidden;overflow-y:auto;"  tabindex="-1" role="dialog"
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
				<div class="modal-footer">
				<div class="form-group">
					<div class="col-md-12 col-xs-12">
						<button type="button" class="btn btn-default btn-block quitBtn"
							tabIndex="26" id="quitBtn" data-dismiss="modal" name="quitBtn"
							value="login" tabindex="4" data-loading-text="正在返回......">
							<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
						</button>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="alertModal" tabindex="-2" role="dialog" style="z-index:999999;"
		aria-labelledby="alertModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="alertModalLabel">操作提示</h4>
				</div>
				<div class="modal-body" id="alertMessageBody"></div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datatables.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {

			$("#download").click(function(){
				$("#searchForm").attr("action", "<c:url value='/pm/order/download.html'/>");
				$("#searchForm").submit();
			});

			$("#datepicker.input-daterange").datepicker({
				autoclose : true,
				language : "zh-CN",
				todayHighlight : true,
				todayBtn : "linked",
				format:"yyyy-mm-dd",
				endDate:new Date()
			});

			$(".editBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("href");
				$("#editModal").load(url,function(e) {
							$('#editModal').modal({
								backdrop : "static"
							});
						});
			});
			$("#dataList a.ajaxLink").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				var method="POST";
				if($(this).attr("data-role")){
					method="DELETE";
					if(!window.confirm("删除后无法恢复,是否继续?")){
						return false;
					}
				}
				$.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						var m=$.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#alertMessageBody").empty().html("操作成功");
							$("#alertModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								$("#search").click();
							}, 2000);
							
						}else{
							$("#alertMessageBody").empty().html("操作失败,"+m.message).addClass("text-danger");
							$("#alertModal").modal({backdrop:"static",show:true});
						}
						
					}else{
						$("#alertMessageBody").empty().html("操作失败").addClass("text-danger");
						$("#alertModal").modal({backdrop:"static",show:true});
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
