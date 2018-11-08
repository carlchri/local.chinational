package org.chi.aem.common.utils;

import org.apache.commons.lang3.StringUtils;
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

    public static String getHeaderImage(Page page){
    	LOGGER.info("in Get Header Image");
        return getImage(page, DESKTOP_IMAGE_SRC, DESKTOP_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
    }

    private static String getImage(Page page, String origImageProp, String imageNode, String updatedImageProp){
    	LOGGER.info("in Get get Image");
        if (page != null) {
            // check if original is not empty, as without it, there is no updated image
            ValueMap pageProps = page.getProperties();
            if (!pageProps.isEmpty()) {
                String origImage = pageProps.get(origImageProp, "");
                LOGGER.debug("Orig Image: " + origImage);
                if (!StringUtils.isEmpty(origImage)) {
                    // check for component's node, and if it has src
                    Node pageNode = page.adaptTo(Node.class);
                    try {
                        Node hiNode = pageNode.getNode(JCR_CONTENT_NODE_PATH + imageNode);
                        if (hiNode != null && hiNode.hasProperty(updatedImageProp)) {
                            LOGGER.debug("Return updatedImageProp");
                            return hiNode.getProperty(updatedImageProp).getString();
                        }
                    } catch (javax.jcr.RepositoryException rex) {
                        LOGGER.error("Exception getting image from node: " + rex);
                    }
                    // if not, use original image
                    LOGGER.debug("Return original image");
                    return origImage;
                }
            }
        }
        return null;
    }

    public static String getTileImage(Page page, String articleType){
        String tileImage = null;
        if (page != null) {
        	LOGGER.info("in Get Tile Image");
            // check tileImageOption
            // useHeaderImage, editHeaderImage or separateTileImage
            ValueMap pageProps = page.getProperties();
            if (!pageProps.isEmpty() && pageProps.containsKey(TILE_IMAGE_OPTION)) {
                String tileImageOption = pageProps.get(TILE_IMAGE_OPTION, "");
                if (tileImageOption != null) {
                    if (tileImageOption.equals(TILE_IMAGE_USE_HEADER)) {
                        // if use headerImage, get headerImage
                        tileImage = getHeaderImage(page);
                    } else if (tileImageOption.equals(TILE_IMAGE_EDIT_HEADER)) {
                        // if editHeaderImage, get edited version
                        tileImage = getImage(page, IPAD_IMAGE_SRC, MOBILE_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
                    } else if (tileImageOption.equals(TILE_IMAGE_SEPARATE)) {
                        // if separate image, get orig or updated image
                        tileImage = getImage(page, DESKTOP_IMAGE_SRC, IPAD_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
                    }
                }
            }
        }
        // if all fail, get default image
        return getDefaultTileImage(tileImage, articleType, page);
    }

    private static String getDefaultTileImage(String tileImage, String articleType, Page page) {
        if (tileImage == null || tileImage.equals("")) {
            // if no article type is defined, default would be news
        	if(StringUtils.isEmpty(articleType) || articleType.equals("news")){
        		//tileImage = DefaultValuesUtils.getDefaultNewsTileImgSrc(page);
        	} else if(articleType.equals("blogs")){
        		//tileImage = DefaultValuesUtils.getDefaultBlogsTileImgSrc(page);
        	}
        }
        return tileImage;
    }

}
