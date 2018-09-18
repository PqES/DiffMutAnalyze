let projectHash = window.location.pathname;
let indexLastSeparator = projectHash.lastIndexOf('/');
projectHash = projectHash.substring(indexLastSeparator + 1);

var project;
var originalCodeIndex;
var mutantCodeIndex;
var startAnalyzeTime;
var difficultySelected = null;

var originalCode = CodeMirror.fromTextArea(document
  .getElementById("original-code"), {
    lineNumbers: true,
    mode: "text/x-java",
    matchBrackets: true,
    readOnly: true,
    viewportMargin: Infinity,
    lineWrapping: true,
    scrollbarStyle: "null"
  });
originalCode.setValue("// Código original");

var mutantCode = CodeMirror.fromTextArea(document
  .getElementById("mutant-code"), {
    lineNumbers: true,
    mode: "text/x-java",
    matchBrackets: true,
    readOnly: true,
    viewportMargin: Infinity,
    lineWrapping: true,
    scrollbarStyle: "null"
  });
mutantCode.setValue("// Código mutante");

function likertSelect(elem) {
  if (difficultySelected !== null) {
    $('#' + difficultySelected)[0].classList.remove('btn-selected');
  }
  difficultySelected = elem.id;
  elem.classList.add('btn-selected');
}

function reportIsComplete(originalCodeIndex, mutantCodeIndex) {
  let report = project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].report;
  return report !== undefined && report !== null && report.difficulty !== null 
          && report.equivalent !== null;
}

function focusNotAnalyze() {
  let N = project.originalCodes.length;
  for (let i = 0; i < N; i++) {
    let originalCode = project.originalCodes[i];
    let mutantsLength = originalCode.mutantCodes.length;
    for (let j = 0; j < mutantsLength; j++) {
      if (!reportIsComplete(i, j)) {
        $('#' + i + '-' + j).focus();
        return;
      }
    }
  }
}

function biggerMutantList() {
  let N = project.originalCodes.length;
  let bigger = project.originalCodes[0].mutantCodes.length;
  for (let i = 1; i < N; i++) {
    let length = project.originalCodes[1].mutantCodes.length;
    if (length > bigger) {
      bigger = length;
    }
  }
  return bigger;
}

function changeAnalyzeCodes(lastOriginalCodeIndex, lastMutantCodeIndex) {
  updateReport(lastOriginalCodeIndex, lastMutantCodeIndex);
  loadActualCodes(lastOriginalCodeIndex);
}

function getReport(originalCodeIndex, mutantCodeIndex) {
  return project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].report;
}

function setReport(originalCodeIndex, mutantCodeIndex, report) {
  project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].report = report;
}

function initReport(originalCodeIndex, mutantCodeIndex) {
  let report = getReport(originalCodeIndex, mutantCodeIndex);
  if (report === undefined || report === null) {
    report = new Object();
    report.id = null;
    report.equivalence = null;
    report.difficulty = null;
    report.analysisTime = 0;
    setReport(originalCodeIndex, mutantCodeIndex, report);
  }
}

const EQUIVALENT = "EQUIVALENT";

const NOT_EQUIVALENT = "NOT_EQUIVALENT";

function getEnumEquivalence(equivalence) {
  if (equivalence === undefined || equivalence === null) {
    return null;
  }
  if (equivalence) {
    return EQUIVALENT;
  } else {
    return NOT_EQUIVALENT;
  }
}

const DIFFICULTIES = ["ONE", "TWO", "THREE", "FOUR", "FIVE"];

const DIFFICULTIES_TO_NUMBER = {"ONE": 1, "TWO": 2, "THREE": 3, "FOUR": 4, "FIVE": 5}

function getDifficultyValue(difficulty) {
  if (difficulty === undefined || difficulty === null) {
    return null;
  }
  return DIFFICULTIES[difficulty - 1];
}

function getDifficultyNumber(difficulty) {
  return DIFFICULTIES_TO_NUMBER[difficulty];
}

