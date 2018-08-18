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

	private String tagsDesc = "";
	
    @Override
    public void activate() throws Exception {
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
		   		// LOGGER.info("Tag Title: " + tagName);
		   		if(!tagsDesc.isEmpty()){
		   			tagsDesc += ", " + tagName;
		   		}else{
		   			tagsDesc = tagName;
		   		}
		   	 }
	    }
   		// LOGGER.info("tagDesc: " + tagsDesc);
    }

    public String getTagsDesc() {
        return tagsDesc;
    }


}