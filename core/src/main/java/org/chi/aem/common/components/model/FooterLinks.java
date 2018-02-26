/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = Resource.class)
public class FooterLinks {

	private static final Logger LOGGER = LoggerFactory.getLogger(FooterLinks.class);

	public static final String PROP_ITEMS = "items";
	public List<Link> links;


	@ScriptVariable
	private ValueMap currentStyle;


	@PostConstruct
	protected void init() {
		links = new ArrayList<Link>();
		Object itemObj = currentStyle.get(PROP_ITEMS);
		LOGGER.info("itemObj:" + itemObj);
		if (itemObj != null) {
			Resource itemRes = (Resource)itemObj;
			LOGGER.info("itemRes:" + itemRes);
			if (itemRes.hasChildren()) {
				LOGGER.info("itemRes has children");
				Iterator<Resource> linkResources = itemRes.listChildren();
				while (linkResources.hasNext()) {
					Link link = linkResources.next().adaptTo(Link.class);
					if (link != null)
						LOGGER.info("link:" + link.getLinkText());
						links.add(link);
				}

			}
		}
        LOGGER.info("links:" + links.toString());
	}

 
}