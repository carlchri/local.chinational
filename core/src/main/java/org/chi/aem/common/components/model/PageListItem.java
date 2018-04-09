/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.day.cq.dam.api.Asset;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

public class PageListItem {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageListItem.class);
    private static final String DC_TITLE = "dc:title";
    private static final String DC_DESCRIPTION = "dc:description";
    protected SlingHttpServletRequest request;
    protected Page page;
    protected Asset asset;

    public PageListItem(@Nonnull SlingHttpServletRequest request, @Nonnull Resource resource) {
        this.request = request;
        Page page = getPage(resource);
        if (page != null) {
            this.page = page;
        }else {
            Asset asset = getAsset(resource);
            this.asset = asset;
        }
    }

    public String getTitle() {
        String title = null;
        if(page != null) {
            title = page.getNavigationTitle();
            if (StringUtils.isEmpty( title )) {
                title = page.getPageTitle();
            }
            if (StringUtils.isEmpty( title )) {
                title = page.getTitle();
            }
            if (StringUtils.isEmpty( title )) {
                title = page.getName();
            }
        }else {
            if(asset.getMetadataValue(DC_TITLE) != null) {
                title = asset.getMetadataValue(DC_TITLE).toString();
            }
            if(StringUtils.isEmpty( title )) {
                title = asset.getName();
            }
        }
        return title;
    }

    public String getDescription() {
        String description = "";
        if(page != null) {
            String templateTitle = page.getTemplate().getTitle();
            if (templateTitle.contains("Blogs Details Page") || templateTitle.contains("News Details Page")) {
                description = page.getProperties().get("excerpt", "");
            }else {
                description = page.getDescription();
            }
        }else {
            if(asset.getMetadataValue(DC_DESCRIPTION) != null) {
                description = asset.getMetadataValue(DC_DESCRIPTION);
            }
        }
        return description;
    }

    public Calendar getLastModified() {
        return page.getLastModified();
    }

    public String getPath() {
        String path = "";
        if(page != null) {
            path = page.getPath() + ".html";
        }else {
            path = asset.getPath();
        }
        return path;
    }

    private Page getPage(Resource resource) {
        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                return pageManager.getContainingPage(resource);
            }
        }
        return null;
    }

    private Asset getAsset(Resource resource) {
        if (resource != null) {
            return resource.adaptTo(Asset.class);
        }
        return null;
    }

    private Page getRedirectTarget(@Nonnull Page page) {
        Page result = page;
        String redirectTarget;
        PageManager pageManager = page.getPageManager();
        Set<String> redirectCandidates = new LinkedHashSet<>();
        redirectCandidates.add(page.getPath());
        while (result != null && StringUtils.isNotEmpty((redirectTarget = result.getProperties().get("cq:redirectTarget", String.class)))) {
            result = pageManager.getPage(redirectTarget);
            if (result != null) {
                if (!redirectCandidates.add(result.getPath())) {
                    LOGGER.warn("Detected redirect loop for the following pages: {}.", redirectCandidates.toString());
                    break;
                }
            }
        }
        return result;
    }

}