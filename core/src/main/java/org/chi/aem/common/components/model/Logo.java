/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.chi.aem.common.utils.DesignUtils;
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

		@ScriptVariable
		private Page currentPage;

		@SlingObject
		private ResourceResolver resourceResolver;


	private String imagePath;

		private String imageAltText;
	
		private String linkUrl;
	
		private boolean targetBlank;
	
		@PostConstruct
		protected void init() {
			/*Designer designer = resourceResolver.adaptTo(Designer.class);
			LOGGER.info("designer: " + designer);
			Design currentDesign = designer.getDesign(currentPage);
			LOGGER.info("currentDesign: " + currentDesign);
			LOGGER.info("currentDesign.getContentResource(): " + currentDesign.getContentResource());
			Resource logoRes = currentDesign.getContentResource().getChild("basepage/logo");
			LOGGER.info("logoRes: " + logoRes);
			if (logoRes != null) {
				ValueMap currDesign = logoRes.getValueMap();
				LOGGER.info("currDesign: " + currDesign);
				imagePath = currDesign.get(PROP_IMAGE_PATH, "");
				LOGGER.info("imagepath from currDesign: " + imagePath);
			}*/

			String compName = DesignUtils.getComponentName(currentStyle.get("path"));
			ValueMap designMap = DesignUtils.getDesignMap(resourceResolver, currentPage, compName);
			if (designMap == null) {
				designMap = currentStyle;
			}

			imagePath = designMap.get(PROP_IMAGE_PATH, "");
			LOGGER.info("imagepath from designMap: " + imagePath);
			imageAltText = designMap.get(PROP_IMAGE_ALT_TEXT, "");
			linkUrl = designMap.get(PROP_LINK_URL, "#");
			targetBlank = designMap.get(TARGET_BLANK, false);
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