
// This JavaScript code must be replaced in Production by newslist.js 
// and correspondent code logic in media-center.html

jQuery(function($) {
    var filters = {
        'ByYear': ['2017', '2018'],
        'NewsArticle' : ['Newest first', 'Oldest first', 'Alphabetically'],
        'PressRelease' :  ['From A to Z', 'From Z to A'],
    }
    
    var $filters = $('#filtered-element');
    $('#search_news_list').change(function () {
        var search_news_list = $(this).val(), lcns = filters[search_news_list] || [];
        
        var html = $.map(lcns, function(lcn){
            return '<option value="' + lcn + '">' + lcn + '</option>'
        }).join('');
        $filters.html(html)
    });
});