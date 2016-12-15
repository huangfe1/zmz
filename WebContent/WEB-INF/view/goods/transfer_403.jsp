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
			<h4 class="modal-title text-primary" id="myModalLabel">无法完成转货请求</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				
				<div class="row">				
					<div class="col-md-12 col-xs-12">
					<div class="alert alert-danger" role="alert">${message},无法转货</div>
					</div>
				</div>
				</form>
			</div>
		</div>
		<div class="modal-footer">
			<div class="form-group">
				<div class="col-md-12 col-xs-12">
					<button type="button" class="btn btn-default btn-block quitBtn" tabIndex="26"
						id="quitBtn" data-dismiss="modal" name="quitBtn" value="login"
						tabindex="4" data-loading-text="正在返回......">
						<span class="glyphicon glyphicon-remove-sign">&nbsp;</span>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</div>


<jsp:include page="/WEB-INF/view/common/head.jsp"></jsp:include>
<jsp:include page="/WEB-INF/view/common/startbootstrap.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		init();
	});
	function init() {
	}
</script>
