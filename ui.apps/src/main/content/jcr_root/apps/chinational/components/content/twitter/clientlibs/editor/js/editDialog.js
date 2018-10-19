(function (document, $, ns) {
    "use strict";
    
    // This function will replace all instances of '@' character from twitter user
    
    $(document).on("click", ".cq-dialog-submit", function (e) {
       
    		var $form 		= $(this).closest("form.foundation-form");
        	var	twitterUser = $form.find("[name='./user']").val();
        	
        	twitterUser = twitterUser.replace( /\@/g, "" );		// g = all instances found
        	$form.find("[name='./user']").val(twitterUser);
    });
    
})(document, Granite.$, Granite.author);