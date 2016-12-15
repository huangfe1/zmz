<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/common.jsp"%>
<ul class="pager">
	<c:choose>
		<c:when test="${currentPage==1}">
			<li class="disabled"><a  rel="prev">首页</a></li>
			<li class="disabled"><a rel="start">上一页</a></li>
		</c:when>
		<c:otherwise>
		<li><a href=" <c:url value='${first}'/> " rel="prev">首页</a></li>
			<li><a href=" <c:url value='${prev}'/> " rel="prev">上一页</a></li>
		</c:otherwise>
	</c:choose>
	<li><a>共${totalRows}条 ${totalPages}页 当前第${currentPage}页</a></li>
	<c:choose>
		<c:when test="${currentPage==totalPages}">
			<li class="disabled"><a rel="next">下一页</a></li>
			<li class="disabled"><a rel="next">末页</a></li>
		</c:when>
		<c:otherwise>
			<li><a href=" <c:url value='${next}'/> " rel="next">下一页</a></li>
			<li><a href=" <c:url value='${end}'/> " rel="next">末页</a></li>
		</c:otherwise>
	</c:choose>
	<c:if test="${totalPages > 1}">
	<li>
      <input type="number" style="padding:0px;text-align:center;width:3.5em;" value="${currentPage}" min="1" max="${totalPages}" id="gotoPageNum" size="1" placeholder="">
      <a class="btn btn-default" href="${gotoPage}" onclick="gotoPage(this)">前往</a>
    </li>
   	</c:if>
</ul>
<script>
function gotoPage(t){
	t.href=t.href+document.getElementById("gotoPageNum").value;
}
</script>