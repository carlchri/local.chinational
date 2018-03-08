$(document).ready(function(){
    $('#contact-us-submit').click(function() {
        var senderEmailAddress= $('#senderEmailAddress').val() ;
        $.ajax({
             type: 'GET',
             url:'/bin/services/contactUs',
             data:'senderEmailAddress='+ senderEmailAddress,
             success: function(msg){
                $("#message").replaceWith(msg)
             }
         });
     });
});
