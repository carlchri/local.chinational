
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

$(window).on("load", function() {
	adjustHeightWidth();
});
