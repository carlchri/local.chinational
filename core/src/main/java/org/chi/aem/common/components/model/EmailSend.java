/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import org.chi.aem.common.utils.EmailUtils;

public class EmailSend extends WCMUsePojo {

    public static final Logger LOGGER = LoggerFactory.getLogger(EmailSend.class);

    @Override
    public void activate() throws Exception {
    	EmailUtils emailUtils = new EmailUtils();
    	// emailUtils.sendEmail();
    	
    }

}