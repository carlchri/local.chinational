$(document).ready(function () {

    $(".photo-band-form-select").change(function () {
        $("option", $(this)).each(function (index) {
            if ($(this).is(":selected")) {
                $(this).css("backgroundColor", "#FFFFFF");
            }
            else {
                $(this).css("backgroundColor", "#ddf4f8");
            }
        });
    });
});