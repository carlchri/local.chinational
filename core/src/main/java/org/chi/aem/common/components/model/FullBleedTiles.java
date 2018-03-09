/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class FullBleedTiles extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(FullBleedTiles.class);
    private static final String NO_OF_TILES = "noOfBleedTiles";
    private static final String BLEED_BOX = "bleed-box";
    private static final String TOP = "top";
    private Map<String, String> tileMap;
    private int noOfTiles;

    @Override
    public void activate() throws Exception {
    	noOfTiles = Integer.parseInt(getProperties().get(NO_OF_TILES, "1"));
        tileMap = new LinkedHashMap<String, String>();
        Resource currentResource = getResource();
        Resource tileResource = null;
        for (int i = 1; i <= noOfTiles; i++) {
            tileResource = currentResource.getChild(BLEED_BOX + i);
            tileMap.put(BLEED_BOX + i,getBackGroundColor(tileResource));
	  	}
    }
/*
    private String getClass(Resource tileResource, String firstClass) {
        if(tileResource != null) {
            if(!tileResource.getValueMap().get("imagePosition", TOP).equalsIgnoreCase(TOP)) {
                return firstClass + " flex-box";
            }
        }
        return firstClass;
    }
    */
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
 }