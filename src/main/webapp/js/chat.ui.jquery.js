$(document).ready(function() { 
 
    $('#btn-submit').click(function() {  
 
        $(".error").hide();
        var hasError = false;
        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
 
        var emailaddressVal = $("#user_email_address").val();
        if(emailaddressVal == '') {
            $("#user_email_address").after('<span class="error">Please enter your email address.</span>');
            hasError = true;
        }
 
        else if(!emailReg.test(emailaddressVal)) {
            $("#user_email_address").after('<span class="error">Enter a valid email address.</span>');
            hasError = true;
        }
        
        var displayNameVal = $("#user_display_name").val();
        if(displayNameVal == '') {
            $("#user_display_name").after('<span class="error">Please enter a Display Name!</span>');
            hasError = true;
        }
 
        if(hasError == true) { return false; }
 
    });
});