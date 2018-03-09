/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

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
public class LocationDetail {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(LocationDetail.class);
	    
	    public static final String PROP_ICON_SELECTOR = "icon";	
	    public static final String PROP_LOCATION_TEXT = "locationText";
	    public static final String PROP_LINK_URL = "linkUrl";
	    public static final String TARGET_BLANK = "targetBlank";

		@Inject
		private ResourceResolver resourceResolver;
	
		@Inject
		@Named(PROP_ICON_SELECTOR)
		@Optional
		@Default(values="iconNotRequired")
		private String iconClass;
	
		@Inject
		@Named(PROP_LOCATION_TEXT)
		@Optional
		@Default(values="Location Text")
		private String locationText;
	
		@Inject
		@Named(PROP_LINK_URL)
		@Optional
		@Default(values="#")
		private String linkUrl;
	
		@Inject
		@Named(TARGET_BLANK)
		@Optional
		@Default(values="false")
		private boolean targetBlank;
	
		@PostConstruct
		protected void init() {
	       if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
	            linkUrl = LinkUtils.externalize(linkUrl);            
	        }
		}
	
		public String getLocationText() {
			return locationText;
		}
	
		public String getIconClass() {
			return iconClass;
		}
		
		public String getLinkUrl() {
			return linkUrl;
		}
	
		public boolean getTargetBlank() {
			return targetBlank;
		}
}