var mTimer;
var lastShownMessageId = -1;

function showMessage(chatMessage) {
	$('#div_chat').append("<span id=\"chatMessageSender\" class=\"chatSender\" title=\"" + chatMessage.user.emailAddress + "\">Sender (" 
								+ chatMessage.user.displayName + ")</span>" 
								+ "<span id=\"chatMessageText\" class=\"chatText\">: " + chatMessage.text + '</span><br>');
	lastShownMessageId = chatMessage.messageId;
	console.log("messageId=" + chatMessage.messageId);
}

function send_message() {
	if ($('#messageBox').val() == "") {
		return;
	}
	$.post('send.do', { chatMessage:document.getElementById('messageBox').value }, 
		function () {
			// call back function
			console.log("In the send_message callback. Just clear the textbox and unset the default lastShownMessageId");
			$('#messageBox').val("");
			if (lastShownMessageId == -1) {
				lastShownMessageId = 0;
			}
//if (data.text != "") {
//			showMessage(data);
//			$('#div_chat').scrollTop($('#div_chat')[0].scrollHeight);
//			
		}
	);
}

//Gets the current messages from the server
function new_pollChatText() {
	$.post('poll.do',	{ lastMessageId:lastShownMessageId}, 
		function (data) {
			// call back function
			console.log("In the new_pollChatText callback!");
			if (data) {
				$.each(data, function(key, chatMessage) { 
					console.log(key + ': ' + chatMessage.text);
					showMessage(chatMessage);
				});
				$('#div_chat').scrollTop($('#div_chat')[0].scrollHeight);
			}
			mTimer = setTimeout('new_pollChatText();', 2000); //Refresh our chat in 5 seconds
		}
	);	
}

$('#btn-send').click( function() {
	console.log("A button was clicked. Finally, some logging!");
	//send_message();
	new_send_message();
});

$('#btn-poll').click( function() {
	console.log("A button was clicked. Start long-poll request/reponse for new messages.");
	longPollChatText();
});

$('#form-chat').submit( function() {
	console.log("A form actually block-submitted. Finally!");
	send_message();
	return false;
});

$(document).ready( function() {
	console.log("doing some focussing");
	$('#messageBox').focus();
	//new_pollChatText();
	//longPollChatText();
});

function showBasicMessage(chatMessage) {
	$('#div_chat').append("<span id=\"chatMessageSender\" class=\"chatSender\" title=\"" + "_userEmailAddress" + "\">Sender (" 
								+ "_userDisplayName" + ")</span>" 
								+ "<span id=\"chatMessageText\" class=\"chatText\">: " + chatMessage + '</span><br>');
}

function new_send_message() {
	if ($('#messageBox').val() == "") {
		return;
	}
	$.post('chatMessageServlet', { chatMessage:document.getElementById('messageBox').value }, 
		function (data) {
			// call back function
			console.log("In the send_message callback. Clear the textbox");
			$('#messageBox').val("");
			console.log("data=" + data);
			if (lastShownMessageId == -1) {
				lastShownMessageId = 0;
			}
			console.log("Call longPollChatText");
			longPollChatText();
		}
	);
}

//Gets the current messages from the server
function longPollChatText() {
	$.post('chatMessageServlet', { }, 
		function (data) {
			// call back function
			console.log("In the longPollChatText callback!");
			if (data) {
				$.each(data, function(key, chatMessage) { 
					console.log(key + ': ' + chatMessage);
					showBasicMessage(chatMessage);
				});
				$('#div_chat').scrollTop($('#div_chat')[0].scrollHeight);
			}
			longPollChatText();
		}
	);	
}

