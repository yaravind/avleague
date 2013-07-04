<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Matches</title>
</head>
<body>

<f:form action="new" method="POST" commandName="newMatch">
<f:label path="name">Pool </f:label>
<f:input path="name" />
<br/>
<f:label path="teamA.nodeId">Team A </f:label>
<f:input path="teamA.nodeId" />
<br/>
<f:label path="teamB.nodeId">Team B </f:label>
<f:input path="teamB.nodeId" />
<br/>
<input type="submit" value="Create"/>
</f:form>
</body>
</html>