function updateReportView(originalCodeIndex, mutantCodeIndex) {
  let report = getReport(originalCodeIndex, mutantCodeIndex);
  let equivalent = $('#radio-equivalent').prop('checked');
  let notEquivalent = $('#radio-not-equivalent').prop('checked');
  report.equivalence = null;
  if (equivalent) {
    report.equivalence = EQUIVALENT;
  } else if (notEquivalent) {
    report.equivalence = NOT_EQUIVALENT;
  }
  if (difficultySelected === null) {
    report.difficulty = null;
  } else {
    let difficulty = parseInt(difficultySelected[difficultySelected.length - 1]);
    report.difficulty = getDifficultyValue(difficulty);
  }
  let cell = $('#' + originalCodeIndex + '-' + mutantCodeIndex)[0];
  cell.classList.remove('mutant-not-analyze');
  if (reportIsComplete(originalCodeIndex, mutantCodeIndex)) {
    cell.classList.remove('mutant-skip');
    cell.classList.add('mutant-analyze');
  } else {
    cell.classList.remove('mutant-analyze');
    cell.classList.add('mutant-skip');
  }
}

function updateReport(originalCodeIndex, mutantCodeIndex) {
  updateReportView(originalCodeIndex, mutantCodeIndex);
  endTime = new Date();
  var report = getReport(originalCodeIndex, mutantCodeIndex);
  report.analysisTime += endTime - startAnalyzeTime;
  // TODO SEND REPORT TO SERVER
  let reportServer = {
    id : report.id,
    equivalence : report.equivalence,
    difficulty : report.difficulty,
    analysisTime : report.analysisTime,
    mutantCodeId : project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].id
  };
  console.log(reportServer);
  $.ajax({
    type: "post",
    url: "/report", 
    data: JSON.stringify(reportServer),
    success: function(data) {
      report.id = data;
      console.log(data);
    },
    contentType: "application/json"
  });
}

function loadReportView() {
  initReport(originalCodeIndex, mutantCodeIndex);
  $('#radio-not-equivalent').prop('checked', false);
  $('#radio-equivalent').prop('checked', false);
  let report = getReport(originalCodeIndex, mutantCodeIndex);
  if (report.equivalence !== null) {
    if (report.equivalence === EQUIVALENT) {
      $('#radio-equivalent').prop('checked', true);
    } else {
      $('#radio-not-equivalent').prop('checked', true);
    }
  }
  if (difficultySelected !== null) {
    $('#' + difficultySelected)[0].classList.remove('btn-selected');
    difficultySelected = null;
  }
  if (report.difficulty !== null) {
    let difficultyNumber = getDifficultyNumber(report.difficulty);
    $('#likert-' + difficultyNumber)[0].classList.add('btn-selected');
    difficultySelected = 'likert-' + difficultyNumber;
  }
}

var loadingAnotherCode;

function loadNext() {
  if (!underAnalysis) {
    toastr.error('Projeto está pausado, dê play para navegar no projeto!', 'Projeto não está em análise!');
    return;
  }
  if (loadingAnotherCode) {
    toastr.success('Há um outro código sendo carregado!', 'Não é possível avançar!');
    return;
  }
  loadingAnotherCode = true;
  let lastOriginalCodeIndex = originalCodeIndex;
  let lastMutantCodeIndex = mutantCodeIndex;
  mutantCodeIndex++;
  if (mutantCodeIndex === project.originalCodes[originalCodeIndex].mutantCodes.length) {
    originalCodeIndex++;
    if (originalCodeIndex === project.originalCodes.length) {
      originalCodeIndex = project.originalCodes.length - 1;
      mutantCodeIndex = project.originalCodes[originalCodeIndex].mutantCodes.length - 1;
      console.log(originalCodeIndex + ","  + mutantCodeIndex);
      toastr.success('Este é o último mutante do projeto!', 'Não é possível avançar!');
      loadingAnotherCode = false;
      return;
    }
    mutantCodeIndex = 0;
    toastr.info('Classe a ser analisada: "' + project.originalCodes[originalCodeIndex].filename + '"!', 'Próxima classe!');
  }
  console.log(originalCodeIndex + ","  + mutantCodeIndex);
  changeAnalyzeCodes(lastOriginalCodeIndex, lastMutantCodeIndex);
  toastr.info('Mutante a ser analisado: ' + project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].id + '!', 'Próximo mutante!');
  loadingAnotherCode = false;
}

