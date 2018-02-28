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

@Model(adaptables = SlingHttpServletRequest.class)
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
		targetBlank = getCurrentStyle().get(APPT_TARGET_TO , false);
		linkUrl = getCurrentStyle().get(APPT_LABEL, "#");
		apptText = getCurrentStyle().get(APPT_LABEL , "Default");
		LOGGER.debug("FooterContactUs label:" + apptText);
		if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
			linkUrl = LinkUtils.externalize(linkUrl);
		}
	}

	@Override
	public void activate() throws Exception {
		LOGGER.debug("activate called");
		String resourcePath = getCurrentStyle().getPath();
		LOGGER.debug("***resourcePath=" + resourcePath);
		Resource res = getResourceResolver().getResource(resourcePath);
		LOGGER.debug("***resource=" + res);
		if (res != null && res.hasChildren()) {
			populateModel(icons, res.getChild(ICONS));
		}
		initApptAttributes();
	}

	private List<Icon> populateModel(List<Icon> list, Resource resource) {
		if (resource != null) {
			Iterator<Resource> linkResources = resource.listChildren();
			while (linkResources.hasNext()) {
				Icon link = linkResources.next().adaptTo(Icon.class);
				if (link != null)
					list.add(link);
			}
		}
		return list;
	}

}