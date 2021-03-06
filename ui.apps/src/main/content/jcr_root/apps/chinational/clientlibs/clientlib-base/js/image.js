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
        	// this is for tiles & news list component
            var $container = $(this),
            imgWithPicture = $container.find('picture');
            if (!imgWithPicture || imgWithPicture.length == 0) {
                imgUrl = $container.find('img').attr('src');
                if (imgUrl ) {
                    $container
                        .css('backgroundImage', 'url(' + imgUrl + ')')
                        .addClass('ie-object-fit');
                }
            }
        });

        $('.image-row').each(function() {
            // this is for full bleed top images, which are under picture
            var $container = $(this),
            imgUrl = $container.find('img').attr('src');
            if (imgUrl ) {
                $container
                    .css('backgroundImage', 'url(' + imgUrl + ')')
                    .addClass('ie-object-fit');
            }
        });

     }

});