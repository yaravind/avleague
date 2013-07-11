<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>New league</title>
</head>
<body>
<f:form action="new" method="POST" commandName="newLeague">
<f:label path="name">Provide the name of the league: </f:label>
<f:input path="name" />
<br/>
<f:label path="startDate">When does the league start? </f:label>
<f:input path="startDate" />
<br/>
<f:label path="endDate">When does the league end? </f:label>
<f:input path="endDate" />
<br/>
<%-- <f:label path="endDate">When does the league end? </f:label>
<f:input path="endDate" />
<br/>
<f:label path="venueName">Where is the league being organized? The name of the venue or park. </f:label>
<f:input path="venueName" />
<br/>
<f:label path="courtName1">Name of the court at the venue</f:label>
<f:input path="courtName1" />
<br/>
<f:label path="courtName2">Name of the court at the venue</f:label>
<f:input path="courtName2" />
<br/>
<f:label path="courtName3">Name of the court at the venue</f:label>
<f:input path="courtName3" />
<br/> --%>
<input type="submit" value="Create"/>
</f:form>
</body>
</html>