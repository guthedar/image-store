'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var multipleUploadForm = document.querySelector('#multipleUploadForm');
var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

var singleDeleteError = document.querySelector('#singleDeleteError');
var singleDeleteSuccess = document.querySelector('#singleDeleteSuccess');

var retrieveForm = document.querySelector('#retrieveForm');
var retrievebyteForm = document.querySelector('#retrievebyteForm');

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function retrieveFile(file) {
    var formData = new FormData();
    formData.append("file", file);
    
    console.log("input file name is "+ file);
    var params = "User="+document.getElementById('loggedUser').innerHTML;
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/retrieveFile/"+file+"?"+params);
    xhr.responseType = 'blob';

    xhr.onload = function(e) {
    	  if (this.status == 200) {
    	var blob = this.response;
    	//console.log(blob);
    	 document.getElementById("retrievedImg").src = window.URL.createObjectURL(blob);
    	  }
    	  document.getElementById("snackbar").innerHTML = "A file was retrieved";
    	  showSnackBar();
    	  CreateTableFromJSON();
    	};

    	xhr.onerror = function(e) {
    	  alert("Error " + e.target.status + " occurred while receiving the document.");
    	};

    xhr.send(formData);
    
}

function deleteFile(file) {
    var formData = new FormData();
    formData.append("file", file);
    
    console.log("input file name is "+ file);
    var params = "User="+document.getElementById('loggedUser').innerHTML;
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/delete/"+file+"?"+params);

    xhr.onload = function() {
        //var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
        	/*singleDeleteError.style.display = "none";
        	singleDeleteSuccess.innerHTML = "<p>File Deleted Successfully.</p>;
        	singleDeleteSuccess.style.display = "block";*/
            document.getElementById("snackbar").innerHTML = "A file was deleted";
            showSnackBar();
            CreateTableFromJSON();
        } else {
        	 /*singleFileUploadSuccess.style.display = "none";
             singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";*/
        }
    }

    xhr.send(formData);
}


/*function retrievebyteFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/retrieveByte/"+file);

    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE) {
        	console.log(xhr.responseText);
        	//document.getElementById("retrievedImg").src = window.URL.createObjectURL(xhr.responseText);
        	document.getElementById("retrievedbyteImg").src = "data:image/png;base64," + xhr.responseText;
        }
    }

    xhr.send(formData);
}
*/


function uploadMultipleFiles(files) {
    var formData = new FormData();
    for(var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }
    
    var params = "User="+document.getElementById('loggedUser').innerHTML;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadMultipleFiles"+"?"+params);

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            /*multipleFileUploadError.style.display = "none";
            var content = "<p>All Files Uploaded Successfully</p>";
            for(var i = 0; i < response.length; i++) {
                content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
            }
            multipleFileUploadSuccess.innerHTML = content;
            multipleFileUploadSuccess.style.display = "block";*/
            document.getElementById("snackbar").innerHTML = "A file was uploaded";
            showSnackBar();
            CreateTableFromJSON();
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

/*singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);*/


multipleUploadForm.addEventListener('submit', function(event){
    var files = multipleFileUploadInput.files;
    if(files.length === 0) {
        multipleFileUploadError.innerHTML = "Please select at least one file";
        multipleFileUploadError.style.display = "block";
    }
    uploadMultipleFiles(files);
    event.preventDefault();
}, true);

retrieveForm.addEventListener('submit', function(event){
    var fileName = document.getElementById("retrieveInput").value;
	if(fileName.length === 0) {
        multipleFileUploadError.innerHTML = "Please enter at least one fileName";
        multipleFileUploadError.style.display = "block";
    }
    retrieveFile(fileName);
    event.preventDefault();
}, true);

singleDeleteForm.addEventListener('submit', function(event){
    var fileName = document.getElementById("singleDeleteInput").value;
	if(fileName.length === 0) {
        multipleFileUploadError.innerHTML = "Please enter at least one fileName";
        multipleFileUploadError.style.display = "block";
    }
    deleteFile(fileName);
    event.preventDefault();
}, true);


/*retrievebyteForm.addEventListener('submit', function(event){
	   // var fileName = retrieveForm.value;
		var fileName = 'da39635e-f2db-49cf-9057-e4f96334fa1f';
	    if(fileName.length === 0) {
	        multipleFileUploadError.innerHTML = "Please enter at least one fileName";
	        multipleFileUploadError.style.display = "block";
	    }
	    retrievebyteFile(fileName);
	    event.preventDefault();
	}, true);*/

function CreateTableFromJSON(){
	var req = new XMLHttpRequest();
	req.overrideMimeType("application/json");
	req.open('GET', "/history/"+document.getElementById('loggedUser').innerHTML, true);
	req.onload  = function() {
	   var jsonResponse = JSON.parse(req.responseText);
	   console.log(jsonResponse);
	   CreatedTableComplete(jsonResponse)
	};
	req.send(null);
}


function CreatedTableComplete(jsonResponse) {
    var myImages = jsonResponse;
    //document.getElementById("showData").innerHTML = "";
    // EXTRACT VALUE FOR HTML HEADER. 
    // ('Book ID', 'Book Name', 'Category' and 'Price')
    var col = [];
    for (var i = 0; i < myImages.length; i++) {
        for (var key in myImages[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }
    if(col.length!=0)
    	col.push("Icon");
    // CREATE DYNAMIC TABLE.
    var table = document.createElement("table");
    	table.style.cssText = 'width:550px;';

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

    var tr = table.insertRow(-1);                   // TABLE ROW.

    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.style.cssText = 'height:40px; font-size:1.5em;'
        th.innerHTML = col[i].toUpperCase();
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < myImages.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = myImages[i][col[j]];
            if(j===col.length-1)
            {
	            if(myImages[i]["operation"]==="Upload"){
	            	tabCell.innerHTML = "<img src=\"/images/upload.png\" width=\"25px\" height=\"25px\">";
	            }
	            else if(myImages[i]["operation"]==="Retrieve"){
	            	tabCell.innerHTML = "<img src=\"/images/retrieve.png\" width=\"25px\" height=\"25px\">";
	            }
	            else if(myImages[i]["operation"]==="Download"){
	            	tabCell.innerHTML = "<img src=\"/images/download.png\" width=\"25px\" height=\"25px\">";
	            }
	            else if(myImages[i]["operation"]==="Delete"){
	            	tabCell.innerHTML = "<img src=\"/images/delete.png\" width=\"25px\" height=\"25px\">";
	            }
            }
        }
        
        	
    }

    // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
    var divContainer = document.getElementById("showData");
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

function showSnackBar() {
	  var x = document.getElementById("snackbar");
	  x.className = "show";
	  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
	}



