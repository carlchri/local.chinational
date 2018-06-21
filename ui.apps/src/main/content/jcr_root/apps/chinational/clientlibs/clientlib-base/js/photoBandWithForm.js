$(document).ready(function () {

    if ($( "body" ).hasClass( "campaignlandingpage" )) {
    	var $headerHeight = $('.campaignlandingpage .header');
    	// alert($headerHeight.outerHeight(true));
    	$('.campaignlandingpage .body-content').css( "margin-top", $headerHeight.outerHeight(true)+'px' );
    	// $(".campaignlandingpage .body-content").css( "margin-top", "112px" );
        }
    
    // $.each($('.photo-band-form-select'), function (index, value) {
    $('.photo-band-form-select').each(function (index) { 
    	var selectName = $(this).attr('name');
    	$(this).multiselect({
    	  nonSelectedText: selectName,
	  	  enableFiltering: false,
	  	  enableCaseInsensitiveFiltering: false
	  	  // buttonWidth:'380px'
	  	 });
    });

    // $('.photo-band-form-multiselect').multipleSelect();
    // $.each($('.photo-band-form-multiselect'), function (index, value) {
    $('.photo-band-form-multiselect').each(function (index) { 
      var selectName = $(this).attr('name');
      $(this).multiselect({
    	  nonSelectedText: selectName,
    	  enableFiltering: false,
    	  enableCaseInsensitiveFiltering: false
    	  // buttonWidth:'380px'
    	 });
    });

    function validateForm(){

        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
        var numberReg = /^(?:(?:\+?1\s*(?:[.-]\s*)?)?(?:\(\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\s*\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\s*(?:[.-]\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\s*(?:[.-]\s*)?([0-9]{4})$/;
        
		$('.photo-band-form').find('input').each(function(){
			// alert("found input field in form");
			$(this).css('border', 'inherit');
		    if($(this).prop('required')){
				// alert("required field");
				if ($(this).val() === "") {
					//alert("inside empty validate");
					  $(this).css('border', 'solid 2px red');
			          $("#pbf_error_message").show();
			          $(this).focus();
			          return false;
			        }else if($(this).attr("type") == "email" && !emailReg.test($(this).val())){
			        	// alert("inside email validate");
					  $(this).css('border', 'solid 2px red');
			          $("#pbf_error_message").text("Please provide valid e-mail address.");
			          $("#pbf_error_message").show();
			          $(this).focus();
			          return false;
			        } else if($(this).attr("type") == "tel" && !numberReg.test($(this).val())){
			        	//alert("inside tel validate");
						  $(this).css('border', 'solid 2px red');
				          $("#pbf_error_message").text("Please provide valid telephone address.");
				          $("#pbf_error_message").show();
				          $(this).focus();
				          return false;
				        }
		    }
		    
			$('.photo-band-form').find('.photo-band-form-multiselect, .photo-band-form-select').each(function(){
				// alert("found select field in form");
				$(this).siblings('.btn-group').css('border', 'none');
			    if($(this).prop('required')){
			    	var selectIsEmpty = true;
					// alert("required field");
			    	var form_data = $('.photo-band-form').serializeArray();
			    	// alert(form_data[2].name);
			    	var $select_name = $(this).attr('name');
			    	// alert($select_name)
					$.each(form_data, function(i, field){
						if(field.name == $select_name){
							selectIsEmpty = false;
						}
					});

					if (selectIsEmpty) {
						  $(this).siblings('.btn-group').css('border', 'solid 2px red');
				          $("#pbf_error_message").show();
				          $(this).siblings('.btn-group .button').focus();
				          return false;
				        }
			    }
			});    		
		    
		});    		
    }   
    
    //('.photo-band-form').on('submit', function(event){
    $("#photo-band-form-button").click(function() {			
  	      // alert("inside submit");
    	  event.preventDefault();
    	  var submitForm=false;
          $("#pbf_error_message").text("The above highlighted field is required.");
          $('#pbf_success_message').hide();
          $('#pbf_error_message').hide();
          var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
          var numberReg = /^(?:(?:\+?1\s*(?:[.-]\s*)?)?(?:\(\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\s*\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\s*(?:[.-]\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\s*(?:[.-]\s*)?([0-9]{4})$/;
          var total_validation_items = $('.photo-band-form').find('input, .photo-band-form-multiselect, .photo-band-form-select').length;
  		$('.photo-band-form').find('input, .photo-band-form-multiselect, .photo-band-form-select').each(function(ind){
  			if($(this).is("input")){
  				// alert("inside input");
	  			$(this).css('border', 'inherit');
	  		    if($(this).prop('required')){
	  				// alert("required field");
	  				if ($(this).val() === "") {
	  					//alert("inside empty validate");
	  					  $(this).css('border', 'solid 2px red');
	  			          $("#pbf_error_message").show();
	  			          $(this).focus();
	  			          return false;
	  			        }else if($(this).attr("type") == "email" && !emailReg.test($(this).val())){
	  			        	alert("inside email validate");
	  					  $(this).css('border', 'solid 2px red');
	  			          $("#pbf_error_message").text("Please provide valid e-mail address.");
	  			          $("#pbf_error_message").show();
	  			          $(this).focus();
	  			          return false;
	  			        } else if($(this).attr("type") == "tel" && !numberReg.test($(this).val())){
	  			        	//alert("inside tel validate");
	  						  $(this).css('border', 'solid 2px red');
	  				          $("#pbf_error_message").text("Please provide valid telephone address.");
	  				          $("#pbf_error_message").show();
	  				          $(this).focus();
	  				          return false;
	  				        }
	  		    }
  			}
  			if($(this).is("select")){
  				// alert("inside select");
  				$(this).siblings('.btn-group').css('border', 'none');
  			    if($(this).prop('required')){
  			     	window.selectIndex = ind;
  			     	//alert(window.selectIndex);
  			    	window["selectIsEmpty"+window.selectIndex] = true;
  			    	
  			    	// selectIsEmpty['select'+i] = true;
  					//alert("required field");
  			    	var form_data = $('.photo-band-form').serializeArray();
  			    	// alert(form_data[2].name);
  			    	var $select_name = $(this).attr('name');
  			    	// alert($select_name)
  					$.each(form_data, function(i, field){
  						if(field.name == $select_name){
  							window["selectIsEmpty"+window.selectIndex] = false;
  						}
  					});
  					//alert(window["selectIsEmpty"+window.selectIndex]);
  					if (window["selectIsEmpty"+window.selectIndex]) {
  						  $(this).siblings('.btn-group').css('border', 'solid 2px red');
  				          $("#pbf_error_message").show();
  				          $(this).siblings('.btn-group .button').focus();
  				          return false;
  				        }
  			    }
 				
  			}
  			if(ind === total_validation_items - 1) {
  				return submitForm=true;
  		    }  			
  		    
  		});    		
    	  
/*		$('.photo-band-form').find('.photo-band-form-multiselect, .photo-band-form-select').each(function(ind){
			// alert("found select field in form");
    	  // event.preventDefault();
			$(this).siblings('.btn-group').css('border', 'none');
		    if($(this).prop('required')){
		     	window.selectIndex = ind;
		     	alert(window.selectIndex);
		    	window["selectIsEmpty"+window.selectIndex] = true;
		    	
		    	// selectIsEmpty['select'+i] = true;
				// alert("required field");
		    	var form_data = $('.photo-band-form').serializeArray();
		    	// alert(form_data[2].name);
		    	var $select_name = $(this).attr('name');
		    	// alert($select_name)
				$.each(form_data, function(i, field){
					if(field.name == $select_name){
						window["selectIsEmpty"+window.selectIndex] = false;
					}
				});
				alert(window["selectIsEmpty"+window.selectIndex]);
				if (window["selectIsEmpty"+window.selectIndex]) {
					  $(this).siblings('.btn-group').css('border', 'solid 2px red');
			          $("#pbf_error_message").show();
			          $(this).siblings('.btn-group .button').focus();
			          return false;
			        }
		    }
		    return submitForm=true;
		});    		
*/	    
    	  if(submitForm){
        	  var form_data = $('.photo-band-form').serialize();
        	  // alert(form_data);
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
	
	    	    $('.photo-band-form-multiselect option:selected').each(function(){
	    	     $(this).prop('selected', false);
	    	    });
	    	    $('.photo-band-form-multiselect').multiselect('refresh');
	    	    $('.photo-band-form-select option:selected').prop('selected', false);
	    	    $('.photo-band-form-select').multiselect('refresh');
	    	    $('.photo-band-form-textfield').val("");
	    	    // document.getElementById("photo-band-form").reset();
	    	    
	    	   }
	    	  });
    	  }
    	  
    	 });
});