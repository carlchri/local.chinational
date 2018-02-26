/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.LinkUtils;
import com.adobe.cq.sightly.WCMUsePojo;

public class Button extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(Button.class);

    public static final String PROP_LINK_TO = "linkTo";
    public static final String PROP_MODAL_LINK_TO = "modalLinkTo";
    public static final String TARGET_BLANK = "targetBlank";
    public static final String PROP_LINK_LABEL = "label";
    public static final String PROP_LINK_MODAL_PREFIX = "myModal";

    private String linkTo;
    private String modalLinkTo;
    private boolean targetBlank;
    private String label;

    @Override
    public void activate() throws Exception {
        Resource resource = getResource();
        ValueMap properties = getProperties();
		targetBlank = properties.get(TARGET_BLANK, false);
        linkTo = properties.get(PROP_LINK_TO, "#");
        label = properties.get(PROP_LINK_LABEL, "Button");
        modalLinkTo = properties.get(PROP_MODAL_LINK_TO, "#");
        if (StringUtils.isNotEmpty(linkTo) && !"#".equals(linkTo)) {
            linkTo = LinkUtils.externalize(linkTo);            
        }
        if (StringUtils.isNotEmpty(modalLinkTo) && !"#".equals(modalLinkTo)) {
          modalLinkTo = LinkUtils.externalize(modalLinkTo);            
       }

        LOGGER.debug("resource: {}", resource.getPath());
        LOGGER.debug("linkTo: {}", linkTo);
    }

    public String getHash() {
        if (label != null) {
            return PROP_LINK_MODAL_PREFIX + label.hashCode();
        }
        return PROP_LINK_MODAL_PREFIX + (int)Math.round(Math.random());
    }

    public String getHashTarget() {
        return "#" + getHash();
    }

    public String getLinkTo() {
        return linkTo;
    }

    public String getModalLinkTo() {
        return modalLinkTo;
    }

    public boolean getTargetBlank() {
        return targetBlank;
    }

}