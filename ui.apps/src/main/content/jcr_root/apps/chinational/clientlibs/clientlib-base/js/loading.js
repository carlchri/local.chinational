$(document).ready(function(){
	console.log('Loadiing Page');
	$('.loading').fadeOut('slow');

	$("#showButtonLoad").on('click',function(){
		$('.loading').fadeIn('slow');
		console.log('show');
	});
	$("#hideButtonLoad").on('click',function(){
		// hideLoading();
		$('.loading').fadeOut('slow');
		console.log('hide');
	});


		
});