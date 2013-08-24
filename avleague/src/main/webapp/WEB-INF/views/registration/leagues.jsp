<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Previous Leagues</title>
</head>
<body>
  <c:if test="${not empty pastLeagues}">
  	<form action="selectTeam" method="post">
  	<input type="hidden" name="participatedInEarlierLeague" value="${participatedInEarlierLeague}"/>
      <c:forEach items="${pastLeagues}" var="league">
      	<h2>${league.name}</h2>
      	<p>Commenced on <fmt:formatDate type="date" value="${league.startDate}" /> and ended on <fmt:formatDate type="date" value="${league.endDate}" /></p>
      	<c:forEach items="${league.teams}" var="team">
        	<p><input type="radio" name="selectedTeam"  value="${team.nodeId}"/> ${team.name}</p>
        </c:forEach>
      </c:forEach>
      <input type="submit" value="Submit">
      </form>
  </c:if>
</body>
</html>