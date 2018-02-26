/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

/**
 * Defines the {@code FooterLinkCollection} Sling Model used for the
 * {@code /apps/chinational/foundation/structure/footLinks} component. This
 * component gets list of footer links.
 * 
 */

// Model(adaptables = SlingHttpServletRequest.class)
public class FooterLinkCollection extends WCMUsePojo {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FooterLinkCollection.class);

	private static final String ITEMS = "items";

	private List<Link> items = new ArrayList<Link>();

	public List<Link> getItems() {
		return items;
	}

	@Override
	public void activate() throws Exception {
		LOGGER.info("activate called");

		String resourcePath = getCurrentStyle().getPath();
		Resource res = getResourceResolver().getResource(resourcePath);
		LOGGER.info("***resource=" + res);

		populateModel(items, res.getChild(ITEMS));
	}

	private List<Link> populateModel(List<Link> list, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				Link link = linkResources.next().adaptTo(Link.class);
				if (link != null)
					list.add(link);
			}
		}

		return list;
	}

}