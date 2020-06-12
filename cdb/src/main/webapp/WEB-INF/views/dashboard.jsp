<%--
	Affichage d'une page de Computer.
	Attributs demand�s
		-> computerList : List<ComputerDTO> Une liste d'instances de Computer � afficher.
 		-> page : page courante.
 		-> pageList : Liste des pages affichables

--%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="com.excilys.model.ComputerDTO,com.excilys.model.Page,java.util.ArrayList,java.util.List,java.util.Objects"%>
<%
    List<ComputerDTO> computerList = (List<ComputerDTO>) request.getAttribute("computerList");
    Page currentPage = (Page) request.getAttribute("page");
    List<Integer> pageList = (List<Integer>) request.getAttribute("pageList");
    String message = (String) request.getAttribute("message");
    if (pageList == null) {
        pageList = new ArrayList<>();
    }
%>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="page"> Application - Computer
				Database </a>
		</div>
	</header>

	<section id="main">
		<%
		    if (message != null) {
		%>
		<div class="container">
			<div class="alert alert-danger">
				<%=message%>
				<br /> 
			</div>
		</div>
		<%
		    }
		%>
	</section>
	<div class="container">
		<h1 id="homeTitle"><%=currentPage.getTotalNumberOfElements()%>
			Computers found
		</h1>
		<div id="actions" class="form-horizontal">
			<div class="pull-left">
				<!-- TODO : Recherche d'un ordinateur -->
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
					<!-- TODO : suppression d'ordinateur -->
					<th class="editMode" style="width: 60px; height: 22px;"><input
						type="checkbox" id="selectall" /> <span
						style="vertical-align: top;"> - <a href="#"
							id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
								class="fa fa-trash-o fa-lg"></i>
						</a>
					</span></th>
					<th>Computer name</th>
					<th>Introduced date</th>
					<!-- Table header for Discontinued Date -->
					<th>Discontinued date</th>
					<!-- Table header for Company -->
					<th>Company</th>

				</tr>
			</thead>
			<!-- Browse attribute computers -->
			<tbody>
				<%
				    for (ComputerDTO c : computerList) {
				%>
				<tr>
					<td class="editMode"><input type="checkbox" name="cb"
						class="cb" value="<%=c.getId()%>"></td>
					<td><a href="editComputer?id=<%=c.getId()%>" onclick=""> <%=c.getName()%>
					</a></td>
					<td><%=Objects.toString(c.getIntroductionDate(), "")%></td>
					<td><%=Objects.toString(c.getDiscontinuationDate(), "")%></td>
					<td><%=Objects.toString(c.getCompany() == null ? "" : c.getCompany().getName(), "")%></td>
					<%
					    }
					%>
				</tr>
			</tbody>
		</table>
	</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<li><a
					href="page?pageNumber=<%=Math.max(1, currentPage.getPageNumber() - 1)%>&pageLength=<%=currentPage.getPageLength()%>"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>

				<%
				    for (Integer pageNumber : pageList) {
				%>
				<li><a
					href="page?pageNumber=<%=pageNumber%>&pageLength=
					<%=currentPage.getPageLength()%>">
						<%=pageNumber%>
				</a></li>
				<%
				    }
				%>
				<li><a
					href="page?pageNumber=<%=Math.min(currentPage.getNbOfPages(), currentPage.getPageNumber() + 1)%>&pageLength=<%=currentPage.getPageLength()%>"
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