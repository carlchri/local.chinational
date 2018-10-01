"use strict";
use(function () {

    var info 			= {}; 
    var uri 			= "https://www.google.com/maps/search/?api=1&query=";
    var myAddress 		= granite.resource.properties["address"]; 

     if ((myAddress == false) || (this.value2 == 'imageLocation')) {
    	var myAddress 		= this.value;
    }

    var res 			= encodeURIComponent(myAddress);


    info.address		= myAddress;
    info.addressURL 	= uri + res;

    return info;
    
});