package org.chi.aem.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;

import javax.jcr.Node;
import javax.jcr.NodeIterator;


/**
 * Utility class to get path of header and tile image for news and blog
 */
public final class NewsBlogImageUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(NewsBlogImageUtils.class);
    public final static String HEADER_IMAGE_NODE = "nb-header-image";
    public final static String TILE_IMAGE_NODE = "nb-tile-image";
    public final static String TILE_FROM_HEADER_IMAGE_NODE = "nb-tile-from-header-image";

    public final static String TILE_IMAGE_SRC = "tileImageSrc";
    public final static String HEADER_IMAGE_SRC = "imageSrc";
    public final static String COMPONENT_IMAGE_SRC = "fileReference";
    public final static String COMPONENT_IMAGE_UPDATED_SRC = "src";

    public final static String JCR_CONTENT_NODE_PATH = "jcr:content/";

    public final static String TILE_IMAGE_OPTION = "tileImageOption";
    public final static String TILE_IMAGE_USE_HEADER = "useHeaderImage";
    public final static String TILE_IMAGE_EDIT_HEADER = "editHeaderImage";
    public final static String TILE_IMAGE_SEPARATE = "separateTileImage";

    public static String getHeaderImage(Page page){
        return getImage(page, HEADER_IMAGE_SRC, HEADER_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
    }

    private static String getImage(Page page, String origImageProp, String imageNode, String updatedImageProp){
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
                        tileImage = getImage(page, HEADER_IMAGE_SRC, TILE_FROM_HEADER_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
                    } else if (tileImageOption.equals(TILE_IMAGE_SEPARATE)) {
                        // if separate image, get orig or updated image
                        tileImage = getImage(page, TILE_IMAGE_SRC, TILE_IMAGE_NODE, COMPONENT_IMAGE_UPDATED_SRC);
                    }
                }
            }
        }
        // if all fail, get default image
        return getDefaultTileImage(tileImage, articleType, page);
    }

    private static String getDefaultTileImage(String tileImage, String articleType, Page page){
        if (tileImage == null || tileImage.equals("")) {
            // if no article type is defined, default would be news
        	if(StringUtils.isEmpty(articleType) || articleType.equals("news")){
        		tileImage = DefaultValuesUtils.getDefaultNewsTileImgSrc(page);
        	} else if(articleType.equals("blogs")){
        		tileImage = DefaultValuesUtils.getDefaultBlogsTileImgSrc(page);
        	}
        }
        return tileImage;
    }

}
