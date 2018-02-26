/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;

public class CalloutTiles extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(CalloutTiles.class);

    public static final String NO_OF_CALLOUT_TILES = "noOfCalloutTiles";

    private List<String> calloutTilesList; 
    private int noOfTiles;
    private int cssClassNo;
    private String cssClass;
    private boolean isLastCss = false;
    private String lastCssClass = "";
    
    @Override
    public void activate() throws Exception {
    	noOfTiles = Integer.parseInt(getProperties().get(NO_OF_CALLOUT_TILES, "1"));
        calloutTilesList = new ArrayList<String>();		
        for (int i = 1; i <= noOfTiles; i++) {
	  	   calloutTilesList.add("Tile" + "-" + i);
	  	}
        cssClassNo = 12/noOfTiles;
        cssClass = "col-sm-" + (cssClassNo * 2) + " " + "col-md-" + cssClassNo;
        if (12 % noOfTiles !=0){
        	isLastCss = true;
        	lastCssClass = "col-sm-" + ((cssClassNo + 12 % noOfTiles) * 2) + " " + "col-md-" + (cssClassNo + 12 % noOfTiles);
        }
    }

    public List<String> getCalloutTilesList() {
 	      //LOGGER.info("getCalloutTilesList Called :" + calloutTilesList);
    	  return calloutTilesList;
    }
    
    public String getCssClass() {
	  // LOGGER.info("getCssClass :" + cssClass);
  	  return cssClass;
  }

    public boolean getIsLastCss() {
  	  return isLastCss;
  }

    public String getLastCssClass() {
	  // LOGGER.info("lastCssClass :" + lastCssClass);
  	  return lastCssClass;
  }
 }