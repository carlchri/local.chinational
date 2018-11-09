
function adjustHeightWidth() {
	
	$('#myLocationComponent #image img').each(function() {
						
		var tds = $(this);
		var image = new Image();
		image.src = tds.attr("src");

		if (image.naturalWidth > 703 && image.naturalHeight > 395) {
			$(this).removeClass();
			$(this).addClass("image-responsive-3");

		} else {
			if (image.naturalWidth > 703 && image.naturalHeight <= 395) {
				$(this).removeClass();
				$(this).addClass("image-responsive");

			} else {
				if (image.naturalWidth <= 703 && image.naturalHeight > 395) {
					$(this).removeClass();
					$(this).addClass("image-responsive-2");

				} else {
					if (image.naturalWidth <= 703 && image.naturalHeight <= 395) {
						$(this).removeClass();
						$(this).addClass("image-responsive-4");
					
					}
				}
			}
		}
	});
}

function reCalculateGoogleMapHeight() {
	
	if ($(window).width() < 600) {
		
		// Getting the height value on the 16:9 ratio for all components with google maps	
		var divWidth = $(".map-box").width();
		var new_value = (9*divWidth)/16;
		$(".map-box").height(new_value);
		$("#myLocationComponent #image").height(new_value);
		$('#myLocationComponent #image img').height(new_value);
		
	} else {
		
		$(".map-box").height(395);
		$("#myLocationComponent #image").height(395);
		$('#myLocationComponent #image img').height(395);
	}
}

$(window).on("load", function() {
	adjustHeightWidth();
});

$(window).on("resize", function() {
	reCalculateGoogleMapHeight();
});
