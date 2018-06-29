
$(document).ready(function(){

    window.onunload = searchNewsListOption();
    function searchNewsListOption() {
        $("#search_news_year option:selected").removeAttr("selected");
        $('#search_news_year option:contains("ChooseYear")').attr('selected', 'selected');
        $("#search_blogs_year option:selected").removeAttr("selected");
        $('#search_blogs_year option:contains("ChooseYear")').attr('selected', 'selected');
	}

    var NEWS_HITS_PER_PAGE = 10;

	$('.news-list-form').each(function(){
		var $newsID = this.id;
	    //var window["start_index"+$newsID] = 0;
		var start_index = 0;
	    var news_filter = "AllItems";
	    var news_filter_year = "ChooseYear";
	
	    var total_results = parseInt($('#'+$newsID+' #total_results').val());
	    
	    
	    newsLoadMoreShowHide($newsID, total_results);          
	
	    $('#'+$newsID+' #search_news_list').on('change', function() {
	        var url = $(this).val(); // get selected value
	        if (url) { // require a URL
	            window.location = url; // redirect
	        }
	        return false;
	    });
	
	    var nfv = $('#'+$newsID+' #news_filter').val();
		if($('#'+$newsID+' .select_news_filter').length){
			alert("filter enabled");
			$('#'+$newsID+' #search_news_list option:selected').removeAttr("selected");
		    $('#'+$newsID+' #search_news_list option[name='+nfv+']').prop('selected', true);
		    if($('#'+$newsID+' #search_news_list option:selected').text() != 'All Items'){
		    	$('#mcp_heading').text(($('#'+$newsID+' #search_news_list option:selected').text())+'s');
		    	$('.media_page_tag_desc').show();
		    	$('.media_page_tag_desc').css('display','block');
		    	$('.media_page_tag_desc').text($('#'+$newsID+' #news_tag_desc').val());
		    }
		}
	    
	    $('#'+$newsID+' #search_news_year').on('change', function() {
	        start_index = 0;
	        $('#'+$newsID+' .filtered_list').html("");
	        news_filter = $('#'+$newsID+' #search_news_list option:selected').attr('name') ;
	        news_filter_year = $('#'+$newsID+' #search_news_year').val() ;    
			$('.loading').show();
	        newslistAjaxCall($newsID, start_index, news_filter, news_filter_year);
	    });
	
	    $('#'+$newsID+' .filtered_list_show_more').click(function () {
			start_index += NEWS_HITS_PER_PAGE; 
			if($('#'+$newsID+' .select_news_filter').length){
		        news_filter = $('#'+$newsID+' #search_news_list option:selected').attr('name') ;
		        news_filter_year = $('#'+$newsID+' #search_news_year').val() ;   
			}
			$('.loading_next').show();
	        newslistAjaxCall($newsID, start_index, news_filter, news_filter_year);
	    });
	    
    });


    function newsLoadMoreShowHide(el1, el2) {
        // Compare size of results with total results and hide LOAD MORE, if not required
        var size_li = $('#'+el1+' .filtered_list li').size();
        $('#'+el1+' .filtered_list_show_more').show();        
        if(size_li >= el2) {
	        $('#'+el1+' .filtered_list_show_more').hide();
        }
	}

    function newslistAjaxCall(el1, el2, el3, el4) {
        $('#'+el1+' .filtered_list_show_more').hide();
		var current_page_path = $('#'+el1+' #current_page_path').val() ; 

   		var servletURL = current_page_path + '.newsservlet.' + el3 + '.' + el2 + '.' + el4 + '.html';
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
                var total_results = json.total_results;  
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
				newsLoadMoreShowHide(el1, total_results);          
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
