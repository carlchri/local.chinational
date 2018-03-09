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
import org.chi.aem.common.components.model.LocationDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Model(adaptables = Resource.class)
public class LocationCollection {
 

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationCollection.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("items")
	private Resource detailsResource;
	
	public java.util.List<LocationDetail> locationDetails;
 
	@PostConstruct
	protected void init() {
		locationDetails = new ArrayList<LocationDetail>();
		if (detailsResource != null) {
			populateModel(locationDetails, detailsResource);
		}else{
			locationDetails.add(new LocationDetail());
		}
        // LOGGER.info("locationDetails:" + locationDetails.toString());
	}
	public static java.util.List<LocationDetail> populateModel(
			java.util.List<LocationDetail> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> detailResources = resource.listChildren();
			while (detailResources.hasNext()) {
				LocationDetail detail = detailResources.next().adaptTo(LocationDetail.class);
				if (detail != null)
					array.add(detail);
			}
		}
        // LOGGER.info("locationDetails:" + array.toString());
		return array;
	}
 
}