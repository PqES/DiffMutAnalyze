let inputs = document.getElementsByClassName("analyze-input");
let link = window.location.href;
let lastIndexHostname = link.indexOf(window.location.hostname) + window.location.hostname.length;
if (link.charAt(lastIndexHostname) === ':') {
  lastIndexHostname = link.indexOf('/', lastIndexHostname);
}
let hostname =  link.substring(0, lastIndexHostname);
for (let input of inputs) {
  input.value = hostname + input.value;
  input.style.width = input.value.length * 8 + "px";
}

function copyData(inputDataId) {
  let copyText = document.getElementById(inputDataId);
  copyText.select();
  document.execCommand("copy");
}