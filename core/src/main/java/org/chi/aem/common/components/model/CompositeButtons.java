/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;

public class CompositeButtons extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(CompositeButtons.class);

    public static final String NO_OF_BUTTONS = "noOfButtons";

    private List<String> buttonList; 
    
    private int width;

    @Override
    public void activate() throws Exception {
        buttonList = new ArrayList<String>();		
        width = 100/(Integer.parseInt(getProperties().get(NO_OF_BUTTONS, "1")));
        LOGGER.info("getWidth Called :" + width);
        for (int i = 1; i <= Integer.parseInt(getProperties().get(NO_OF_BUTTONS, "1")); i++) {
	  	   buttonList.add("Button" + "-" + i);
	  	}
    }

    public List<String> getButtonList() {
 	     // LOGGER.info("getButtonList Called :" + buttonList);
    	  return buttonList;
    }
    
    public int getWidth() {
	  // LOGGER.info("getWidth Called :" + width);
   	  return width;
   }

}