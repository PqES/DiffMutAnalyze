/**
 * @description Representa um nó interno da árvore de diretórios, 
 * portanto representa um diretório.
 */
class InternalNodeTree {
  constructor(name, nodes) {
    this.name = name;
    this.nodes = nodes;
  }
}

/**
 * @description Recupera o nome do nó.
 * 
 * @return {string} Nome do nó.
 */
InternalNodeTree.prototype.getName = function() {
  return this.name;
}

/**
 * @description Representa um nó folha, um arquivo Java com a sua 
 * lista de mutantes.
 */
class LeafNodeTree {
  constructor(file, mutants) {
    this.file = file;
    this.mutants = mutants;
  }
}


class UnitReport {
  constructor() {
    this.time = 0;
  }
}

UnitReport.prototype.isComplete = function() {
  return this.difficulty !== undefined && this.equivalent !== undefined;
};

/**
 * @description Recupera o nome do nó.
 * 
 * @return {string} Nome do nó.
 */
LeafNodeTree.prototype.getName = function() {
  return this.file.name;
};

LeafNodeTree.prototype.createReports = function() {
  this.reports = [];
  let N = this.mutants.length;
  for (let i = 0; i < N; i++) {
    this.reports.push(new UnitReport());
  }
};

/**
 * String da extensão Java.
 */
const JAVA_EXTENSION = "java";
/**
 * Nome do diretório avô de um mutante.
 */
const MUTANT_DIRECTORY = "mutants";
/**
 * Palavra chave para identificar arquivos de teste.
 */
const TEST = 'test';

/**
 * @description Verifica se um determinado arquivo possui extensão Java.
 * 
 * @param {File} file Arquivo a ser verificada extensão.
 * 
 * @return {boolean} true, caso o arquivo possui extensão Java, caso contrário false.
 */
function isJavaExtension(file) {
  return file.name.split(".").pop() === JAVA_EXTENSION;
}

/**
 * @description Tokeniza os nomes de diretórios, subdiretórios e do próprio arquivo 
 * em um array de strings. 
 * 
 * @param {File} file Arquivo a ser tokenizado.
 * 
 * @return {string[]} Array com os tokens do arquivo.
 */
function splitBySeparatorDirectory(file) {
  return file.webkitRelativePath.split("/");
}

/**
 * @description Verifica se um arquivo Java é um arquivo Mutante.
 * A regra para ser mutante é que deve possuir um diretório chamado 'mutants' no seu 
 * caminho seguido de um diretório com um número de identificação deste mutante. 
 * Removendo estes dois diretórios o mutante deve ficar com o mesmo caminho do original.
 * 
 * @param {File} file Arquivo a ser verificado se é um mutante.
 * 
 * @return {boolean} true se é um arquivo Java mutante, caso contrário false.
 */
function isMutant(file) {
  if (!isJavaExtension(file)) {
    return false;
  }
  let tokens = splitBySeparatorDirectory(file);
  let N = tokens.length - 2;
  for (let i = 0; i < N; i++) {
    if (tokens[i] === MUTANT_DIRECTORY) {
      if (tokens[i + 1] === Number.parseInt(tokens[i + 1]).toString()) {
        return true;
      }
      return false;
    }
  }
  return false;
}

/**
 * @description Verifica se um arquivo Java não é um arquivo Mutante.
 * A regra para ser mutante é que o seu diretório avô deve chamar 'mutant'.
 * 
 * @param {File} file Arquivo a ser verificado se não é um mutante.
 * 
 * @return {boolean} true se o arquivo Java não é mutante, caso contrário false.
 */
function isNotMutant(file) {
  return !isMutant(file);
}

/**
 * @description Verifica se um determinado arquivo Java não é um arquivo de teste.
 * A regra para ser arquivo de teste é que deve conter a palavra-chave 'test' em 
 * seu caminho relativo.
 * 
 * @param {File} javaFile Arquivo Java a ser verificado se não é um arquivo de teste.
 * 
 * @return {boolean} true se o arquivo Java não é de teste, caso contrário false.
 */
function isNotJavaTest(javaFile) {
  return !(javaFile.webkitRelativePath.toLowerCase().includes(TEST));
}

/**
 * @description Filtra uma lista de arquivos em uma lista de arquivos de extensão Java.
 * 
 * @param {File[]} allFiles Lista de arquivos a serem filtrados.
 * 
 * @return {File[]} Lista de arquivos de extensão Java.
 */
