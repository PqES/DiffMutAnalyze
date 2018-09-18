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

<!-- Custom styles for this template -->
<link rel="stylesheet" href="/resources/css/signin.css">

<title>Login - DiffMutAnalyze</title>
</head>

<body>

  <div class="container">

    <h1 style="text-align: center;">
      <b>DiffMutAnalyze</b>
    </h1>
    
    <hr>

    <c:if test="${message != null}">
      <div class="alert ${message.alertType.name} alert-dismissable">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">
          <span aria-hidden="true">&times;</span>
        </a>
        ${message.content}
      </div>
    </c:if>

    <form:form action="/user/login" modelAttribute="login" method="post" class="form-signin">
      <h2 class="form-signin-heading">Login</h2>

      <form:label for="inputEmail" class="sr-only" path="email">Email</form:label>
      <form:input type="email" id="inputEmail" class="form-control" placeholder="Email address" required="true" autofocus="" path="email"/>

      <form:label for="inputPassword" class="sr-only" path="email">Password</form:label>
      <form:input type="password" id="inputPassword" class="form-control" placeholder="Senha" required="true" path="password"/>

      <div class="checkbox">
        <label> <input type="checkbox" value="remember-me">
          Lembrar-me
        </label>
      </div>

      <button class="btn btn-lg btn-primary btn-block" type="submit">Logar</button>
    </form:form>

    <hr>

    <div align="center">
      <a href="/user/register">Criar nova conta</a>
    </div>

  </div>

  <!-- Javascript -->
  
  <!-- JQuery js -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  
  <!-- Bootstrap js -->
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
  
  <!-- Toastr js -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

</body>
</html>