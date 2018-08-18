
$(document).ready(function(){

    var size_li = $("div.physician_tiles_list").length;
    var x=8;
    $('div.physician_tiles_list:lt('+x+')').show();
    
    if(x >= size_li) {
	    $('.physician_list_show_more').hide();
    }

    $('.physician_list_show_more').click(function () {
        x= (x+8 <= size_li) ? x+8 : size_li;
        $('div.physician_tiles_list:lt('+x+')').show();
        if(x >= size_li) {
			    $('.physician_list_show_more').hide();
        }
    });

});
