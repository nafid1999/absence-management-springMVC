<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../fragments/adminHeader.jsp" />

<div class="container">

	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<div class="container-fluid">

			<jsp:include page="../fragments/menu.jsp" />

		</div>
	</nav>


	<div>
		<h3>history of  authentication  for : ${username} </h3>
	</div>

	<div>



		<table class="table">
			<thead>
				<tr>
					<th scope="col">adresse IP</th>
					<th scope="col">Date de connexion</th>
					<th scope="col">login of connection : <th>

				</tr>
			</thead>
			<c:forEach items="${eventlist}" var="event">
			<c:if test="${event.typeEvenement== connexion_event}">
				
				<tr>
					
					<td><c:out value="${event.adresseIP}" /></td>
					<td><c:out value="lE ${event.dateHeure}" /></td>
					<td> <a href="#" class="bold h5 ">${event.details}</a> </td>


				</tr>
            	</c:if>
            
			</c:forEach>

		</table>
	</div>

	<jsp:include page="../fragments/adminfooter.jsp" />