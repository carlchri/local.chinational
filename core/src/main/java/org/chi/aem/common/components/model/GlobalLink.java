/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class)
public class GlobalLink {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalLink.class);

	@ScriptVariable
	private ValueMap currentStyle;

	    public static final String PROP_LINK_TEXT = "linkText";
	    public static final String PROP_LINK_URL = "linkUrl";
	    public static final String TARGET_BLANK = "targetBlank";

		@Inject
		private ResourceResolver resourceResolver;

		private boolean targetBlank;
		private String linkTo;
		private String label;
	
		@PostConstruct
		protected void init() {
			targetBlank = currentStyle.get(TARGET_BLANK , false);
			linkTo = currentStyle.get(PROP_LINK_URL, "#");
			label = currentStyle.get(PROP_LINK_TEXT , "Default");
			if (StringUtils.isNotEmpty(linkTo) && !"#".equals(linkTo)) {
				linkTo = LinkUtils.externalize(linkTo);
			}

		}
	
		public String getLinkText() {
			return label;
		}
	
		public String getLinkUrl() {
			return linkTo;
		}
	
		public boolean getTargetBlank() {
			return targetBlank;
		}
}