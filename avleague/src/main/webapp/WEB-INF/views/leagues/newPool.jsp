<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Venue of the legaue.</title>
</head>
<body>
	<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />
	<a href="${cp}/leagues/">Home</a>
	<p>
		The <strong>${level.name}</strong> has below pools. Use the below form to add one more pool.

		<ul>
			<c:forEach items="${level.pools}" var="pool">
				<li>${pool.name}- ${pool.nodeId}</li>
				<c:if test="${not empty level.pools}">
					<ul>
						<c:forEach items="${pool.teams}" var="team">
							<li>${team.name}- ${team.nodeId}</li>
						</c:forEach>
					</ul>
				</c:if>
			</c:forEach>
		</ul>
	</p>

	<form action="/avl/leagues/${league.name}/levels/${level.name}/pools" method="post">
		<label for="poolName">New Pool </label> <input name="poolName" /> <br /> <label for="teams">Select the teams that belong to this Pool</label>
		<c:choose>
			<c:when test="${not empty league.teams}">
				<select name='teams' multiple="multiple" size="25">
					<c:forEach items="${league.teams}" var="team">
						<option value="${team.nodeId}">${team.name}- ${team.nodeId}</option>
					</c:forEach>
				</select>
			</c:when>
			<c:otherwise>
				<p>Add teams to the League before creating the pools.</p>
			</c:otherwise>
		</c:choose>
		<br /> <input type="submit" value="Add Pool" />
	</form>
</body>
</html>