/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.chi.aem.common.utils.ResourceResolverFactoryService;
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

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;


public class TagsTitle extends WCMUsePojo {

    //@Inject is not working in WCMUsePojo, hence using SlingScripthandler to get service
    ResourceResolverFactoryService resourceResolverFactoryService;

    public static final Logger LOGGER = LoggerFactory.getLogger(TagsTitle.class);

    ResourceResolverFactory resourceResolverFactory;
    ResourceResolver resourceResolver;

	private String tagNames = "";
	private String tagDescriptions = "";
    private Map<String, String> tagsMap = new HashMap<String, String>();
	
    @Override
    public void activate() throws Exception {
    	// We need to use system resolver for getting tags, else its not working in publish
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "tagManagement");
        try {
            resourceResolverFactoryService= getSlingScriptHelper().getService(ResourceResolverFactoryService.class);
			if (resourceResolverFactoryService != null)
            	resourceResolverFactory = resourceResolverFactoryService.getResourceResolverFactory();
            if (resourceResolverFactory != null)
            	resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        }
        catch(Exception e)
        {
            LOGGER.error("Exception to get resource resolver:", e);
        }
		Tag[] tags = null;
        Page page = getCurrentPage();
        if(page != null && resourceResolver != null){
	        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
	    	tags = tagManager.getTagsForSubtree(page.adaptTo(Resource.class), false);
        }
	    if(tags.length !=0){
		   	 for(Tag tag : tags){
		   		String tagName = tag.getTitle();
		   		String tagDescription = tag.getDescription();
		   		tagsMap.put(tag.getName(),tagName);
		   		LOGGER.debug("Tag Title: " + tagName);
				LOGGER.debug("Tag Description: " + tagDescription);
				// TODO - create link for tags, when clicked on, will take user back to main blog/news page
				// and display blog marked only for that tag.
				tagNames = addOrUpdate(tagName, tagNames) ;
				tagDescriptions = addOrUpdate(tagDescription, tagDescriptions) ;
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

    public Map<String, String> getTagsMap() {
        return tagsMap;
    }

}