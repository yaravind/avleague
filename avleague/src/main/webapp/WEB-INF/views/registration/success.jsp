<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>${newTeamName}. Registration complete.</title>
</head>
<body>
<%-- <%
java.util.List<com.aravind.avl.domain.Player> o = (java.util.List<com.aravind.avl.domain.Player>) request.getAttribute("playingEight");
java.util.Iterator<com.aravind.avl.domain.Player> it= o.iterator();
while(it.hasNext())
{
	com.aravind.avl.domain.Player pv=it.next();
	out.println(pv.getName()+"&nbsp;"+pv.getNodeId()+"<br/>");
}
%> --%>
<ul>
	<c:forEach items="${playingEight}" var="p">
		<li>
			<c:choose>
				<c:when test="${p.captain}">
					<strong>${p.name} (Captain)</strong>
				</c:when>
				<c:otherwise>
					${p.name}
				</c:otherwise>
			</c:choose>
		</li>
	</c:forEach>					
</ul>
<p>Congratulations <strong>${designatedCaptain}</strong>. You have successfully registered your <strong>${newTeamName}</strong> team. Please check your email on next steps.</p>
</body>
</html>