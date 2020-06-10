<%-- 
	Page JSP permettant la saisie pour d'ajouter un ordinateur dans la base de donnée 
	Attributs nécessaires
		-> companyList : List<CompanyDTO> la liste des entreprises présentes dans la base
--%>

<%@ page import="com.excilys.model.CompanyDTO,java.util.List"%>
<%
    List<CompanyDTO> companyList = (List<CompanyDTO>) request.getAttribute("companyList");
%>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">


<script>
	function validateForm() {
		var form = document.forms["addComputer"];
		var name = form["computerName"].value;
		if (name.trim() == "") {
			alert("Pas de nom");
			console.log("no name");
			return false;
		}
		console.log("récup des dates");
		var introDateStr = form["introduced"].value;
		var discoDateStr = form["discontinued"].value;
		var companyId = form["companyId"].value; // Inutile
		console.log("intro:");
		console.log(introDateStr);

		if (introDateStr.trim() != "") {
			var introDate = Date.parse(introDateStr);
			if (discoDateStr.trim() != "") {
				var discoDate = Date.parse(discoDateStr);
				if (discoDate < introDate) {
					alert("Arrêt avant début de la production");
					return false;
				}
			}
		} else if (discoDateStr != "") {
			alert("La date d'arrêt de production est définie mais pas la date d'intro");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application -
				Computer Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<form name="addComputer" action="addComputer" method="POST"
						onsubmit="return validateForm()">
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" id="computerName"
									name="computerName" placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced"
									name="introduced" placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinued" placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<option value="0">--</option>
									<%
									    for (CompanyDTO company : companyList) {
									%>
									<option value="<%=company.getId()%>"><%=company.getName()%></option>
									<%
									    }
									%>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Add" class="btn btn-primary">
							or <a href="page" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>