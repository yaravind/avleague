<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
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
							<p>
								<strong>${league.name} - ${league.nodeId}</strong>
							</p>
							<p>
								<em><fmt:formatDate type="date" value="${league.startDate}" /></em> - <em><fmt:formatDate type="date" value="${league.endDate}" /></em>
							</p>
							<p>
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
								<c:if test="${not empty league.allLevels}">
									<ul>
										<c:forEach items="${league.allLevels}" var="level">
											<li><strong>${level.name} - ${level.nodeId}</strong> <a href="${cp}/leagues/${league.name}/levels/${level.name}/pools">Add Pool</a> <c:if test="${not empty level.pools}">
													<ul>
														<c:forEach items="${level.pools}" var="pool">
															<li><a href="${cp}/leagues/${league.name}/levels/${level.name}/pools/${pool.name}">Pool ${pool.name} - ${pool.nodeId}</a></li>
															<ul>
																<li>
																	<form id="myform" method="post" action="${cp}/leagues/${league.name}/levels/${level.name}/pools/${pool.name}/matchForm">
																		<input type="submit" value="Create matches"/>
																		<%-- <a href="${cp}/leagues/${league.name}/levels/${level.name}/pools/${pool.name}/matches" onclick="this.parentNode.submit()">Create matches</a> --%>
																	</form>
																</li>
																<li><a href="${cp}/leagues/${league.name}/levels/${level.name}/pools/${pool.name}/matches/">List matches</a></li>
															</ul>
														</c:forEach>
													</ul>
												</c:if></li>
										</c:forEach>
									</ul>
								</c:if>
								<a href="${cp}/leagues/${league.name}/levels/">Add Level</a>
							</p> <c:forEach items="${league.teams}" var="team">
								<li>
									<p>${team.name}-${team.nodeId}</p>
									<ul>
										<c:forEach items="${team.players}" var="player">
											<li>${player.name}-${player.nodeId }</li>
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