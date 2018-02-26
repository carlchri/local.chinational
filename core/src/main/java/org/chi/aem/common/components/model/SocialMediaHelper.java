/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package org.chi.aem.common.components.model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper class for page functionality related to page sharing by user on social media platforms.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = {com.adobe.cq.wcm.core.components.models.SocialMediaHelper.class, ComponentExporter.class})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SocialMediaHelper implements com.adobe.cq.wcm.core.components.models.SocialMediaHelper {


    private static final Logger LOGGER = LoggerFactory.getLogger(SocialMediaHelper.class);

    //Open Graph & Twritter metadata property names
    static final String OG_TITLE = "og:title";
    static final String TWITTER_TITLE = "twitter:title";
    static final String OG_URL = "og:url";
    static final String OG_TYPE = "og:type";
    static final String TWITTER_TYPE = "twitter:card";
    static final String OG_SITE_NAME = "og:site_name";
    static final String TWITTER_SITE_NAME = "twitter:site";
    static final String OG_IMAGE = "og:image";
    static final String TWITTER_IMAGE = "twitter:image";
    static final String OG_DESCRIPTION = "og:description";
    static final String TWITTER_DESCRIPTION = "twitter:description";

    static final String OG_TITLE_PROP = "ogtitle";
    static final String OG_IMAGE_PROP = "ogimage";
    static final String OG_DESCRIPTION_PROP = "ogdesc";

    @ScriptVariable
    private Page currentPage = null;

    @Self
    private SlingHttpServletRequest request = null;

    @ScriptVariable
    private SlingHttpServletResponse response = null;

    @SlingObject
    private ResourceResolver resourceResolver = null;

    @OSGiService
    private Externalizer externalizer = null;


    /**
     * Holds the metadata for a page.
     */
    private Map<String, String> metadata;

    //*************** WEB INTERFACE METHODS *******************

    @Override
    public boolean isFacebookEnabled() {
        return false;
    }

    @Override
    public boolean isPinterestEnabled() {
        return false;
    }

    @Override
    public boolean isSocialMediaEnabled() {
        return true;
    }

    @Override
    public boolean hasFacebookSharing() {
        return false;
    }

    @Override
    public boolean hasPinterestSharing() {
        return false;
    }

    @Override
    public Map<String, String> getMetadata() {
        if (metadata == null) {
            initMetadata();
        }
        return metadata;
    }



    /**
     * Prepares Open Graph and Twitter metadata for a page to be shared on social media services.
     */
    private void initMetadata() {
        metadata = new LinkedHashMap<>();
        if (isSocialMediaEnabled()) {
            WebsiteMetadata websiteMetadata = createMetadataProvider();
            String title = websiteMetadata.getTitle();
            put(OG_TITLE, title);
            put(TWITTER_TITLE, title);

            put(OG_URL, websiteMetadata.getURL());

            String type = websiteMetadata.getTypeName();
            put(OG_TYPE, type);
            put(TWITTER_TYPE, type);

            String site = websiteMetadata.getSiteName();
            put(OG_SITE_NAME, site);
            put(TWITTER_SITE_NAME, site);

            String image = websiteMetadata.getImage();
            put(OG_IMAGE, image);
            put(TWITTER_IMAGE, image);

            String desc = websiteMetadata.getDescription();
            put(OG_DESCRIPTION, desc);
            put(TWITTER_DESCRIPTION, desc);
        }
    }

    /**
     * Put non-blank named values in metadata map.
     */
    private void put(String name, String value) {
        if (StringUtils.isNotBlank(value)) {
            metadata.put(name, value);
        }
    }

    /**
     * Instantiates the suitable metadata provider based on the contents of the current page.
     */
    private WebsiteMetadata createMetadataProvider() {
        return new WebsiteMetadataProvider();
    }


    /**
     * Provides metadata based on the content of a generic webpage.
     */
    private interface WebsiteMetadata {
        enum Type {website}

        String getTitle();

        String getURL();

        Type getType();

        String getTypeName();

        String getImage();

        String getDescription();

        String getSiteName();
    }



    private class WebsiteMetadataProvider implements WebsiteMetadata {
        private static final String PN_IMAGE_FILE_JCR_CONTENT = "image/file/" + JcrConstants.JCR_CONTENT;
        private ValueMap pageProp = currentPage.getProperties();

        private String getPageProperties(String propName){
            Object propObj = pageProp.get(propName);
            if (propObj != null ) {
                return String.valueOf(propObj);
            }
            return null;
        }

        @Override
        public String getTitle() {
            String title = getPageProperties(OG_TITLE_PROP);
            if (LinkUtils.isNullOrBlank(title)) {
                title = currentPage.getPageTitle();
                if (StringUtils.isBlank(title)) {
                    title = currentPage.getTitle();
                    if (StringUtils.isBlank(title)) {
                        title = currentPage.getName();
                    }
                }
            }
            return title;
        }

        @Override
        public String getURL() {
            String pagePath = currentPage.getPath();
            String extension = request.getRequestPathInfo().getExtension();
            String url = externalizer.publishLink(resourceResolver, pagePath) + "." + extension;
            return url;
        }

        @Override
        public Type getType() {
            return Type.website;
        }

        @Override
        public String getTypeName() {
            return getType().name();
        }

        @Override
        public String getImage() {
            String image = getPageProperties(OG_IMAGE_PROP);
            if (LinkUtils.isNullOrBlank(image)) {
                image = getThumbnailUrl(currentPage, 800, 480);
            }
            image = externalizer.publishLink(resourceResolver, image);
            return image;
        }

        private String getThumbnailUrl(Page page, int width, int height) {
            String ck = "";

            ValueMap metadata = page.getProperties(PN_IMAGE_FILE_JCR_CONTENT);
            if (metadata != null) {
                Calendar imageLastModified = metadata.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
                Calendar pageLastModified = page.getLastModified();
                if (pageLastModified != null && pageLastModified.after(imageLastModified)) {
                    ck += pageLastModified.getTimeInMillis() / 1000;
                } else if (imageLastModified != null) {
                    ck += imageLastModified.getTimeInMillis() / 1000;
                } else if (pageLastModified != null) {
                    ck += pageLastModified.getTimeInMillis() / 1000;
                }
            }

            return page.getPath() + ".thumb." + width + "." + height + ".png?ck=" + ck;
        }


        @Override
        public String getDescription() {
            String desc = getPageProperties(OG_DESCRIPTION_PROP);
            if (LinkUtils.isNullOrBlank(desc)) {
                desc = currentPage.getDescription();
            }
            return desc;
        }

        @Override
        public String getSiteName() {
            Page page = findRootPage();

            String pageTitle = page.getPageTitle();
            if (StringUtils.isNotBlank(pageTitle)) {
                return pageTitle;
            }

            Resource content = page.getContentResource();
            if (content == null) {
                return null;
            }
            String title = content.getValueMap().get(JcrConstants.JCR_TITLE, String.class);
            if (StringUtils.isBlank(title)) {
                return null;
            }
            return title;
        }

        private Page findRootPage() {
            Page page = currentPage;
            while (true) {
                Page parent = page.getParent();
                if (parent == null) {
                    return page;
                } else {
                    page = parent;
                }
            }
        }
    }

}
