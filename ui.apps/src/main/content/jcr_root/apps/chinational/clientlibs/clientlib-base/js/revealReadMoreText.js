var $el, $ps, $up, totalHeight;

$(".sidebar-box .button").click(function() {
      
  totalHeight = 0;

  $el = $(this);
  $p  = $el.parent();
  $up = $p.parent();
  $ps = $up.find("p:not('.read-more')");
  
  // measure how tall inside should be by adding together heights of all inside paragraphs (except read-more paragraph)
  $ps.each(function() {
    totalHeight += $(this).outerHeight()+12;
  });
        
  $up
    .css({
      // Set height to prevent instant jumpdown when max height is removed
      "min-height": $up.height(),
      "max-height": 9999,
      "margin-bottom" :-50
    })
    .animate({
      "min-height": totalHeight
    });
  
  // fade out read-more
  $p.fadeOut(10);
  
  // prevent jump-down
  return false;
    
});

if (jQuery(window).width() < 768) {
   if(($(".loc-details-box-desc").outerHeight()) > 100){
    	$(".read-more-button-wrapper").show();
    }
    
}

window.addEventListener("resize", function() {
    //reset location read-more height
    if(( jQuery(window).width() < 768 && $(".loc-details-box-desc").outerHeight()) > 100){
            $(".read-more-box").css("min-height", '');
            $(".read-more-box").css("max-height", '99px');
        	$(".read-more-button-wrapper").show();
     }
}, false);

var $locReadMoreButton, $ldbs, $locDetailsBox, locTotalHeight;

$(".loc-details-box .read-more-button").click(function() {
    
	  locTotalHeight = 0;

	  $locReadMoreButton = $(this);
	  $readMoreButtonWrapper  = $locReadMoreButton.parent();
	  $locDetailsBox = $readMoreButtonWrapper.parent();
	  $ldbs = $locDetailsBox.find(".loc-details-box-desc");
	  $readMoreBox = $locDetailsBox.find(".read-more-box");
	  
	  // added by Davinder for Location read-more
	  $ldbs.each(function() {
		    locTotalHeight += $(this).outerHeight();
		  });
	 // reset max height
	  $readMoreBox.css( "max-height", '' );
	  $readMoreBox.css( "min-height", locTotalHeight+'px' );        
	  
	  // fade out read-more
	  // $readMoreButtonWrapper.fadeOut(10);
	  $readMoreButtonWrapper.hide();
	  
	  // prevent jump-down
	  return false;
	    
	});