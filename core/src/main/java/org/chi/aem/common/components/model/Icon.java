/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class)
public class Icon {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(Icon.class);
	    
	    public static final String PROP_ICON_TYPE = "iconType";
	    public static final String PROP_LINK_URL = "linkUrl";
	    public static final String TARGET_BLANK = "targetBlank";

		@Inject
		private ResourceResolver resourceResolver;
	
		@Inject
		@Named(PROP_ICON_TYPE)
		@Optional
		private String iconType;
	
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
	
		public String getIconType() {
			return iconType;
		}
	
		public String getLinkUrl() {
			return linkUrl;
		}
	
		public boolean getTargetBlank() {
			return targetBlank;
		}
}