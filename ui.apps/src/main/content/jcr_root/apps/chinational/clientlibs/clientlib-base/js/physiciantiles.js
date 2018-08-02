
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

    // For members pagination below
    // author: German
    
    var size_members 			= $("div.members_tiles_list").length;
	var tiles_per_page 			= parseInt($('div#membersContainer').prop("class"));
    var tiles_per_page_adjusted = tiles_per_page-1;


    // Do not apply below code when in editing mode

    if ($('div#first').parents('div#wrapper').hasClass('previewMode')) {

        // Hide the rest of tiles after the first batch (using parent div with id first)
        $('div.members_tiles_list:gt('+tiles_per_page_adjusted+')').parents('div#first').addClass("removeTile");
    
        // Hide LOAD MORE button when reaching all members
        if(tiles_per_page_adjusted >= size_members) {
            $('.members_list_show_more').addClass("removeTile");
        }
    
        // When clicking LOAD MORE do:
        $('.members_list_show_more').click(function () {

            // Get the next batch of member tiles
            tiles_per_page_adjusted = (tiles_per_page_adjusted + tiles_per_page <= size_members) ? tiles_per_page_adjusted + tiles_per_page + 1 : size_members;
            
            // Show them
            $('div.members_tiles_list:lt('+ tiles_per_page_adjusted +')').parents('div#first').removeClass("removeTile");
            
            // Hide LOAD MORE button when reaching all members
            if(tiles_per_page_adjusted >= size_members) {
                $('.members_list_show_more').addClass("removeTile");
            }
        });
    };
});


