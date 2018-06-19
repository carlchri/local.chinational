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
import org.chi.aem.common.components.model.SelectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Model(adaptables = Resource.class)
public class SelectOptionsCollection {
 
	public java.util.List<SelectOptions> selectOptionsCollection;
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectOptionsCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("options")
	private Resource optionsResource;
 
	@PostConstruct
	protected void init() {
		selectOptionsCollection = new ArrayList<SelectOptions>();
		if (optionsResource != null) {
			populateModel(selectOptionsCollection, optionsResource);
		}else{
			selectOptionsCollection.add(new SelectOptions());
		}
        // LOGGER.info("selectOptionsCollection:" + selectOptionsCollection.toString());
	}
	public static java.util.List<SelectOptions> populateModel(
			java.util.List<SelectOptions> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				SelectOptions link = linkResources.next().adaptTo(SelectOptions.class);
				if (link != null)
					array.add(link);
			}
		}
        // LOGGER.info("selectOptionsCollection:" + array.toString());
		return array;
	}
	
    public String getHash() {
        return "select" + UUID.randomUUID();
    }

 
}