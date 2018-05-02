/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.chi.aem.common.utils.DesignUtils;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

public class GlobalLink extends WCMUsePojo {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalLink.class);

		@ScriptVariable
		private ValueMap currentStyle;

	    public static final String PROP_LINK_TEXT = "linkText";
	    public static final String PROP_LINK_URL = "linkUrl";
	    public static final String TARGET_BLANK = "targetBlank";

		private boolean targetBlank;
		private String linkTo;
		private String label;

		@Override
		public void activate() throws Exception {
			ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());
			targetBlank = designMap.get(TARGET_BLANK , false);
			linkTo = designMap.get(PROP_LINK_URL, "#");
			label = designMap.get(PROP_LINK_TEXT , "Default");
			LOGGER.debug("GlobalLink label:" + label);
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