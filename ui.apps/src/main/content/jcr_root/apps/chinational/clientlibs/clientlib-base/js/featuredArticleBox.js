$('document').ready(function(){

    // Show hide feature article management
    $(".selectionShowHide").click(function () {
        $(".chi_dual_box").toggle();
    });

    // console.log('Dual List box loaded! OK Great Simon!!!');
    var listBox = $('select[name="duallistbox_output[]"]').bootstrapDualListbox({
        selectedListLabel: 'Selected Features',
        nonSelectedListLabel: 'Available Articles'
    });
    var pageUrl = String(window.location.pathname);
    // console.log("page url : "+pageUrl)
    var pageUrlsplit = pageUrl.split(".")[0];
    // console.log("The page Url : "+pageUrlsplit);

    //TagsFormBox
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
        var pageArray = $('#tagsFormBox input[name="' + selectedTagName + '"]').val().split(",");

        $.each(pageArray, function(i) {
            // set specific options to selected
            $('select[name="duallistbox_output[]"]').find('option[value="' + pageArray[i] + '"]').prop('selected', true);
        });

        // refresh the dual box
        $('select[name="duallistbox_output[]"]').bootstrapDualListbox('refresh', true);

        // duallistbox_output[]_helper1
        // duallistbox_output[]_helper2

        $('[name="duallistbox_output[]_helper1"]').children('option').each(function() {
            var optionTag = $(this).attr('class');
            $(this).show();
            if (optionTag != null && selectedTagName != 'AllItems' && !optionTag.endsWith(selectedTagName)) {
                //console.log("Hide option with tag: " + optionTag);
                $(this).hide();
            }
        });

        return false;
    });

    // Submit function
    $("#listBoxForm").submit(function() {
        var featuredListPages = [] || {} ;
        var featuredListPagesString = [] || {} ;
        var featuredTag, featuredSelectTag, featuredTagSuffix, featuredSelectTagPrefix;

        featuredSelectTagPrefix = $('#featuredTagPrefix').val();
        featuredTagSuffix = $("#tagsFormBox select").find('option:selected').attr('name') || "AllItems";
        if (featuredTagSuffix == null) {
            featuredTag = "AllItems";
        }
        //featuredSelectTag = featuredSelectTagPrefix + featuredTagSuffix;
        featuredSelectTag = featuredTagSuffix;
        featuredListPages = $('[name="duallistbox_output[]"]').val();

        if (featuredListPages != null ) {
            featuredListPagesString = featuredListPages.toString().replace("[", "").replace("]", "").split(',');
        } else {
            featuredListPagesString = ["No article"];
        }
            // console.log("featuredListPages length :: "+featuredListPages.length);
            if (featuredListPagesString.length >= 0 && featuredListPagesString.length  <= 3) {
                var featuredListPagesJsonString = JSON.stringify(featuredListPagesString);
                $.ajax({
                    type: 'GET',
                    async: false,
                    url: '/content/national/en.featurednewslistservlet.html',
                    data: {'featuredPagesList': featuredListPagesJsonString, 'requestPagePath': pageUrlsplit, 'featuredTag': featuredTag, "featuredPagesTag": featuredSelectTag}, //passing values to servlet

                    success: function (msg) {
                        location.reload(true);
                        console.log("Featured Pages :: " + featuredListPagesString);
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