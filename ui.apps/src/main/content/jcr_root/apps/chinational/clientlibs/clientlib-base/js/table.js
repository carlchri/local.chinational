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
        var tabw = $(this).attr('width') || 'auto';
        var tabh = $(this).attr('height') || 'auto';
        var tabDWidth = $(this).find('td').attr('width' ) || $(this).find('td:last').attr('width' ) || '216' ;
        var tabDWHeaderCellWidth = $(this).find('th:first').attr('width') || 216;
        var tabDWHeaderWidth = 0;

        // console.log("Table width : "+tabw);
        // console.log("TabDWidth : "+tabDWidth);
        // console.log("tabDWHeaderCellWidth : "+tabDWHeaderCellWidth);

        $(this).find('tr th').each(function(){
            $(this).width(tabDWHeaderCellWidth);
            $(this).removeAttr('width');
            thCount++;
        });

        // console.log("tabDWHeaderCellWidth : "+tabDWHeaderCellWidth);
        $(this).find('th').width(tabDWHeaderCellWidth);
        $(this).find('td').width(tabDWidth);

        $(this).find('tr:last td').each(function(){
            // $(this).width(tabDWidth);
            tdCount++;
        });
        // console.log("Table Data Width : "+tabDWidth);
        // console.log("TD Count : "+tdCount);

        tableWidth = tabw;
        tabDWHeaderWidth = tabDWidth*tdCount;
        // console.log("tabDWHeaderWidth : "+tabDWHeaderWidth);

        // if (tableWidth)
        //     console.log('tableWidth : '+tableWidth+', tabDWidth : '+tabDWidth+', tdCount : '+tdCount );

        // if (tabDWHeaderWidth)
        //     console.log('tableWidth : '+tabDWHeaderWidth+' tabw : '+tabw+' thCount : '+thCount );

        $(this).removeClass('wHeader');
        $(this).addClass('noHeader');
        $(this).width(tabDWHeaderWidth).height(tabh).css('overflow-y','auto').css('overflow-x','hidden').css('display','block');
        $(this).wrap('<div class="chi-table"><div class="scroller-container" style="width: '+tableWidth+'px"></div></div>');
        var tHeader = $(this).find('tr th');
        if (tHeader.length ) {
            var newTHeaderHTML = tHeader.parent('tr').html();
            var newCaption = caption;
            tabDWHeaderWidth = tabDWHeaderCellWidth*tdCount;
            tHeader.remove();
            caption.remove();
            $(this).removeClass('noHeader');
            $(this).addClass('wHeader');
            $(this).width(tabDWHeaderWidth);
            $(this).parent('.scroller-container').prepend('<table class="headerTable'+tableCount+'" style="width: '+tabDWHeaderWidth+'px;">'+newTHeaderHTML+'</table>');

            if (newCaption)
                $(this).parent('.scroller-container').prepend(newCaption);
            $(this).find('td').width(tabDWHeaderCellWidth);

            $('<div />', { html: '&shy;<style> table.headerTable'+tableCount+' th {width :'+tabDWHeaderCellWidth+'px;} table.headerTable'+tableCount+' td {width :'+tabDWHeaderCellWidth+'px;}</style>' }).appendTo("body");
        }
    });

});