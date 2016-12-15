<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	String ctx = request.getContextPath();
	request.setAttribute("ctx", ctx);
	String weburl = request.getScheme() + "://" + request.getServerName() + ":"
			+ request.getServerPort() + ctx + "/";
	request.setAttribute("weburl", weburl);
	request.setAttribute("keywords", "dreamer");
%>
