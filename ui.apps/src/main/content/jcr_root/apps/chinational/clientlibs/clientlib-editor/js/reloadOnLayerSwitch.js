(function ($, channel) {
    'use strict';
    $(function () {
        channel.on('cq-layer-activated', function (event) {
            var ANNOTATE_LAYER = 'Annotate';
            if (event.prevLayer && event.layer !== event.prevLayer && event.prevLayer !== ANNOTATE_LAYER && event.layer !== ANNOTATE_LAYER) {
	            location.reload();
            }
        });
    });
})(Granite.$, jQuery(document));