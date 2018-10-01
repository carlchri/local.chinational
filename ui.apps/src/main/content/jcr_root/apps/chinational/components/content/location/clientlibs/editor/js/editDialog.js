(function ($, channel, Coral) {
    'use strict';

    var EDIT_DIALOG = ".chi-location-editDialog";
    var LOC_DETAILS_BOX_OPTIONS = ".chi-location-details-box-options";
    var LOC_DETAILS_BOX_HEADING = ".chi-location-details-box-heading";
    var LOC_DESCRIPTION = ".chi-location-description";

    function checkAndDisplay(element1, element2, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element1.show();
            element2.show();
        } else {
            element1.hide();
            element2.hide();
        }
    }

    function handleTextarea(dialog) {
        var component = dialog.find(LOC_DETAILS_BOX_OPTIONS)[0];
        var locDetailsBoxHeading = dialog.find(LOC_DETAILS_BOX_HEADING);
        var locDescription = dialog.find(LOC_DESCRIPTION);
        checkAndDisplay(locDetailsBoxHeading,
        	locDescription,        		
            "enable",
            component.value);
        component.on("change", function () {
            checkAndDisplay(locDetailsBoxHeading,
               	locDescription,        		
                "enable",
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
