<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

<html>
<head>
<title>Awards for ${league.name}</title>
</head>
<body>
	<form action="${cp}/leagues/${league.name}/awards/assign" method="post">
		<c:if test="${not empty league.teamAwards}">
			<p>
				<strong>Team Awards</strong>
			</p>
			<c:forEach items="${league.teamAwards}" var="award">
				<label>${award.awardFor}:</label>
				<select name="${award.awardFor}">
					<c:forEach items="${league.teams}" var="team">
						<option value="${team.nodeId}">${team.name}</option>
					</c:forEach>
				</select>
				<br />
			</c:forEach>
		</c:if>
		<c:if test="${not empty league.individualAwards}">
			<p>
				<strong>Individual Awards</strong>
			</p>
			<ul>
				<c:forEach items="${league.individualAwards}" var="award">
					<label>${award.awardFor}:</label>
					<select name="${award.awardFor}">
						<c:forEach items="${teamToPlayers}" var="entry">
							<optgroup label="${entry.key}">
								<c:forEach items="${entry.value}" var="player">
									<option value="${player.nodeId }">${player.name}</option>
								</c:forEach>
							</optgroup>
						</c:forEach>
					</select>
					<br />
				</c:forEach>
			</ul>
		</c:if>
		<input type="Submit" />
	</form>
</body>
</html>