function filterJavaFiles(allFiles) {
  return allFiles.filter(isJavaExtension).filter(isNotJavaTest);
}

/**
 * @description Verifica se um determinado token do caminho relativo de um arquivo é um 
 * token valido para um arquivo Java. Os tokens inválidos para o caminho Java são os que
 * não constituem nem o caminho do projeto e nem o pacote do arquivo Java, ou seja, os 
 * considerados inválidos são { 'src', 'main', 'java' }.
 * 
 * @param {string} token Token do caminho relativo de um arquivo.
 * 
 * @return {boolean} true se o token do arquivo Java é valido, caso contrário false.
 */
function validyJavaToken(token) {
  return token !== 'src' && token !== 'main' && token !== 'java';
}

/**
 * @description Tokeniza os nomes de diretórios, subdiretórios e do próprio arquivo 
 * de um determinado arquivo Java em um array de strings ignorando os diretórios que
 * não fazem parte de um pacote do arquivo Java (exemplo: src, main, java).
 * 
 * @param {File} javaFile Arquivo Java a ser tokenizado.
 * 
 * @return {string[]} Array com os tokens do arquivo Java ignorando os diretórios 
 * que não fazem parte do pacote do arquivo.
 */
function getTokensJavaFile(javaFile) {
  return splitBySeparatorDirectory(javaFile).filter(validyJavaToken);
}

/**
 * @description Tokeniza os nomes de diretórios, subdiretórios e do próprio arquivo 
 * de um determinado mutante em um array de strings ignorando os dois diretórios que
 * o identificam como mutante o diretório 'mutants' e o diretório subsequente com seu
 * identificador. 
 * 
 * @param {File} mutant Arquivo mutante a ser tokenizado.
 * 
 * @return {string[]} Array com os tokens do arquivo mutante ignorando os diretórios 
 * próprios de um mutante.
 */
function getTokensMutant(mutant) {
  let tokensMutant = getTokensJavaFile(mutant);
  let tokensMutantFiltered = [];
  let N = tokensMutant.length;
  let i = 0;
  while (tokensMutant[i] !== MUTANT_DIRECTORY) {
    tokensMutantFiltered.push(tokensMutant[i]);
    i++;
  }
  for (i += 2; i < N; i++) {
    tokensMutantFiltered.push(tokensMutant[i]);
  }
  return tokensMutantFiltered;
}

/**
 * @description Verifica se um determinado mutante é o mutante de um determinado arquivo Java.
 * 
 * @param {File} file Arquivo Java a ser verificado.
 * @param {File} mutant Mutante a ser verificado.
 * 
 * @return {boolean} true, se o mutante é mutante do arquivo Java, caso contrário false.
 */
function isMyMutant(file, mutant) {
  if (!isJavaExtension(file) || isNotMutant(mutant) || file.name !== mutant.name) {
    return false;
  }
  let tokensFile = getTokensJavaFile(file);
  let tokensMutant = getTokensMutant(mutant);
  let N = tokensFile.length;
  if (N !== tokensMutant.length) {
    return false;
  }
  for (let i = 0; i < N; i++) {
    if (tokensFile[i] !== tokensMutant[i]) {
      return false;
    }
  }
  return true;
}

/**
 * @description Recupera o número identificador de um determinado mutante. O número 
 * identificador de um mutante é o nome do diretório subsequente ao diretório 'mutants'.  
 * 
 * @param {File} mutantFile Arquivo mutante a recuperar o identificador.
 * 
 * @return {number} número identificador do mutante.
 */
function getIdMutant(mutantFile) {
  let tokens = splitBySeparatorDirectory(mutantFile);
  let N = tokens.length - 2;
  for (let i = 0; i < N; i++) {
    if (tokens[i] === MUTANT_DIRECTORY) {
      return Number.parseInt(tokens[i + 1]);
    }
  }
  return -1;
}

/**
 * @description Cria um array de LeafNodeTree com os arquivos de extensão Java recebidos por parâmetros,
 * os arquivos mutantes são automaticamente relacionados aos arquivos originais. 
 * 
 * @param {File[]} javaFiles Array de arquivos de extensão Java de um determinado projeto.
 * 
 * @return {LeafNodeTree[]} Array de LeafNodeTree criados com os arquivos de extensão Java do projeto.
 */
