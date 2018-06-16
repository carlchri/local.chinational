$(document).ready(function () {

    $(".photo-band-form-select").change(function () {
        $("option", $(this)).each(function (index) {
            if ($(this).is(":selected")) {
                $(this).css("backgroundColor", "#FFFFFF");
            }
            else {
                $(this).css("backgroundColor", "#ddf4f8");
            }
        });
    });
    
    if ($( "body" ).hasClass( "campaignlandingpage" )) {
    	var $headerHeight = $('.campaignlandingpage .header');
    	// alert($headerHeight.outerHeight(true));
    	$('.campaignlandingpage .body-content').css( "margin-top", $headerHeight.outerHeight(true)+'px' );
    	// $(".campaignlandingpage .body-content").css( "margin-top", "112px" );
        }
    
    textScaleToSize();
    
    function textScaleToSize() {
    	  var elements = $('.photo-band-with-form .intro-caption-white');
    	  if(elements.length < 0) {
    	    return;
    	  }
    	  elements.each(function(i, element) {
    	    while(element.scrollWidth > element.offsetWidth || element.scrollHeight > element.offsetHeight) {
    	      var newFontSize = (parseFloat($(element).css('font-size').slice(0, -2)) * 0.95) + 'px';
    	      $(element).css('font-size', newFontSize);
    	    }
    	  });
    	}

});