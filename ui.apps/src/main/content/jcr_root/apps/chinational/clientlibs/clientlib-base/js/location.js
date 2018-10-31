
$(function(){
	$("#image").load(function(){
		
		// Getting the natural size of the image
		var image = new Image();
		image.src = $("img", this).attr("src");
		console.log('width: ' + image.naturalWidth + ' and height: ' + image.naturalHeight);

	});
});
