<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Players of {playedInTeam.name}</title>
</head>
<body>
 <c:if test="${not empty playedInTeam.players}">
  	<form action="newTeam" method="post">
  	<ul>
      <c:forEach items="${playedInTeam.players}" var="player">
        <li>
	       <input type="checkbox" name="selectedPlayers"  value="${player.nodeId}"/> ${player.name}
        </li>        
        </c:forEach>
    </ul>
   <input type="submit" value="Submit">
    </form>
  </c:if>
</body>
</html>