<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Add awards for league</title>
</head>
<body>
	<c:set var="cp" value="${pageContext.request.contextPath}" scope="application" />
	
	<form action="${cp}/leagues/${leagueName}/awards" method="POST">
		<label for="awardFor">Award Name: </label>
		<input type="text" name="awardFor" /> <br />
		
		<label for="unitPrice">Unit Price: </label>
		<input type="text" name="unitPrice" /> <br />
		
		<label for="quantity">Quantity: </label>
		<input type="text" name="quantity" /> <br />
		<input type="submit" value="Add Award" />
	</form>
</body>
</html>