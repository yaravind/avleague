<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>${teamName}. Registration complete.</title>
</head>
<body>
	<a href="/avl">Home</a>
	<br />
	<ul>
		<c:forEach items="${playerList}" var="p">
			<li><c:choose>
					<c:when test="${p.captain}">
						<strong>${p.name} (Captain)</strong>
					</c:when>
					<c:otherwise>
					${p.name}
				</c:otherwise>
				</c:choose></li>
		</c:forEach>
	</ul>
	<p>
		Congratulations <strong>${designatedCaptain}</strong>. You have successfully registered your <strong>${teamName}</strong> team. Please check your
		email on next steps.
	</p>
</body>
</html>