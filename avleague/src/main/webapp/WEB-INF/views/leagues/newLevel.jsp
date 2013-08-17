<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
	<p>
		The <strong>${league.name}</strong> has below levels. Use the below
		form to add one more level.

		<ul>
			<c:forEach items="${league.allLevels}" var="level">
				<li>${level.name}- ${level.nodeId}</li>
			</c:forEach>
		</ul>
	</p>

	<form action="/avl/leagues/${league.name}/levels" method="post">
		<input type="hidden" name="leagueName" value="${league.name}" /> <label
			for="levelName">New level </label> <input name="levelName" /> <label
			for="levelName">Will be played after completion of </label> <select
			name='beforeLevel'>
			<c:choose>
				<c:when test="${not empty league.allLevels}">
					<c:forEach items="${league.allLevels}" var="level">
						<option value="${level.nodeId}">${level.name}</option>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<option value="-1" selected="selected">&nbsp;</option>
				</c:otherwise>
			</c:choose>
		</select> <br /> <input type="submit" value="Add Level" />
	</form>
</body>
</html>