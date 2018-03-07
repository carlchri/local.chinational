
$(document).ready(function(){

    $('#search_blogs_list').on('change', function() {
      if (this.value == 'By Year')
      {
        $("#select_blogs_by_year").show();

      }
      else
      {
        $("#select_blogs_by_year").hide();
         $("#filter_blogs_form").submit();
      }
    });

    $('#search_blogs_year').on('change', function() {
        $("#filter_blogs_form").submit(); 
    });

    var size_li = $(".filtered_blogs_list li").size();
    var x=10;
    $('.filtered_blogs_list li:lt('+x+')').show();
    
    if(x >= size_li) {
	    $('.filtered_blogs_list_show_more').hide();
    }

    $('.filtered_blogs_list_show_more').click(function () {
        x= (x+10 <= size_li) ? x+10 : size_li;
        $('.filtered_blogs_list li:lt('+x+')').show();
        if(x >= size_li) {
			    $('.filtered_blogs_list_show_more').hide();
        }
    });

});
