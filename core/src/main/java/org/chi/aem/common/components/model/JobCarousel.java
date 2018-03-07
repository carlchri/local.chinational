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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.PageManager;
 
@Model(adaptables = Resource.class)
public class JobCarousel {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(JobCarousel.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource tilesResource;
 
    // storing list of all Job Tiles Identifier
    private java.util.List<String> tilesIdentifier;
    private List<String> contentTilesList;
    
    private PageManager pageManager;
	
	@PostConstruct
	protected void init() {
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
		tilesIdentifier = new ArrayList<String>();
        contentTilesList = new ArrayList<String>();
        
		if (tilesResource != null) {
			populateModel(tilesIdentifier, tilesResource);
		}
	}
	
	public java.util.List<String> populateModel(
			java.util.List<String> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			int i =1;
			while (linkResources.hasNext()) {
				String identifier = linkResources.next().getValueMap().get("jobTileIdentifier", String.class);
				array.add(identifier);
		  	   contentTilesList.add("JobTile" + "-" + i++);
			}
		}
        // LOGGER.info("tilesIdentifier:" + array.toString());
		return array;
	}
	public Collection<String> getTilesIdentifier() {
        // LOGGER.info("tilesIdentifier: " + tilesIdentifier);
        return tilesIdentifier;
    }

	public Collection<String> getContentTilesList() {
        // LOGGER.info("contentTilesList: " + contentTilesList);
        return contentTilesList;
    }
 
}