/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.utils;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.chi.aem.common.utils.ResourceResolverFactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.LinkUtils;
import com.adobe.cq.sightly.WCMUsePojo;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;


public class DefaultValues extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultValues.class);

    private static final String DEFAULT_NEWS_PATH  = "defaultNewsPath";
    private static final String DEFAULT_BLOGS_PATH  = "defaultBlogsPath";
    private static final String DEFAULT_TILE_IMG_SRC  = "defaultTileImgSrc";
    private static final String DEFAULT_MESSAGE  = "could not find default value";

    Resource resource;

    @Override
    public void activate() throws Exception {
    	resource = getResource();
    }

    public String getDefaultNewsPath() {
		String defaultNewsPath = "";
    	if(resource != null){
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultNewsPath = pageProperties.getInherited(DEFAULT_NEWS_PATH, String.class);
			// LOGGER.info("Default News Path : " + defaultNewsPath);
			if (defaultNewsPath == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultNewsPath = DEFAULT_MESSAGE;
			}
    	} else {
            defaultNewsPath = DEFAULT_MESSAGE;
    	}

        return defaultNewsPath;
    }

    public String getDefaultBlogsPath() {
		String defaultBlogsPath = "";
    	if(resource != null){
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultBlogsPath = pageProperties.getInherited(DEFAULT_BLOGS_PATH, String.class);
			// LOGGER.info("Default Blogs Path : " + defaultBlogsPath);
			if (defaultBlogsPath == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultBlogsPath = DEFAULT_MESSAGE;
			}
    	} else {
            defaultBlogsPath = DEFAULT_MESSAGE;
    	}

        return defaultBlogsPath;
    }

    public String getDefaultTileImgSrc() {
		String defaultTileImgSrc = "";
    	if(resource != null){
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultTileImgSrc = pageProperties.getInherited(DEFAULT_TILE_IMG_SRC, String.class);
			// LOGGER.info("Default Tile Image Src : " + defaultTileImgSrc);
			if (defaultTileImgSrc == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultTileImgSrc = DEFAULT_MESSAGE;
			}
    	} else {
            defaultTileImgSrc = DEFAULT_MESSAGE;
    	}
    	
       if (StringUtils.isNotEmpty(defaultTileImgSrc) && !"#".equals(defaultTileImgSrc)) {
    	   defaultTileImgSrc = LinkUtils.externalize(defaultTileImgSrc);            
        }
       // LOGGER.info("Default Tile Image Src : " + defaultTileImgSrc);
        return defaultTileImgSrc;
    }

}