
$(document).ready(function(){

    $('#search_news_list').on('change', function() {
      if (this.value == 'By Year')
      {
        $("#select_news_by_year").show();

      }
      else
      {
        $("#select_news_by_year").hide();
         $("#filter_news_form").submit();
      }
    });

    $('#search_news_year').on('change', function() {
        $("#filter_news_form").submit(); 
    });

    var size_li = $(".filtered_list li").size();
    var x=10;
    $('.filtered_list li:lt('+x+')').show();
    
    if(x >= size_li) {
	    $('.filtered_list_show_more').hide();
    }

    $('.filtered_list_show_more').click(function () {
        x= (x+10 <= size_li) ? x+10 : size_li;
        $('.filtered_list li:lt('+x+')').show();
        if(x >= size_li) {
			    $('.filtered_list_show_more').hide();
        }
    });

});
