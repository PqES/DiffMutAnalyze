
var difficultyDataset = JSON.parse(document.getElementById("difficulty-dataset").innerText);
var difficultyBarChart = new Chart(document.getElementById("difficulty-bar-chart"), {
  type: 'bar',
  data: {
    labels: [1, 2, 3, 4, 5],
    datasets: [{
      label: "Quantidade de relatórios",
      backgroundColor: ["#3e95cd", "#8e5ea2","#3cba9f","#e8c3b9","#c45850"],
      data: difficultyDataset
    }]
  },
  options: {
    legend: { display: false },
    scales: {
      xAxes: [{
        gridLines: {
          offsetGridLines: true
        }
      }],
      yAxes: [{
        ticks: {
          beginAtZero:true
        }
      }]
    },
    title: {
      display: true,
      text: 'Nível de dificuldade para analisar mutantes'
    }
  }
});

var equivalenceDataset = JSON.parse(document.getElementById("equivalence-dataset").innerText);
var equivalencePieChart = new Chart(document.getElementById("equivalence-pie-chart"), {
  type: 'pie',
  data: {
    labels: ["Equivalente", "Não equivalente"],
    datasets: [{
      label: "Quantidade de relatórios",
      backgroundColor: ["#3e95cd", "#8e5ea2"],
      data: equivalenceDataset
    }]
  },
  options: {
    title: {
      display: true,
      text: 'Equivalência de mutantes nos relatórios dos usuários'
    }
  }
});

var mutantsStatusDataset = JSON.parse(document.getElementById("mutants-status-dataset").innerText);
var mutantsStatusPieChart = new Chart(document.getElementById("mutants-status-pie-chart"), {
  type: 'pie',
  data: {
    labels: ["Vivo", "Morto"],
    datasets: [{
      label: "Quantidade de mutantes",
      backgroundColor: ["#3e95cd", "#8e5ea2"],
      data: mutantsStatusDataset
    }]
  },
  options: {
    title: {
      display: true,
      text: 'Status dos mutantes em relação aos testes de unidade'
    }
  }
});

var mutantsEquivalenceDataset = JSON.parse(document.getElementById("mutants-equivalence-dataset").innerText);
var mutantsEquivalencePieChart = new Chart(document.getElementById("mutants-equivalence-pie-chart"), {
  type: 'pie',
  data: {
    labels: ["Equivalente", "Não equivalente"],
    datasets: [{
      label: "Quantidade de não mutantes",
      backgroundColor: ["#3e95cd", "#8e5ea2"],
      data: mutantsEquivalenceDataset
    }]
  },
  options: {
    title: {
      display: true,
      text: 'Equivalência de mutantes determinada pelo consenso nos relatórios dos usuários'
    }
  }
});

