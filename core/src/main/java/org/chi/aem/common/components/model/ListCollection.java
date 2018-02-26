/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import java.util.ArrayList;
import java.util.Iterator;
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
 
@Model(adaptables = Resource.class)
public class ListCollection {
 
	public java.util.List<Link> links;
    private static final Logger LOGGER = LoggerFactory.getLogger(ListCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource linksResource;
 
	@PostConstruct
	protected void init() {
		links = new ArrayList<Link>();
		if (linksResource != null) {
			populateModel(links, linksResource);
		}else{
			links.add(new Link());
		}
        // LOGGER.info("links:" + links.toString());
	}
	public static java.util.List<Link> populateModel(
			java.util.List<Link> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				Link link = linkResources.next().adaptTo(Link.class);
				if (link != null)
					array.add(link);
			}
		}
        // LOGGER.info("links:" + array.toString());
		return array;
	}
 
}