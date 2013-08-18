<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

<table border="1">
	<c:if test="${not empty match}">
    	<tr valign="top">
			<td>
				<p>				
					<strong><a href="${cp}/leagues/${leagueName}/levels/${levelName}/pools/${poolName}/matches/${match.name}">${match.name} - ${match.nodeId}</a></strong>
				</p>
				<p>Between <strong>${match.teamA.name}</strong> V <strong>${match.teamB.name}</strong></p>
				<p>
				at <fmt:formatDate type="date" value="${match.time}" />
					<strong><fmt:formatDate type="time" value="${match.time}"  timeStyle="short"/></strong>
				</p>
				<p>on <strong>${match.playedOnCourt.name}<strong></p>
			</td>
		</tr>
	</c:if>
	<c:forEach items="${matches}" var="match">
		<tr valign="top">
			<td>
				<p>				
					<strong><a href="${cp}/leagues/${leagueName}/levels/${levelName}/pools/${poolName}/matches/${match.name}">${match.name} - ${match.nodeId}</a></strong>
				</p>
				<p>Between <strong>${match.teamA.name}</strong> V <strong>${match.teamB.name}</strong></p>
				<p>
				at <fmt:formatDate type="date" value="${match.time}" />
					<strong><fmt:formatDate type="time" value="${match.time}"  timeStyle="short"/></strong>
				</p>
				<p>on <strong>${match.playedOnCourt.name}<strong></p>
			</td>
		</tr>
	</c:forEach>
</table>