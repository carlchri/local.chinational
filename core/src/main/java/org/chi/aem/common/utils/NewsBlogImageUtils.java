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
public final class NewsBlogImageUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(NewsBlogImageUtils.class);
    public final static String HEADER_IMAGE_NODE = "nb-header-image";
    public final static String TILE_IMAGE_NODE = "nb-tile-image";
    public final static String TILE_FROM_HEADER_IMAGE_NODE = "nb-tile-from-header-image";

    public final static String TILE_IMAGE_SRC = "tileImageSrc";
    public final static String HEADER_IMAGE_SRC = "imageSrc";
    public final static String COMPONENT_IMAGE_SRC = "fileReference";

    public static String getHeaderImage(Page page){
        if (page != null) {
            // check if HEADER_IMAGE_SRC is not empty, as without it, there is no detailed image
            ValueMap headerProps = page.getProperties();
            if (!headerProps.isEmpty()) {
                String headerImage = headerProps.get(HEADER_IMAGE_SRC, "");
                if (!StringUtils.isEmpty(headerImage)) {
                    // check for component HEADER_IMAGE_NODE, and if it has src
                    Node pageNode = page.adaptTo(Node.class);
                    Node hiNode = pageNode.getNode(HEADER_IMAGE_NODE);
                    if (hiNode != null ) {

                    }
                    return headerImage;
                    // if not, use HEADER_IMAGE_SRC
                }
            }
        }
        return null;
    }

}
