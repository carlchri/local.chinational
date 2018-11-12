package org.chi.aem.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

import javax.jcr.Node;

/**
 * Utility class to get path of header and tile image for news and blog
 */
public final class ImageUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
    public final static String DESKTOP_IMAGE_NODE = "desktop";
    public final static String IPAD_IMAGE_NODE = "ipad";
    public final static String MOBILE_IMAGE_NODE = "mobile";

    public final static String DESKTOP_IMAGE_SRC = "desktopImageSrc";
    public final static String IPAD_IMAGE_SRC = "ipadImageSrc";
    public final static String MOBILE_IMAGE_SRC = "mobileImageSrc";
    
    public final static String COMPONENT_IMAGE_SRC = "fileReference";
    public final static String COMPONENT_IMAGE_UPDATED_SRC = "src";

    public final static String JCR_CONTENT_NODE_PATH = "jcr:content/";

    public final static String TILE_IMAGE_OPTION = "tileImageOption";
    public final static String TILE_IMAGE_USE_HEADER = "useHeaderImage";
    public final static String TILE_IMAGE_EDIT_HEADER = "editHeaderImage";
    public final static String TILE_IMAGE_SEPARATE = "separateTileImage";

    public static String getHeaderImage(Resource page) {
    	LOGGER.info("in Get Header Image");
        return getImage(page, DESKTOP_IMAGE_SRC, DESKTOP_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
    }

    private static String getImage(Resource page, String origImageProp, String imageNode, String updatedImageProp) {
    	LOGGER.info("in Get get Image 1");
       
        return "/content/dam/chi-national/website/graphics/chi_logo.png";
    }

    public static String getImage(Resource resource) {
    	LOGGER.info("in Get get Image 2");
        return "/content/dam/chi-national/website/graphics/chi_logo.png";
    }

}
