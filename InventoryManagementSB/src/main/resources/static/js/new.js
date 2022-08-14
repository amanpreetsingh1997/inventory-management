const fillOptions = document.getElementsByClassName("setQuantity");
const quantity = document.getElementsByClassName("getQuantity");
const box = document.getElementsByClassName('toggleBoxes');
const booleanInputs = document.getElementsByClassName('booleanValues');
var contents;
for (let i = 0; i <= fillOptions.length; i++) {
	for (let j=1; j <= quantity[i].innerText; j++) {
		contents += "<option>" + j + "</option>";
	}
	fillOptions[i].innerHTML = contents;
	contents = "";
}

function validate(){
	for(let i=0;i<box.length;i++)
		if(box[i].checked)
			booleanInputs[i].value = "true";
}