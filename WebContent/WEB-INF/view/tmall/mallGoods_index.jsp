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
<style type="text/css">
td{
word-break: keep-all;
}
</style>
<title>积分商城商品管理</title>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/menu.html"></jsp:include>
		<div id="page-wrapper">
			<nav class="navbar navbar-default" role="navigation">
				<div class="container-fluid">
					<div class="collapse navbar-collapse" id="top-navbar-collapse">
						<form class="navbar-form navbar-left" id="searchForm"
							role="search" action="<c:url value='/mall/goods/index.html'/>"
							method="GET">
							<div class="form-group">
								<label class="">商品名称</label> <input type="text"
									value="${parameter.entity.name}" name="entity.name" id="name"
									autofocus class="typeahead form-control" placeholder="商品名称">
								<label class="">状态</label> 
								<select name="entity.shelf" id="shelf" class="form-control">
									<option value="">所有</option>
									<option value="1" >上架</option>
									<option value="0" >下架</option>
								</select>
								<button type="button" class="btn btn-primary" id="searchDT"
									name="searchDT">
									<span class="glyphicon glyphicon-search searchBtn"></span>&nbsp;查询
								</button>
							</div>
						</form>
						<ul class="nav navbar-nav navbar-right">
							<li><button type="button"
									class="btn btn-primary navbar-btn addBtn">
									<li class="fa fa-plus-square fa-fw"></li>新增商品
								</button></li>
						</ul>
					</div>
				</div>
			</nav>
			<div class="row">
				<div class="col-lg-12 col-md-12">
					<div class="table-responsive">
						<table id="mainTable"
							class="table table-striped table-bordered table-hover table-condensed">
							<thead>
								<tr>
									<th>选择</th>
									<th>商品名称</th>
									<th>规格</th>
									<th>价格</th>
									<th>所需积分</th>
									<th>积分价格</th>
									<th>返券额</th>
									<th>返福利积分</th>
									<th>当前库存</th>
									<th>是否上架</th>
									<th>显示顺序</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="dataList">
								<%-- <c:forEach items="${goods}" var="g">
									<tr data-row="${g.id}" <c:if test="${g.shelf}"> class="danger"</c:if>>
										<td><input type="checkbox" value="${g.id}"></td>
										<td><a class="editBtn"
											href="<c:url value='/goods/edit.html?id=${g.id}' /> ">${g.name}</a></td>
										<td>${g.spec}</td>
										<td>${g.price}</td>
										<td>${g.pointPrice}</td>
										<td>${g.voucher}</td>
										<td>${g.shelf ? "是" : "否" }</td>
										<td>${g.sequence}</td>
										<td><a class="btn btn-success default editBtn"
											href="<c:url value='/mall/goods/edit.html?id=${g.id}' /> "><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>修改</a>
											<a class="ajaxLink btn btn-danger default" data-role="delete"
											href="<c:url value='/mall/goods/remove.json?id=${g.id}' /> "><span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除</a>
										</td>
									</tr>
								</c:forEach> --%>
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
	<div class="modal fade" id="winModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="winModalLabel"></h4>
				</div>
				<div class="modal-body" id="winBody"></div>
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
			var langUrl = "<c:url value='/resources/js/datatables-plugins/i18n/Chinese.json'/>";
			var dt = $("#mainTable").DataTable({
				"language" : {
					"url" : langUrl
				},
				"processing" : true,
				"serverSide" : true,
				"searching" : false,
				"lengthChange" : false,
				"ordering" : false,
				"pageLength" : 15,
				"ajax" : {
					"url" : "<c:url value='/mall/goods/query.json'/>",
					"data" : function(d) {
						return $.extend({}, d, {
							"entity.name" : $('#name').val(),"entity.shelf":$('#shelf').val(),
							"useDatatables":"true"
						});
					}
				},
				"columns" : [ {
					"data" : "id"
				},{
					"data" : "name"
				}, {
					"data" : "spec"
				},{
					"data" : "price"
				},{
					"data" :"pointPrice"
				},{
					"data" : "moneyPrice"
				},{
					"data" : "voucher"
				},{
					"data" :"benefitPoints"
				},{
					"data" : "stockQuantity"
				},{
					"data" : "shelf"
				},{
					"data" : "sequence"
				},{
					"data" : "id"
				} ],
				"columnDefs" : [ {
					"render" : function(data, type, row) {
						return "<input type='checkbox' name='ids' value='"+data + "'/>";
					},
					"targets" : 0
				},{
					"render" : function(data, type, row) {
						return data?"是":"否";
					},
					"targets" : 9
				},{
					"render" : function(data, type, row) {
						var render="<a class=\"btn btn-success default editBtn\""+
						"href=\"<c:url value='/mall/goods/edit.html?id="+data+"' /> \">"+
								"<span class=\"glyphicon glyphicon-wrench\" aria-hidden=\"true\"></span>修改</a>&nbsp;"+
								"<a class=\"ajaxLink btn btn-danger default\" data-role=\"delete\""+
								"href=\"<c:url value='/mall/goods/remove.json?id="+data+"' /> \"><span class=\"glyphicon glyphicon-trash\" aria-hidden=\"true\"></span>删除</a>";
						return render;
					},
					"targets" : 11
				} ]
			});
			$('#mainTable tbody').on('click', 'tr', function() {
				if ($(this).hasClass('selected')) {
					$(this).removeClass('selected');
					$(this).find("input[type='checkbox']").prop("checked", false);
				} else {
					dt.$('tr.selected').removeClass('selected');
					$(this).addClass('selected');
					$(this).find("input[type='checkbox']").prop("checked", true);
				}
			});
			$("#searchDT").click(function(e) {
				dt.draw();
			});
			$(".addBtn").click(
					function(e) {
						e.preventDefault();
						e.stopPropagation();
						$("#myModal").load(
								"<c:url value='/mall/goods/edit.html'/>",
								function(e) {
									$('#myModal').modal({
										backdrop : "static"
									});
								});
					});
			$("#mainTable tbody").delegate(".editBtn","click",function(e){
				e.preventDefault();
				e.stopPropagation();
				var url=$(this).attr("href");
				$("#myModal").load(url,function(e) {
							$('#myModal').modal({
								backdrop : "static"
							});
						});
			});
			$("#dataList").delegate("a.ajaxLink","click",function(e){
				e.preventDefault();
				e.stopPropagation();
				var method="POST";
				if($(this).attr("data-role")){
					method="DELETE";
					if(!window.confirm("商品档案删除后无法恢复,是否继续?")){
						return false;
					}
				}				
				var url=$(this).attr("href");
				$.ajax({url:url,method:method,complete:function(xhr,ts){
					if(xhr.status>=200 && xhr.status<300){
						var m = $.parseJSON(xhr.responseText);
						if(m.flag=="0"){
							$("#winBody").empty().html("操作成功");
							$("#winModal").modal({backdrop:"static",show:true});
							window.setTimeout(function() {
								$("#searchDT").click();
							}, 1500);
						}else{
							$("#winBody").empty().html("操作失败").addClass("text-danger");
							$("#winModal").modal({backdrop:"static",show:true});
						}
					}else{
						$("#winBody").empty().html("操作失败").addClass("text-danger");
						$("#winModal").modal({backdrop:"static",show:true});
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
