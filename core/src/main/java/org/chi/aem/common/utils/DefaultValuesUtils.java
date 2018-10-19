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
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Map;


public final class DefaultValuesUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultValuesUtils.class);

    private static final String DEFAULT_NEWS_PATH  = "defaultNewsPath";
    private static final String DEFAULT_BLOGS_PATH  = "defaultBlogsPath";
    private static final String DEFAULT_BLOGS_TILE_IMG_SRC  = "defaultBlogsTileImgSrc";
    private static final String DEFAULT_NEWS_TILE_IMG_SRC  = "defaultNewsTileImgSrc";
    private static final String DEFAULT_MEMBERS_PHOTO_IMG_SRC  = "defaultMembersPhotoSrc";
    private static final String DEFAULT_NEWS_BLOG_IMG  = "/etc/designs/chicommon/images/blogNewsDefGraphic.jpg";
    private static final String EXTERNALIZER_DOMAIN  = "siteExternailizer";
    private static final String DEFAULT_EXTERNALIZER_DOMAIN = "national";
    private static final String JCR_CONTENT = "jcr:content";
    private static final String DEFAULT_MESSAGE  = "could not find default value";

    public static String getDefaultValue(Page page, String pName) {
        return getDefaultValue(page, pName, DEFAULT_MESSAGE);
    }

    public static String getDefaultValue(Page page, String pName, String defValue) {
        String defaultPath = "";
        if(page != null){
            Resource resource = page.adaptTo(Resource.class);
            LOGGER.debug("Resource Path : " + resource.getPath());
            final InheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(resource);
            defaultPath = pageProperties.getInherited(pName, String.class);
            LOGGER.debug("Inherited Value : " + defaultPath + ", for attribute: " + pName);
            if (defaultPath == null) {
                // if property is directly at the page level, InheritanceValueMap does not find it, so check once at main page level too
                // this happens with SiteMap
                LOGGER.debug("could not find inherited property for resource, lets try main resource page ", resource.getPath());
                try {
                    Node propNode = resource.adaptTo(Node.class);
                    Node jcrNode = propNode.getNode(JCR_CONTENT);
                    if (jcrNode.hasProperty(pName)) {
                        LOGGER.debug("JCR node has value!");
                        defaultPath = jcrNode.getProperty(pName).getString();
                    }
                } catch (RepositoryException rex) {
                    LOGGER.error("error reading respoitory to get attribute: " + pName);
                }
                if (defaultPath == null) {
                    LOGGER.info("could not find property for resource, using default value", resource.getPath());
                    defaultPath = defValue;
                }
            }
        } else {
            defaultPath = defValue;
        }

        return defaultPath;
    }

    public static String getSiteExternalizer(Page page) {
        return getDefaultValue(page, EXTERNALIZER_DOMAIN, DEFAULT_EXTERNALIZER_DOMAIN);
    }

    public static String getDefaultNewsPath(Page page) {
        return getDefaultValue(page, DEFAULT_NEWS_PATH);
    }

    public static String getDefaultBlogsPath(Page page) {
        return getDefaultValue(page, DEFAULT_BLOGS_PATH);
    }

    public static String getDefaultBlogsTileImgSrc(Page page) {
		String defaultBlogsTileImgSrc = getDefaultValue(page, DEFAULT_BLOGS_TILE_IMG_SRC, DEFAULT_NEWS_BLOG_IMG);
       if (StringUtils.isNotEmpty(defaultBlogsTileImgSrc) && !"#".equals(defaultBlogsTileImgSrc)) {
    	   defaultBlogsTileImgSrc = LinkUtils.externalize(defaultBlogsTileImgSrc);            
        }
       // LOGGER.info("Default Blogs Tile Image Src : " + defaultTileImgSrc);
        return defaultBlogsTileImgSrc;
    }

    public static String getDefaultNewsTileImgSrc(Page page) {
		String defaultNewsTileImgSrc = getDefaultValue(page, DEFAULT_NEWS_TILE_IMG_SRC, DEFAULT_NEWS_BLOG_IMG);
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