$(document).ready(function () {

/*    $("#photo-band-form-select").change(function () {
        $("option", $(this)).each(function (index) {
            if ($(this).is(":selected")) {
                $(this).css("backgroundColor", "#FFFFFF");
            }
            else {
                $(this).css("backgroundColor", "#ddf4f8");
            }
        });
    });
*/    
    if ($( "body" ).hasClass( "campaignlandingpage" )) {
    	var $headerHeight = $('.campaignlandingpage .header');
    	// alert($headerHeight.outerHeight(true));
    	$('.campaignlandingpage .body-content').css( "margin-top", $headerHeight.outerHeight(true)+'px' );
    	// $(".campaignlandingpage .body-content").css( "margin-top", "112px" );
        }
    
    function textScaleToSize() {
    	  var elements = $('.photo-band-with-form .intro-caption-white');
    	  if(elements.length < 0) {
    	    return;
    	  }
    	  elements.each(function(i, element) {
    	    while(element.scrollWidth > element.offsetWidth || element.scrollHeight > element.offsetHeight) {
    	      var newFontSize = (parseInt($(element).css('font-size').slice(0, -2)) - 1) + 'px';
    	      $(element).css('font-size', newFontSize);
    	    }
    	  });
    	}
    
    $('#photo-band-form-select').multiselect({
  	  nonSelectedText: 'Dropdown list',
  	  enableFiltering: false,
  	  enableCaseInsensitiveFiltering: false
  	  // buttonWidth:'380px'
  	 });

    // $('#photo-band-form-multiselect').multipleSelect();
      $('#photo-band-form-multiselect').multiselect({
    	  nonSelectedText: 'Checkbox list',
    	  enableFiltering: false,
    	  enableCaseInsensitiveFiltering: false
    	  // buttonWidth:'380px'
    	 });
      
      $('.photo-band-form').on('submit', function(event){
  	      // alert("inside submit");
    	  event.preventDefault();
    	  var form_data = $(this).serialize();
    	  // alert(form_data);
          $('#pbf_success_message').hide();
          var current_page_path = $('#current_page_path').val() ; 

          var servletURL = current_page_path + '.photoBandFormServlet.html';
    	  
    	  $.ajax({
    	   url:servletURL,
    	   method:"GET",
    	   data:form_data,
           success: function(msg, status, xhr){
           	var json = msg;
           	var ct = xhr.getResponseHeader("content-type") || "";
               if (ct.indexOf('html') > -1) {
                 json = jQuery.parseJSON(msg);
               }
       	    // alert("msg : " + msg);
            $('#pbf_success_message').show();
    	    $(".photo-band-form")[0].reset(); // To reset form fields
/*
    	    $('#photo-band-form-multiselect option:selected').each(function(){
    	     $(this).prop('selected', false);
    	    });
    	    $('#photo-band-form-multiselect').multiselect('refresh');
    	    $('#photo-band-form-select option:selected').prop('selected', false);
    	    $('#photo-band-form-select').multiselect('refresh');
    	    $('#photo-band-form-textfield').val("");
    	    // document.getElementById("photo-band-form").reset();
*/    	    
    	   }
    	  });
    	 });
});