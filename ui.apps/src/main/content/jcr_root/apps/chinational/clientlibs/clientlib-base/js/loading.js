// DO NOT REQUIRE THIS FILE - CAN BE DELETED IN AEM
$(document).ready(function(){
	// console.log('Loading Page');
	// $('.loading').fadeOut('slow');

	$("#showButtonLoad").on('click',function(){
		$('.loading').fadeIn('slow');
		// console.log('show');
	});
	
	$("#hideButtonLoad").on('click',function(){
		// hideLoading();
		$('.loading').fadeOut('slow');
		// console.log('hide');
	});


		
});