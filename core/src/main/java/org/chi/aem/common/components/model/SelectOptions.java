/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.Default;

@Model(adaptables = Resource.class)
public class SelectOptions {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(SelectOptions.class);
	    
	    public static final String OPTIONS_VALUE = "optionValue";	
	    public static final String OPTIONS_TEXT = "optionText";

		@Inject
		@Named(OPTIONS_VALUE)
		@Optional
		@Default(values="Placeholder Text")
		private String optionValue;
	
		@Inject
		@Named(OPTIONS_TEXT)
		@Optional
		@Default(values="Placeholder Text")
		private String optionText;
	
		@PostConstruct
		protected void init() {
		}
	
		public String getOptionValue() {
			return optionValue;
		}
	
		public String getOptionText() {
			return optionText;
		}
}