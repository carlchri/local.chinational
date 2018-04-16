$(document).ready(function() {
    // WARNING - ONLY DETECTS IF IE 11 OR LESS.
    isIE();
    var isIE11 = navigator.userAgent.indexOf(".NET CLR") > -1;
    var isIE11orLess = isIE11 || navigator.appVersion.indexOf("MSIE") != -1;

    function isIE() {
        if (isIE11orLess == true) {
            $('html').addClass('isIE');
        } else {
            $('html').addClass('isNotIE');
        }
        return isIE11orLess;
    }

    if (isIE()) {

        $('.full-image').each(function() {
        	console.log("is not IE");
            var $container = $(this),
                imgUrl = $container.find('img').attr('src');
            if (imgUrl) {
                $container
                    .css('backgroundImage', 'url(' + imgUrl + ')')
                    .addClass('ie-object-fit');
            }
        });
    }
});