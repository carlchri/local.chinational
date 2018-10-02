package org.chi.aem.common.components.model;

import java.util.ArrayList;
import java.util.Iterator;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
 
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.components.model.WheelCompartmentsDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Model(adaptables = Resource.class)

public class WheelCompartmentsCollection {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(WheelCompartmentsCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource detailsResource;
	
	public java.util.List<WheelCompartmentsDetail> wheelCompartmentsDetails;
 
	@PostConstruct
	protected void init() {
		wheelCompartmentsDetails = new ArrayList<WheelCompartmentsDetail>();
		if (detailsResource != null) {
			populateModel(wheelCompartmentsDetails, detailsResource);
		}
		
	}

	public static java.util.List<WheelCompartmentsDetail> populateModel(
			java.util.List<WheelCompartmentsDetail> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> detailResources = resource.listChildren();
			while (detailResources.hasNext()) {
				WheelCompartmentsDetail detail = detailResources.next().adaptTo(WheelCompartmentsDetail.class);
				if (detail != null)
					array.add(detail);
			}
		}
		return array;
	}
}
