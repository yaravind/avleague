<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />
<a href="${cp}/leagues/">Home</a>
<table border="1">
	<c:if test="${not empty match}">
		<tr valign="top">
			<td>
				<p>
					<strong><a href="${cp}/leagues/${leagueName}/levels/${levelName}/pools/${poolName}/matches/${match.name}">${match.name} - ${match.nodeId}</a></strong>
				</p>
				<p>
					<p>Between</p> 
					<p>Team A: <strong>${match.teamA.name}</strong></p>
				<p>Team B: <strong>${match.teamB.name}</strong></p>
				</p>
				<p>
					at
					<fmt:formatDate type="date" value="${match.time}" />
					<strong><fmt:formatDate type="time" value="${match.time}" timeStyle="short" /></strong>
				</p>
				<p>
					on <strong>${match.playedOnCourt.name}</strong>
				</p>
				
				<p>
				<form action="${cp}/leagues/${leagueName}/levels/${levelName}/pools/${poolName}/matches/${match.name}" method="post">
					<c:choose>
						<c:when test="${empty match.winner }">
							<label for="winner">Choose winner: </label>
							<select name="winner">
								<option value="">Choose winner</option>
								<option value="${match.teamA.nodeId}">${match.teamA.name}</option>
								<option value="${match.teamB.nodeId}">${match.teamB.name}</option>
							</select>
							<br />
							<label for="teamAScore">Team A Score</label>
							<input type="text" name="teamAScore"/>
							<br />
							<label for="teamBScore">Team B Score:</label>
							<input type="text" name="teamBScore"/>
						</c:when>
						<c:otherwise>
							<p><strong>Winner:</strong> ${match.winner.name}. Score: <strong>${match.teamAScore} - ${match.teamBScore}</strong></p>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${empty match.mvp }">
							<label for="mvp">Choose MVP:</label>
							<select name="mvp">
								<optgroup label="${match.teamA.name}">
									<c:forEach items="${match.teamAPlaying6}" var="player">
										<option value="${player.nodeId }">${player.name}</option>
									</c:forEach>
								</optgroup>
								<optgroup label="${match.teamB.name}">
									<c:forEach items="${match.teamBPlaying6}" var="player">
										<option value="${player.nodeId }">${player.name}</option>
									</c:forEach>
								</optgroup>
							</select>
							<br />
						</c:when>
						<c:otherwise>
							<p><strong>MVP</strong>: ${match.mvp.name}</p>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${empty match.comments }">
							<label for="comments">Comments: </label>
							<textarea rows="5" cols="120" name="comments"></textarea>
						</c:when>
						<c:otherwise>
							<p>Comments: ${match.comments }</p>
						</c:otherwise>
					</c:choose>
					<br /> 
					<label for="subtitutions">Substitutions: </label> 
					<input type="text" name="subtitutions" value="TODO ability to add substitutions" size="100" /> 
					<br /> <input type="submit" value="Submit">
				</form>
				</p>
			</td>
		</tr>
	</c:if>
	<c:forEach items="${matches}" var="match">
		<tr valign="top">
			<td>
				<p>
					<strong><a href="${cp}/leagues/${leagueName}/levels/${levelName}/pools/${poolName}/matches/${match.name}">${match.name} - ${match.nodeId}</a></strong>
				</p>
				<p>
					Between <strong>${match.teamA.name}</strong> V <strong>${match.teamB.name}</strong>
				</p>
				<p>
					at
					<fmt:formatDate type="date" value="${match.time}" />
					<strong><fmt:formatDate type="time" value="${match.time}" timeStyle="short" /></strong>
				</p>
				<p>
					on <strong>${match.playedOnCourt.name}<strong>
				</p>
			</td>
		</tr>
	</c:forEach>
</table>