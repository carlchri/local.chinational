/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables=SlingHttpServletRequest.class) 
public class DateFormatting { 
	
    public static final Logger LOGGER = LoggerFactory.getLogger(DateFormatting.class);
    
	@Inject // injected as parameter 
	private Calendar date; 

	@Inject // injected as parameter 
	private String dateFormat; 
	
	public String formattedDate;
	
	@PostConstruct 
	protected void init() { 
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat); 
		formattedDate = formatter.format(date.getTime()).toUpperCase(); 
		// LOGGER.info("formattedDate:" + formattedDate);
	} 
}