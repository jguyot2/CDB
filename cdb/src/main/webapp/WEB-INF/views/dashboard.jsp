<%--
	Affichage d'une page de Computer.
	Attributs demandés
		-> computerList : List<ComputerDTO> Une liste d'instances de Computer à afficher.
 		-> page : page courante.
 		-> pageList : Liste des pages affichables
 // TODO : maj
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ page
	import="com.excilys.model.ComputerDTO,com.excilys.model.Page,java.util.ArrayList,java.util.List,java.util.Objects"%>

<c:set value="${requestScope.computerList}" var="computerList" />
<c:set value="${requestScope.page}" var="currentPage" />
<c:set value="${requestScope.pageList}" var="pageList" />
<c:set value="${requestScope.search}" var="search" />
<c:set value="${requestScope.urlSearch}" var="searchUrl" />
<c:set value="${requestScope.sortParameterValue}" var="sortUrl" />
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="../css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="../css/font-awesome.css" rel="stylesheet" media="screen">
<link href="../css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> Application - Computer
				Database </a>
		</div>
	</header>
	<section id="main">
		<c:if test="${not empty message}">
			<div class="container">
				<div class="alert alert-danger">
					${message} <br />
				</div>
			</div>
		</c:if>
	</section>
	<div class="container">
		<h1 id="homeTitle">${currentPage.totalNumberOfElements}computers
			found</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<form id="searchForm" action="#" method="GET" class="form-inline">
					<input type="search" id="searchbox" name="search"
						class="form-control" placeholder="Search name" /> <input
						type="submit" id="searchsubmit" value="Filter by name"
						class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer" href="addComputer">Add
					Computer</a> <a class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();">Edit</a>
			</div>
		</div>
	</div>
	<!-- Form de suppression  -->
	<form id="deleteForm" action="#" method="POST">
		<input type="hidden" name="selection" value="">
	</form>

	<div class="container" style="margin-top: 10px;">
		<table class="table table-striped table-bordered">
			<thead>
				<tr>
					<!-- Variable declarations for passing labels as parameters -->
					<!-- Table header for Computer Name -->
					<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="#"
							id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
								class="fa fa-trash-o fa-lg"></i>
						</a>
					</span></th>
					<th>Computer name <a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
						<c:param name="pageLength" value="${currentPage.pageLength}" />
						<c:if test="${not empty search}"> 
						 		<c:param name="search" value="${search}" />	
				 		</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="name-asc"/>
					</c:url>
						style="font-size: xx-large">&#8593; </a> <a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
					
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="name-desc"/>
					</c:url>
						style="font-size: xx-large">&#8595; </a>
					</th>
					<th>Introduced date <a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="introduced-asc"/>
					</c:url>
						style="font-size: xx-large">&#8593; </a><a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="introduced-desc"/>
					</c:url>
						style="font-size: xx-large">&#8595; </a></th>
					<!-- Table header for Discontinued Date -->
					<th>Discontinued date <a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="discontinued-asc"/>
					</c:url>
						style="font-size: xx-large">&#8593; </a><a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
						<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>	<c:param name="newSortParam" value="discontinued-desc"/>
					</c:url>
						style="font-size: xx-large">&#8595; </a></th>
					<!-- Table header for Company -->
					<th>Company <a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>		<c:param name="newSortParam" value="companyName-asc"/>
					</c:url>
						style="font-size: xx-large">&#8593; </a><a
						href=<c:url value="page">
						<c:param name ="pageNumber" value="0" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />	
					</c:if>
						<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>	<c:param name="newSortParam" value="companyName-desc"/>
					</c:url>
						style="font-size: xx-large">&#8595; </a></th>
				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody>

				<c:forEach items="${computerList}" var="c">
					<tr>
						<td class="editMode"><input type="checkbox" name="cb"
							class="cb" value="<c:out value="${c.id}" />"></td>
						<td><a href="editComputer?id=<c:out value="${c.id}" />"
							onclick=""> ${c.name} </a></td>
						<td>${c.introductionDate}</td>
						<td>${c.discontinuationDate}</td>
						<td>${c.company.name}</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">

				<c:if test="${currentPage.pageNumber > 0}">
					<li><a
						href=<c:url value="page">
					<c:param name ="pageNumber" value="${currentPage.pageNumber - 1}" />
					<c:param name="pageLength" value="${currentPage.pageLength}" /> 
					<c:if test="${not empty search}"> 
						<c:param name="search" value="${search}" />
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>
				</c:url>
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>

					</a></li>
				</c:if>
				<c:forEach items="${pageList}" var="pageNumber">
					<li><a
						href=<c:url value="page">
					<c:param name="pageNumber" value="${pageNumber}" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}">
						<c:param name="search" value="${search}" />
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>
				</c:url>>
							${pageNumber} </a></li>
				</c:forEach>
				<li><a
					href=<c:url value="page">  
						<c:param name="pageNumber"
						value="${ Math.min(currentPage.nbOfPages + 0, currentPage.pageNumber + 1)}" />
					<c:param name="pageLength" value="${currentPage.pageLength}" />
					<c:if test="${not empty search}">
						<c:param name="search" value="${search}" />
					</c:if>
					<c:if test="${ not empty sortUrl}">
						<c:param name="sort" value="${sortUrl}" />
					</c:if>
				</c:url>
					aria-label="Next"> <span aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>
		</div>
		<div class="btn-group btn-group-sm pull-right" role="group">
			<button type="button" class="btn btn-default">10</button>
			<button type="button" class="btn btn-default">50</button>
			<button type="button" class="btn btn-default">100</button>
		</div>

	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>