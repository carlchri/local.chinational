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

	private List<Icon> icons = new ArrayList<Icon>();
	private boolean targetBlank;
	private String linkUrl;
	private String apptText;

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


	private void initApptAttributes() {
        LOGGER.info("FooterContactUs initApptAttributes called");
		targetBlank = getCurrentStyle().get(APPT_TARGET_TO , false);
		linkUrl = getCurrentStyle().get(APPT_LINK, "#");
		apptText = getCurrentStyle().get(APPT_LABEL , "Default");
		LOGGER.info("FooterContactUs label:" + apptText);
		if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
			linkUrl = LinkUtils.externalize(linkUrl);
		}
	}

	@Override
	public void activate() throws Exception {
		LOGGER.info("FooterContactUs activate called");
		String resourcePath = getCurrentStyle().getPath();
		LOGGER.info ("FooterContactUs resourcePath=" + resourcePath);
		Resource res = getResourceResolver().getResource(resourcePath);
		LOGGER.info("FooterContactUs resource=" + res);
		if (res != null && res.hasChildren()) {
            LOGGER.info("FooterContactUs resource has children");
			populateModel(res.getChild(ICONS));
		}
		initApptAttributes();
	}

	private void populateModel(Resource resource) {
        LOGGER.info("FooterContactUs populateModel called");
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
                LOGGER.info("FooterContactUs found icons");
				Icon link = linkResources.next().adaptTo(Icon.class);
				if (link != null) {
                    LOGGER.info("FooterContactUs populateModel add icon: " + link.getIconType());
                    icons.add(link);
                }
			}
		}
	}

}