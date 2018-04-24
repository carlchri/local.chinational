/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analyticsnippets extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(Analyticsnippets.class);
    private String analyticSnippets;

    @Override
    public void activate() throws Exception {
        Page currentPage = getCurrentPage();
        analyticSnippets = getAnalyticSneppets( currentPage);

    }

    private String getAnalyticSneppets( Page aPage ) {
        String analyticSnippets = aPage.getProperties().get("analyticsnippets", "");
        if(LinkUtils.isNullOrBlank(analyticSnippets) && aPage.getParent() != null) {
            aPage = aPage.getParent();
            analyticSnippets = getAnalyticSneppets( aPage);
        }
        return analyticSnippets;
    }

    public String getAnalyticSnippets() {
        return analyticSnippets;
    }
}