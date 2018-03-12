/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.chi.aem.common.utils.DesignUtils;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Defines the {@code FooterLinkCollection} Sling Model used for the
 * {@code /apps/chinational/foundation/structure/footLinks} component. This
 * component gets list of footer links.
 * 
 */

//@Model(adaptables = SlingHttpServletRequest.class)
public class FooterContactUs extends WCMUsePojo {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FooterContactUs.class);

	private static final String ICONS = "icons";
	private static final String APPT_LABEL = "label";
	private static final String APPT_LINK = "linkTo";
	private static final String APPT_TARGET_TO = "targetBlank";
	private static final String PROP_PLACEHOLDER_TEXT = "nlPlaceholderText";
	private static final String PROP_BUTTON_TEXT = "nlButtonText";
	private static final String PROP_HEADING = "heading";
	private static final String PROP_SUB_HEADING = "subheading";

	private List<Icon> icons = new ArrayList<Icon>();
	private boolean targetBlank;
	private String linkUrl;
	private String apptText;
	private String nlPlaceholderText;
	private String nlButtonText;
	private String heading;
	private String subheading;

	public List<Icon> getIcons() {
		return icons;
	}

	public String getApptText() {
		return apptText;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public boolean getTargetBlank() {
		return targetBlank;
	}

	public String getNlPlaceholderText() {
		return nlPlaceholderText;
	}

	public String getNlButtonText() {
		return nlButtonText;
	}

	public String getHeading() {
		return heading;
	}

	public String getSubheading() {
		return subheading;
	}

	private void initApptAttributes() {
        LOGGER.debug("FooterContactUs initApptAttributes called");
		ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());
		targetBlank = designMap.get(APPT_TARGET_TO , false);
		linkUrl = designMap.get(APPT_LINK, "#");
		apptText = designMap.get(APPT_LABEL , "Default");
		LOGGER.debug("FooterContactUs label:" + apptText);
		if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
			linkUrl = LinkUtils.externalize(linkUrl);
		}
		nlPlaceholderText = designMap.get(PROP_PLACEHOLDER_TEXT, "");
		nlButtonText = designMap.get(PROP_BUTTON_TEXT, "");
		heading = designMap.get(PROP_HEADING, "");
		subheading = designMap.get(PROP_SUB_HEADING, "");
	}

	@Override
	public void activate() throws Exception {
		LOGGER.debug("FooterContactUs activate called");
		Resource res = DesignUtils.getDesignResource(getResourceResolver(), getCurrentDesign(), getCurrentStyle());
		if (res != null && res.hasChildren()) {
            LOGGER.debug("FooterContactUs resource has children");
			populateModel(res.getChild(ICONS));
		}
		initApptAttributes();
	}

	private void populateModel(Resource resource) {
        LOGGER.debug("FooterContactUs populateModel called");
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
                LOGGER.debug("FooterContactUs found icons");
				Icon link = linkResources.next().adaptTo(Icon.class);
				if (link != null) {
                    LOGGER.debug("FooterContactUs populateModel add icon: " + link.getIconType());
                    icons.add(link);
                }
			}
		}
	}

}