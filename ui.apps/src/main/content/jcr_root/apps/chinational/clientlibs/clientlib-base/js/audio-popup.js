(function($) {
	$(function() {

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
			
			var myAudio = $("button.audio-player-button").attr("data-file");

			// Adding DOWNLOAD control to player
			
			$("<div class='mejs-download-control'><a download='audio_file' href='"+ myAudio + "'>&nbsp;</a></div>").insertAfter(".mejs__time");
			
			// Adding file source to player
			
			$("#audio-player_html5").attr("src", myAudio);

		});

	});

})(jQuery);
