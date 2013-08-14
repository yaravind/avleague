<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Atlanta Volleyball League - Where fun leads to charity.</title>
</head>
<body>
	<ul>
		<li>Leagues
			<ul>
				<li><a href="leagues/list">List all leagues.</a></li>
				<li><a href="leagues/new">Organize new league.</a></li>
				<li><a href="leagues/delete">Cancel an existing league.</a></li>
			</ul>
		</li>
		<li>Registrations
			<ul>
				<li><a href="registration">Register your team.</a></li>
			</ul>
		</li>
		<li>Administration
			<ul>
				<li><a href="admin/populate">Import previous leagues.</a></li>
				<!-- <li><a href="leagues/matches/new">Add match to a league</a></li> -->
			</ul>
		</li>
	</ul>
</body>
</html>
