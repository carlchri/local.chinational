package org.chi.aem.common.utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.components.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class DesignUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(DesignUtils.class);
    private static final String BASEPAGE_PATH = "basepage";
    private static final String SLASH_PATH = "/";
    private static final String JCR_PATH = "jcr:content";
    private static final String PATTERN_STR = "jcr:content\\/([a-z]\\w+)";



    public static ValueMap getDesignMap(Design currentDesign, Style currentStyle) {
        //  convert stylepath to use basepage
        ///etc/designs/chinational/jcr:content/mediacenterpage/footerContactUs/footerCULinks
        // would become - /etc/designs/chinational/jcr:content/basepage/footerContactUs/footerCULinks
        String stylePath = currentStyle.getPath();
        LOGGER.debug("stylePath: " + stylePath);
        String newStylePath = stylePath.replaceAll(PATTERN_STR,
                        JCR_PATH + SLASH_PATH + BASEPAGE_PATH);
        LOGGER.debug("newStylePath: " + newStylePath);
        String newDesignChildPath =  newStylePath.substring(newStylePath.indexOf(currentDesign.getPath()));
        Resource compRes = currentDesign.getContentResource().getChild(newDesignChildPath );
        if (compRes != null) {
            return compRes.getValueMap();
        }
        LOGGER.debug("Design resource not available, get value map from style path");
        return currentStyle;

    }

    public static Resource getDesignResource(ResourceResolver resourceResolver, Design currentDesign, Style currentStyle) {
        //  convert stylepath to use basepage
        ///etc/designs/chinational/jcr:content/mediacenterpage/footerContactUs/footerCULinks
        // would become - /etc/designs/chinational/jcr:content/basepage/footerContactUs/footerCULinks
        String stylePath = currentStyle.getPath();
        LOGGER.debug("stylePath: " + stylePath);
        String newStylePath = stylePath.replaceAll(PATTERN_STR,
                JCR_PATH + SLASH_PATH + BASEPAGE_PATH);
        LOGGER.debug("newStylePath: " + newStylePath);
        String newDesignChildPath =  newStylePath.substring(newStylePath.indexOf(currentDesign.getPath()));
        Resource compRes = currentDesign.getContentResource().getChild(newDesignChildPath );
        if (compRes == null) {
            LOGGER.debug("Design resource not available, get resource from style path");
            compRes = resourceResolver.getResource(stylePath);
        }
        return compRes;

    }


    public static Resource getDesignResource(Design currentDesign, String componentName) {
        if (componentName != null) {
            return currentDesign.getContentResource().getChild(BASEPAGE_PATH + SLASH_PATH+ componentName);
        }
        return null;
    }
}
