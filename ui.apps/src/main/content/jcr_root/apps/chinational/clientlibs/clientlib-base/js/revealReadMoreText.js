var $el, $ps, $up, totalHeight;

$(".sidebar-box .button").click(function() {
      
  totalHeight = 0;

  $el = $(this);
  $p  = $el.parent();
  $up = $p.parent();
  $ps = $up.find("p:not('.read-more')");
  
  // measure how tall inside should be by adding together heights of all inside paragraphs (except read-more paragraph)
  $ps.each(function() {
    totalHeight += $(this).outerHeight()+12;
  });
        
  $up
    .css({
      // Set height to prevent instant jumpdown when max height is removed
      "min-height": $up.height(),
      "max-height": 9999,
      "margin-bottom" :-50
    })
    .animate({
      "min-height": totalHeight
    });
  
  // fade out read-more
  $p.fadeOut(10);
  
  // prevent jump-down
  return false;
    
});