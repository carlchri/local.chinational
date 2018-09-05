package org.chi.aem.common.utils.impl;

import com.adobe.acs.commons.email.EmailService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.chi.aem.common.utils.EmailServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EmailServiceWrapper Implementation...
 */
@Component(
        name="ACS Commons Email Service Wrapper...",
        metatype = true, immediate = true
)
@Property(name="service.description", value="ACS Commons Email Service Wrapper")
@Service
public class EmailServiceWrapperImpl implements EmailServiceWrapper {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Reference    
    EmailService emailService;

    @Override
    public EmailService getEmailServiceWrapper() throws Exception {
    	LOGGER.info("emailService in wrapper : " + emailService);
    	return emailService;
    }

}
