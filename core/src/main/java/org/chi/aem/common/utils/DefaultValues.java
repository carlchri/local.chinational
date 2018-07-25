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
    private static final String DEFAULT_BLOGS_TILE_IMG_SRC  = "defaultBlogsTileImgSrc";
    private static final String DEFAULT_NEWS_TILE_IMG_SRC  = "defaultNewsTileImgSrc";
    private static final String DEFAULT_MEMBERS_TILE_IMG_SRC  = "defaultMembersPhotoSrc";
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

    public String getDefaultBlogsTileImgSrc() {
		String defaultBlogsTileImgSrc = "";
    	if(resource != null){
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultBlogsTileImgSrc = pageProperties.getInherited(DEFAULT_BLOGS_TILE_IMG_SRC, String.class);
			// LOGGER.info("Default Blogs Tile Image Src : " + defaultBlogsTileImgSrc);
			if (defaultBlogsTileImgSrc == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultBlogsTileImgSrc = DEFAULT_MESSAGE;
			}
    	} else {
            defaultBlogsTileImgSrc = DEFAULT_MESSAGE;
    	}
    	
       if (StringUtils.isNotEmpty(defaultBlogsTileImgSrc) && !"#".equals(defaultBlogsTileImgSrc)) {
    	   defaultBlogsTileImgSrc = LinkUtils.externalize(defaultBlogsTileImgSrc);            
        }
       // LOGGER.info("Default Blogs Tile Image Src : " + defaultTileImgSrc);
        return defaultBlogsTileImgSrc;
    }

    public String getDefaultNewssTileImgSrc() {
		String defaultNewsTileImgSrc = "";
    	if(resource != null){
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultNewsTileImgSrc = pageProperties.getInherited(DEFAULT_NEWS_TILE_IMG_SRC, String.class);
			// LOGGER.info("Default News Tile Image Src : " + defaultNewsTileImgSrc);
			if (defaultNewsTileImgSrc == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultNewsTileImgSrc = DEFAULT_MESSAGE;
			}
    	} else {
            defaultNewsTileImgSrc = DEFAULT_MESSAGE;
    	}
    	
       if (StringUtils.isNotEmpty(defaultNewsTileImgSrc) && !"#".equals(defaultNewsTileImgSrc)) {
    	   defaultNewsTileImgSrc = LinkUtils.externalize(defaultNewsTileImgSrc);            
        }
       // LOGGER.info("Default News Tile Image Src : " + defaultTileImgSrc);
        return defaultNewsTileImgSrc;
    }

    public String getDefaultMembersPhotoSrc() {
		String defaultMembersPhotoSrc = "";
    	if(resource != null){
    		LOGGER.info("Resource Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultMembersPhotoSrc = pageProperties.getInherited(DEFAULT_MEMBERS_TILE_IMG_SRC, String.class);
			LOGGER.info("Default Members Tile Image Src : " + defaultMembersPhotoSrc);
			if (defaultMembersPhotoSrc == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultMembersPhotoSrc = DEFAULT_MESSAGE;
			}
    	} else {
    		defaultMembersPhotoSrc = DEFAULT_MESSAGE;
    	}
    	
       if (StringUtils.isNotEmpty(defaultMembersPhotoSrc) && !"#".equals(defaultMembersPhotoSrc)) {
    	   defaultMembersPhotoSrc = LinkUtils.externalize(defaultMembersPhotoSrc);            
        }
        LOGGER.info("Default Members Tile Image Src : " + defaultMembersPhotoSrc);
        return defaultMembersPhotoSrc;
    }
}