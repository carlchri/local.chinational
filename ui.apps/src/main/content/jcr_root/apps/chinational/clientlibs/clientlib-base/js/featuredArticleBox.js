$('document').ready(function(){

    // hide the box initially
    $(".chi_dual_box").toggle();
    var initialSelectedTagName = $('#tagsFormBox select').find('option:selected').attr('name');

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
    var pageUrlsplit = pageUrl.split(".")[0];

    // Submit function
    $("#listBoxForm").submit(function() {
        var featuredListPages = [] || {} ;
        var featuredListPagesString = [] || {} ;
        var featuredSelectTag;

        featuredSelectTag = $('#featuredTagPrefix').val();
        if (featuredSelectTag == null) {
            featuredSelectTag = "AllItems";
        }
        $('[name="duallistbox_output[]_helper2"]').find('option').each(function() {
                    featuredListPages.push($(this).val());
        });

        if (featuredListPages != null ) {
            featuredListPagesString = featuredListPages.toString().replace("[", "").replace("]", "").split(',');
        } else {
            featuredListPagesString = [""];
        }
            if (featuredListPagesString.length >= 0 && featuredListPagesString.length  <= 3) {
                var featuredListPagesJsonString = JSON.stringify(featuredListPagesString);
                $.ajax({
                    type: 'GET',
                    async: false,
                    url: '/content/national/en.featurednewslistservlet.html',
                    data: {'featuredPagesList': featuredListPagesJsonString, 'requestPagePath': pageUrlsplit, "featuredPagesTag": featuredSelectTag}, //passing values to servlet
                    success: function (msg) {
                        location.reload(true);
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