
$(document).ready(function(){

	// only for initial text
	
	$('.interactive-wheel .left').css('margin-top','-15px')
		
	// Changing the style for ul elements in the text if any
	
	var lineStyle = $('.left ul').css('list-style-position');
	
	if (lineStyle == "inside") { 
		
		$('.left ul').css('list-style-position','outside');
		$('.left ul').css('list-style-type','circle');    // German: Fixing MAM-145 with circle bullet point
		
	}
	
	function reCalculateHeight() {
	    
		// Checking the height of the header for adjustment of the text in mobile
		
		var $heightDiv = $('.header-padding');


		// Adjustments for text container
		
        if ( (($("#right-rail-container").length > 0) && ($(window).width() > 990) && ($(window).width() < 1125)) || (($(window).width() > 599) && ($(window).width() < 720))) {
			$(".interactive-wheel .left").css("max-width", "222px");
		}
		else {
			$(".interactive-wheel .left").css("max-width", "272px");
		}

        // Adjustments for space between header and text depending on the element/initial text shown
        
		if (($(".header-padding").height() > 40) && ($(window).width() < 600)) {

            if ($('.initial-text').css('display') == 'none') {
				var new_val = "152px";    // When the header uses 2 lines height and any element is active
	       		$(".interactive-wheel .left.mobile-adjustment").css("padding-top", new_val);
            }
            else {
				var new_val = "135px";    // When the header uses 2 lines height and initial text is active
	       		$(".interactive-wheel .left.mobile-adjustment").css("padding-top", new_val);
            }
	    }
		
		else {
			
			if (($(".header-padding").height() < 40) && ($(window).width() < 600)) {
				var old_val = "122px";    // When the header uses 1 line height
			    $(".interactive-wheel .left.mobile-adjustment").css("padding-top", old_val);
			}
			
			else {
				 if($('.initial-text').css('display') == 'none') {
					 $(".interactive-wheel .left.mobile-adjustment").css("padding-top", '17px');
				 }
				 else {
					 
						 $(".interactive-wheel .left.mobile-adjustment").css("margin-top", '-15px');
						 $(".interactive-wheel .left.mobile-adjustment").css("padding-top", '0px');
					 
				 }
			}
		}
			
		
		// Recalculate the size of the other elements...
		
		var addingHeights = 0;
		var parent = $('.wheelContainer');
	    var header = $('.header-padding.mobile-adjustment');
		var child = $('.left');
		var child2 = $('.right');

	    
		if ($(window).width() <= 600) {
	        addingHeights = (child.height() + child2.height() + header.height() + parseInt($('.sub-wheelContainer').css('padding-top')) + parseInt($('.sub-wheelContainer').css('padding-bottom')) + 100);
		}
		else {
	        addingHeights = (child.height() + header.height() + parseInt($('.sub-wheelContainer').css('padding-top')) + parseInt($('.sub-wheelContainer').css('padding-bottom')));
		}
		
		if ( addingHeights > parent.height()) {
	    
			parent.css({ height: addingHeights });   // Updating container height with the total height of all elements in container
		}
	}

  $(window).on("resize", reCalculateHeight);

 
  reCalculateHeight();
	
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
	    reCalculateHeight();
	});
	
	$('.section1').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text1').show();
	    reCalculateHeight();
	});
	
	$('.section2').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text2').show();
	    reCalculateHeight();
	});
	
	$('.section3').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text3').show();
	    reCalculateHeight();
	});
	
	$('.section4').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text4').show();
	    reCalculateHeight();
	});
});