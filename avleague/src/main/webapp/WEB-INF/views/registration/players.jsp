<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Players of ${teamName}</title>
<script type="text/javascript">
	function handleClick(myRadio, index) {
		myRadio.value = document.getElementsByName('newPlayer' + index)[0].value;
	}
</script>
</head>
<body>

	<f:form action="newTeam" method="post">
		<h2>${teamName}</h2>
		<input type="hidden" name="teamName" value="${teamName}"/>
		<label for="newTeamName"> Register with new name: <input type="text" name="newTeamName"></label>
		<br />
		<br />
		<table border="1">
			<thead>
				<tr>
					<th>Full Name</th>
					<th>Designate as Captain</th>
					<th>Participated in the below previous Leagues</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${players}" var="playerView">
					<tr>
						<td><input type="checkbox" name="players" value="${playerView.nodeId}" />${playerView.name}</td>
						<td><input type="radio" name="isCaptain" value="${playerView.name}" /></td>
						<td>Show list of previous leagues that this player has played <%-- TODO <c:forEach items="${playerView.participatedInLeagueNames}" var="leagueName">					
								${leagueName}<br/>
							</c:forEach> --%>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="3"><strong>I've new few players this time.</strong></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer1"> Full Name: <input type="text" name="newPlayer1" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 1);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer2"> Full Name: <input type="text" name="newPlayer2" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 2);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer3"> Full Name: <input type="text" name="newPlayer3" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 3);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer4"> Full Name: <input type="text" name="newPlayer4" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 4);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer5"> Full Name: <input type="text" name="newPlayer5" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 5);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer6"> Full Name: <input type="text" name="newPlayer6" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 6);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer7"> Full Name: <input type="text" name="newPlayer7" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 7);" /></td>
				</tr>
				<tr>
					<td colspan="2"><label for="newPlayer8"> Full Name: <input type="text" name="newPlayer8" />
					</label></td>
					<td><input type="radio" name="isCaptain" value="${playerView.name}" onclick="handleClick(this, 8);" /></td>
				</tr>
				<tr>
					<td colspan="3"><input type="submit" value="Submit"></td>
				</tr>
			</tbody>
		</table>
	</f:form>
</body>
</html>