/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~
 ~ Used by News and Blog details page template to get header image
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.chi.aem.common.utils.NewsBlogImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsBlogImageModel extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(NewsBlogImageModel.class);

    private String imagePath;

    @Override
    public void activate() throws Exception {
        imagePath = NewsBlogImageUtils.getHeaderImage(getCurrentPage());
        LOGGER.debug("imagePath: " + imagePath);
    }


    public String getImagePath() {
        return imagePath;
    }



}