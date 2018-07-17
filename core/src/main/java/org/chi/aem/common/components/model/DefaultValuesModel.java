/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~
 ~ Used by News and Blog details page template to get header image
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;

import org.apache.commons.lang3.StringUtils;
import org.chi.aem.common.utils.LinkUtils;
import org.chi.aem.common.utils.DefaultValuesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultValuesModel extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultValuesModel.class);

    private String imagePath;

    @Override
    public void activate() throws Exception {
    }


    public String getDefaultNewsPath() {
        return DefaultValuesUtils.getDefaultNewsPath(getCurrentPage());
    }

    public String getDefaultBlogsPath() {
        return DefaultValuesUtils.getDefaultBlogsPath(getCurrentPage());
    }

    public String getDefaultBlogsTileImgSrc() {
        return DefaultValuesUtils.getDefaultBlogsTileImgSrc(getCurrentPage());
    }

    public String getDefaultNewsTileImgSrc() {
        return DefaultValuesUtils.getDefaultNewsTileImgSrc(getCurrentPage());
    }

}