(function ($, channel, Coral) {
    'use strict';

    var EDIT_DIALOG = ".chi-news-detail-page-editDialog";
    var TILE_IMAGE_OPTIONS = ".chi-details-page-tile-image-options";
    var TILE_IMAGE_SRC = ".chi-details-page-tile-img-src";

    function checkAndDisplay(element, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element.show();
        } else {
            element.hide();
        }
    }

    function handleTextarea(dialog) {
        var component = dialog.find(TILE_IMAGE_OPTIONS)[0];
        var tileImageSrc = dialog.find(TILE_IMAGE_SRC);
        checkAndDisplay(tileImageSrc,
            "separateTileImage",
            component.value);
        component.on("change", function () {
            checkAndDisplay(tileImageSrc,
                "separateTileImage",
                component.value);
        });
    }

    function initialise(dialog) {
        dialog = $(dialog);
        handleTextarea(dialog);
    }

    channel.on("foundation-contentloaded", function (e) {
        if ($(e.target).find(EDIT_DIALOG).length > 0) {
            Coral.commons.ready(e.target, function (component) {
                initialise(component);
            });
        }
    });

})(jQuery, jQuery(document), Coral);
