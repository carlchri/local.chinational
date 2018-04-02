$(document).ready(function() {
    var addHrLength = $('.physicianTiles .row .tiles-wrapper').length - 3;
    $('.physicianTiles .row .tiles-wrapper').each(function(index) {
        if (addHrLength % 2 === 0) {
            $('.physicianTiles .tiles-wrapper.filler .tile-inner').hide();
            console.log($('.physicianTiles .row .tiles-wrapper').length - 3);
        }
    });
    


    var addHrLength = $('.profileCarousel .row .tiles-wrapper').length - 3;
    $('.profileCarousel .row .tiles-wrapper').each(function(index) {
        if (addHrLength % 2 === 0) {
            $('profileCarousel .tiles-wrapper.filler .tile-inner').hide();
            console.log($('.profileCarousel .row .tiles-wrapper').length - 3);
        }
    });

});

