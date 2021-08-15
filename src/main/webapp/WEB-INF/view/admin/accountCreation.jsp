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
		<h3>List of Persons</h3>
	</div>
	
	<div class="col-4"><form
				action="${pageContext.request.contextPath}/admin/serachPersonByCniOrCne"
				class="d-flex justify-content-center" method="GET">
				<input name="cinOrcne" class="form-control me-2" type="search"
					placeholder="search By CIN or CNE" aria-label="Search">
				<button class="btn btn-outline-success" type="submit">Search</button>
			</form>
			
			</div>

	<div>
				<c:choose>
	
	 <c:when test="${personList!=null}">
	
		<table class="table">
			<thead>
				<tr>
					<th scope="col">CIN</th>
					<th scope="col">Nom & Prénom</th>
					<th scope="col">Email</th>
					<th scope="col">Actions</th>
				</tr>
			</thead>
			
							 <c:forEach items="${personList}" var="p">
								<tr>
									<td><c:out value="${p.cin}" /></td>
									<td><c:out value="${p.nom} / ${p.prenom}" /></td>
									<td><c:out value="${p.email}" /></td>
								
				
									<td>
										<ul>
												<li><a
												href="${pageContext.request.contextPath}/admin/createAccountForm/${p.idUtilisateur}">Create Account</a></li>
				
										</ul>
									</td>
				
								</tr>
				
							</c:forEach>
							
									</table>
							
					  </c:when>
					
					  
			

		      <c:otherwise>
					    
						      <div class="alert alert-info">no item found</div> 

					  </c:otherwise>
					</c:choose>
	</div>

	<jsp:include page="../fragments/adminfooter.jsp" />