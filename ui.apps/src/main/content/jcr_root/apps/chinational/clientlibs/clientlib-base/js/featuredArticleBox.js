$('document').ready(function(){

    // hide the box initially
    $(".chi_dual_box").toggle();
    var initialSelectedTagName = $('#tagsFormBox select').find('option:selected').attr('name');
    //console.log("initial selected tag: " + initialSelectedTagName);

    // Show hide feature article management
    $(".selectionShowHide").click(function () {
        $(".chi_dual_box").toggle();
    });

    var listBox = $('select[name="duallistbox_output[]"]').bootstrapDualListbox({
        selectedListLabel: 'Selected Features',
        nonSelectedListLabel: 'Available Articles',
        sortByInputOrder: true
    });
    var pageUrl = String(window.location.pathname);
    // console.log("page url : "+pageUrl)
    var pageUrlsplit = pageUrl.split(".")[0];
    // console.log("The page Url : "+pageUrlsplit);

    //TagsFormBox
    /*
    $('#tagsFormBox select').on('change', function () {
        var tagUrlValue = $(this).val().toString();
        //console.log("Selected tag Value : "+tagUrlValue);

        // update selected
        var selectedTagName = $(this).find('option:selected').attr('name');
        $('#featuredCurrentSelectedTag').text(selectedTagName);
        // unselect all selected items
        $('[name="duallistbox_output[]"]').find('option:selected').each(function() {
            $(this).prop('selected', false);
        });
        // select items for this tag
        var inputAttr = $('#tagsFormBox input[name="' + selectedTagName + '"]');
        if (inputAttr && inputAttr.val()) {
            var pageArray = inputAttr.val().split(",");
            $.each(pageArray, function(i) {
                // set specific options to selected
                $('select[name="duallistbox_output[]"]').find('option[value="' + pageArray[i] + '"]').prop('selected', true);
            });
        }

        // refresh the dual box
        $('select[name="duallistbox_output[]"]').bootstrapDualListbox('refresh', true);

        // only update options that not selected

       // $('[name="duallistbox_output[]_helper1"]').children('option').each(function() {
       //     var optionTag = $(this).attr('class');
       //     $(this).show();
            // hide is there is no tag associated or tag is present, but not the right one
       //     if ((optionTag == null && selectedTagName != 'AllItems' )||
       //             ( optionTag != null && selectedTagName != 'AllItems' && !optionTag.endsWith(selectedTagName))) {
       //         $(this).hide();
       //     }
        //});

        return false;
    });
    */

    // Submit function
    $("#listBoxForm").submit(function() {
        var featuredListPages = [] || {} ;
        var featuredListPagesString = [] || {} ;
        var featuredSelectTag;// featuredTag, featuredTagSuffix, featuredSelectTagPrefix;

        //featuredSelectTagPrefix = $('#featuredTagPrefix').val();
        featuredSelectTag = $('#featuredTagPrefix').val();
        //$("#tagsFormBox select").find('option:selected').attr('name') || "AllItems";
        if (featuredSelectTag == null) {
            featuredSelectTag = "AllItems";
        }
        //featuredSelectTag = featuredSelectTagPrefix + featuredTagSuffix;
        //featuredSelectTag = featuredTagSuffix;
        featuredListPages = $('[name="duallistbox_output[]"]').val();
        console.log("select op featuredListPages: " + featuredListPages);

        if (featuredListPages != null ) {
            featuredListPagesString = featuredListPages.toString().replace("[", "").replace("]", "").split(',');
            console.log("array to string featuredListPagesString: " + featuredListPagesString);
        } else {
            featuredListPagesString = [""];
        }
            // console.log("featuredListPages length :: "+featuredListPages.length);
            if (featuredListPagesString.length >= 0 && featuredListPagesString.length  <= 3) {
                var featuredListPagesJsonString = JSON.stringify(featuredListPagesString);
                console.log("string to JSON - featuredListPagesJsonString: " + featuredListPagesJsonString);
                $.ajax({
                    type: 'GET',
                    async: false,
                    url: '/content/national/en.featurednewslistservlet.html',
                    data: {'featuredPagesList': featuredListPagesJsonString, 'requestPagePath': pageUrlsplit, "featuredPagesTag": featuredSelectTag}, //passing values to servlet

                    success: function (msg) {
                        location.reload(true);
                        //console.log("Featured Pages :: " + featuredListPagesString);
                    },
                    error: function (xhr, status, error) {
                        console.log(error);
                    }
                });
                return false;
            } else {
                alert("Please select up to three Articles");
                return false;
            }
    });
});