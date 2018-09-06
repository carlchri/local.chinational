$(document).ready(function(){
    // console.log("Table JS ready!");
	var table = $('table');
    var tableCount = 0;
    table.each(function(){
        tableCount++;
        var tdCount = 0;
        var thCount = 0;
        var tableWidth = 0;
        var caption = $(this).find('caption');
        var tabw = $(this).attr('width') || '216';
        var tabh = $(this).attr('height') || 'auto';
        var tabDWidth = $(this).find('td:first').attr('width') ;
        var tabDWHeaderCellWidth = $(this).find('th:first').attr('width');
        var tabDWHeaderWidth = 0;

        console.log("TabDWidth : "+tabDWidth);
        console.log("tabDWHeaderCellWidth : "+tabDWHeaderCellWidth);

        $(this).find('tr:first td').each(function(){
            $(this).width(tabDWidth);
			tdCount++;
        });
        $(this).find('tr:first th').each(function(){
			thCount++;
        });

        tableWidth = tabw*tdCount;
        tabDWHeaderWidth = tabDWHeaderCellWidth*thCount;

        if (tableWidth)
        	console.log('tableWidth : '+tableWidth+' tabw : '+tabw+' tdCount : '+tdCount );

        if (tabDWHeaderWidth)
        	console.log('tableWidth : '+tabDWHeaderWidth+' tabw : '+tabw+' thCount : '+thCount );

        $(this).removeClass('wHeader');
        $(this).addClass('noHeader');
        $(this).width(tableWidth).height(tabh).css('overflow-y','auto').css('overflow-x','hidden').css('display','block');
        $(this).wrap('<div class="chi-table"><div class="scroller-container" style="min-width: '+tableWidth+'px"></div></div>');
        var tHeader = $(this).find('tr th');
        if (tHeader.length) {
            var newTHeader = tHeader.parent('tr').html();
            var newCaption = caption;
            tHeader.remove();
            caption.remove();
            $(this).width(tabDWHeaderWidth);
            $(this).parent('.scroller-container').prepend('<table class="headerTable'+tableCount+'" style="width: '+tabDWHeaderWidth+'px;">'+newTHeader+'</table>');




            if (newCaption)
				$(this).parent('.scroller-container').prepend(newCaption);

            $(this).removeClass('noHeader');
        	$(this).addClass('wHeader');

            var cssTemplate = '<style>table.headerTable th{ background:red}</style>';
            $('head').append(cssTemplate);
			$(this).find('td').width(tabDWHeaderCellWidth);
            $('<div />', { html: '&shy;<style> table.headerTable'+tableCount+' th {width :'+tabDWHeaderCellWidth+'px;} table.headerTable'+tableCount+' td {width :'+tabDWHeaderCellWidth+'px;}</style>' }).appendTo("body");
        }
    });

});