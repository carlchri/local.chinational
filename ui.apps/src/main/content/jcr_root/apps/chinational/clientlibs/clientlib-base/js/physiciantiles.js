
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

    // For members tiles height fix
    // author: German
    
    (function() {

    	var currentTallest = 0;
    	var currentRowStart = 0;
    	var rowDivs = new Array();

    	function setConformingHeight(el, newHeight) {
	    	 
    		// set the height to something new, but remember the original height in case things change
	    	 el.data("originalHeight", (el.data("originalHeight") == undefined) ? (el.height()) : (el.data("originalHeight")));
	    	 el.height(newHeight);
    	}

    	function getOriginalHeight(el) {
    		
	    	 // if the height has changed, send the originalHeight
	    	 return (el.data("originalHeight") == undefined) ? (el.height()) : (el.data("originalHeight"));
    	}

    	function columnConform() {

	    	 // find the tallest DIV in the row, and set the heights of all of the DIVs to match it.
	    	 $('div.columnFix').each(function(index) {
	
		    	  if(currentRowStart != $(this).position().top) {
		
			    	   // we just came to a new row.  Set all the heights on the completed row
			    	   for(currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) {
			    		   
			    		   // if the tile is hidden for the pagination then do not change the height to zero
		                   if (currentTallest != 0 ) {
		                	   setConformingHeight(rowDivs[currentDiv], currentTallest);
		                   }
			    	   }
			    		   
			    	   // set the variables for the new row
			    	   rowDivs.length = 0; // empty the array
			    	   currentRowStart = $(this).position().top;
			    	   currentTallest = getOriginalHeight($(this));
			    	   rowDivs.push($(this));
		
		    	  } else {
		
			    	   // another div on the current row.  Add it to the list and check if it's taller
			    	   rowDivs.push($(this));
			    	   currentTallest = (currentTallest < getOriginalHeight($(this))) ? (getOriginalHeight($(this))) : (currentTallest);
		
		    	  }
		    	  
		    	  // do the last row
		    	  for(currentDiv = 0 ; currentDiv < rowDivs.length ; currentDiv++) 
		    		  
		    		  // if the tile is hidden for the pagination then do not change the height to zero
		              if (currentTallest != 0 ) {
		            	  setConformingHeight(rowDivs[currentDiv], currentTallest);
		              }
	    	 });
    	}

    	$(window).resize(function() {
    		columnConform();
    	});

    	$(document).ready(function() {
    		columnConform();
    	});
    })();
});