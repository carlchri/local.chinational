/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;

public class TilesAndNewsList extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(TilesAndNewsList.class);

    public static final String NO_OF_CONTENT_TILES = "noOfContentTiles";

    private List<String> contentTilesList; 
    
    @Override
    public void activate() throws Exception {
        contentTilesList = new ArrayList<String>();
        // if no tiles present, authros would configure it
        for (int i = 1; i <= Integer.parseInt(getProperties().get(NO_OF_CONTENT_TILES, "0")); i++) {
	  	   contentTilesList.add("Tile" + "-" + i);
	  	}
    }

    public List<String> getContentTilesList() {
 	      //LOGGER.info("getContentTilesList Called :" + contentTilesList);
    	  return contentTilesList;
    }
 }