function createLeafNodesTree(javaFiles) {
  let leafNodesTree = [];
  let leafNodeFiles = javaFiles.filter(isNotMutant);
  leafNodeFiles.sort((a, b) => a.webkitRelativePath.localeCompare(b.webkitRelativePath));
  let leafNodeMutants = javaFiles.filter(isMutant);
  let N = leafNodeMutants.length;
  let removedMutantsIndex = new Array(N);
  removedMutantsIndex.fill(false);;
  leafNodeFiles.forEach(function(file, indexFile) {
    let mutants = [];
    let index = 0;
    leafNodeMutants.forEach(function(mutant, indexMutant) {
      if (!removedMutantsIndex[indexMutant] && isMyMutant(file, mutant)) {
        mutants.push({ file: mutant, id: getIdMutant(mutant) });
        removedMutantsIndex[indexMutant] = true;
      }
    });
    mutants.sort((a, b) => a.id - b.id);
    leafNodesTree.push(new LeafNodeTree(file, mutants));
  });
  return leafNodesTree;
}

/**
 * @description Recupera um determinado diretório da árvore de diretórios, caso o diretório
 * não exista, cria o diretório e recupera. 
 * 
 * @param {InternalNodeTree} parentNode Nó pai do diretório a ser recuperado.
 * @param {string} directoryName Nome do diretório a ser recuperado.
 * 
 * @return {LeafNodeTree} Diretório recuperado pelo seu nome e seu nó pai.
 */
function getOrCreateNodeDirectory(parentNode, directoryName) {
  let nodes = parentNode.nodes;
  let N = nodes.length;
  for (let i = 0; i < N; i++) {
    if (nodes[i].name !== undefined && nodes[i].name === directoryName) {
      return nodes[i];
    }
  }
  let newNode = new InternalNodeTree(directoryName, []);
  nodes.push(newNode);
  return newNode;
}

/**
 * @description Insere um determinado nó folha na árvore de diretórios.
 * 
 * @param {InternalNodeTree} rootNode Nó raíz da árvore de diretórios. 
 * @param {LeafNodeTree} leafNode Nó folha a ser inserido na árvore de diretórios.
 */
function addLeafNode(rootNode, leafNode) {
  let tokens = splitBySeparatorDirectory(leafNode.file);
  tokens = tokens.slice(1, tokens.length - 1);
  let actualNode = rootNode;
  tokens.forEach(function(directory) {
    actualNode = getOrCreateNodeDirectory(actualNode, directory);
  });
  actualNode.nodes.push(leafNode);
}

/**
 * @description Ordena a árvore de diretórios por ordem alfabética.
 * 
 * @param {InternalNodeTree} rootNode Nó raíz da árvore de diretórios.
 */
function sortTree(rootNode) {
  rootNode.nodes.sort(function(a, b) {
    return a.getName().localeCompare(b.getName());
  });
  rootNode.nodes.forEach(function(node, index, array) {
    if (node.nodes !== undefined) {
      sortTree(node);
    }
  });
}

/**
 * @description Cria a árvore de diretórios com o array dos nós folhas da árvore.
 * 
 * @param {LeafNodeTree[]} leafNodesTree Array de nós folhas da árvore de diretórios.
 * 
 * @return {InternalNodeTree} Nó raiz da árvore de diretórios.
 */
function createInternalNodesTree(leafNodesTree) {
  if (leafNodesTree.length === 0) {
    return undefined;
  }
  let rootNode = new InternalNodeTree(splitBySeparatorDirectory(leafNodesTree[0].file)[0], []);
  leafNodesTree.forEach(function(leafNode) {
    addLeafNode(rootNode, leafNode);
  });
  return rootNode;
}

/**
 * @description Cria a árvore de diretórios baseado em uma array de arquivos, 
 * filtrando estes arquivos apenas para arquivos Java e relacionando arquivos 
 * mutantes com estes arquivos Java.
 * 
 * @param {File[]} files Array de arquivos a ser criado árvore de diretórios.
 * 
 * @return {(InternalNodeTree, LeafNodeTree[])} Nó raiz da árvore de diretórios, e lista de folhas
 * da árvore (arquivos Java).
 */
