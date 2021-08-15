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
		<h3>historiques de connexion </h3>
	</div>

	<div>



		<table class="table">
			<thead>
				<tr>
					<th scope="col">Login</th>
					<th scope="col">Role</th>
					<th scope="col">adresse IP</th>
					<th scope="col">Date de connexion</th>
					<th scope="col">pages <th>
					

				</tr>
			</thead>
			<c:forEach items="${eventlist}" var="event">
				<tr>
					<td><c:out value="${event.compte.login}" /></td>

					<td>
					<c:choose>
					  <c:when test="${event.compte.role.nomRole== 'ROLE_ADMIN'}">
					     <span class="badge bg-primary">ADMINISTRATEUR</span>
					  </c:when>
					
					  <c:otherwise>
					   <span class="badge bg-warning"><c:out value="${event.compte.role.nomRole}" /></span>
					  </c:otherwise>
					</c:choose>
					</td>
					<td><c:out value="${event.adresseIP}" /></td>
					<td><c:out value="lE ${event.dateHeure}" /></td>
					<td> <a href="${event.details}">${event.details}</a> </td>

					
					
	            
				</tr>

			</c:forEach>

		</table>
	</div>

	<jsp:include page="../fragments/adminfooter.jsp" />