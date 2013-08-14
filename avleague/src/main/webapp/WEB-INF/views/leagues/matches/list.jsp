<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Matches for League ${league.name}</title>
</head>
<body>

	<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

	<a href="/avl">Home</a>
	<br />
	<h3>Matches for League ${league.name}</h3>

<c:forEach items="${levels}" var="level">
	<h2>${level.name }</h2>
		<c:choose>
			<c:when test="${not empty level.fixtures}">
				<table border="1">
					<c:forEach items="${level.fixtures}" var="match">
						<tr valign="top">
							<td>
								<p>
									<strong><a href="${cp}/leagues/${league.name}/matches/${match.name}">${match.name} - ${match.nodeId}</a></strong>
								</p>
								<p>
									Time: <fmt:formatDate type="time" value="${match.time}" />
									<%-- 
									${match.level}
									<em></em> - <em><fmt:formatDate type="date" value="${league.endDate}" /></em> --%>
								</p>
								<p>Pool: ${match.pool.name}</p>
								<p>Court: ${match.playedOnCourt.name}</p>
							</td>
						</tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
				<p>No matches found.</p>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</body>
</html>