
$(document).ready(function(){

    var start_index = 0;
    var NEWS_HITS_PER_PAGE = 10;

    var total_results = parseInt($('#total_results').val());

	newsLoadMoreShowHide();          

    $('#search_news_list').on('change', function() {
      if (this.value == 'By Year')
      {
        $("#select_news_by_year").show();

      }
      else
      {
        $("#select_news_by_year").hide();
        $("#select_news_by_year option:selected").removeAttr("selected");
        $("#select_news_by_year option[value='Choose Year']").attr('selected', 'selected');  
        start_index = 0;

        // to clear contents on the page  
        $('.filtered_list').html("");

        newslistAjaxCall();
      }
    });

    $('#search_news_year').on('change', function() {
          if (this.value != 'Choose Year')
          {
            start_index = 0;
            $('.filtered_list').html("");
            newslistAjaxCall();
          }
    });

    $('.filtered_list_show_more').click(function () {
		start_index += NEWS_HITS_PER_PAGE; 
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
        //Get the user-defined values
        var search_news_list = $('#search_news_list').val() ; 
        var search_news_year = $('#search_news_year').val() ; 
        var media_page_path = $('#media_page_path').val() ; 
		var current_page_path = $('#current_page_path').val() ; 

   		var servletURL = current_page_path + '.newsservlet.' + search_news_list + '.' + search_news_year + '.' + start_index + '.html';

        $.ajax({
            type: 'GET',    
            url: servletURL,
            // data:'search_news_list='+ search_news_list+'&search_news_year='+ search_news_year+'&media_page_path='+ media_page_path+'&start_index='+ start_index,
            success: function(msg){
                var json = jQuery.parseJSON(msg); 
                total_results = json.total_results;            
                $.each(json.jsonNews, function(index, item) {
            		$("<li>").append(
            			$("<article>").append(
						$("<a href=" + item.newsURL + ".html>").append(
            				$("<h3>").append(
            					$("<span class='txt-green'>").text(item.newsHeading)
            				)
            			),
                        $("<span class='txt-green'>").text(item.publishDate),
                        $("<p>").text(item.excerpt)
             		)).appendTo(".filtered_list");
                });
				newsLoadMoreShowHide();          
            },
			error: function (err) {
	           	alert(err);
            }                
        });
    }
});
