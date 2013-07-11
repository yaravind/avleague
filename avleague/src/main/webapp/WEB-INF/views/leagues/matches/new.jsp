<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Matches</title>
</head>
<body>

	<f:form action="new" method="POST" commandName="newMatch">
		<f:label path="pool">Pool </f:label>
		<f:input path="pool" />
		<br />
		<f:label path="level">Level</f:label>
		<f:select path="level" items="${levels}" />
		<br />
		<f:label path="teamA.nodeId">Team A </f:label>
		<f:select path="teamA.nodeId" items="${teamsOfCurrentLeague}" />
		<br />
		<f:label path="teamB.nodeId">Team B </f:label>
		<f:select path="teamB.nodeId" items="${teamsOfCurrentLeague}" />
		<br />
		<f:label path="playedOnCourt">Court where this match will be played on </f:label>
		<f:select path="playedOnCourt" items="${courts}" />
		<br />
		<input type="submit" value="Create" />
	</f:form>
</body>
</html>