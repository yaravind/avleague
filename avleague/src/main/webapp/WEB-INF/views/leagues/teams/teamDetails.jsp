<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- <%
out.println(request.getAttribute("teams"));
%> --%>
<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

<p>
	<big>${team.name}-${team.nodeId}</big>
</p>
<p>${team.pool.name}</p>
<p>Previous name(s): ${team.previouslyKnownAs.name}</p>

<p>Players</p>
<ul>
	<c:forEach items="${team.players}" var="player">
		<li>${player.name}-${player.nodeId }</li>
	</c:forEach>
</ul>
