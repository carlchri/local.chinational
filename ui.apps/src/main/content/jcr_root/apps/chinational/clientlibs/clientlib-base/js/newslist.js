
$(document).ready(function(){

    var start_index = 0;
    var news_filter = "SortByMostRecent";
    var NEWS_HITS_PER_PAGE = 10;

    var total_results = parseInt($('#total_results').val());

    window.onunload = searchNewsListOption();
    
    function searchNewsListOption() {
        $("#select_news_by_year").hide();
        $("#search_news_list option:selected").removeAttr("selected");
        $("#search_news_list option[value='SortByMostRecent']").attr('selected', 'selected');
        $("#select_blogs_by_year").hide();
        $("#search_blogs_list option:selected").removeAttr("selected");
        $("#search_blogs_list option[value='SortByMostRecent']").attr('selected', 'selected');  
	}

	 newsLoadMoreShowHide();          

    $('#search_news_list').on('change', function() {
      if (this.value == 'ByYear')
      {
        $("#select_news_by_year").show();

      }
      else
      {
        $("#select_news_by_year").hide();
        $("#select_news_by_year option:selected").removeAttr("selected");
        $("#select_news_by_year option[value='ChooseYear']").attr('selected', 'selected');  
        start_index = 0;

        // to clear contents on the page  
        $('.filtered_list').html("");
        news_filter = $('#search_news_list').val() ;
		$('.loading').show();
        newslistAjaxCall();
      }
    });

    $('#search_news_year').on('change', function() {
          if (this.value != 'ChooseYear')
          {
            start_index = 0;
            $('.filtered_list').html("");
            news_filter = $('#search_news_year').val() ;    
    		$('.loading').show();
            newslistAjaxCall();
          }
    });

    $('.filtered_list_show_more').click(function () {
		start_index += NEWS_HITS_PER_PAGE; 
		$('.loading_next').show();
        newslistAjaxCall();
    });

    function newsLoadMoreShowHide() {
        // Compare size of results with total results and hide LOAD MORE, if not required
        size_li = $(".filtered_list li").size();
        $('.filtered_list_show_more').show();        
        if(size_li >= total_results) {
	        $('.filtered_list_show_more').hide();
        }
	}

    function newslistAjaxCall() {
        $('.filtered_list_show_more').hide();
		var current_page_path = $('#current_page_path').val() ; 

   		var servletURL = current_page_path + '.newsservlet.' + news_filter + '.' + start_index + '.html';
		$('#loadingmessage').show();
        $.ajax({
            type: 'GET',    
            url: servletURL,
            success: function(msg, status, xhr){
            	var json = msg;
            	var ct = xhr.getResponseHeader("content-type") || "";
                if (ct.indexOf('html') > -1) {
                  json = jQuery.parseJSON(msg);
                }
                total_results = json.total_results;  
        		$('.loading').hide();    
        		$('.loading_next').hide();    
        		$.each(json.jsonNews, function(index, item) {
            		$("<li>").append(
            			$("<article>").append(
						$("<a href=" + item.newsURL + ".html>").append(
            				$("<h4 class='news_blog_list_heading'>").append(
            					$("<span class='news_heading_hover'>").text(item.newsHeading)
            				)
            			),
                        $("<span class='news_blog_list_heading'>").text(item.publishDate),
                        $("<p>").text(item.excerpt)
             		)).appendTo(".filtered_list");
                });
				newsLoadMoreShowHide();          
            },
			error: function (err) {
				// console.log("Error in Loading.");
            }                
        });
    }
    
});
