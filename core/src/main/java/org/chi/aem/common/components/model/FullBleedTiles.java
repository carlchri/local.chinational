/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullBleedTiles extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(FullBleedTiles.class);

    public static final String NO_OF_TILES = "noOfBleedTiles";

    private Map<String, String> tileMap;
    private int noOfTiles;
    private int cssClassNo;
    private String cssClass;
    private boolean isLastCss = false;
    private String lastCssClass = "";

    @Override
    public void activate() throws Exception {
    	noOfTiles = Integer.parseInt(getProperties().get(NO_OF_TILES, "1"));
        tileMap = new HashMap<String, String>();
        Resource currentResource = getResource();
        Resource tileResource = null;
        for (int i = 1; i <= noOfTiles; i++) {
            tileResource = currentResource.getChild("bleed-box" + i);
            tileMap.put("bleed-box" + i,getBackGroundColor(tileResource));
	  	}
        cssClassNo = 12/noOfTiles;
        cssClass = "col-sm-" + (cssClassNo * 2) + " " + "col-md-" + cssClassNo;
        if (12 % noOfTiles !=0){
        	isLastCss = true;
        	lastCssClass = "col-sm-" + ((cssClassNo + 12 % noOfTiles) * 2) + " " + "col-md-" + (cssClassNo + 12 % noOfTiles);
        }
    }

    private String getBackGroundColor(Resource tileResource) {
        if(tileResource != null) {
            return tileResource.getValueMap().get("backgroundColor", null);
        }
        return null;
    }

    public Map<String, String> getTileMap() {
 	      //LOGGER.info("getTilesList Called :" + tilesList);
    	  return tileMap;
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