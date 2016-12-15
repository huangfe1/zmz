<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<style>
<!--
-->
.none {
	display: none;
}
</style>
<div class="modal-dialog modal-lg" role="document">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title text-primary" id="myModalLabel">物流规则信息编辑</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<form action="<c:url value='/logistics/edit.json'/>" name="editForm" enctype="multipart/form-data"
							class="form-horizontal"  id="editForm" method="post">
				<div class="row">				
					<div class="col-md-12 col-xs-12">
					<div class="panel panel-primary">
							<div class="panel-heading">
								基本信息
							</div>
							<div class="panel-body">	
						
							<input type="hidden" name="id" value="${parameter.entity.id}">
							<div class="form-group">
								<label for="provinces" class="col-sm-2 control-label">地区</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="provinces" tabIndex="10"
										name="provinces" value="${parameter.entity.provinces}"
										placeholder="湖南省+四川省+安徽省">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>
							

							


							
							<!-- 重量区间 -->
							<div class="form-group">
								<label for="weights" class="col-sm-2 control-label">重量区间</label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="weights" tabIndex="13"
										name="weights" value="${parameter.entity.weights}"
										placeholder="重量阀值_重量阀值_首重大小">
								</div>
								<div class="col-md-4 col-xs-4 text-error"></div>
							</div>

								<!-- 价格区间 -->
								<div class="form-group">
									<label for="prices" class="col-sm-2 control-label">价格区间</label>
									<div class="col-sm-8">
										<input type="text" class="form-control" id="prices" tabIndex="13"
											   name="prices" value="${parameter.entity.prices}"
											   placeholder="区间价_区间价_首重价_超出价/Kg">
									</div>
									<div class="col-md-4 col-xs-4 text-error"></div>
								</div>




						<!-- </form>-->
						</div>
						</div>
					</div>

				</div>
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
				<div class="col-md-6 col-xs-12">
					<button type="button" class="btn btn-success btn-block" form="editForm" tabIndex="27"
						id="saveBtn" name="saveBtn" value="saveBtn" tabindex="4"
						data-loading-text="正在提交......">
						<span class="glyphicon glyphicon-save">&nbsp;</span>保存
					</button>
				</div>
			</div>
		</div>
	</div>
</div>


<jsp:include page="/WEB-INF/view/common/form.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
		initRmLevel();
		rmLevel();
	});
	
	function rmLevel(){
		$(":radio").click(function(){
			  var type=$(this).val();
			  hideInput(type);
		});
	}
	
	function initRmLevel(){
		var type=$("input:radio:checked").val();
		if(type!=undefined){
			hideInput(type);
		}
		}
	
	function hideInput(type){
		  $("#levelTable tr.lc").each(function (){
				if($(this).attr("tag")!=type){
				$(this).find("input").attr("disabled","disabled"); 
				$(this).css("display","none");
				}else{
					$(this).find("input").removeAttr("disabled"); 
					$(this).css("display","");
				}
		  });
	}
	

	function init() {
		$("#editName").focus().select();
		var btn = null;
		$("#editForm").validate({submitHandler : function(form) {
						$(form).ajaxSubmit({
									beforeSubmit : function(arr, $form, options) {
										btn.button("loading");
									},
									success : function(responseText,
											statusText, xhr, $form) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										if(m.flag=="0"){
											alert("保存成功");
										$(".quitBtn").click();
											$("#search").click();
										}else{
											alert("保存失败");
										}
										
									},
									error : function(xhr, textStatus,
											errorThrown) {
										var m = $.parseJSON(xhr.responseText);
										btn.button("reset");
										alert("保存失败");
									}
								});
					},
					rules : {
						provinces : {
							required : true
						},
						weights:{
							required: true
						},
						prices:{
							required: true
						},
					},
					onkeyup : false,
					messages : {
						provinces : {
							required : "地区名称必须填写"
						},
						weights:{
							required:"重量区间必须填写"
						},
						prices:{
							required:"价格区间必须填写"
						},
					},
					focusInvalid : true,
					errorClass : "text-danger",
					validClass : "valid",
					errorElement : "small",
					errorPlacement : function(error, element) {
						error.appendTo(element.closest("div.form-group")
								.children("div.text-error"));
					}
				});
		$("#editForm").find("input[type='checkbox']").change(function(e){
			var $t=$(this);
			var next=$t.next("input[type='hidden']");
			$t.prop("checked")?next.val(1):next.val(0);
		});
		

	
		
		$("#saveBtn").click(function(e) {
			btn = $(this).button();
			$(document.forms["editForm"]).submit(); 
			/* $('#uploadModal').modal({
				backdrop : "static"
			});
			
			setInterval("getProgress()",1000); */
		});
	
		
	}

	
</script>
