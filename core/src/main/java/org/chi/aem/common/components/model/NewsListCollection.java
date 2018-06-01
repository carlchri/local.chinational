/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
 
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import com.day.cq.wcm.api.Page; 
import org.chi.aem.common.components.model.NewsListManual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Model(adaptables = Resource.class)
public class NewsListCollection {
 
	public java.util.List<NewsListManual> newsListCollection;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsListCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource linksResource;
 
	@PostConstruct
	protected void init() {
		newsListCollection = new ArrayList<NewsListManual>();
		if (linksResource != null) {
			populateModel(newsListCollection, linksResource);
		}else{
			newsListCollection.add(new NewsListManual());
		}
        // LOGGER.info("newsListCollection:" + newsListCollection.toString());
	}
	public static java.util.List<NewsListManual> populateModel(
			java.util.List<NewsListManual> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				NewsListManual link = linkResources.next().adaptTo(NewsListManual.class);
				if (link != null)
					array.add(link);
			}
		}
        // LOGGER.info("newsListCollection:" + array.toString());
		return array;
	}
	
    public String getHash() {
        return "myList" + UUID.randomUUID();
    }

 
}