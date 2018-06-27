(function ($) {
    'use strict';
	
	//this is selector values and should be defined as granite:class property on dialog datepicker field
    var TO_EMAIL_VALUE = '.cmp-pbwf--editor-to-email'; 
    var PROP_ERROR_MESSAGE = 'error-message';

    $.validator.register({
        selector: TO_EMAIL_VALUE,
        validate: function (el) {
            var	toEmail = el.val(),
            	patterns = {
             		emailRegex: /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/
        		};
            if(!patterns.emailRegex.test(toEmail)) {
                return el.data(PROP_ERROR_MESSAGE);
            }
        }
    });

})(jQuery);
