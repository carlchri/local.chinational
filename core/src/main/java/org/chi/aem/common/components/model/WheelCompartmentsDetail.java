package org.chi.aem.common.components.model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.Default;

@Model(adaptables = Resource.class)

public class WheelCompartmentsDetail {

	public static final Logger LOGGER = LoggerFactory.getLogger(WheelCompartmentsDetail.class);
    
    public static final String PROP_COMPARTMENT_TITLE = "compartment_title";
    public static final String PROP_COMPARTMENT_IMAGE = "compartment_image";
    public static final String PROP_COMPARTMENT_TEXT = "compartment_text";

	@Inject
	private ResourceResolver resourceResolver;


	@Inject
	@Named(PROP_COMPARTMENT_TITLE)
	@Optional
	@Default(values="Compartment Title")
	private String compartment_title;

	@Inject
	@Named(PROP_COMPARTMENT_IMAGE)
	@Optional
	@Default(values="Compartment Image")
	private String compartment_image;

	@Inject
	@Named(PROP_COMPARTMENT_TEXT)
	@Optional
	@Default(values="Compartment Text")
	private String compartment_text;

	@PostConstruct
	protected void init() {
      
	}

	public String getCompartment_title() {
		return compartment_title;
	}
	
	public String getCompartment_image() {
		return compartment_image;
	}

	public String getCompartment_text() {
		return compartment_text;
	}
}
