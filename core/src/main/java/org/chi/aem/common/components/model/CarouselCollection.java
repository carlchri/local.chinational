/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
 
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import com.day.cq.wcm.api.Page; 
import org.chi.aem.common.components.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.PageManager;
 
@Model(adaptables = Resource.class)
public class CarouselCollection {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(CarouselCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource linksResource;
 
    // storing list of all physician Tiles
    private java.util.List<Page> links;
    
    private PageManager pageManager;
	
	@PostConstruct
	protected void init() {
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
		links = new ArrayList<Page>();
		if (linksResource != null) {
			populateModel(links, linksResource);
		}
	}
	
	public java.util.List<Page> populateModel(
			java.util.List<Page> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				String pagePath = linkResources.next().getValueMap().get("linkUrl", String.class);
				Page page = pageManager.getContainingPage(resourceResolver.getResource(pagePath));
				if (page != null && !array.contains(page)){
					if(page.getProperties().get("cq:template").equals("/apps/chinational/templates/directoryprofilepage")){
						array.add(page);
					}
				}
			}
		}
        // LOGGER.info("links:" + array.toString());
		return array;
	}
	public Collection<Page> getLinks() {
        // LOGGER.info("links: " + links);
        return links;
    }

 
}