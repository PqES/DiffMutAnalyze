var rootNode;
var leafNodes;
var index;
var mutantIndex;
var beginAnalyzeTime;
var difficultySelected = undefined;

function likertSelect(elem) {
  if (difficultySelected !== undefined) {
    $('#' + difficultySelected)[0].classList.remove('btn-selected');
  }
  difficultySelected = elem.id;
  elem.classList.add('btn-selected');
}

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

document
  .getElementById("file-url")
  .addEventListener(
    "change",
    function (e) {
      let files = fileListToArray(e.target.files);
      let tree = createTreeView(files);
      if (tree.rootNode === undefined) {
        toastr.error('Não foi possível abrir o projeto!', 'Error!');
        return;
      }
      rootNode = tree.rootNode;
      $("#project-name").val(rootNode.text);
      leafNodes = tree.leafNodes;
      index = 0;
      mutantIndex = 0;
      loadLeaf(leafNodes[index]);
      loadMutant(leafNodes[index].mutants[mutantIndex].file);
      createTable();
      beginAnalyzeTime = new Date();
      toastr.info('Classe a ser analisada: "' +
        leafNodes[index].file.name + '"!',
        'Primeira classe!');
      let treeRow = document.getElementById("tree-row");
      while (treeRow.firstChild) {
        treeRow.removeChild(treeRow.firstChild);
      }
      $("#tree-row").append(
        '<div id="tree" class="demo"></div>');
      $('#tree')
        .on(
          "changed.jstree",
          function (e, data) {
            if (data.selected.length) {
              let node = data.instance
                .get_node(data.selected[0]);
              if (isDirectory(node)) {
                node.state.opened = !node.state.opened;
              } else {
                let oldIndex = index;
                let oldMutantIndex = mutantIndex;
                index = getIndex(node.original);
                mutantIndex = 0;
                toastr
                  .info(
                    'Classe a ser analisada: "' +
                    leafNodes[index].file.name +
                    '"!',
                    'Nova classe!');
                changeAnalyzeObj(oldIndex,
                  mutantIndex);
              }
            }
          }).jstree({
          core: {
            data: [rootNode]
          }
        });

    }, false);

console.log("RUN ANALYZE");

function focusNotAnalyze() {
  let N = leafNodes.length;
  for (let i = 0; i < N; i++) {
    let leafNode = leafNodes[i];
    let mutantsLength = leafNode.mutants.length;
    for (let j = 0; j < mutantsLength; j++) {
      if (!leafNode.reports[j].isComplete()) {
        $('#' + i + '-' + j).focus();
        return;
      }
    }
  }
}

function biggerMutantList() {
  let N = leafNodes.length;
  let bigger = leafNodes[0].mutants.length;
  for (let i = 1; i < N; i++) {
    let length = leafNodes[i].mutants.length;
    if (length > bigger) {
      bigger = length;
    }
  }
  return bigger;
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
    cell.innerHTML = i;
    row.appendChild(cell);
  }
  // BODY
  let tableBody = document.getElementById('table-body');
  let numRows = leafNodes.length;
  let cellButton;
  for (i = 0; i < numRows; i++) {
    row = tableBody.insertRow(i);
    let j = 0;
    cell = document.createElement('th');
    cell.innerHTML = leafNodes[i].getName();
    row.appendChild(cell);
    let mutantsLength = leafNodes[i].mutants.length + 1;
    for (j++; j < mutantsLength; j++) {
      cell = row.insertCell(j);
      cellButton = document.createElement('button');
      //cell.id = i + "," + (j - 1);
      cellButton.classList.add('btn');
      cellButton.classList.add('btn-default');
      cellButton.classList.add('mutant-not-analyze');
      cellButton.id = i + "-" + (j - 1);
      cell.appendChild(cellButton);
      cellButton.addEventListener('click', function (event) {
        let oldIndex = index;
        let oldMutantIndex = mutantIndex;
        console.log(event);
        let tokens = event.target.attributes.id.value.split("-");
        index = parseInt(tokens[0]);
        mutantIndex = parseInt(tokens[1]);
        console.log(event);
        changeAnalyzeObj(oldIndex, oldMutantIndex);
      });
    }
  }
}

