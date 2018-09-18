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

<%-- Codemirror css --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/codemirror.min.css">

<!-- JSTREE css -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.3/themes/default/style.min.css">

<!-- Analyse js -->
<link rel="stylesheet" href="/resources/css/analyze.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

<title>Home - DiffMutAnalyze</title>
</head>

<body>

  <%@ include file="../header.jsp"%>


  <div class="container-fluid">
    <%-- <div class="row">
      <!-- Visualização do diretório em Árvore -->
      <div class="col-xs-9" id="tree-row"></div>
    </div> --%>

    <!-- Título tabela de mutantes -->
    <div class="row">
      <h4 align="center">
        <b>Tabela de mutantes</b>
      </h4>
    </div>

    <!-- Gridview -->
    <div class="row">
      <div class="table-wrapper-2">
        <table class="table table-striped">
          <thead id="table-head"></thead>
          <tbody id="table-body"></tbody>
        </table>
      </div>
    </div>

    <!-- Legenda grid view -->
    <div class="row" id="table-legend" name="table-legend">
      <div class="row">
        <div class="col-xs-6">
          <b>Legenda:</b>
        </div>
        <div class="col-xs-6">
          <button type="button" class="btn btn-primary" onclick="focusNotAnalyze()">Buscar mutante não analizado</button>
        </div>
      </div>

      <div class="row">
        <div class="col-xs-2">Mutante não analisado:</div>
        <div class="col-xs-3">
          <button class="btn btn-default mutant-not-analyze"></button>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-2">Mutante analisado:</div>
        <div class="col-xs-3">
          <button class="btn btn-default mutant-analyze"></button>
        </div>
      </div>
      <div class="row">
        <div class="col-xs-2">Mutante pulado:</div>
        <div class="col-xs-3">
          <button class="btn btn-default mutant-skip"></button>
        </div>
      </div>
    </div>

    <hr/>

    <div class="row" style="text-align: center;" id="button-play-legend">
      Análise em andamento
    </div>
    <div class="row" style="text-align: center;">
      <button type="button" id="button-play" class="btn btn-success btn-lg" onclick='buttonPlayPress()'>
        <i class="fa fa-pause"></i>
      </button>
    </div>

    <hr/>

    <!-- Título para os códigos Java e mutante -->
    <div class="row">
      <div class="col-xs-6">
        <h4 align="center" id="original-code-title">
          <b>Código original</b>
        </h4>
      </div>
      <div class="col-xs-6">
        <h4 align="center" id="mutant-code-title">
          <b>Código mutante</b>
        </h4>
      </div>
    </div>

    <!-- Códigos Java original e mutante -->
    <div class="row my-scroll-style" id="diff-div" style="margin-bottom: 25px">
      <div class="col-xs-6">
        <textarea class="codemirror-textarea" id="original-code"></textarea>
      </div>
      <div class="col-xs-6">
        <textarea class="codemirror-textarea" id="mutant-code"></textarea>
      </div>
    </div>

    <div class="row div-align-right"  style="margin-bottom: 75px;">
      <!-- Definição de mutante equivalente -->
      <form>
        <label class="radio-inline">
          <input type="radio" id="radio-equivalent" name="radio-equivalency">Equivalente
        </label>
        
        <label class="radio-inline">
          <input type="radio" id="radio-not-equivalent" name="radio-equivalency">Não equivalente
        </label>
        
        <div class="btn-group margin-left" role="group" aria-label="...">
          <a class="btn btn-link  btn-label disabled" disabled>Muito fácil</a>
          <button type="button" class="btn btn-default likert-1" id="likert-1" onclick="likertSelect(this, 1)">1</button>
          <button type="button" class="btn btn-default likert-2" id="likert-2" onclick="likertSelect(this, 2)">2</button>
          <button type="button" class="btn btn-default likert-3" id="likert-3" onclick="likertSelect(this, 3)">3</button>
          <button type="button" class="btn btn-default likert-4" id="likert-4" onclick="likertSelect(this, 4)">4</button>
          <button type="button" class="btn btn-default likert-5" id="likert-5" onclick="likertSelect(this, 5)">5</button>
          <a class="btn btn-link btn-label disabled" disabled>Muito difícil</a>
        </div>

        <button type="button" class="btn margin-left" onclick="loadPrevious()">Previous</button>
        <button type="button" class="btn btn-primary" onclick="loadNext()">Next</button>
      </form>
      <!-- Navegação básica de mutantes (anterior, próximo) -->
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

  <!-- add basic CodeMirror functionality -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/codemirror.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/addon/selection/active-line.min.js"></script>

  <!-- add Javascript-mode dependencies -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/javascript/javascript.min.js"></script>

  <!-- add PHP-mode dependencies (replace dependency loading by require.js!) -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/xml/xml.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/htmlmixed/htmlmixed.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/clike/clike.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/php/php.min.js"></script>

  <!-- add SPARQL-mode dependencies -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/sparql/sparql.min.js"></script>

  <!-- JSTREE js -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.3/jstree.min.js"></script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/d3-selection/1.2.0/d3-selection.min.js"></script>

  <!-- Analyze js -->
  <script src="/resources/js/analyze.js"></script>

</body>
</html>
