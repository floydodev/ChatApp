var request, ie;
var firstTime = true;
var lastShownMessageId = -1;

function newRequest() {
	var httpRequest;
	if (window.XMLHttpRequest) { // Mozilla, Safari, ...
		ie = 0;
		httpRequest = new XMLHttpRequest();
		if (httpRequest.overrideMimeType) {
			httpRequest.overrideMimeType('text/xml');
		}
	}
	else { // IE
		ie = 1;
		try {
			httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
		}
		catch (e) {}
		
		if ( typeof httpRequest == 'undefined' ) {
		
			try {
				httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
			}
			catch (f) {}
		}
	}
	if (!httpRequest) {
		alert('Giving up :( Cannot create an XMLHTTP instance');
		return false;
	}
	else {
		return httpRequest ;
	}

}

function showMessage(chatMessage) {
	$('#div_chat').append("<span id=\"chatMessageSender\" class=\"chatSender\" title=\"" + chatMessage.user.emailAddress + "\">Sender (" 
								+ chatMessage.user.displayName + ")</span>" 
								+ "<span id=\"chatMessageText\" class=\"chatText\">: " + chatMessage.text + '</span><br>');
	lastShownMessageId = chatMessage.messageId;
	console.log("bbb messageId=" + chatMessage.messageId);
}

/* this function receives the chat message and writes it to the text area */
function receive(request) {
	//var txt=document.getElementById("div_chat");
	//txt.innerHTML=txt.innerHTML+request.responseText;
	var myJSONResponse = JSON.parse(request.responseText);
	console.log('myJSONResponse=' + myJSONResponse);
	$.each(myJSONResponse, function(key, chatMessage) { 
		console.log(key + ': ' + chatMessage);
		console.log(key + ': chatMessage.text=' + chatMessage.text);
		showMessage(chatMessage);
	});
	send('connect');	/* current request is dead, must create a new one */
}

function handleSubmit() {
	console.log("Handle the submission. Return false to prevent actual form submission " +
			"(which causes session attribute to be removed)");
//	if (firstTime) {
//		firstTime = false;
//		console.log('Try sending twice first time around to see if that creates connection properly');
//		send('send');
//	}
	send('send');
	return false;
}


/* this function initiates a request, either to send a message or to connect */
function send(arg) {
	var url = 'chatMessageServlet';
	
	if ( typeof request == 'undefined' ) {
	/* create new request */
		request =  newRequest() ;
	}

	request.open("POST", url, true);
	//request.setRequestHeader("Content-Type", "application/x-javascript;");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	
	request.onreadystatechange = function() {
		/* we are interested only in a complete response */
		if (request.readyState >= 4 && request.status == 200) {				
			if (request.responseText) {
				receive(request);
			}	      
		}
	};

	if ( arg.substring(0,4)=="send") {
		arg = document.myForm.messageBox.value;
		document.myForm.messageBox.value="";
		document.myForm.messageBox.focus();
	
		//request.send(arg);
		lastShownMessageId = 0;
		request.send("action=send&chatMessage=" + arg);
	}
	else if (arg.substring(0,7)=="connect") {
		
		request.send("action=poll&lastMessageId=" + lastShownMessageId);
	}
}