package org.chi.aem.common.utils;

import com.adobe.acs.commons.email.EmailService;

/**
 * ACS Commons EmailService Service Wrapper...
 */

public interface EmailServiceWrapper {

	public EmailService getEmailServiceWrapper() throws Exception;
}