function loadPrevious() {
  if (!underAnalysis) {
    toastr.error('Projeto está pausado, dê play para navegar no projeto!', 'Projeto não está em análise!');
    return;
  }
  if (loadingAnotherCode) {
    toastr.success('Há um outro código sendo carregado!', 'Não é possível voltar!');
    return;
  }
  loadingAnotherCode = true;
  let lastOriginalCodeIndex = originalCodeIndex;
  let lastMutantCodeIndex = mutantCodeIndex;
  mutantCodeIndex--;
  if (mutantCodeIndex === -1) {
    originalCodeIndex--;
    if (originalCodeIndex === -1) {
      originalCodeIndex = 0;
      mutantCodeIndex = 0;
      toastr.error('Este é o primeiro mutante do projeto.', 'Não é possível voltar!');
      loadingAnotherCode = false;
      return;
    }
    mutantCodeIndex = project.originalCodes[originalCodeIndex].mutantCodes.length - 1;
    toastr.info('Classe a ser analisada: "' + project.originalCodes[originalCodeIndex].filename + '"!', 'Classe anterior!');
  }
  console.log(originalCodeIndex + ","  + mutantCodeIndex);
  changeAnalyzeCodes(lastOriginalCodeIndex, lastMutantCodeIndex);
  toastr.info('Mutante a ser analisado: ' + project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].id + '!', 'Mutante anterior!');
  loadingAnotherCode = false;
}


function getOriginalCodeFilepath() {
  return projectHash + '@sep@' + project.originalCodes[originalCodeIndex].filepath.replaceAll("/", "@sep@");
}

function loadOriginalCode() {
  isOriginalCodeLoaded = false;
  $.get("/project/file/" + getOriginalCodeFilepath(), function(data) {
    originalCode.setValue(data);
    isOriginalCodeLoaded = true;
  });
}

function getMutantCodeFilepath() {
  return projectHash + '@sep@' + project.originalCodes[originalCodeIndex].mutantCodes[mutantCodeIndex].filepath.replaceAll("/", "@sep@");
}

var isOriginalCodeLoaded = false;


function loadMutantCode() {
  $.get("/project/file/" + getMutantCodeFilepath(), function(data) {
    mutantCode.setValue(data);
    applyDiffOnLoaded();
  });
}

function applyDiffOnLoaded() {
  if (isOriginalCodeLoaded) {
    applyDiff();
    startAnalyzeTime = new Date();
  } else {
    console.log("WAIT");
    setTimeout(applyDiffOnLoaded, 200);
  }
}


function loadActualCodes(lastOriginalCodeIndex) {
  if (lastOriginalCodeIndex != originalCodeIndex) {
    loadOriginalCode();
  }
  loadMutantCode();
  loadReportView();
}

function createTable() {
  let numColumns = biggerMutantList() + 1;
  // HEADER
  let tableHead = document.getElementById('table-head');
  let row = tableHead.insertRow(0);
  let i = 0;
  let cell = document.createElement('th');
  cell.innerHTML = "files/mutants";
  row.appendChild(cell);
  for (i = 1; i < numColumns; i++) {
    cell = document.createElement('th');
    let text = document.createTextNode(i);
    cell.appendChild(text);
    row.appendChild(cell);
  }
  // BODY
  let tableBody = document.getElementById('table-body');
  let numRows = project.originalCodes.length;
  let cellButton;
  for (i = 0; i < numRows; i++) {
    row = tableBody.insertRow(i);
    let j = 0;
    cell = document.createElement('th');
    let text = document.createTextNode(project.originalCodes[i].filename);
    cell.appendChild(text);
    row.appendChild(cell);
    let mutantsLength = project.originalCodes[i].mutantCodes.length + 1;
    for (j++; j < mutantsLength; j++) {
      cell = row.insertCell(j);
      cellButton = document.createElement('button');
      cellButton.classList.add('btn');
      cellButton.classList.add('btn-default');
      cellButton.classList.add('mutant-not-analyze');
      cellButton.id = i + "-" + (j - 1);
      cell.appendChild(cellButton);
      cellButton.addEventListener('click', function (event) {
        if (!underAnalysis) {
          toastr.error('Projeto está pausado, dê play para navegar no projeto!', 'Projeto não está em análise!');
          return;
        }
        let lastOriginalCodeIndex = originalCodeIndex;
        let lastMutantCodeIndex = mutantCodeIndex;
        let tokens = event.target.attributes.id.value.split("-");
        originalCodeIndex = parseInt(tokens[0]);
        mutantCodeIndex = parseInt(tokens[1]);
        changeAnalyzeCodes(lastOriginalCodeIndex, lastMutantCodeIndex);
      });
    }
    while (j < numColumns) {
      row.insertCell(j);
      j++;
    }
  }
}

