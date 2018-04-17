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

    // check if container full width
    if ($('div.body-content').hasClass('right-rail-container')) {
        $('profile-carousel').addClass('profile-carousel-rr').removeClass('profile-carousel');
    }



    $('#full-width-container .media-carousel').owlCarousel({
        nav: true,
        // dots: false,
        autoHeight: true,
        margin: 15,
        loop: false,
        autoWidth: true,
        // stagePadding: 10,
        nestedItemSelector: 'slide',
        responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            768: {
                items: 3
            },
            1024: {
                items: 5
            }
        }
    });

    $('#right-rail-container .media-carousel').owlCarousel({
        nav: true,
        // dots: true,
        autoHeight: true,
        margin: 15,
        loop: false,
        autoWidth: true,
        // stagePadding: 100,
        responsiveClass: true,
        nestedItemSelector: 'slide',
        responsive: {
            0: {
                items: 1
            },
            768: {
                items: 2
            }
        }
    });

    $('#full-width-container .media-carousel-mobile').owlCarousel({
        nav: true,
        // loop: false,
        dots: false,
        autoHeight: true,
        // loop: true,
        // autoWidth: true,
        // stagePadding: 100,
        // responsiveClass: true,
        nestedItemSelector: 'slide',
        responsive: {
            0: {
                items: 1
            }
        }
    });
    $('#right-rail-container .media-carousel-mobile').owlCarousel({
        nav: true,
        // loop: false,
        dots: false,
        autoHeight: true,
        // loop: true,
        // autoWidth: true,
        // stagePadding: 100,
        // responsiveClass: true,
        nestedItemSelector: 'slide',
        responsive: {
            0: {
                items: 1
            }
        }
    });
    $('#full-width-container .profile-carousel').owlCarousel({
        nav: true,
        // loop: false,
        dots: false,
        autoHeight: true,
        // loop: true,
        // autoWidth: true,
        // stagePadding: 100,
        // responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            1024: {
                items: 2
            },
            1300: {
                items: 3
            } 
        }
    });
    $('#right-rail-container .profile-carousel').owlCarousel({
        nav: true,
        // loop: false,
        dots: false,
        autoHeight: true,
        // loop: true,
        // autoWidth: true,
        // stagePadding: 100,
        // responsiveClass: true,
        responsive: {
            0: {
                items: 1
            },
            1300: {
                items: 2
            }
        }
    });
    checkClasses();
    $('.owl-carousel').on('translated.owl.carousel', function(event) {
        checkClasses();
        removedActiveItem();
    });
    $(window).resize(function() {
        checkClasses();
        removedActiveItem();
    });

    function checkClasses() {

        var total = $('.media-carousel .owl-stage .owl-item.active').length;
        // var rrOwlActive = $('#right-rail-container .media-carousel .owl-stage .owl-item.active');
        // $('#right-rail-container .media-carousel .owl-stage .owl-item').removeClass('fullOpacity firstActiveItem lastActiveItem');
        // // console.log("Number of Items: " + rrOwlActive.length);

        // if (rrOwlActive.first().width() > 300) {
        //     rrOwlActive.first().addClass('fullOpacity');
        //     rrOwlActive.first().next().removeClass('fullOpacity');
        // } else if (rrOwlActive.first().width() < 300 && rrOwlActive.first().next().width() < 300) {
        //     rrOwlActive.first().addClass('fullOpacity');
        //     rrOwlActive.first().next().addClass('fullOpacity');
        //     // console.log("higer the :" + index + " : " + $(this).width());
        // } else {
        //     rrOwlActive.first().addClass('fullOpacity');
        // }

        var fOwlActive = $('#full-width-container .media-carousel .owl-stage .owl-item.active');
        $('#full-width-container .media-carousel.media-carousel .owl-stage .owl-item').removeClass('fullOpacity firstActiveItem lastActiveItem');
        $('#full-width-container .media-carousel .owl-stage .owl-item.active').each(function(index) {
            // add class to first item
            if (index === 0) {
                // this is the first one
                $(this).addClass('firstActiveItem');
            }
            // if (index === 1) {
            //     // this is the first one
            //     $(this).addClass('secondActiveItem');
            // }
            // if (index === 2) {
            //     // this is the first one
            //     $(this).addClass('thirdActiveItem');
            // }
            // add class to last class
            if (index === total - 1 && total > 1) {
                $(this).addClass('lastActiveItem');
                console.log("Last Item added");
            }
            // if (index === total - 2 && total > 1) {
            //     $(this).addClass('secondToLastActiveItem');
            // }
            // if ($(this).hasClass('lastActiveItem')) {
            //     if ($(this).width() > 250) {
            //         $(this).removeClass('fullOpacity');
            //     }
            // }

            if ($(window).width() > 1024) {
                // console.log("desktop");
                $(this).not('.lastActiveItem').addClass('fullOpacity');
            } else if ($('window').width() <= 1024 && $('window').width() > 768) {
                // console.log("tablet");
                if (fOwlActive.first().width() > 300 || fOwlActive.first().next().width() > 300) {
                    // console.log('First and second large')
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else {
                    // console.log('All Small');
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 2) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                }
            } else if ($('window').width() < 767 && $('window').width() >= 0) {
                // console.log('Small Tablets');
                if (fOwlActive.first().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else if (fOwlActive.first().next().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else if (fOwlActive.first().next().next().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 2) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                }
            } else {

            }
        });

        // Right rail 
        var rrOwlActive = $('#right-rail-container .media-carousel .owl-stage .owl-item.active');
        $('#right-rail-container .media-carousel.media-carousel .owl-stage .owl-item').removeClass('fullOpacity firstActiveItem lastActiveItem');
        $('#right-rail-container .media-carousel .owl-stage .owl-item.active').each(function(index) {
            // add class to first item
            if (index === 0) {
                // this is the first one
                $(this).addClass('firstActiveItem');
            }
            // if (index === 1) {
            //     // this is the first one
            //     $(this).addClass('secondActiveItem');
            // }
            // if (index === 2) {
            //     // this is the first one
            //     $(this).addClass('thirdActiveItem');
            // }
            // add class to last class
            if (index === total - 1 && total > 1) {
                $(this).addClass('lastActiveItem');
            }
            // if (index === total - 2 && total > 1) {
            //     $(this).addClass('secondToLastActiveItem');
            // }
            // if ($(this).hasClass('lastActiveItem')) {
            //     if ($(this).width() > 250) {
            //         $(this).removeClass('fullOpacity');
            //     }
            // }

            if ($(window).width() > 1024) {
                // console.log("desktop");
                $(this).not('.lastActiveItem').addClass('fullOpacity');
            } else if ($('window').width() <= 1024 && $('window').width() > 768) {
                // console.log("tablet");
                if (rrOwlActive.first().width() > 300 || rrOwlActive.first().next().width() > 300) {
                    // console.log('First and second large')
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else {
                    // console.log('All Small');
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 2) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                }
            } else if ($('window').width() < 767 && $('window').width() >= 0) {
                // console.log('Small Tablets');
                if (rrOwlActive.first().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else if (rrOwlActive.first().next().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else if (rrOwlActive.first().next().next().width() > 300) {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                } else {
                    if (index === 0) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 1) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                    if (index === 2) {
                        // this is the first one
                        $(this).addClass('fullOpacity');
                    }
                }
            } else {

            }
        });
    }

    function removedActiveItem() {
        $('.firstActiveItem').each(function(index) {
            if (index === 1) {
                // this is the first one
                $(this).removeClass('fullOpacity');
                console.log("removedClass");
            }
        });
    }



    /* Audio Player */
    var audioFile;
    var audioTitle;
    var ply = document.getElementById('audio-player');
    $('.audio-player-button').on('click', function() {
        audioFile = $(this).data('file');
        audioTitle = $(this).data('audio-title');
    });

    $('#audio-modal').on('hidden.bs.modal', function() {
        // console.log("Hello Simon");
        var oldSrc = ply.src; // just to remember the old source

        ply.src = "";
    });

    $('#audio-modal').on('show.bs.modal', function() {
        ply.src = audioFile;
        $('#audio-modal-Label').text(audioTitle);
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

    // search result menu scroll botton
    // function checkOffset() {
    //     if ($('#search-pagination').offset().top + $('#search-pagination').height() >=
    //         $('#page-footer').offset().top - 10)
    //         $('#search-pagination').css('position', 'absolute');
    //     if ($(document).scrollTop() + window.innerHeight < $('#page-footer').offset().top)
    //         $('#search-pagination').css('position', 'fixed'); // restore when you scroll up
    //     // $('#search-pagination').text($(document).scrollTop() + window.innerHeight);
    // }
    // $(document).scroll(function() {
    //     // checkOffset();
    // });

    // Flexbutton all button container click
    $('.flexible-button').click(function() {
        var wTarget = $(this).find("a").attr("target");
        var wLoc = $(this).find("a").attr("href");
        if (wTarget == "_blank" || wTarget == '_Blank') {
            window.open(wLoc, '_blank');
            //console.log('Target was Blank');
        } else {
            window.location = $(this).find("a").attr("href");
            // console.log('Button Was Clicked');
            return false;
        }
    });

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