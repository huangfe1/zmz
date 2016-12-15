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
<title>发货信息</title>
</head>
<body >
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<!-- <div class="collapse navbar-collapse" id="top-navbar-collapse"> -->
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/delivery/index.html'/>"
							method="GET">
							<input type="hidden" name="from" value="${user.id}">
							<div class="form-group">
								<label class="">收货人</label> <input type="text"
									value="${parameter.entity.consigneeName}" name="entity.consigneeName" id="consigneeName"
									autofocus class="typeahead form-control" placeholder="收货人姓名">
								<label class="">申请发货人</label> <input type="text"
									value="${parameter.entity.userByApplyAgent.realName}" name="entity.userByApplyAgent.realName" id="applyAgent"
									autofocus class="typeahead form-control" placeholder="申请发货人姓名/编码">
								<label class="">联系电话</label> <input type="text"
																   value="${parameter.entity.mobile}" name="entity.mobile" id="mobile"
																   autofocus class="typeahead form-control" placeholder="联系电话">
								<label>申请时间</label>
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
										${s.desc}</option>
									</c:forEach>
								</select> 
								<button type="submit" class="btn btn-primary" id="search"
									name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
								<button type="button" class="btn btn-danger  btn-re" id="download"
										name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;下载
								</button>
							</div>
						</form>
						<!-- </div> -->
						<ul class="nav navbar-nav navbar-right">
							<c:choose>
								<c:when test="${user.agent}">
								<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>请求发货
								</button></li>
								</c:when>
								<c:when test="${user.admin}">
									<a href="<c:url value="/delivery/getOrders.html"/>">下载最近未发货的订单</a>
                                    <button type="button" class="btn btn-primary" id="upload"
                                            name="upload">
                                        <span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;上传
                                    </button>
                                </c:when>
							</c:choose>
						</ul>
					
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
									<th>收货人</th>
									<th>请求发货人</th>
                                    <th>代理编号</th>
									<th>货物数量</th>
									<th>联系电话</th>
									<th>订单来源</th>
									<th>请求时间</th>
									<th>发货时间</th>
									<th>物流单号</th>
									<th>物流公司</th>
									<th>状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${dls}" var="g">
									<tr data-row="${g.id}">
										<td><input type="checkbox" value="${g.id}"></td>
										<td>${g.orderNo}</td>
										<td>${g.consigneeName}</a></td>
										<td>${g.userByApplyAgent.realName}</td>
                                        <td>${g.userByApplyAgent.agentCode}</td>
										<td>${g.quantity}</td>
										<td>${g.mobile}</td>
										<td>${g.applyOrigin.toString()}</td>
										<td>${g.applyTime}</td>
										<td>${g.deliveryTime}</td>
										<td>${g.logisticsCode}</td>
										<td>${g.logistics}</td>
										<td>${g.status.desc}</td>
										<td>
										<c:choose>
										<c:when test="${user.agent}">
										<a class="btn btn-success default editBtn"
											<%--<c:if test="${g.status!='NEW' }">disabled="disabled"</c:if>--%>
											href="<c:url value='/delivery/edit.html?id=${g.id}' /> "><span class="fa fa-wrench fa-fw" aria-hidden="true"></span>详情</a>
										</c:when>
										<c:when test="${user.admin}">
											<%-- <c:if test="${g.status=='NEW' }">
											<a class="btn btn-success default editBtn" 
											href="<c:url value='/delivery/confirm.html?id=${g.id}' /> "><span class="glyphicon glyphicon-ok fa-fw" aria-hidden="true"></span>确认</a>
											</c:if> --%>
											<c:if test="${g.status=='CONFIRM' or (g.status=='NEW' and g.applyOrigin=='SYSTEM') }">
											<a class="btn btn-success default editBtn" 
											href="<c:url value='/delivery/delivery.html?id=${g.id}' /> "><span class="glyphicon glyphicon-ok fa-fw" aria-hidden="true"></span>发货</a>
											</c:if>
											<c:if test="${g.status=='DELIVERY' }">
											<a class="btn btn-info default editBtn" 
											href="<c:url value='/delivery/delivery.html?id=${g.id}' /> "><span class="glyphicon glyphicon-wrench fa-fw" aria-hidden="true"></span>修改物流</a>
											<a class="btn btn-info  printBtn" 
											href="<c:url value='/delivery/print.html?id=${g.id}' /> "  target="print"><span class="glyphicon glyphicon-print fa-fw" aria-hidden="true"></span>打印</a>
											</c:if>
										</c:when>
										</c:choose>	
											<a class="btn btn-danger default ajaxLink" data-role="delete"
											<c:if test="${g.status!='NEW' }">disabled="disabled"</c:if>
											href="<c:url value='/delivery/remove.json?id=${g.id}' /> "><span class="fa fa-trash fa-fw" aria-hidden="true"></span>删除</a>
										
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
	
	<div class="modal fade" id="selectModal" tabindex="-2" role="dialog" style="z-index:999999;"
		aria-labelledby="selectModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="selectModalLabel"></h4>
				</div>
				<div class="modal-body" id="selectBody"></div>
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
				$("#searchForm").attr("action", "<c:url value="/delivery/download.html"/>");
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

            $("#upload").click(function(e){
                var url="<c:url value="/delivery/upload.html"/>";
                $("#editModal").load(url,function(e) {
                    $('#editModal').modal({
                        backdrop : "static"
                    });
                });
            });


			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#editModal").load(
								"<c:url value='/delivery/edit.html'/>",
								function(e) {
									$('#editModal').modal({
										backdrop : "static"
									});
								});
					});
			$(".editBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				$("#editModal").load($(this).attr("href"),function(e) {
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
							$("#messageBody").empty().html("操作成功");
							$("#editModal").modal({backdrop:"static",show:true});
							$("#search").click();
						}else{
							$("#messageBody").empty().html("操作失败,"+m.message).addClass("text-danger");
							$("#editModal").modal({backdrop:"static",show:true});
						}
						
					}else{
						$("#messageBody").empty().html("操作失败").addClass("text-danger");
						$("#editModal").modal({backdrop:"static",show:true});
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
