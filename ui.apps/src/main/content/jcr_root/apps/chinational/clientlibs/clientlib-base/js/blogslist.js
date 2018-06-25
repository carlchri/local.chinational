
$(document).ready(function(){

    var blogs_start_index = 0;
    // var blogs_filter = "AllItems";
    var blogs_filter_year = "ChooseYear"
    var BLOGS_HITS_PER_PAGE = 10;

    var blogs_total_results = parseInt($('#total_results').val());

	blogsLoadMoreShowHide();          

    $('#search_blogs_list').on('change', function() {
        var url = $(this).val(); // get selected value
        if (url) { // require a URL
            window.location = url; // redirect
        }
        return false;
    });

    $('#search_blogs_year').on('change', function() {
        blogs_start_index = 0;
        $('.filtered_blogs_list').html("");
        // blogs_filter = $('#search_blogs_list').val() ;
        blogs_filter_year = $('#search_blogs_year').val();
		$('.loading_blogs').show();
        newslistAjaxCall();
    	
/*          if (this.value != 'ChooseYear')
          {
            blogs_start_index = 0;
            $('.filtered_blogs_list').html("");
            blogs_filter = $('#search_blogs_year').val() ;            
    		$('.loading_blogs').show();
            blogslistAjaxCall();
          }
*/          
    });

    $('.filtered_blogs_list_show_more').click(function () {
		blogs_start_index += BLOGS_HITS_PER_PAGE; 
		$('.loading_blogs_next').show();
        blogslistAjaxCall();
    });

    function blogsLoadMoreShowHide() {
        // Compare size of results with total results and hide LOAD MORE, if not required
        size_li = $(".filtered_blogs_list li").size();
        $('.filtered_blogs_list_show_more').show();        
        if(size_li >= blogs_total_results) {
	        $('.filtered_blogs_list_show_more').hide();
        }
	}

    function blogslistAjaxCall() {
        $('.filtered_blogs_list_show_more').hide();
		var current_page_path = $('#current_page_path').val() ; 

   		var servletURL = current_page_path + '.blogsservlet.' + blogs_filter + '.' + blogs_start_index +  '.' + blogs_filter_year + '.html';

        $.ajax({
            type: 'GET',    
            url: servletURL,
            success: function(msg, status, xhr){
            	var json = msg;
            	var ct = xhr.getResponseHeader("content-type") || "";
                if (ct.indexOf('html') > -1) {
                  json = jQuery.parseJSON(msg);
                }
                blogs_total_results = json.total_results;            
        		$('.loading_blogs').hide();    
        		$('.loading_blogs_next').hide();    
                $.each(json.jsonBlogs, function(index, item) {
            		$("<li>").append(
            			$("<article>").append(
						$("<a href=" + item.blogsURL + ".html>").append(
            				$("<h3>").append(
            					$("<span class='txt-green blogs_heading_hover'>").text(item.blogsHeading)
            				)
            			),
                        $("<span class='txt-green'>").text(item.publishDate),
                        $("<p>").text(item.excerpt)
             		)).appendTo(".filtered_blogs_list");
                });
				blogsLoadMoreShowHide();          
            },
			error: function (err) {
				// console.log("Error in Loading.");
            }                
        });
    }
});
