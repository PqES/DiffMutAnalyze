
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="row">
	<div class="col-sm-2"></div>

	<div class="col-sm-8" align="center">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="/user/home">DiffMutAnalyze</a>
				</div>
				<ul class="nav navbar-nav">
					<li class="active"><a href="/user/home">Home</a></li>
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">Perfil<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="/user/configurations">Configurações</a></li>
						</ul>
					</li>
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">Projeto<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="/project/my">Meus projetos</a></li>
							<%-- <li><a href="/project/analyse">Analisar projeto</a></li> --%>
							<li><a href="/project/register">Cadastrar projeto</a></li>
						</ul>
					</li>
					<li><a href="/user/logout">Sair</a></li>
				</ul>
			</div>
		</nav>

		<c:if test="${message != null}">
      <div class="alert ${message.alertType.name} alert-dismissable">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">
          <span aria-hidden="true">&times;</span>
        </a>
        ${message.content}
      </div>
    </c:if>
	</div>
</div>


