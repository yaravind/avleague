<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>New match for Pool ${pool.name}</title>
</head>
<body>
	<p>
		Add new match for Pool <strong>${pool.name}</strong> of Level <strong>${level.name}</strong>
	</p>
	<f:form action="matches" method="POST">
		<%-- commandName="newMatch" --%>
		<label for="teamA">Team A </label>

		<select name='teamA'>
			<c:forEach items="${pool.teams}" var="team">
				<option value="${team.nodeId}">${team.name}-${team.nodeId}</option>
			</c:forEach>
		</select>
		<br />
		<label for="teamAPlaying6">Playing 6 of Team A:</label>
		<select name="teamAPlaying6" multiple="multiple" size="10">
			<c:forEach items="${teamToPlayers}" var="entry">
				<optgroup label="${entry.key}">
					<c:forEach items="${entry.value}" var="player">
						<option value="${player.nodeId }">${player.name}</option>
					</c:forEach>
				</optgroup>
			</c:forEach>
		</select
		<br />
		<label for="teamBPlaying6">Playing 6 of Team B:</label>	
		<select name="teamBPlaying6" multiple="multiple" size="10">
			<c:forEach items="${teamToPlayers}" var="entry">
				<optgroup label="${entry.key}">
					<c:forEach items="${entry.value}" var="player">
						<option value="${player.nodeId }">${player.name}</option>
					</c:forEach>
				</optgroup>
			</c:forEach>
		</select>
		<br />
		<label for="teamB">Team B </label>
		<select name='teamB'>
			<c:forEach items="${pool.teams}" var="team">
				<option value="${team.nodeId}">${team.name}-${team.nodeId}</option>
			</c:forEach>
		</select>
		<br />
		<label for="venueAndCourt">Court where this match will be played on </label>
		<select name='venueAndCourt'>
			<c:forEach items="${venues}" var="venue">
				<c:forEach items="${venue.courts}" var="court">
					<option value="${venue.name} - ${court.name}">${venue.name}- ${court.name}</option>
				</c:forEach>
			</c:forEach>
		</select>
		<br />

		<label for="time">Date and time for the match (MM-dd-yyyy HH:mm) HH = 0-23 </label>
		<input name="time" />
		<br />
		<input type="submit" value="Add Match" />
	
	
	
	</f:form>
</body>
</html>