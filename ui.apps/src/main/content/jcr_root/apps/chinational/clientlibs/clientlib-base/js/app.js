jQuery(document).ready(function() {
    var showChar = 100;
    var ellipsestext = "...";
    var moretext = "more";
    var lesstext = "less";

    jQuery('.menu-icon, .side-menu-close, .overlay').click(function() {
        jQuery('.main-nav').toggleClass('open');
        jQuery('.overlay').fadeToggle(350);
        jQuery('.menu-icon').toggleClass('active');
        if (jQuery('.menu-icon').hasClass('active')) {
            jQuery('.menu-label').text('CLOSE');
        } else {
            jQuery('.menu-label').text('MENU');
        }
    });
    var lastItem;
    $('.header ul li a').click(function(e) {
        var currentItem = $(this);
        jQuery('.header ul li a').removeClass('dd-opened');
        jQuery(this).addClass('dd-opened');
        e.stopPropagation();
        if ($(this).next('.hidden-menu').css('display') == ('none')) {
            $(lastItem).next('.hidden-menu').slideUp();
            $(this).next('.hidden-menu').slideDown();
            jQuery('.header ul li a').removeClass('dd-opened');
            jQuery(this).addClass('dd-opened');
        } else {
            $(this).next('.hidden-menu').slideUp();
            jQuery('.header ul li a').removeClass('dd-opened');
        }
        lastItem = $(this);
    });
    $(document).click(function() {
        jQuery('.hidden-menu', this).slideUp();
        jQuery('.header ul li a').removeClass('dd-opened');
    });
    $('#myDropdown').on('show.bs.dropdown', function() {
        // do something…
    });
    var $owl = $('.owl-carousel');
    $owl.owlCarousel({
        nav: true,
        loop: false,
        dots: false,
        autoHeight: true,
        loop: true,
        autoWidth: true,
        stagePadding: 100,
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            1024: {
                items: 2
            },
            1200: {
                items: 3
            }
        }
    });


    // var active_owl = $('.owl-item.active:last');
    // var last_active = active_owl[active_owl.length - 1];
    // last_active.addClass('opacity_low');
    // owl.on('changed.owl.carousel', function(event) {
    //    console.log('simon');
    //    slopacity;
    // });

    /* Audio Player */
    $('#audio-modal').on('hidden.bs.modal', function() {
        console.log("Hello Simon");
        var ply = document.getElementById('audio-player');

        var oldSrc = ply.src; // just to remember the old source

        ply.src = "";
    });

    $('.audio-player-button').click(function() {
        console.log('Audio Transfer');
        var audio_file = $(this).data('file');
        $('#audio-player').attr('src', audio_file);
        console.log("audio_file: " + audio_file);
    });

    $('#audio-modal').on('show.bs.modal', function() {
        console.log("Hello Simon Modal Show");
    });

    /* Read more function */
    $('.more').each(function() {
        var content = $(this).html();
        if (content.length > showChar) {
            var c = content.substr(0, showChar);
            var h = content.substr(showChar - 1, content.length - showChar);
            var html = c + '<span class="moreellipses">' + ellipsestext + '&nbsp;</span><span class="morecontent"><span>' + h + '</span>&nbsp;&nbsp;<a href="" class="morelink">' + moretext + '</a></span>';
            $(this).html(html);
        }
    });
    $(".morelink").click(function() {
        if ($(this).hasClass("less")) {
            $(this).removeClass("less");
            $(this).html(moretext);
        } else {
            $(this).addClass("less");
            $(this).html(lesstext);
        }
        $(this).parent().prev().toggle();
        $(this).prev().toggle();
        return false;
    });
    $(".more").click(function() {
        $('.more-content').toggleClass('max-length');
    });
    // $(window).resize(function() {
    //     /* carousel image width*/
    //     var screenWidth = screen.width; //get the screen width in pixels
    //     var pixelsPerPercentage = screenWidth * .01; //get the pixels per percentage
    //     var imageWidthInPer = 50; //set the variable of the width of the image in percentages
    //     var imageWidthInPix = imageWidthInPer * pixelsPerPercentage + 40; //set the variable of the width of the image in pixels

    //     $(".show-mobile .sub-Thumbs .audio-container img").width(imageWidthInPix + "px"); //set the width of the image in pixels
    //     console.log(imageWidthInPix);
    // });

})
if (jQuery(window).width() > 992) {
    jQuery(function() {
        createSticky($(".main-nav"));
    });

    function createSticky(sticky) {
        if (typeof sticky !== "undefined") {
            var pos = sticky.offset().top,
                win = $(window);
            win.on("scroll", function() {
                win.scrollTop() >= pos ? sticky.addClass("pstn-fxd") : sticky.removeClass("pstn-fxd");
            });
        }
    }
}