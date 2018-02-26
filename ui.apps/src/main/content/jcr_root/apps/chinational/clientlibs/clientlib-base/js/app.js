jQuery(document).ready(function(){
	jQuery('.menu-icon, .side-menu-close, .overlay').click(function(){
		jQuery('.main-nav').toggleClass('open');
		jQuery('.overlay').fadeToggle(350);
		jQuery('.menu-icon').toggleClass('active');
    if( jQuery('.menu-icon').hasClass('active') ) {
      jQuery('.menu-label').text('CLOSE');
    }else {
      jQuery('.menu-label').text('MENU');
    }
	});
	var lastItem;
	$('.header ul li a').click(function(e) {
			var currentItem = $(this);
			jQuery('.header ul li a').removeClass('dd-opened');
			jQuery(this).addClass('dd-opened');
			e.stopPropagation();
			if ($(this).next('.hidden-menu').css('display') == ('none')) {
					$(lastItem).next('.hidden-menu').slideUp();
					$(this).next('.hidden-menu').slideDown();
					jQuery('.header ul li a').removeClass('dd-opened');
					jQuery(this).addClass('dd-opened');
			} else {
					$(this).next('.hidden-menu').slideUp();
					jQuery('.header ul li a').removeClass('dd-opened');
			}
			lastItem = $(this);
	});
	$(document).click(function() {
    jQuery('.hidden-menu', this).slideUp();
		jQuery('.header ul li a').removeClass('dd-opened');
	});
	$('#myDropdown').on('show.bs.dropdown', function () {
  // do something…
	})
	var owl = $('.owl-carousel');
	owl.owlCarousel({
		margin: 30,
		nav: true,
		loop: false,
		dots: false,
		autoHeight:true,
		responsive: {
			0: {
				items: 1
			},
			1024: {
				items: 2
			},
			1200: {
				items: 3
			}
		}
	})

})
if ( jQuery(window).width() > 992 ) {
	jQuery(function(){
		createSticky($(".main-nav"));
	});
	function createSticky(sticky) {
		if (typeof sticky !== "undefined") {
			var	pos = sticky.offset().top,
					win = $(window);
			win.on("scroll", function() {
	    		win.scrollTop() >= pos ? sticky.addClass("pstn-fxd") : sticky.removeClass("pstn-fxd");
			});
		}
	}
}
