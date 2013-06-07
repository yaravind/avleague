<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Players of ${newTeamName}</title>
</head>
<body>

<h1>${newTeamName}</h1>

<%-- <%
java.util.List<com.aravind.avl.controller.PlayerView> o = (java.util.List<com.aravind.avl.controller.PlayerView>) request.getAttribute("playingEight");
java.util.Iterator<com.aravind.avl.controller.PlayerView> it= o.iterator();
while(it.hasNext())
{
	com.aravind.avl.controller.PlayerView pv=it.next();
	out.println(pv.getName());
	out.println("<br/>"+pv.getPlayerId());
}
%>
 --%>
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
<form action="end" method="POST">
<input type="submit" value="Submit">
</form>

</body>
</html>