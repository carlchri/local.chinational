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


public final class DefaultValuesUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultValuesUtils.class);

    private static final String DEFAULT_NEWS_PATH  = "defaultNewsPath";
    private static final String DEFAULT_BLOGS_PATH  = "defaultBlogsPath";
    private static final String DEFAULT_BLOGS_TILE_IMG_SRC  = "defaultBlogsTileImgSrc";
    private static final String DEFAULT_NEWS_TILE_IMG_SRC  = "defaultNewsTileImgSrc";
    private static final String DEFAULT_MEMBERS_PHOTO_IMG_SRC  = "defaultMembersPhotoSrc";
    private static final String DEFAULT_MESSAGE  = "could not find default value";

    public static String getDefaultValue(Page page, String pName) {
		String defaultPath = "";
    	if(page != null){
    		Resource resource = page.adaptTo(Resource.class);
    		// LOGGER.info("Resourec Path : " + resource.getPath());
			final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
			defaultPath = pageProperties.getInherited(pName, String.class);
			// LOGGER.info("Default Path : " + defaultPath);
			if (defaultPath == null) {
	            LOGGER.trace("could not find inherited property for ", resource);
	            defaultPath = DEFAULT_MESSAGE;
			}
    	} else {
            defaultPath = DEFAULT_MESSAGE;
    	}

        return defaultPath;
    }

    public static String getDefaultNewsPath(Page page) {
        return getDefaultValue(page, DEFAULT_NEWS_PATH);
    }

    public static String getDefaultBlogsPath(Page page) {
        return getDefaultValue(page, DEFAULT_BLOGS_PATH);
    }

    public static String getDefaultBlogsTileImgSrc(Page page) {
		String defaultBlogsTileImgSrc = getDefaultValue(page, DEFAULT_BLOGS_TILE_IMG_SRC);
       if (StringUtils.isNotEmpty(defaultBlogsTileImgSrc) && !"#".equals(defaultBlogsTileImgSrc)) {
    	   defaultBlogsTileImgSrc = LinkUtils.externalize(defaultBlogsTileImgSrc);            
        }
       // LOGGER.info("Default Blogs Tile Image Src : " + defaultTileImgSrc);
        return defaultBlogsTileImgSrc;
    }

    public static String getDefaultNewsTileImgSrc(Page page) {
		String defaultNewsTileImgSrc = getDefaultValue(page, DEFAULT_NEWS_TILE_IMG_SRC);
       if (StringUtils.isNotEmpty(defaultNewsTileImgSrc) && !"#".equals(defaultNewsTileImgSrc)) {
    	   defaultNewsTileImgSrc = LinkUtils.externalize(defaultNewsTileImgSrc);            
        }
       // LOGGER.info("Default News Tile Image Src : " + defaultTileImgSrc);
        return defaultNewsTileImgSrc;
    }

    public static String getDefaultMembersPhotoSrc(Page page) {
        String defaultMembersPhotoSrc = getDefaultValue(page, DEFAULT_MEMBERS_PHOTO_IMG_SRC);
        if (StringUtils.isNotEmpty(defaultMembersPhotoSrc) && !"#".equals(defaultMembersPhotoSrc)) {
            defaultMembersPhotoSrc = LinkUtils.externalize(defaultMembersPhotoSrc);
        }
        LOGGER.info("Default Members Photo Image Src : " + defaultMembersPhotoSrc);
        return defaultMembersPhotoSrc;
    }

}