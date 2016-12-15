<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<div class="row">
	<div class="col-md-12">
		<ul class="pager">
			<c:choose>
				<c:when test="${currentPage==1}">
					<li class="disabled"><a rel="start">上一页</a></li>
				</c:when>
				<c:otherwise>
					<li><a href=" <c:url value='${prev}'/> " rel="prev">上一页</a></li>
				</c:otherwise>
			</c:choose>
			<li><a>共${totalRows}条 ${totalPages}页 当前第${currentPage}页</a></li>
			<c:choose>
				<c:when test="${currentPage==totalPages || totalPages==0}">
					<li class="disabled"><a rel="next">下一页</a></li>
				</c:when>
				<c:otherwise>
					<li><a href=" <c:url value='${next}'/> " rel="next">下一页</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>