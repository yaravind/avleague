<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />

<p>
	<c:if test="${not empty league.teamAwards}">
		<p>
			<strong>Team Awards</strong>
		</p>
		<ul>
			<c:forEach items="${league.teamAwards}" var="award">
				<li><strong>${award.awardFor}</strong> - ${award.nodeId}. (Awarded to <strong>${award.awardedTo.name}</strong>)</li>
			</c:forEach>
		</ul>
	</c:if>
</p>

<p>
	<c:if test="${not empty league.individualAwards}">
		<p>
			<strong>Individual Awards</strong>
		</p>
		<ul>
			<c:forEach items="${league.individualAwards}" var="award">
				<li><strong>${award.awardFor}</strong> - ${award.nodeId}. (Awarded to <strong>${award.awardedTo.name}</strong>)</li>
			</c:forEach>
		</ul>
	</c:if>
	
</p>
<p>
	<a href="${cp}/leagues/${league.name}/awards/awardForm">Add Award</a><br />
	<a href="${cp}/leagues/${league.name}/awards/assignForm">Assign Awards</a>
</p>