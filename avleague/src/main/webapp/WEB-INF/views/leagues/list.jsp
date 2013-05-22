<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Volleyball Leagues</title>
</head>
<body>
<c:choose>
  <c:when test="${not empty leagues}">
      <c:forEach items="${leagues}" var="league">
      	<p>${league.name}</p>
      	<p>${league.startDate}</p>
      	<p>${league.endDate}</p>
      	<ul>
      	<c:forEach items="${league.teams}" var="team">
        <li>
	        <p>${team.name}</p>
	        <ul>
	        	<c:forEach items="${team.players}" var="player">
	        		<li>${player.name}</li>        
	        	</c:forEach>
	        </ul>
        </li>        
        </c:forEach>
        </ul>
      </c:forEach>
      <hr/>
      <p>${team.nodeId}</p>
      <p>${team.name}</p>
  </c:when>
  <c:otherwise>
    <h2>No leagues were populated.</h2>
  </c:otherwise>
</c:choose>
</body>
</html>