function createTree(files) {
  let javaFiles = filterJavaFiles(files);
  let leafNodes = createLeafNodesTree(javaFiles);
  let rootNode = createInternalNodesTree(leafNodes);
  return {
    rootNode: rootNode,
    leafNodes: leafNodes
  };
}

function isNodeLeaf(node) {
  return node.name === undefined;
}

function joinOnlyChildWithParent(rootNode) {
  let N = rootNode.nodes.length;
  while (N === 1) {
    if (!isNodeLeaf(rootNode.nodes[0])) {
      rootNode.name += '/' + rootNode.nodes[0].name;
      rootNode.nodes = rootNode.nodes[0].nodes;
    } else {
      break;
    }
    N = rootNode.nodes.length;
  }
  if (isNodeLeaf(rootNode)) {
    return;
  }
  for (let i = 0; i < N; i++) {
    if (!isNodeLeaf(rootNode.nodes[i])) {
      joinOnlyChildWithParent(rootNode.nodes[i]);
    }
  }
}

function createTreeWithoutJavaNoMutants(files) {
  let javaFiles = filterJavaFiles(files);
  let leafNodes = createLeafNodesTree(javaFiles).filter(node => node.mutants.length > 0);
  let rootNode = createInternalNodesTree(leafNodes);
  joinOnlyChildWithParent(rootNode);
  return {
    rootNode: rootNode,
    leafNodes: leafNodes
  };
}

var idCount;

function isDirectory(node) {
  return (node.name !== undefined) || (node.original !== undefined &&
    node.original.name !== undefined);
}

function insertViewAttrsLeafNode(leafNode) {
  leafNode.text = leafNode.getName();
  leafNode.id = idCount;
  leafNode.icon = "jstree-file";
  leafNode.createReports();
  idCount++;
}

function insertViewAttrsInternNode(internNode) {
  if (internNode === undefined) {
    return;
  }
  internNode.id = idCount;
  internNode.text = internNode.getName();
  internNode.children = internNode.nodes;
  internNode.state = { opened: false };
  idCount++;
  internNode.children.forEach(function(child) {
    if (isDirectory(child)) {
      insertViewAttrsInternNode(child);
    } else {
      insertViewAttrsLeafNode(child);
    }
  });
}

function createTreeView(files) {
  idCount = 0;
  let tree = createTreeWithoutJavaNoMutants(files);
  insertViewAttrsInternNode(tree.rootNode);
  return tree;
}

/**
 * @description Converte uma estrutura FileList em um array simples de File.
 * 
 * @param {FileList} fileList Estrutura FileList a ser convertida.
 * 
 * @return {File[]} Array de File convertido.
 */
function fileListToArray(fileList) {
  let files = [];
  let len = fileList.length;
  for (let i = 0; i < len; i++) {
    files.push(fileList[i]);
  }
  return files;
}

function loadMutant(mutantFile) {
  let readerMutant = new FileReader();
  readerMutant.onload = function(event) {
    mutantCode.setValue(event.target.result);
    applyDiff();
  };
  readerMutant.readAsText(mutantFile);
}

function loadLeaf(node) {
  let reader = new FileReader();
  reader.onload = function(event) {
    originalCode.setValue(event.target.result);
    if (node.mutants.length === 0) {
      return;
    }
  };
  reader.readAsText(node.file);
}


function setReportView(oldIndex, oldMutantIndex) {
  let report = leafNodes[oldIndex].reports[oldMutantIndex];
  let equivalent = $('#radio-equivalent').prop('checked');
  let not_equivalent = $('#radio-not-equivalent').prop('checked');
  report.equivalent = undefined;
  if (equivalent) {
    report.equivalent = true;
  } else if (not_equivalent) {
    report.equivalent = false;
  }
  if (difficultySelected === undefined) {
    report.difficulty = undefined;
  } else {
    report.difficulty = parseInt(difficultySelected[difficultySelected.length - 1]);
  }
  let cell = $('#' + oldIndex + '-' + oldMutantIndex)[0];
  console.log(cell);
  cell.classList.remove('mutant-not-analyze');
  if (leafNodes[oldIndex].reports[oldMutantIndex].isComplete()) {
    cell.classList.remove('mutant-skip');
    cell.classList.add('mutant-analyze');
  } else {

    cell.classList.remove('mutant-analyze');
    cell.classList.add('mutant-skip');
  }
}

