/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class Logo {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(Logo.class);
	    
	    public static final String PROP_IMAGE_PATH = "imagepath";
	    public static final String PROP_IMAGE_ALT_TEXT = "altText";
		public static final String PROP_LINK_URL = "link";
	    public static final String TARGET_BLANK = "target";

		@ScriptVariable
		private ValueMap currentStyle;

		private String imagePath;

		private String imageAltText;
	
		private String linkUrl;
	
		private boolean targetBlank;
	
		@PostConstruct
		protected void init() {
			imagePath = currentStyle.get(PROP_IMAGE_PATH, "");
			imageAltText = currentStyle.get(PROP_IMAGE_ALT_TEXT, "");
			linkUrl = currentStyle.get(PROP_LINK_URL, "#");
			targetBlank = currentStyle.get(TARGET_BLANK, false);
	       	if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
	            linkUrl = LinkUtils.externalize(linkUrl);
			}
		}
	
		public String getImagePath() {
			return imagePath;
		}

		public String getImageAltText() {
		return imageAltText;
	}
	
		public String getLinkUrl() {
			return linkUrl;
		}
	
		public boolean getTargetBlank() {
			return targetBlank;
		}
}