(function ($, channel, Coral) {
    'use strict';

    var EDIT_DIALOG = ".chi-profile-carousel-manual-editDialog";
    var PCM_HEADING_SELECTION = ".chi-pcm-heading-selection";
    var PCM_HEADING = ".chi-pcm-carousel-heading";

    function checkAndDisplay(element, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element.show();
        } else {
            element.hide();
        }
    }

    function handleTextarea(dialog) {
        var component = dialog.find(PCM_HEADING_SELECTION)[0];
        var pcmHeading = dialog.find(PCM_HEADING);
        checkAndDisplay(pcmHeading,
            "headingRequired",
            component.value);
        component.on("change", function () {
            checkAndDisplay(pcmHeading,
                "headingRequired",
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
