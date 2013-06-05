<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<p>Did you participate in earlier leagues?</p>
<form action="registration/start" method="post">
<input type="radio" name="participatedInEarlierLeague" checked="checked" value="Yes"/>Yes
<input type="radio" name="participatedInEarlierLeague"  value="No"/>No
<input type="submit" value="Submit">
</form>
</body>
</html>