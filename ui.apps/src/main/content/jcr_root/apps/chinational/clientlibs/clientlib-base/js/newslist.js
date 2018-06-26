
$(document).ready(function(){

    var start_index = 0;
    var news_filter = "AllItems";
    var news_filter_year = "ChooseYear"
    var NEWS_HITS_PER_PAGE = 10;

    var total_results = parseInt($('#total_results').val());
    
    /*$('#media_page_refresh').val() == 'yes' ? window.location.reload(true) : $('#media_page_refresh').val('yes');*/

    window.onunload = searchNewsListOption();
    function searchNewsListOption() {
    	// alert("in firefox");
        // $("#select_news_by_year").hide();
        $("#select_news_by_year option:selected").removeAttr("selected");
        $('#select_news_by_year option:contains("ChooseYear")').attr('selected', 'selected');
        // $("#select_blogs_by_year").hide();
        // $("#search_blogs_list option:selected").removeAttr("selected");
        // $('#search_blogs_list option:contains("AllItems")').attr('selected', 'selected');  
	}

    
/*    // needs to change this in new logic. we might not need this function
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
	$("#search_news_list option:selected").removeAttr("selected");
    $('#search_news_list option[name='+nfv+']').prop('selected', true);
    // $('#search_news_list option:contains('+nfv+')').attr('selected', 'selected');
    if($("#search_news_list option:selected").text() != 'All Items'){
    	$('#mcp_heading').text(($("#search_news_list option:selected").text())+'s');
    	$('.media_page_tag_desc').show();
    	$('.media_page_tag_desc').css('display','block');
    	$('.media_page_tag_desc').text($('#news_tag_desc').val());
    }
    
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
        var size_li = $(".filtered_list li").size();
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

/* May 30, 2018 Added by Davinder for CHIP13 - newsListManual component "SEE MORE" button */
    window.manual_news_id = "id1";
    $('.news_list_manual_wrapper').each(function(){
    	window.manual_news_id = this.id;
    	window["news_list_manual_size"+window.manual_news_id] = $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_manual_list li').length;
	    window["news_list_manual_load_size"+window.manual_news_id] = 10;
	    $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_manual_list  li:lt('+window["news_list_manual_load_size"+window.manual_news_id]+')').show();
	    
	    if(window["news_list_manual_load_size"+window.manual_news_id] >= window["news_list_manual_size"+window.manual_news_id]) {
		    $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_list_manual_more').hide();
	    }
    });

    $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_list_manual_more').click(function () {
    	window["news_list_manual_load_size"+window.manual_news_id]= (window["news_list_manual_load_size"+window.manual_news_id]+10 <= window["news_list_manual_size"+window.manual_news_id]) ? window["news_list_manual_load_size"+window.manual_news_id]+10 : window["news_list_manual_size"+window.manual_news_id];
	    $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_manual_list  li:lt('+window["news_list_manual_load_size"+window.manual_news_id]+')').show();

	    if(window["news_list_manual_load_size"+window.manual_news_id] >= window["news_list_manual_size"+window.manual_news_id]) {
		    $('.news_list_manual_wrapper#'+window.manual_news_id + ' .news_list_manual_more').hide();
	    }
    });
/* End of additions by Davinder */    

});