function reloadReportView() {
  $('#radio-not-equivalent').prop('checked', false);
  $('#radio-equivalent').prop('checked', false);
  let report = leafNodes[index].reports[mutantIndex];
  if (report.equivalent !== undefined) {
    if (report.equivalent === true) {
      $('#radio-equivalent').prop('checked', true);
    } else {
      $('#radio-not-equivalent').prop('checked', true);
    }
  }
  if (difficultySelected !== undefined) {
    $('#' + difficultySelected)[0].classList.remove('btn-selected');
    difficultySelected = undefined;
  }
  if (report.difficulty !== undefined) {
    $('#likert-' + report.difficulty)[0].classList.add('btn-selected');
    difficultySelected = 'likert-' + report.difficulty;
  }
}

function changeAnalyzeObj(oldIndex, oldMutantIndex) {
  setReportView(oldIndex, oldMutantIndex);
  endTime = new Date();
  leafNodes[oldIndex].reports[oldMutantIndex].time += endTime - beginAnalyzeTime;
  if (oldIndex != index) {
    loadLeaf(leafNodes[index]);
  }
  loadMutant(leafNodes[index].mutants[mutantIndex].file);
  reloadReportView();
  beginAnalyzeTime = new Date();
}

function loadNext() {
  let oldIndex = index;
  let oldMutantIndex = mutantIndex;
  mutantIndex++;
  if (mutantIndex === leafNodes[index].mutants.length) {
    index++;
    if (index === leafNodes.length) {
      mutantIndex--;
      index--;
      toastr.success('Todas classes do projeto foram analisadas!', 'Projeto analisado!');
      return;
    }
    mutantIndex = 0;
    toastr.info('Classe a ser analisada: "' + leafNodes[index].file.name + '"!', 'Próxima classe!');
  }
  toastr.info('Mutante a ser analisado: ' + leafNodes[index].mutants[mutantIndex].id + '!', 'Próximo mutante!');
  changeAnalyzeObj(oldIndex, oldMutantIndex);
}

function loadPrevious() {
  let oldIndex = index;
  let oldMutantIndex = mutantIndex;
  mutantIndex--;
  if (mutantIndex === -1) {
    index--;
    if (index === -1) {
      index = 0;
      mutantIndex = 0;
      toastr.error('Este é o primeiro mutante a ser analisado.', 'Não é possível voltar!');
      return;
    }
    mutantIndex = leafNodes[index].mutants.length - 1;
    toastr.info('Classe a ser analisada: "' + leafNodes[index].file.name + '"!', 'Classe anterior!');
  }
  toastr.info('Mutante a ser analisado: ' + leafNodes[index].mutants[mutantIndex].id + '!', 'Mutante anterior!');
  changeAnalyzeObj(oldIndex, oldMutantIndex);
}


function getIndex(node) {
  let N = leafNodes.length;
  let id = node.id;
  for (let i = 0; i < N; i++) {
    if (leafNodes[i].id === id) {
      return i;
    }
  }
  return -1;
}

const MAX_DIFF_DIV = 350;
var lastLinesOriginal = [];

function applyDiff() {
  let lines = diffLines(originalCode.getValue(), mutantCode.getValue());
  //console.log("DIFF_LINES -> " + lines.length);
  // if (lines.length > 1) {
  //   alert("Diff lines length -> " + lines.length);
  // }
  lastLinesOriginal.forEach(function(line) {
    originalCode.removeLineClass(line, "wrap", "diff-background-style-original");
  });
  lastLinesOriginal = lines;
  let count = 0;
  lines.forEach(function(line) {
    //console.log("applyclass -> " + line);
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

function diffLines(original, mutant) {
  let lines = [];
  let linesOriginal = original.split("\n");
  let linesMutant = mutant.split("\n");
  let max = linesOriginal.length;
  if (linesMutant.length < max) {
    max = linesMutant.length;
  }
  //console.log("LINHAS -> " + max);
  let desloc = 0;
  for (let i = 0; i < max; i++) {
    if (linesOriginal[i + desloc] !== linesMutant[i]) {
      lines.push(i);
      while (linesOriginal[i + 1 + desloc] !== linesMutant[i + 1]) {
        lines.push(i + desloc + 1);
        desloc++;
      }
    }
  }
  return lines;
}