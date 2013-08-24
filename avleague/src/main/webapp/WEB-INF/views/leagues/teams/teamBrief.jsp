<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- <%
out.println(request.getAttribute("teams"));
%> --%>
<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

<ul>
	<c:forEach items="${team.players}" var="player">
		<li>${player.name}-${player.nodeId }</li>
	</c:forEach>
</ul>
