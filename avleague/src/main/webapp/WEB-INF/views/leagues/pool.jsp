<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Pool - ${poolName}</title>
</head>
<body>
	<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />
	<a href="${cp}/leagues/">Home</a>
	<p>
		<big>Teams</big>
	</p>
	<ul>
		<c:forEach items="${pool.teams}" var="team">
			<li>${team.name}-${team.nodeId}</li>
		</c:forEach>
	</ul>

	<p>
		<big>Matches</big>
	</p>
	<jsp:include page="matches/matchDetails.jsp"></jsp:include>
</body>
</html>