function defineFirstCodes() {
  if (defineFirstCodesNotAnalyze()) {
    return;
  }
  let N = project.originalCodes.length;
  for (let i = 0; i < N; i++) {
    let originalCode = project.originalCodes[i];
    let mutantsLength = originalCode.mutantCodes.length;
    if (mutantsLength > 0) {
      originalCodeIndex = i;
      mutantCodeIndex = 0;
      return;
    }
  }
}

function defineFirstCodesNotAnalyze() {
  let N = project.originalCodes.length;
  for (let i = 0; i < N; i++) {
    let originalCode = project.originalCodes[i];
    let mutantsLength = originalCode.mutantCodes.length;
    for (let j = 0; j < mutantsLength; j++) {
      if (!reportIsComplete(i, j)) {
        originalCodeIndex = i;
        mutantCodeIndex = j;
        return true;
      }
    }
  }
  return false;
}

String.prototype.replaceAll = function(search, replacement) {
  var target = this;
  return target.replace(new RegExp(search, 'g'), replacement);
};

const MAX_DIFF_DIV = 350;
var lastLinesOriginal = [];

function applyDiff() {
  let lines = diffLines(originalCode.getValue(), mutantCode.getValue());
  lastLinesOriginal.forEach(function(line) {
    originalCode.removeLineClass(line, "wrap", "diff-background-style-original");
  });
  lastLinesOriginal = lines;
  let count = 0;
  lines.forEach(function(line) {
    originalCode.addLineClass(line, "wrap", "diff-background-style-original");
    if (count == 0) {
      mutantCode.addLineClass(line, "wrap", "diff-background-style-mutant");
    }
    count++;
  });
  if (lines.length === 0) {
    return;
  }
  let numberOfLines = originalCode.lineCount();
  let diffDiv = $("#diff-div")[0];
  let height = diffDiv.scrollHeight;
  let heightPosition = ((height / numberOfLines) * lines[0]) - (MAX_DIFF_DIV / 2);
  heightPosition = Number.parseInt(heightPosition.toFixed());
  diffDiv.scrollTop = heightPosition;
}

function diffLines(originalCode, mutantCode) {
  let lines = [];
  let linesOriginalCode = originalCode.split("\n");
  let linesMutantCode = mutantCode.split("\n");
  let max = linesOriginalCode.length;
  if (linesMutantCode.length < max) {
    max = linesMutantCode.length;
  }
  let desloc = 0;
  for (let i = 0; i < max; i++) {
    if (linesOriginalCode[i + desloc] !== linesMutantCode[i]) {
      lines.push(i);
      while (linesOriginalCode[i + 1 + desloc] !== linesMutantCode[i + 1]) {
        lines.push(i + desloc + 1);
        desloc++;
      }
    }
  }
  return lines;
}

var underAnalysis = true;

function buttonPlayPress() {
  if (underAnalysis) {
    underAnalysis = false;
    endTime = new Date();
    let report = getReport(originalCodeIndex, mutantCodeIndex);
    report.analysisTime += endTime - startAnalyzeTime;
    let button = d3.select("#button-play").classed("btn-success", false); 
    button.classed("btn-danger", true);
    button.select("i").attr("class", "fa fa-play");
    document.getElementById("button-play-legend").innerText = "Análise em estado de pausa";
  } else {
    underAnalysis = true;
    document.getElementById("button-play-legend").innerText = "Análise em andamento";
    let button = d3.select("#button-play").classed("btn-success", true); 
    button.classed("btn-danger", false);
    button.select("i").attr("class", "fa fa-pause");
    startAnalyzeTime = new Date();
  }
}


$.get("/project/info/" + projectHash, function(data) {
  project = data;
  console.log(project);
  createTable();
  defineFirstCodes();
  loadActualCodes(-1);
});

