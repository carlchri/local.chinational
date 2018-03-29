/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Defines the {@code QuoteAndImage} Sling Model used for the {@code /apps/chinational/foundation/composites/quoteAndImage}
 * component. This component displays quote with optional image and buttons.
 */
@Model(adaptables = Resource.class)
public class QuoteAndImage {

    public static final Logger LOGGER = LoggerFactory.getLogger(QuoteAndImage.class);

    @Inject
    private ResourceResolver resourceResolver;

    public static final String PROP_IMAGE_NOT_REQUIRED = "imageNotRequired";
    public static final String PROP_BUTTON_NOT_REQUIRED = "buttonNotRequired";
    public static final String PROP_NUM_OF_BUTTONS = "noOfButtons";
    public static final String PROP_GREY_BG_NOT_REQUIRED = "greybgRequired";
    public static final String PROP_HEADING = "heading";
    public static final String PROP_SUB_HEADING = "subheading";
    public static final String PROP_CAPTION = "caption";

    @Inject
    @Named(PROP_HEADING)
    @Optional
    @Default(values="")
    private String heading;

    @Inject
    @Named(PROP_SUB_HEADING)
    @Optional
    @Default(values="Sub Heading")
    private String subHeading;

    @Inject
    @Named(PROP_CAPTION)
    @Optional
    @Default(values="Caption")
    private String caption;

    @Inject
    @Named(PROP_IMAGE_NOT_REQUIRED)
    @Optional
    private boolean imageNotRequired;

    @Inject
    @Named(PROP_BUTTON_NOT_REQUIRED)
    @Optional
    private boolean buttonNotRequired;

    @Inject
    @Named(PROP_GREY_BG_NOT_REQUIRED)
    @Optional
    private boolean greyBgNotRequired;

    @Inject
    @Named(PROP_NUM_OF_BUTTONS)
    @Optional
    private String noOfButtons;

    private int numberOfButtons;

    @PostConstruct
    protected void init() {
        try {
            if (noOfButtons != null) {
                numberOfButtons = Integer.parseInt(noOfButtons);
            }
        } catch (NumberFormatException nfe) {
            LOGGER.error("Error parsing number of buttons", nfe);
        }
    }

    public String getHeading() {
        return heading;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public String getCaption() {
        return caption;
    }

    public boolean isImageNotRequired() {
        return imageNotRequired;
    }

    public boolean isButtonNotRequired() {
        return buttonNotRequired;
    }

    public boolean isGreyBgNotRequired() {
        return greyBgNotRequired;
    }

    public int getNumberOfButtons() {
        return numberOfButtons;
    }
}