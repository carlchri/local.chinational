/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.DesignUtils;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logo extends WCMUsePojo {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(Logo.class);

	public static final String PROP_IMAGE_PATH = "imagepath";
	public static final String PROP_IMAGE_MOBILE_PATH = "imageMobilePath";
	public static final String PROP_IMAGE_ALT_TEXT = "altText";
	public static final String PROP_LINK_URL = "link";
	public static final String TARGET_BLANK = "target";

	private String imagePath;

	private String imageMobilePath;

	private String imageAltText;

	private String linkUrl;

	private boolean targetBlank;

	@Override
	public void activate() throws Exception {
		// LOGGER.debug("currentStyle getPath:" + getCurrentStyle().getPath());
		// LOGGER.debug("currentDesign getPath:" + getCurrentDesign().getPath());

		// gets map from basepage, if available
		ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());

		imagePath = designMap.get(PROP_IMAGE_PATH, "");
		// LOGGER.debug("imagepath from designMap currDesign: " + imagePath);
		imageMobilePath = designMap.get(PROP_IMAGE_MOBILE_PATH, "");
		if(imageMobilePath.isEmpty()){
			imageMobilePath = imagePath;
		}
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

	public String getImageMobilePath() {
		return imageMobilePath;
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