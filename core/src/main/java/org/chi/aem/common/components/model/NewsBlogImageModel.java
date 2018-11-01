/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~
 ~ Used by News and Blog details page template to get header image
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.chi.aem.common.utils.NewsBlogImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsBlogImageModel extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(NewsBlogImageModel.class);

    private String imagePath;
    private String pagePath;
    private String articleType;

    @Override
    public void activate() throws Exception {
    	pagePath = get("path", String.class);
    	articleType = get("articleType", String.class);
        imagePath = NewsBlogImageUtils.getHeaderImage(getCurrentPage());
        LOGGER.debug("imagePath: " + imagePath);
    }


    public String getImagePath() {
        return imagePath;
    }

    public Page getPage() {
		Page page = null;
		if (getRequest() == null) {
			return page;
		}
		ResourceResolver resolver = getRequest().getResourceResolver();
		if (StringUtils.startsWith(pagePath, "/") && resolver != null) {
			PageManager pageManager = resolver.adaptTo(PageManager.class);
			page = pageManager.getContainingPage(pagePath);
			pageManager = null;
		}
		
		if(page != null){
			LOGGER.debug("Page: " + page.getPath());
		}
//		log.info("The page"+ page.getPath());
		return page;
	}
    
    public String getTileImagePath() {
    	String tilesImagePath = NewsBlogImageUtils.getTileImage(getPage(), articleType); 
    	LOGGER.debug("tileImagePath: " + tilesImagePath);
        return tilesImagePath;
    }

}