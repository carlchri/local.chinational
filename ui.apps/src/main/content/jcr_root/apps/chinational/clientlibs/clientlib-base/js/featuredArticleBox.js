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
        var tagValue = $(this).val().toString();
        console.log("This Value : "+tagValue);

        /*
        $.ajax({
            type: 'GET',
            url:'/content/national/en.featurednewslisttagservlet.html',
            data: {'featuredPagesTag': tagValue, 'requestPagePath': pageUrlsplit},
            success: function(msg){
                location.reload(true);
            },
            error: function(xhr, status, error) {
                console.log(error);
            }
        })*/
    });

    // Submit function
    $("#listBoxForm").submit(function() {
        var featuredListPages = [] || {} ;
        var featuredListPagesString = [] || {} ;
        var featuredTag, featuredSelectTag, featuredTagSuffix, featuredSelectTagPrefix;

        featuredSelectTagPrefix = $('#featuredTagPrefix').val();
        featuredTagSuffix = $("#tagsFormBox select").val() || "AllItems";
        if (featuredTagSuffix == null) {
            featuredTag = "AllItems";
        }
        featuredSelectTag = featuredSelectTagPrefix + featuredTagSuffix;
        featuredListPages = $('[name="duallistbox_output[]"]').val();
        // console.log("featured  selected Tag :: "+featuredSelectTag);
        // console.log("Featured Pages tag :: "+ featuredTag);
        // var featuredList = featuredListPages.toString().replace("[", "{").replace("]","}");
        // if (featuredListPages == null || featuredTag == "") {
        //     featuredListPages = ['NoArticle'];
        // }
        // if (featuredSelectTag == null) {
        //     featuredSelectTag = $('#tagsFormBox select option:first').val();
        // }
        // debugger;
        if (featuredListPages != null ) {
            featuredListPagesString = featuredListPages.toString().replace("[", "").replace("]", "").split(',');
        } else {
            featuredListPagesString = ["No article"];
        }
            // console.log("featuredListPages length :: "+featuredListPages.length);
            if (featuredListPagesString.length >= 0 && featuredListPagesString.length  <= 3) {
                // console.log("featuredListPagesString length :: "+featuredListPagesString.length);

                // console.log( featuredListPagesString );
                // console.log(featuredListPagesString[0]);
                // console.log(featuredListPagesString[1]);
                // console.log(featuredListPagesString[2]);
                // console.log(JSON.stringify(featuredListPagesString));
                var featuredListPagesJsonString = JSON.stringify(featuredListPagesString);
                // console.log("Fetured list pages json string :: "+ featuredListPagesJsonString);
                // console.log("Featured selected tag :: "+featuredSelectTag);
                // console.log("Featured Tag :: "+featuredTag);
                // debugger;
                $.ajax({
                    type: 'GET',
                    async: false,
                    url: '/content/national/en.featurednewslistservlet.html',
                    data: {'featuredPagesList': featuredListPagesJsonString, 'requestPagePath': pageUrlsplit, 'featuredTag': featuredTag, "featuredPagesTag": featuredSelectTag}, //passing values to servlet

                    success: function (msg) {
                        // $(this).find('select').refresh();
                        // alert("Press OK to refresh page.");
                        location.reload(true);
                        // console.log("Featured Pages :: " + featuredListPagesString);
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