function timeFormat(time) {
  let millisegundos = time % 1000;
  let segundos = Math.floor(time / 1000);
  let minutos = Math.floor(segundos / 60);
  segundos % 60;
  return minutos + "m" + segundos + "s" + millisegundos;
}

function simulateData() {
  let N = leafNodes.length;
  for (let i = 0; i < N; i++) {
    let mutantsN = leafNodes[i].mutants.length;
    for (let j = 0; j < mutantsN; j++) {
      let report = leafNodes[i].reports[j];
      if (!report.isComplete()) {
        report.time = Math.floor(Math.random() * 1000 * 60 * 25) + 1;
        report.difficulty = Math.floor(Math.random() * 5) + 1;
        report.equivalent = Math.floor(Math.random() * 2);
        if (report.equivalent == 0) {
          report.equivalent = false;
        } else {
          report.equivalent = true;
        }
      }
    }
  }

}

function createTableReport() {

  simulateData();

  let N = leafNodes.length;
  let tableBodyMutant = document
    .getElementById('table-body-report-mutante');
  let tableBodyClass = document
    .getElementById('table-body-report-classes');
  let tableBodyGeneral = document.getElementById('table-body-report-gen');
  let row;
  let cell;
  let statusGeneral = {
    "sumTime": 0,
    "numMutants": 0,
    "sumDifficulty": 0,
    "numEquivalent": 0,
    "numNotEquivalent": 0
  }
  let index = 0;
  for (let i = 0; i < N; i++) {
    let status = {
      "sumTime": 0,
      "numMutants": 0,
      "sumDifficulty": 0,
      "numEquivalent": 0,
      "numNotEquivalent": 0
    }
    let mutantsN = leafNodes[i].mutants.length;
    for (let j = 0; j < mutantsN; j++) {
      let report = leafNodes[i].reports[j];
      row = tableBodyMutant.insertRow(index++);
      cell = document.createElement('th');
      cell.innerHTML = leafNodes[i].getName() + "[" + j + "]";
      row.appendChild(cell);
      cell = row.insertCell(1);
      cell.innerHTML = timeFormat(report.time);
      cell = row.insertCell(2);
      cell.innerHTML = report.difficulty;
      cell = row.insertCell(3);
      cell.innerHTML = report.equivalent;
      // status
      status.sumTime += report.time;
      status.sumDifficulty += report.difficulty;
      if (report.equivalent) {
        status.numEquivalent++;
      } else {
        status.numNotEquivalent++;
      }
      status.numMutants++;
    }
    row = tableBodyClass.insertRow(i);
    cell = document.createElement('th');
    cell.innerHTML = leafNodes[i].getName();
    row.appendChild(cell);
    cell = row.insertCell(1);
    cell.innerHTML = timeFormat(status.sumTime);
    cell = row.insertCell(2);
    cell.innerHTML = timeFormat(Math.floor(status.sumTime /
      status.numMutants));
    cell = row.insertCell(3);
    cell.innerHTML = status.sumDifficulty / status.numMutants;
    cell = row.insertCell(4);
    cell.innerHTML = status.numEquivalent;
    cell = row.insertCell(5);
    cell.innerHTML = status.numNotEquivalent;
    // status general
    statusGeneral.sumTime += status.sumTime;
    statusGeneral.sumDifficulty += status.sumDifficulty;
    statusGeneral.numEquivalent += status.numEquivalent;
    statusGeneral.numNotEquivalent += status.numNotEquivalent;
    statusGeneral.numMutants += status.numMutants;
  }
  row = tableBodyGeneral.insertRow(0);
  cell = row.insertCell(0);
  cell.innerHTML = timeFormat(statusGeneral.sumTime);
  cell = row.insertCell(1);
  cell.innerHTML = timeFormat(Math.floor(statusGeneral.sumTime /
    statusGeneral.numMutants));
  cell = row.insertCell(2);
  cell.innerHTML = statusGeneral.sumDifficulty / statusGeneral.numMutants;
  cell = row.insertCell(3);
  cell.innerHTML = statusGeneral.numEquivalent;
  cell = row.insertCell(4);
  cell.innerHTML = statusGeneral.numNotEquivalent;
  $("#report")[0].removeAttribute('hidden');
}