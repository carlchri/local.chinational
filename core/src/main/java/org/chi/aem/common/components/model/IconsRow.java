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
import org.chi.aem.common.components.model.Icon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Model(adaptables = Resource.class)
public class IconsRow {
 
	public java.util.List<Icon> preIcons;
	public java.util.List<Icon> customIcons;
    private static final Logger LOGGER = LoggerFactory.getLogger(IconsRow.class);
	@Inject
	private ResourceResolver resourceResolver;
 
	@Inject
	@Optional
	@Named("preIcons")
	private Resource preIconsResource;
 
	@Inject
	@Optional
	@Named("customIcons")
	private Resource customIconsResource;
 
	@PostConstruct
	protected void init() {
		preIcons = new ArrayList<Icon>();
		customIcons = new ArrayList<Icon>();
		if (preIconsResource != null) {
			populateModel(preIcons, preIconsResource);
		}else{
			preIcons.add(new Icon());
		}
		if (customIconsResource != null) {
			populateModel(customIcons, customIconsResource);
		}else{
			customIcons.add(new Icon());
		}
        // LOGGER.info("preIcons:" + preIcons.toString());
	}
	public static java.util.List<Icon> populateModel(
			java.util.List<Icon> array, Resource resource) {
		if (resource != null) {
			Iterator<Resource> iconResources = resource.listChildren();
			while (iconResources.hasNext()) {
				Icon icon = iconResources.next().adaptTo(Icon.class);
				if (icon != null)
					array.add(icon);
			}
		}
        // LOGGER.info("preIcons:" + array.toString());
		return array;
	}
 
}