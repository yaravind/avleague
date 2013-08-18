<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="false"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Levels for League: ${league.name}</title>
</head>
<body>

	<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

	<a href="/avl">Home</a>
	<br />
	<p>
		Levels for League <big>${league.name}</big>
	</p>

	<c:choose>
		<c:when test="${not empty league.allLevels}">
			<ul>
				<c:forEach items="${league.allLevels}" var="level">
					<li>
						<p>${level.name}-${level.nodeId}</p> 
						<c:choose>
							<c:when test="${not empty level.pools}">
								<ul>
									<c:forEach items="${level.pools}" var="pool">
										<li>Pool: ${pool.name }- ${pool.nodeId }</li>
									</c:forEach>
								</ul>
							</c:when>
							<c:otherwise>
								No pools created.
							</c:otherwise>
						</c:choose>
					</li>
				</c:forEach>
			</ul>
		</c:when>
		<c:otherwise>
			<p>No levels found.</p>
		</c:otherwise>
	</c:choose>
</body>
</html>