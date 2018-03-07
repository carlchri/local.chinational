/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class)
public class AccordionCollection {
	public java.util.List<Accordion> accordions;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccordionCollection.class);
	public static final String PROP_HEADING = "accordionHeading";
	private String additionalHashString ="";
	@Inject
	@Named(PROP_HEADING)
	@Optional
	private String accordionHeading;

	@Inject
	@Optional
	@Named("items")
	private Resource accordionResource;

	@PostConstruct
	protected void init() {
		accordions = new ArrayList<Accordion>();
		if (accordionResource != null) {
			populateModel(accordions, accordionResource);
			additionalHashString = accordionResource.getPath();
		} else {
			Accordion accordion = new Accordion();
			accordions.add(accordion);
			additionalHashString = accordion.getUniqueId();
		}

		LOGGER.info("accordions:" + accordions.toString());
	}

	public int getHash() {
		if (accordionHeading != null) {
			return (additionalHashString + accordionHeading).hashCode();
		}
		return (additionalHashString + PROP_HEADING).hashCode();
	}

	public String getAccordionHeading() {
		return accordionHeading;
	}

	public java.util.List<Accordion> populateModel(
			java.util.List<Accordion> list, Resource resource) {
		if (resource != null) {
			Iterator<Resource> accordionResources = resource.listChildren();
			while (accordionResources.hasNext()) {
				Accordion accordion = accordionResources.next().adaptTo(
						Accordion.class);
				if (accordion != null)
					list.add(accordion);
			}
		}

		LOGGER.info("accordions: size=" + list.size());

		return list;
	}
}
