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
	<p>
		The <strong>${league.name}</strong> is being played at the below venues. Use the below form to add one more venue.
	
		<ul>
			<c:forEach items="${league.playedAt}" var="venue">
				<li>${venue.name }</li>
			</c:forEach>
		</ul>
	</p>
	
	<form action="/avl/leagues/${league.name}/venues" method="post">
		<input type="hidden" name="leagueName" value="${league.name}" />

		<label for="venueName">Where is the league being organized? The name of the venue or park. </label>
		<input name="venueName" />
		<br />
		<label for="courtName1">Name of the court at the venue: </label>
		<input name="courtName1" />
		<br />
		<label for="courtName2">Name of the court at the venue: </label>
		<input name="courtName2" />
		<br />
		<label for="courtName3">Name of the court at the venue: </label>
		<input name="courtName3" />
		<br />
		<input type="submit" value="Add Venue" />
	</form>
</body>
</html>