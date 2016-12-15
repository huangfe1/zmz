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
<%@include file="/WEB-INF/view/common/datepicker_css.jsp"%>
<%@include file="/WEB-INF/view/common/head_css_fav.jsp"%>
<style>
.input-daterange {
  width: inherit !important;
}
</style>
<title>代金券转让</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<%--<div class="navbar-header">--%>
						<%--<button type="button" class="navbar-toggle animated flip pull-left"--%>
								<%--data-toggle="collapse" data-target="#top-navbar-collapse">--%>
							<%--<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>--%>
							<%--<span class="icon-bar"></span> <span class="icon-bar"></span>--%>
						<%--</button>--%>
					<%--</div>--%>
					<%--<div class="collapse navbar-collapse" id="top-navbar-collapse">--%>
					<div class="col-sm-12 col-md-12 col-xs-12">
						<form class="navbar-form form-horizontal navbar-left" id="searchForm"
							role="search" action="<c:url value='/advance/my.html'/>"
							method="GET">
							<div class="form-group" style="display: none">
							<%--<label>转让时间</label>--%>
										<%--<div class="input-daterange input-group" id="datepicker">--%>
										<%--<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>--%>
										<%--<input type="text" class="form-control pointer" --%>
											<%--value="${parameter.startTime }" id="startDate" data-date-format="yyyy-mm-dd"--%>
											<%--name="startTime" placeholder="开始日期" />--%>
										<%--<span class="input-group-addon">到</span> <input type="text"--%>
											<%--class="form-control pointer"--%>
											<%--value="${parameter.endTime }" --%>
											<%--name="endTime" placeholder="截止日期" />--%>
											<%--<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>--%>
									<%--</div>--%>
								<button type="submit" class="btn btn-primary" id="search"name="search">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
							<%--<li><button type="button"--%>
									<%--class="btn btn-primary navbar-btn transBtn">--%>
									<%--<li class="fa fa-exchange fa-fw"></li>转让预存款(暂未开放)--%>
								<%--</button></li>--%>
						</ul>
						</div>
					</div>
					
				<%--</div>--%>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered table-hover">
							<thead>
								<tr>
									<th>转让人</th>
									<th>转让人代理编码</th>
									<th>受让人</th>
									<th>受让人代理编码</th>
									<th>转让预存款</th>
									<th>备注</th>
									<th>申请时间</th>
									<c:if test="${user.admin}">
										<th>操作</th>
									</c:if>


									<%--<th>转让时间</th>--%>
								</tr>
							</thead>
							<tbody id="dataList">
								<c:forEach items="${pts}" var="l">
									<tr data-row="${l.id}">
										<td>${l.userByFromAgent.realName}</td>
										<td>${l.userByFromAgent.agentCode}</td>
										<td>${l.userByToAgent.realName}</td>
										<td>${l.userByToAgent.agentCode}</td>
										<td>${l.advance}</td>
										<td>(${l.type.desc})${l.remark}</td>
										<td>${l.updateTime}</td>
										<c:if test="${user.admin}">
											<c:if test="${l.type.desc eq '失败/未付款'}">
													<th>		<a class="btn btn-success default ajaxLink" href="<c:url value='/advance/pay/dmz/aliNotify.html?orderId=${l.id}&&money=${l.advance}' /> ">人工充值</a></th>
											</c:if>
											<c:if test="${l.type.desc ne '失败/未付款'}">
												<th>已经充值</th>
											</c:if>
										</c:if>
										<%--<td>${l.transferTime}</td>--%>
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
	<jsp:include page="/WEB-INF/view/common/datepicker_js.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/view/common/script_common.jsp"></jsp:include>
	<script type="text/javascript">
		$(function() {
            $("#dataList a.ajaxLink").click(function(e){
                e.preventDefault();
                e.stopPropagation();
                var method="GET";
                if($(this).attr("data-role")=="delete"){
                    if(!window.confirm("代理数据删除将不能恢复,是否继续?")){
                        return false;
                    }
                    method="DELETE";
                }
                $.ajax({url:$(e.target).attr("href"),method:method,complete:function(xhr,ts){
                    if(xhr.status>=200 && xhr.status<300){
//                        var m=$.parseJSON(xhr.responseText);
                        var m=xhr.responseText;
                        if(m.indexOf("error")!=-1){
                            $("#messageBody").empty().html("操作失败"+m).addClass("text-danger");
                            $("#myModal").modal({backdrop:"static",show:true});
                            alert(m);
                        }else{
                            $("#messageBody").empty().html("操作成功");
                            $("#myModal").modal({backdrop:"static",show:true});
                            $("#search").click();
                        }

                    }else{
                        $("#messageBody").empty().html("操作失败").addClass("text-danger");
                        $("#myModal").modal({backdrop:"static",show:true});
                    }
                }});
            });


			$("#datepicker.input-daterange").datepicker({
				autoclose : true,
				language : "zh-CN",
				todayHighlight : true,
				todayBtn : "linked",
				format:"yyyy-mm-dd",
				endDate:new Date()
			});
			$(".transBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/advance/transfer.html'/>",
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
