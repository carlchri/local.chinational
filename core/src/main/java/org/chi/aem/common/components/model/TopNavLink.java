/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the {@code TopNavLink} Sling Model used for the {@code /apps/chinational/foundation/structure/topnav}
 * component. This component gets list of links for top navigation.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class TopNavLink {

    public static final Logger LOGGER = LoggerFactory.getLogger(TopNavLink.class);

    @ScriptVariable
    private ValueMap currentStyle;

    public static final String PROP_LINK_TO = "link";
    public static final String PROP_LABEL = "label";
    public static final String TARGET_BLANK = "target";

    private List<MenuItem> items = new ArrayList<MenuItem>();


    private void addMenuDetails(String count) {
        boolean targetBlank = currentStyle.get(TARGET_BLANK + count, false);
        String linkTo = currentStyle.get(PROP_LINK_TO + count, "#");
        String label = currentStyle.get(PROP_LABEL + count, "Default");

        if (StringUtils.isNotEmpty(linkTo) && !"#".equals(linkTo)) {
            linkTo = LinkUtils.externalize(linkTo);
        } else {
            LOGGER.info("addMenuDetails: No link for count: " + count);
            return;
        }
        LOGGER.info("add topNav for : " + label + ", url: " + linkTo + ", newWindow: " + targetBlank);
        MenuItem nav = new MenuItem();
        nav.setNewWindow(targetBlank);
        nav.setTitle(label);
        nav.setUrl(linkTo);
        items.add(nav);
        return;
    }

    public List<MenuItem> getItems() {
        LOGGER.debug("getItems called");
        addMenuDetails("1");
        addMenuDetails("2");
        addMenuDetails("3");
        return items;
    }


}