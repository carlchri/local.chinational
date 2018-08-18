$(document).ready(function() {
    $('#audio-modal').on('hidden.bs.modal', function() {
    	console.log("Hello Simon");
        var ply = document.getElementById('audio-player');

        var oldSrc = ply.src; // just to remember the old source

        ply.src = "";
    });

    $('#audio-modal').on('show.bs.modal', function() {
    	console.log("Hello Simon Modal Show");
});
    });