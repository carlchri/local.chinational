package org.chi.aem.common.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.components.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class DesignUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DesignUtils.class);
    private static final String BASEPAGE_PATH = "basepage/";



    public static ValueMap getDesignMap(ResourceResolver resourceResolver,
                                          Page currentPage, String componentName) {
        if (componentName != null) {
            Designer designer = resourceResolver.adaptTo(Designer.class);
            LOGGER.debug("designer: " + designer);
            Design currentDesign = designer.getDesign(currentPage);
            LOGGER.debug("currentDesign: " + currentDesign);
            LOGGER.debug("currentDesign.getContentResource(): " + currentDesign.getContentResource());
            Resource compRes = currentDesign.getContentResource().getChild(BASEPAGE_PATH + componentName);
            LOGGER.debug("compRes: " + compRes);
            if (compRes != null) {
                return compRes.getValueMap();
            }
        }
        return null;
    }

    public static ValueMap getDesignMap(Design currentDesign, String componentName) {
        if (componentName != null) {
            LOGGER.debug("currentDesign: " + currentDesign);
            LOGGER.debug("currentDesign.getContentResource(): " + currentDesign.getContentResource());
            Resource compRes = currentDesign.getContentResource().getChild(BASEPAGE_PATH + componentName);
            if (compRes != null) {
                return compRes.getValueMap();
            }
        }
        return null;
    }

}
