<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="selectModalLabel">${parameter.entity.name}商品价格与阈值</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<div class="table-responsive">
						<table class="table table-striped table-bordered table-condensed"
							id="goodsTable">
							<thead>
								<tr>
									<th>货物名称</th>
									<th>价格</th>
									<th>升级阈值</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
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


	<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
		var langUrl = "<c:url value='/resources/js/datatables-plugins/i18n/Chinese.json'/>";
		var dt = $("#goodsTable")
				.DataTable(
						{
							language : {
								url : langUrl
							},
							"processing" : true,
							"serverSide" : true,
							"searching" : false,
							"lengthChange" : true,
							"ordering" : false,
							"pageLength" : 20,
							"ajax" : {
								"url" : "<c:url value='/price/${parameter.entity.id}/goods.json'/>",
								"data" : function(d) {
									return $.extend({}, d, {
										"useDatatables":"true"
									});
								}
							},
							"columns" : [ {
								"data" : "goodsName"
							},{
								"data" : "price"
							},{
								"data" :"threshold"
							} ],
							"columnDefs" : [
							            	{
												"render" : function(data, type, row) {
													return "<span class='goodsName'>"+data+"</span>";
												},
												"targets" : 0
											}
									]
						});
		$('#goodsTable tbody').on('click', 'tr', function() {
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
			
		
		$("#goodsTable").delegate("button.addGoodsBtn","click",function(e) {
					var id=$(this).attr("data-id");
					var goodsName=$(this).parents("TR").find("span.goodsName").text();
					var tb=$("#itemsBody"),trs=tb.find("TR");
					var newLine=null;
					if(trs){
						trs.each(function(i){
							if($(this).attr("data-id")==id){
								newLine=$(this);
							}
						});
					}
					if(newLine){
						var qs=newLine.find("INPUT[name='quantitys']"),oldQs=qs.val();
						if(!oldQs){
							oldQs=0;
						}
						qs.val(parseInt(oldQs)+1);
					}else{
						newLine=$("<TR>").attr("data-id",id);
						var ftd=$("<TD>").text(goodsName);
						$("<INPUT>").attr("type","hidden").attr("name","goodsIds").val(id).appendTo(ftd);
						ftd.appendTo(newLine);
						var std=$("<TD>");
						$("<input>").attr("type","number").attr("name","quantitys").attr("min","0").val(1).appendTo(std);
						std.appendTo(newLine);
						var ttd=$("<TD>").append($("<A>").addClass("btn btn-danger default removeItem").text("删除").attr("data-role","delete").attr("href","#")
								.append($("<span>").addClass("fa fa-trash fa-fw").attr("aria-hidden","true"))).appendTo(newLine);
						newLine.appendTo(tb);
					}
		});
	
	}
</script>