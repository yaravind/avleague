<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>AVL Leagues</title>
</head>
<body>

<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

	<a href="/avl">Home</a>
	<br />
	<c:choose>
		<c:when test="${not empty leagues}">
			<table border="1">
				<tr valign="top">
					<c:forEach items="${leagues}" var="league">
						<td>
							<p><strong>${league.name} - ${league.nodeId}</strong></p>
							<p>
								<em><fmt:formatDate type="date" value="${league.startDate}" /></em> - <em><fmt:formatDate type="date" value="${league.endDate}" /></em>
							</p>
							<p>
							<a href="${cp}/leagues/${league.nodeId}/matches/new">Create matches</a>
							<br />
							<a href="${cp}/leagues/${league.name}/matches/">List matches</a>							
							<br />
							<!-- c:out value="${league.playedAt}"/ -->
								<c:if test="${not empty league.playedAt}">
									<ul>
										<c:forEach items="${league.playedAt}" var="venue">
											<li>Played at <strong>${venue.name} - ${venue.nodeId}</strong></li>
										</c:forEach>
									</ul>
								</c:if>
								<a href="${cp}/leagues/${league.name}/venues/">Add Venue</a>
							</p>
							<p>
							<strong>Levels</strong><br />
							<c:if test="${not empty levelsByLeague[league.nodeId]}">
									<ul>
										<c:forEach items="${levelsByLeague[league.nodeId]}" var="level">
											<li><strong>${level.name} - ${level.nodeId}</strong></li>
										</c:forEach>
									</ul>
								</c:if>
								<a href="${cp}/leagues/${league.name}/levels/">Add Level</a>
							</p>
							 <c:forEach items="${league.teams}" var="team">
								<li>
									<p>${team.name} - ${team.nodeId}</p>
									<ul>
										<c:forEach items="${team.players}" var="player">
											<li>${player.name} - ${player.nodeId }</li>
										</c:forEach>
									</ul>
								</li>
							</c:forEach>
						</td>
					</c:forEach>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<h2>No leagues were populated.</h2>
		</c:otherwise>
	</c:choose>
</body>
</html>