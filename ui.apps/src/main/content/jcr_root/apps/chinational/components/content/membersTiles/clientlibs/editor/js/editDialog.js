(function ($, channel, Coral) {
    'use strict';

    var EDIT_DIALOG = ".chi-members-tiles-editDialog";
    var MT_MEMBER_LINK_OPTION = ".chi-mt-member-link-option";
    var MT_MEMBER_LINK_URL = ".chi-mt-member-link-url";
    var MT_LINK_TARGET = ".chi-mt-link-target";

    function checkAndDisplay(element1, element2, expectedValue, actualValue) {
        if (expectedValue === actualValue) {
            element1.show();
            element2.show();
        } else {
            element2.hide();
            element1.hide();
        }
    }

    function handleTextarea(dialog) {
        var component = dialog.find(MT_MEMBER_LINK_OPTION)[0];
        var mtMemberLinkUrl = dialog.find(MT_MEMBER_LINK_URL);
        var mtLinkTarget = dialog.find(MT_LINK_TARGET);
        checkAndDisplay(mtMemberLinkUrl,
       		mtLinkTarget,        		
            "linkToPageReqd",
            component.value);
        component.on("change", function () {
            checkAndDisplay(mtMemberLinkUrl,
           		mtLinkTarget,        		
                "linkToPageReqd",
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
