
$(document).ready(function(){

    var blogs_start_index = 0;
    var blogs_filter = "SortByMostRecent";
    var BLOGS_HITS_PER_PAGE = 10;

    var blogs_total_results = parseInt($('#total_results').val());

	blogsLoadMoreShowHide();          

    $('#search_blogs_list').on('change', function() {
      if (this.value == 'ByYear')
      {
        $("#select_blogs_by_year").show();
      }
      else
      {
        $("#select_blogs_by_year").hide();
        $("#select_blogs_by_year option:selected").removeAttr("selected");
        $("#select_blogs_by_year option[value='ChooseYear']").attr('selected', 'selected');  
        blogs_start_index = 0;

        // to clear contents on the page  
        $('.filtered_blogs_list').html("");

        blogs_filter = $('#search_blogs_list').val() ;
		$('.loading_blogs').show();
        blogslistAjaxCall();
      }
    });

    $('#search_blogs_year').on('change', function() {
          if (this.value != 'ChooseYear')
          {
            blogs_start_index = 0;
            $('.filtered_blogs_list').html("");
            blogs_filter = $('#search_blogs_year').val() ;            
    		$('.loading_blogs').show();
            blogslistAjaxCall();
          }
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

   		var servletURL = current_page_path + '.blogsservlet.' + blogs_filter + '.' + blogs_start_index + '.html';

        $.ajax({
            type: 'GET',    
            url: servletURL,
            success: function(msg){
                // var json = jQuery.parseJSON(msg);
            	var json = msg;
                blogs_total_results = json.total_results;            
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
