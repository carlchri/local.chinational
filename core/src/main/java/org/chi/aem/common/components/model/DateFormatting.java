/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;

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
		int day = date.get(Calendar.DAY_OF_MONTH);
		int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;      // 0 to 11
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = day + "/" + month + "/" + year;

        try {
            Date getDate = dateFormatter.parse(dateInString);
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat); 
			formattedDate = formatter.format(getDate).toUpperCase(); 
        } catch (ParseException e) {
            e.printStackTrace();
        }
	} 
}