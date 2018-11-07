
$(document).ready(function(){

// Setting the player options 
	
	$('.chi-audio-player').mediaelementplayer({
		
		// Show controls when playing and mouse is not over the video
		alwaysShowControls : true,
		// the order of controls you want on the control bar (and other plugins below)
		features : [ 'playpause', 'progress', 'current', 'duration', 'volume' ],
		// the separator for current time and total duration
		timeAndDurationSeparator : ' / ',
		// useful for <audio> player loops
	    loop: false,
	    // audio volume should goes horizontal
		audioVolume : 'horizontal',
		// width of audio player
		audioWidth : '100%',
		// enables Flash and Silverlight to resize to content size
		enableAutosize: true,
		// height of audio player
		audioHeight : 45,
		// force iPad's native controls
		iPadUseNativeControls : false,
		// force iPhone's native controls
		iPhoneUseNativeControls: false, 
		// force Android's native controls
		AndroidUseNativeControls: false
		
	});
	
	// Audio Modal starts here
	
	$('#audio-modal').on('hidden.bs.modal', function() {

		// Every time the user closes the modal window the DOWNLOAD control gets removed from player
		$(".mejs-download-control").remove();

	});
	
	
	function reCalculateMediaPlayerHeight() {
				
		var heightM = $(".mejs__container").height();
		if (( heightM == '45') && ($(window).width() < 512)) {
			var new_val = "33.5"; 
       		$(".mejs__container").css("height", new_val);
		}
		
		if (( heightM == '33.5') && ($(window).width() >= 512)) {
			var old_val = "45";    
       		$(".mejs__container").css("height", old_val);
		} 
	}

	
	$(window).on("resize", reCalculateMediaPlayerHeight);

	
	// Triggered when the user clicks on the audio tile from media carousel
	
	$(document).on('click','div[class="audio-container"]', function() {

		// Getting the file source to play
		
		var myAudio = $(this).find("button").attr("data-file");
		var myTitle = $(this).find("#audioHeader").text();
		var myText = $(this).find("#audioExplanation").text();
					
		// Adding DOWNLOAD control to player
		
		$("<div class='mejs-download-control'><a download='audio_file' href='"+ myAudio + "'>&nbsp;</a></div>").insertAfter(".mejs__time");
		
		// Adding file source to player
		
		$("#audio-player_html5").attr("src", myAudio);
		$("#myAudioHeader").text(myTitle);
		$("#myAudioText").text(myText);
		
		reCalculateMediaPlayerHeight();

	});		
	
	
	$('.audiopopup-close').click(function() {
		
		// Two solutions are included here:
		// Closing the popup modal window if using Bootstrap 2.x
		
		//$('div#audio-modal').removeAttr("style");
		//$('.modal-backdrop').remove();
		//$('body').removeClass( "modal-open" );
		
		// Closing the popup modal window if using Bootstrap 3.x

		$('.modal').modal('hide').data('bs.modal', null);
    });
	
});
