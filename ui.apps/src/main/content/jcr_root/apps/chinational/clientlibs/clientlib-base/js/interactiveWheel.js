var waitForFinalEvent = (function() {
	var timers = {};
	return function(callback, ms, uniqueId) {
		if (!uniqueId) {
			uniqueId = "Don't call this twice without a uniqueId";
		}
		if (timers[uniqueId]) {
			clearTimeout(timers[uniqueId]);
		}
		timers[uniqueId] = setTimeout(callback, ms);
	};
})();


// Usage
$(window).resize(function() {
	
	waitForFinalEvent(reCalculateHeight(), 500, "initial resizing");
	
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
	    waitForFinalEvent(reCalculateHeight(), 500, "resizing after section 0");
	});
	
	$('.section1').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text1').show();
	    waitForFinalEvent(reCalculateHeight(), 500, "resizing after section 1");
	});
	
	$('.section2').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text2').show();
	    waitForFinalEvent(reCalculateHeight(), 500, "resizing after section 2");
	});
	
	$('.section3').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text3').show();
	    waitForFinalEvent(reCalculateHeight(), 500, "resizing after section 3");
	});
	
	$('.section4').on('click', function() {
	    $('.initial-text').hide();
	    $('.color-text').hide();
	    $('.text4').show();
	    waitForFinalEvent(reCalculateHeight(), 500, "resizing after section 4");
	});
	
	
	function reCalculateHeight() {
	    
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
	    
	    parent.css({ height: addingHeights });   // Updating container height with the total height of all elements in container
		
	}
});
