(function($) {
	$(function() {

		function reCalculateMediaPlayerHeight() {
			
			// I had to use JavaScript here instead of jQuery because only worked with Safari not Chrome.
			
			var elem   = document.getElementsByClassName("mejs__container");
			var height = elem[0].style.getPropertyValue("height");
			
			if (( height == '45px') && ($(window).width() < 600)) {
				var new_val = "33.5px";    
			    document.getElementsByClassName("mejs__container")[0].style.height = new_val;
			}
			
			if (( height == '33.5px') && ($(window).width() >= 600)) {
				var old_val = "45px";    
				document.getElementsByClassName("mejs__container")[0].style.height = old_val;
			} 
		}
		
		// Audio Modal starts here
		
		$('#audio-modal').on('hidden.bs.modal', function() {

			// Every time the user closes the modal window the DOWNLOAD control gets removed from player
			$(".mejs-download-control").remove();

		});

		// Setting the player options 
		
		$('.chi-audio-player').mediaelementplayer(
				
				{
					alwaysShowControls : true,
					features : [ 'playpause', 'progress', 'current', 'duration', 'volume' ],
					timeAndDurationSeparator : ' / ',
					audioVolume : 'horizontal',
					audioWidth : '100%',
					audioHeight : 45,
					iPadUseNativeControls : false,
					iPhoneUseNativeControls : false,
					AndroidUseNativeControls : false
				});

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

		$(window).on("resize", reCalculateMediaPlayerHeight);
		
	});
})(jQuery);
