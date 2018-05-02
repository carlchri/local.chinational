/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.UUID;
//import com.day.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class Accordion {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(Accordion.class);

	public static final String PROP_ACCORDION_TITLE = "accordionTitle";
	public static final String PROP_ACCORDION_TEXT = "accordionText";
	public static final String EXPAND = "expand";

	@Inject
	@Named(PROP_ACCORDION_TEXT)
	@Optional
	@Default(values = "Accordion Text")
	private String accordionText;

	@Inject
	@Named(PROP_ACCORDION_TITLE)
	@Optional
	@Default(values = "")
	private String accordionTitle;

	@Inject
	@Named(EXPAND)
	@Optional
	@Default(values = "false")
	private boolean expand;
	
	private String uniqueId = UUID.randomUUID().toString();

	public String getUniqueId() {
		LOGGER.debug("***uniqueId=" + uniqueId);
		
		return uniqueId;
	}

	public String getAccordionText() {
		return accordionText;
	}

	public String getAccordionTitle() {
		return accordionTitle;
	}

	public boolean getExpand() {
		return expand;
	}

}
