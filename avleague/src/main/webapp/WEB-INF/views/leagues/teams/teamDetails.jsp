<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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

	<h1>${team.name}-${team.nodeId}</h1>

	<h2>Leagues contested in</h2>
	<ul>
		<c:forEach items="${leaguesContestedIn}" var="league">
			<li>${league}</li>
		</c:forEach>
	</ul>

	<h2>Levels reached per league</h2>
	<ul>
		<c:forEach items="${leagues}" var="league">
			<li>
				<p>${league.name}</p>
				<ul>
					<c:forEach items="${league.allLevels}" var="level">
						<li>${level.name}</li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>

	<h2>Aliases</h2>
	<ul>
		<c:forEach items="${team.aliases}" var="alias">
			<li>${alias}</li>
		</c:forEach>
	</ul>
	<h2>Players</h2>
	<ul>
		<c:forEach items="${team.players}" var="player">
			<li>
				<h3>${player.name}-${player.nodeId }</h3>
				<p>${player.phoneNumber} <c:if test="${not empty player.email}">, ${player.email}</c:if>
					</p>			
				 <h4>Contested in</h4>
				<ul>
					<c:forEach items="${player.playedforInLeague}" var="playedForInLeague">
						<li>${playedForInLeague.inLeague.name } <c:if test="${playedForInLeague.asCaptain}">
								<em> as Captain.</em>
							</c:if> <fmt:formatDate type="date" value="${playedForInLeague.during}" />
						</li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>
</body>
</html>