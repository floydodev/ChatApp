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

$('#btn-send').click( function() {
	console.log("A button was clicked. Finally, some logging!");
	send_message();
});

$('#form-chat').submit( function() {
	console.log("A form actually block-submitted. Finally!");
	send_message();
	return false;
});

$(document).ready( function() {
	console.log("doing some focussing");
	$('#messageBox').focus();
	new_pollChatText();
});	


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

