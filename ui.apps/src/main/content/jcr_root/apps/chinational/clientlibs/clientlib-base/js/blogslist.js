
$(document).ready(function(){
    var BLOGS_HITS_PER_PAGE = 10;

	$('.blogs-list-form').each(function(){
		var $blogsID = this.id;

	    var blogs_start_index = 0;
	    var blogs_filter = "AllItems";
	    var blogs_filter_year = "ChooseYear";
	
	    var blogs_total_results = parseInt($('#'+$blogsID+' #blogs_total_results').val());
	
		blogsLoadMoreShowHide($blogsID, blogs_total_results);          
	
	    $('#'+$blogsID+' #search_blogs_list').on('change', function() {
	        var url = $(this).val(); // get selected value
	        if (url) { // require a URL
	            window.location = url; // redirect
	        }
	        return false;
	    });
	
	    var bfv = $('#'+$blogsID+' #blogs_filter').val();
		if($('#'+$blogsID+' .select_blogs_filter').length){
			$('#'+$blogsID+' #search_blogs_list option:selected').removeAttr("selected");
		    $('#'+$blogsID+' #search_blogs_list option[name='+bfv+']').prop('selected', true);
		    // $('#'+$blogsID+' #search_blogs_list option:contains('+nfv+')').attr('selected', 'selected');
		    if($('#'+$blogsID+' #search_blogs_list option:selected').text() != 'Filter by topic'){
		    	$('#bcp_heading').text(($("#search_blogs_list option:selected").text())+'s');
		    	$('.blog_page_tag_desc').show();
		    	$('.blog_page_tag_desc').css('display','block');
		    	$('.blog_page_tag_desc').text($('#blogs_tag_desc').val());
		    }
		}
	    
	    $('#'+$blogsID+' #search_blogs_year').on('change', function() {
	        blogs_start_index = 0;
	        $('#'+$blogsID+' .filtered_blogs_list').html("");
	        blogs_filter = $('#'+$blogsID+' #search_blogs_list option:selected').attr('name') ;
	        
	        blogs_filter_year = $('#'+$blogsID+' #search_blogs_year').val();
			$('.loading_blogs').show();
	        blogslistAjaxCall($blogsID, blogs_start_index, blogs_filter, blogs_filter_year);
	    });
	
	    $('#'+$blogsID+' .filtered_blogs_list_show_more').click(function () {
			blogs_start_index += BLOGS_HITS_PER_PAGE; 
			if($('#'+$blogsID+' .select_blogs_filter').length){
		        blogs_filter = $('#'+$blogsID+' #search_blogs_list option:selected').attr('name') ;
		        blogs_filter_year = $('#'+$blogsID+' #search_blogs_year').val();
			}
			$('.loading_blogs_next').show();
	        blogslistAjaxCall($blogsID, blogs_start_index, blogs_filter, blogs_filter_year);
	    });
	    
    });

    function blogsLoadMoreShowHide(el1, el2) {
        // Compare size of results with total results and hide LOAD MORE, if not required
        var size_li = $('#'+el1+' .filtered_blogs_list li').size();
        $('#'+el1+' .filtered_blogs_list_show_more').show();        
        if(size_li >= el2) {
	        $('#'+el1+' .filtered_blogs_list_show_more').hide();
        }
	}

    function blogslistAjaxCall(el1, el2, el3, el4) {
        $('#'+el1+' .filtered_blogs_list_show_more').hide();
		var blogs_current_page_path = $('#'+el1+' #blogs_current_page_path').val() ; 

   		var servletURL = blogs_current_page_path + '.blogsservlet.' + el3 + '.' + el2 +  '.' + el4 + '.html';

        $.ajax({
            type: 'GET',    
            url: servletURL,
            success: function(msg, status, xhr){
            	var json = msg;
            	var ct = xhr.getResponseHeader("content-type") || "";
                if (ct.indexOf('html') > -1) {
                  json = jQuery.parseJSON(msg);
                }
                var blogs_total_results = json.total_results;            
        		$('.loading_blogs').hide();    
        		$('.loading_blogs_next').hide();    
                $.each(json.jsonBlogs, function(index, item) {
            		$("<li>").append(
            			$("<article>").append(
						$("<a href=" + item.blogsURL + ".html>").append(
            				$("<h4 class='news_blog_list_heading'>").append(
            					$("<span class='blogs_heading_hover'>").text(item.blogsHeading)
            				)
            			),
                        $("<span class='news_blog_list_heading'>").text(item.publishDate),
                        $("<p>").text(item.excerpt)
             		)).appendTo('#'+el1+' .filtered_blogs_list');
                });
				blogsLoadMoreShowHide(el1, blogs_total_results);          
            },
			error: function (err) {
				// console.log("Error in Loading.");
            }                
        });
    }
    
});
