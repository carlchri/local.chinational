
$(document).ready(function(){

    var start_index = 0;
    var news_filter = "AllItems";
    var news_filter_year = "ChooseYear"
    var NEWS_HITS_PER_PAGE = 10;

    var total_results = parseInt($('#total_results').val());
/*
    window.onunload = searchNewsListOption();
    
    // needs to change this in new logic. we might not need this function
    function searchNewsListOption() {
        // $("#select_news_by_year").hide();
        $("#search_news_list option:selected").removeAttr("selected");
        #mySelect option:contains(abc)
        $('#search_news_list option:contains("AllItems")').attr('selected', 'selected');
        // $("#select_blogs_by_year").hide();
        $("#search_blogs_list option:selected").removeAttr("selected");
        $('#search_blogs_list option:contains("AllItems")').attr('selected', 'selected');  
	}
*/
    newsLoadMoreShowHide();          

    $('#search_news_list').on('change', function() {
        var url = $(this).val(); // get selected value
        if (url) { // require a URL
            window.location = url; // redirect
        }
        return false;
    });

    var nfv = $('#news_filter').val();
alert(nfv);
	$("#search_news_list option:selected").removeAttr("selected");
    $('#search_news_list option:contains('+nfv+')').attr('selected', 'selected');
alert($("#search_news_list").val());

    $('#search_news_year').on('change', function() {
        start_index = 0;
        $('.filtered_list').html("");
        // var xyz = $('option:selected', this).attr('mytag')
        news_filter = $('#search_news_list option:selected').attr('name') ;
        news_filter_year = $('#search_news_year').val() ;    
		$('.loading').show();
        newslistAjaxCall();

/*          if (this.value != 'ChooseYear')
          {
            start_index = 0;
            $('.filtered_list').html("");
            news_filter = $('#search_news_year').val() ;    
    		$('.loading').show();
            newslistAjaxCall();
          }
          */
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

   		var servletURL = current_page_path + '.newsservlet.' + news_filter + '.' + start_index + '.' + news_filter_year + '.html';
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
            				$("<h3>").append(
            					$("<span class='txt-green news_heading_hover'>").text(item.newsHeading)
            				)
            			),
                        $("<span class='txt-green'>").text(item.publishDate),
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
