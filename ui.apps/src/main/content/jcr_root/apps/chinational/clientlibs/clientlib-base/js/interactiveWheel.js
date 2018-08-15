jQuery( document ).ready( function( $ ) {

    /*************************************
        .snot gets .largeSnot when clicked
    *************************************/
    $('.snot').on('click', function() {

        /* Reset */
        if($('.snot').hasClass('largeSnot')){
            var oldSnot = $('.largeSnot').attr('src');
            var oldSnotReset = oldSnot.replace(/large/i, 'small');
            $('.largeSnot').attr('src', oldSnotReset);

            $(this).siblings('.snot').removeClass('largeSnot');
        }

        /* Change the src of the image from small to large */

        $(this).addClass('largeSnot');

        if($(this).hasClass('largeSnot')) {
            var snotSrc = $(this).attr('src');
            var largeSnotSrc = snotSrc.replace(/small/i, 'large');

            $(this).attr('src', largeSnotSrc);
        }

    });


    /*************************************
        Rotate Sections
    *************************************/
    $('.sectionOne').on('click', function(){
        $('.circle-rotate').removeClass('transformOne');
        $('.circle-rotate').removeClass('transformTwo');
        $('.circle-rotate').removeClass('transformThree');
        $('.circle-rotate').removeClass('transformFour');
        $('.circle-rotate').removeClass('transformZero');
        $('.circle-rotate').addClass('transformZero');
    });

    $('.sectionTwo').on('click', function(){
        $('.circle-rotate').removeClass('transformOne');
        $('.circle-rotate').removeClass('transformTwo');
        $('.circle-rotate').removeClass('transformThree');
        $('.circle-rotate').removeClass('transformFour');
        $('.circle-rotate').removeClass('transformZero');
        $('.circle-rotate').addClass('transformFour');
    });

    $('.sectionThree').on('click', function(){
        $('.circle-rotate').removeClass('transformOne');
        $('.circle-rotate').removeClass('transformTwo');
        $('.circle-rotate').removeClass('transformThree');
        $('.circle-rotate').removeClass('transformFour');
        $('.circle-rotate').removeClass('transformZero');
        $('.circle-rotate').addClass('transformThree');
    });

    $('.sectionFour').on('click', function(){
        $('.circle-rotate').removeClass('transformOne');
        $('.circle-rotate').removeClass('transformTwo');
        $('.circle-rotate').removeClass('transformThree');
        $('.circle-rotate').removeClass('transformFour');
        $('.circle-rotate').removeClass('transformZero');
        $('.circle-rotate').addClass('transformTwo');
    });

    $('.sectionFive').on('click', function(){
        $('.circle-rotate').removeClass('transformOne');
        $('.circle-rotate').removeClass('transformTwo');
        $('.circle-rotate').removeClass('transformThree');
        $('.circle-rotate').removeClass('transformFour');
        $('.circle-rotate').removeClass('transformZero');
        $('.circle-rotate').addClass('transformOne');
    });


    /*************************************
        Hide/show the text for colors
    *************************************/

    $('.green').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.green-text').show();
    });

    $('.red').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.red-text').show();
    });

    $('.white').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.white-text').show();
    });

    $('.brown').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.brown-text').show();
    });

    $('.black').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.black-text').show();
    });

});