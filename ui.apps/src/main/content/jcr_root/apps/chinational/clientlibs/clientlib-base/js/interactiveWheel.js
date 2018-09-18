jQuery( document ).ready( function( $ ) {


    /*************************************
        Rotate Sections
    *************************************/
	
    $('.section0').on('click', function(){
        $('.circle-rotate').removeClass('transform0');
        $('.circle-rotate').removeClass('transform1');
        $('.circle-rotate').removeClass('transform2');
        $('.circle-rotate').removeClass('transform3');
        $('.circle-rotate').removeClass('transform4');
        $('.circle-rotate').addClass('transform0');
        $('.snot').removeClass('zoomed');
        $('.snot.section0').addClass('zoomed');
    });

    $('.section1').on('click', function(){
    	$('.circle-rotate').removeClass('transform0');
        $('.circle-rotate').removeClass('transform1');
        $('.circle-rotate').removeClass('transform2');
        $('.circle-rotate').removeClass('transform3');
        $('.circle-rotate').removeClass('transform4');
        $('.circle-rotate').addClass('transform4');
        $('.snot').removeClass('zoomed');
        $('.snot.section1').addClass('zoomed');
    });

    $('.section2').on('click', function(){
    	$('.circle-rotate').removeClass('transform0');
        $('.circle-rotate').removeClass('transform1');
        $('.circle-rotate').removeClass('transform2');
        $('.circle-rotate').removeClass('transform3');
        $('.circle-rotate').removeClass('transform4');
        $('.circle-rotate').addClass('transform3');
        $('.snot').removeClass('zoomed');
        $('.snot.section2').addClass('zoomed');
    });

    $('.section3').on('click', function(){
    	$('.circle-rotate').removeClass('transform0');
        $('.circle-rotate').removeClass('transform1');
        $('.circle-rotate').removeClass('transform2');
        $('.circle-rotate').removeClass('transform3');
        $('.circle-rotate').removeClass('transform4');
        $('.circle-rotate').addClass('transform2');
        $('.snot').removeClass('zoomed');
        $('.snot.section3').addClass('zoomed');
    });

    $('.section4').on('click', function(){
    	$('.circle-rotate').removeClass('transform0');
        $('.circle-rotate').removeClass('transform1');
        $('.circle-rotate').removeClass('transform2');
        $('.circle-rotate').removeClass('transform3');
        $('.circle-rotate').removeClass('transform4');
        $('.circle-rotate').addClass('transform1');
        $('.snot').removeClass('zoomed');
        $('.snot.section4').addClass('zoomed');
    });


    /*************************************
        Hide/show the text for colors
    *************************************/

    $('.section0').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.text0').show();
    });

    $('.section1').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.text1').show();
    });

    $('.section2').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.text2').show();
    });

    $('.section3').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.text3').show();
    });

    $('.section4').on('click', function() {
        $('.initial-text').hide();
        $('.color-text').hide();
        $('.text4').show();
    });

});