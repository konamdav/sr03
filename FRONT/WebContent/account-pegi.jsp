<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="favicon.ico">

<title>Mon compte</title>

<!-- Bootstrap core CSS -->
<link href="dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="dashboard.css" rel="stylesheet">
<script type="text/javascript" src="scripts/script.js"></script>
<script type="text/javascript" src="scripts/panier.js"></script>
<script type="text/javascript" src="scripts/pegi.js"></script>
</head>

<body>

	<jsp:include page="menu.jsp">
		<jsp:param name="curr" value="COMPTE" />
	</jsp:include>


<%
			if(request.getSession().getAttribute("ADMIN")!=null && request.getSession().getAttribute("USER")!=null){
		%>
	<jsp:include page="menu-account.jsp">
		<jsp:param name="curr" value="PEGI" />
	</jsp:include>


	<main class="col-sm-9 offset-sm-3 col-md-8 offset-md-2 pt-3">
	<h1>Mon compte</h1>
	<div class="row">

		<div class="col-md-5">
			<h2>Liste des classifications</h2>
			<div class="table-responsive">
				<table class="table table-striped">
					<thead>
						<tr>
							<th>#</th>
							<th></th>
							<th>Nom</th>
						</tr>
					</thead>
					<tbody id="pegi">
					</tbody>
				</table>
			</div>
		</div>
		<div class="col-md">
			<h2>Cr�ation</h2>
			<div id="create-success"></div>
			<div style="color:red" id="create-error"></div>
			<form id="create" action="" method="post" onsubmit="return submitCreatePegi();">
				<div style="width: 19%; display: inline-block;">Label :</div>
				<input style="width: 80%" id="name" name="name" type="text">
				<br><br>
				<div style="width: 19%; display: inline-block;">URL Image :</div>
				<input style="width: 80%" id="url" name="url" type="text">
				<br><br>
				<button type="submit" class="btn btn-primary">Cr�er</button>
			</form>
			<hr>
			<h2>Modification</h2>
			<div id="update-success"></div>
			<div style="color:red" id="update-error"></div>
			<form id="update" action="" method="post" onsubmit="return submitUpdatePegi();">
			<div style="width: 19%; display: inline-block;">Label :</div>
				<input style="width: 80%" id="name" name="name" type="text">
				<br><br>
				<div style="width: 19%; display: inline-block;">URL Image :</div>
				<input style="width: 80%" id="url" name="url" type="text">
				<br><br>
				<button type="submit" class="btn btn-primary">Modifier</button>
				<input type="hidden" name="id_pegi" id="id_pegi"/>
			</form>
			
			

		</div>
	</div>
	<section class="row  placeholders"></section>
	<script>
		window.onload = function() {
			loadPegi();

		}
	</script>
	</div>
	</div>

	<%
			}
	%>

	<!-- Bootstrap core JavaScript
    ================================================== --> <!-- Placed at the end of the document so the pages load faster -->
	<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js"
		integrity="sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n"
		crossorigin="anonymous"></script> <script>
			window.jQuery
					|| document
							.write('<script src="assets/js/vendor/jquery.min.js"><\/script>')
		</script> <script
		src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js"
		integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb"
		crossorigin="anonymous"></script> <script
		src="dist/js/bootstrap.min.js"></script> <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
