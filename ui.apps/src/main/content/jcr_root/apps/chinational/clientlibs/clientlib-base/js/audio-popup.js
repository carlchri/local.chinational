$(document).ready(function() {
    $('#audio-modal').on('hidden.bs.modal', function() {
    	//console.log("Hello Simon");
        var ply = document.getElementById('audio-player');

        var oldSrc = ply.src; // just to remember the old source

        ply.src = "";
    });

    $('#audio-modal').on('show.bs.modal', function() {
    	//console.log("Hello Simon Modal Show");
    });

    // this is for new audio player
    // it works with multiple instances of player
    $('.chi-audio-player').mediaelementplayer({
            alwaysShowControls: true,
            features: ['playpause','progress','current','duration', 'volume'],
            timeAndDurationSeparator: ' / ',
            audioVolume: 'horizontal',
            audioWidth: '100%',
            audioHeight: 45,
            iPadUseNativeControls: false,
            iPhoneUseNativeControls: false,
            AndroidUseNativeControls: false
          });
	$(".mejs__time").each(
	function (a,b){
		var ctl = $(b);
		var aud = ctl.closest(".mejs__container").find(".chi-audio-player");
		ctl.after("<div class='mejs-download-control'><a download='audio_file' href='"+aud.attr("src")+"'>&nbsp;</a></div>");
	});


});
