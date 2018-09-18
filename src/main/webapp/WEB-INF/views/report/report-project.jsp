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

<!-- JSTREE css -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.3/themes/default/style.min.css">

<!-- Analyse js -->
<link rel="stylesheet" href="/resources/css/analyze.css">
<link rel="stylesheet" href="/resources/css/table-content-center.css">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" integrity="sha384-mzrmE5qonljUremFsqc01SB46JvROS7bZs3IO2EmfFsd15uHvIt+Y8vEf7N7fWAU" crossorigin="anonymous">

<title>Home - DiffMutAnalyze</title>
</head>

<body>

  <%@ include file="../header.jsp"%>

  <div class="container-fluid">

    <h2>Score de mutação => ${projectReportStatistics.mutationScore()}</h2>

    <div class="row">
      <h2>Estatísticas dos relatórios dos usuários sobre a equivalência dos mutantes</h2>
      <table class="table table-striped table-hover">
          <thead>
            <tr>
              <th>Tempo total (ms)</th>
              <th>Tempo médio p/ pessoa (ms)</th>
              <th>Tempo médio p/ classe (ms)</th>
              <th>Tempo médio p/ relatório de mutante (ms)</th>
              <th>Dificuldade média</th>
              <th>Qtd equivalente</th>
              <th>Qtd não equivalente</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>${projectReportStatistics.totalTime}</td>
              <td>${projectReportStatistics.averageTimePerPerson()}</td>
              <td>${projectReportStatistics.averageTimePerMutantReport()}</td>
              <td>${projectReportStatistics.averageTimePerMutantReport()}</td>
              <td>${projectReportStatistics.averageDifficulty()}</td>
              <td>${projectReportStatistics.equivalentQty}</td>
              <td>${projectReportStatistics.notEquivalentQty}</td>
            </tr>
          </tbody>
        </table>
    </div>

    <div class="row">
      <span id="difficulty-dataset" hidden>${projectReportStatistics.difficultyDatasetJson()}</span>
      <div class="col-xs-6">
        <canvas id="difficulty-bar-chart"></canvas>
      </div>
      <span id="equivalence-dataset" hidden>${projectReportStatistics.equivalenceDatasetJson()}</span>
      <div class="col-xs-6">
        <canvas id="equivalence-pie-chart"></canvas>
      </div>
    </div>

    <hr>

    <div class="row">
      <h2>Estatísticas dos relatórios dos usuários sobre a equivalência dos mutantes para cada classe</h2>
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>Classe</th>
            <th>Tempo total (ms)</th>
            <th>Tempo médio p/ pessoa (ms)</th>
            <th>Tempo médio p/ relatório de mutante (ms)</th>
            <th>Dificuldade média</th>
            <th>Qtd equivalente</th>
            <th>Qtd não equivalente</th>
          </tr>
        </thead>
        <tbody>
         <c:forEach var="codeReport" items="${projectReportStatistics.originalCodesStatistics}">	
            <tr>
              <td style="text-align: left !important;"><b>${codeReport.originalCode.filepath}</b></td>
              <td>${codeReport.totalTime}</td>
              <td>${codeReport.averageTimePerPerson()}</td>
              <td>${codeReport.averageTimePerMutantReport()}</td>
              <td>${codeReport.averageDifficulty()}</td>
              <td>${codeReport.equivalentQty}</td>
              <td>${codeReport.notEquivalentQty}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <hr>

    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#collapse-general-mutant-report">Estatísticas gerais dos mutantes</a>
          </h4>
        </div>
        <div id="collapse-general-mutant-report" class="panel-collapse collapse">
          <ul class="list-group">
            <li class="list-group-item">
              <div class="panel-group">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title">
                      <a data-toggle="collapse" href="#collapse-general-mutant-report-table">Estatísticas gerais dos mutantes</a>
                    </h4>
                  </div>
                  <div id="collapse-general-mutant-report-table" class="panel-collapse collapse">
                    <div class="panel-body">
                      <table class="table table-striped table-hover">
                        <thead>
                          <tr>
                            <th>Qtd de relatórios</th>
                            <th>Qtd de pessoas</th>
                            <th>Qtd mutantes mortos</th>
                            <th>Qtd mutantes vivos</th>
                            <th>Qtd mutantes equivalentes*</th>
                            <th>Qtd mutantes não equivalentes*</th>
                            <th>Score de mutação</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>${projectReportStatistics.mutantReportQty}</td>
                            <td>${projectReportStatistics.persons.size()}</td>
                            <td>${projectReportStatistics.deadMutantQty}</td>
                            <td>${projectReportStatistics.aliveMutantQty}</td>
                            <td>${projectReportStatistics.mutantEquivalentQty}</td>
                            <td>${projectReportStatistics.mutantNotEquivalentQty}</td>
                            <td>${projectReportStatistics.mutationScore()}</td>
                          </tr>
                        </tbody>
                      </table>
                      <span>* Um determinado mutante é considerado equivalente quando a maioria dos relatórios pessoais o classifica como equivalente, 
                            caso contrário é considerado não equivalente.</span>
                      <div class="row">
                        <span id="mutants-status-dataset" hidden>${projectReportStatistics.mutantsStatusDatasetJson()}</span>
                        <div class="col-xs-6">
                          <canvas id="mutants-status-pie-chart"></canvas>
                        </div>
                        <span id="mutants-equivalence-dataset" hidden>${projectReportStatistics.mutantsEquivalenceDatasetJson()}</span>
                        <div class="col-xs-6">
                          <canvas id="mutants-equivalence-pie-chart"></canvas>
                        </div>
                      </div>
                    </div>
                    <%-- <div class="panel-footer">Panel Footer</div> --%>
                  </div>
                </div>
              </div>
            </li>
            <li class="list-group-item">
              <div class="panel-group">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title">
                      <a data-toggle="collapse" href="#collapse-general-mutant-report-table-classes">Estatísticas dos mutantes para cada classe</a>
                    </h4>
                  </div>
                  <div id="collapse-general-mutant-report-table-classes" class="panel-collapse collapse">
                    <div class="panel-body">
                      <table class="table table-striped table-hover">
                        <thead>
                          <tr>
                            <th>Classes</th>
                            <th>Qtd de relatórios</th>
                            <th>Qtd de pessoas</th>
                            <th>Qtd mutantes mortos</th>
                            <th>Qtd mutantes vivos</th>
                            <th>Qtd mutantes equivalentes*</th>
                            <th>Qtd mutantes não equivalentes*</th>
                            <th>Score de mutação</th>
                          </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="codeReport" items="${projectReportStatistics.originalCodesStatistics}">	
                              <tr>
                                <td style="text-align: left !important;"><b>${codeReport.originalCode.filepath}</b></td>
                                <td>${codeReport.mutantReportQty}</td>
                                <td>${codeReport.persons.size()}</td>
                                <td>${codeReport.deadMutantQty}</td>
                                <td>${codeReport.aliveMutantQty}</td>
                                <td>${codeReport.mutantEquivalentQty}</td>
                                <td>${codeReport.mutantNotEquivalentQty}</td>
                                <td>${codeReport.mutationScore()}</td>
                              </tr>
                            </c:forEach>
                        </tbody>
                      </table>
                      <span>* Um determinado mutante é considerado equivalente quando a maioria dos relatórios pessoais o classifica como equivalente, 
                            caso contrário é considerado não equivalente.</span>
                    </div>
                    <%-- <div class="panel-footer">Panel Footer</div> --%>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <hr>

    <div class="panel-group" style="margin-bottom: 75px;">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#collapse-person-reports">Relatórios pessoais</a>
          </h4>
        </div>
        <div id="collapse-person-reports" class="panel-collapse collapse">
          <ul class="list-group">
            <c:forEach var="person" items="${projectReportStatistics.personToReport.keySet()}">	
              <li class="list-group-item">
                <div class="panel-group">
                  <div class="panel panel-default">
                    <div class="panel-heading">
                      <h4 class="panel-title">
                        <a data-toggle="collapse" href="#collapse-person-${person.id}">${person.name} - ${person.email}</a>
                      </h4>
                    </div>
                    <div id="collapse-person-${person.id}" class="panel-collapse collapse">
                      <div class="panel-body">
                      <table class="table table-striped table-hover">
                        <thead>
                          <tr>
                            <th>Classe</th>
                            <th>Mutante</th>
                            <th>Equivalência</th>
                            <th>Dificuldade</th>
                            <th>Tempo de análise (ms)</th>
                          </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="report" items="${projectReportStatistics.personToReport.get(person).reports}">	
                              <tr>
                                <td style="text-align: left !important;">${report.mutantCode.originalCode.filepath}</td>
                                <td style="text-align: left !important;">${report.mutantCode.filepath}</td>
                                <td>${report.getEquivalenceView()}</td>
                                <td>${report.getDifficultyView()}</td>
                                <td>${report.analysisTime}</td>
                              </tr>
                            </c:forEach>
                        </tbody>
                      </table>
                      </div>
                      <%-- <div class="panel-footer">Panel Footer</div> --%>
                    </div>
                  </div>
                </div>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>
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

  <script src="https://cdnjs.cloudflare.com/ajax/libs/d3-selection/1.2.0/d3-selection.min.js"></script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.min.js"></script>

  <!-- Analyze js -->
  <%-- <script src="/resources/js/analyze.js"></script> --%>

  <script src="/resources/js/charts.js"></script>


</body>
</html>