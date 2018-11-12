package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageEnhancementModel extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageEnhancementModel.class);

    private String imagePath;
    private String pagePath;

    @Override
    public void activate() throws Exception {
    	pagePath = "/content/dam/chi-national/website/graphics/chi_logo.png";
        imagePath = "/content/dam/chi-national/website/graphics/chi_logo.png";
        LOGGER.info("imagePath: " + imagePath);
    }

    public String getImagePath() {
        return imagePath;
    }
    
    public String getDesktopImagePath() {
    	LOGGER.info("getDesktopImagePath: ");
    	return "/content/dam/chi-national/website/graphics/chi_logo.png";
    }
    
    public String getIpadImagePath() {
    	LOGGER.info("getIpadImagePath: ");
    	return "/content/dam/chi-national/website/graphics/chi_logo.png";
    }
    
    public String getMobileImagePath() {
    	LOGGER.info("getMobileImagePath: ");
    	return "/content/dam/chi-national/website/graphics/chi_logo.png";
    }

}