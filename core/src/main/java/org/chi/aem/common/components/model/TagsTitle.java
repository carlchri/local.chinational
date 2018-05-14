/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.LinkUtils;
import com.adobe.cq.sightly.WCMUsePojo;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;


public class TagsTitle extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(TagsTitle.class);

	private String tagNames = "";
	private String tagDescriptions = "";
	
    @Override
    public void activate() throws Exception {
    	// TODO - do we need toi get system resolved for this, seems like its not working in publish
		Tag[] tags = null;
    	ResourceResolver resourceResolver = getResourceResolver();
        Resource resource = getResource();
        // LOGGER.info("Resource: " + resource.getPath());
        Page page = getCurrentPage();
        if(page != null){
	        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
	    	tags = tagManager.getTagsForSubtree(page.adaptTo(Resource.class), false);
        }
	    if(tags.length !=0){
		   	 for(Tag tag : tags){
		   		String tagName = tag.getTitle();
		   		String tagDescription = tag.getDescription();
		   		LOGGER.debug("Tag Title: " + tagName);
				LOGGER.debug("Tag Description: " + tagDescription);
				// TODO - create link for tags, when clicked on, will take user back to main blog/news page
				// and display blog marked only for that tag.
				tagNames = addOrUpdate(tagName, tagNames) ;
				tagDescriptions = addOrUpdate(tagDescription, tagDescriptions) ;
		   		/*if(!tagName.isEmpty()){
					tagNames += ", " + tagName;
		   		}else{
					tagNames = tagName;
		   		}*/
		   	 }
	    }
   		LOGGER.debug("tagNames: " + tagNames);
		LOGGER.debug("tagDescriptions: " + tagDescriptions);
    }

    private String addOrUpdate(String input, String existingValue) {
		LOGGER.debug("addOrUpdate value: " + existingValue);
		if(!existingValue.isEmpty()){
			existingValue += ", " + input;
		}else{
			existingValue = input;
		}
		LOGGER.debug("addOrUpdate new value: " + existingValue);
		return existingValue;
	}

    public String getTagName() {
        return tagNames;
    }

	public String getTagDescription() {
		return tagDescriptions;
	}


}