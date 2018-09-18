<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<!-- Meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap css -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<!-- Toastr css -->
<link  rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">

<!-- Analyse js -->
<link rel="stylesheet" href="/resources/css/analyze.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

	<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.0/clipboard.min.js"></script>

<title>Projeto - DiffMutAnalyze</title>
</head>

<body>

  <%@ include file="../header.jsp"%>

  <div class="row">
    <!-- Deslocamento -->
		<div class="col-md-2"></div>

		<!-- Lista de projetos -->
		<div class="col-md-8">
			<c:forEach var="project" items="${myProjects}">	
				<!-- Link da postagem -->
				<div class="row">
					<h3><b> ${project.id} - ${project.name} </b></h3>
					<div class="row">
						<a href="/report/${project.hashkey}">Relatórios</a>
					</div>
					<div class="row">
						<a href="/project/analyze/${project.hashkey}">Análise</a>
					</div>
					<div class="row">
						<input class="analyze-input" id="analyze-${project.hashkey}" value="/project/analyze/${project.hashkey}">
						<button class="btn" onclick="copyData('analyze-${project.hashkey}')">
							<i class="fas fa-copy"> Copiar link</i>
						</button>
					</div>
				</div>
				<hr>
			</c:forEach>
		</div>
		
	</div>

  <footer class="footer navbar navbar-default navbar-fixed-bottom">
    <div class="container">
      <p class="text-muted">DiffMutAnalyze</p>
    </div>
  </footer>

  <!-- Javascript -->
  
  <!-- JQuery js -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  
  <!-- Bootstrap js -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  
  <!-- Toastr js -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

	<script src="/resources/js/copy-link.js"></script>

</body>
</html>