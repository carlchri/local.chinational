package org.chi.aem.common.utils;

import org.chi.aem.common.utils.EmailServiceWrapper;

import com.adobe.cq.sightly.WCMUsePojo;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
//import org.osgi.service.component.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.*;
import com.adobe.acs.commons.email.EmailService;
import com.adobe.cq.sightly.WCMUsePojo;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;


public class EmailUtils extends WCMUsePojo {

	protected static final String RESOURCE_TYPE = "chinational/components/content/photoBandWithForm";

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);

	// Get the OSGi Email Service
    // @Inject
    // EmailServiceWrapper emailServiceWrapper;
	
	// @Inject
    EmailService emailService;

    @SlingObject
    private Resource resource;
	
    private static String TEMPLATE_PATH = "/etc/notification/email/chinational/emailTemplate.html";
    private static String SENDER_EMAIL = "admin@adobe.com";
    private static String SUBJECT = "Campaign Form Submitted";
    private static String RECIPIENT_NAME = "Naresh Devnani";
    private static String CC_EMAIL = "CC: rajbalahooda@gmail.com\nCC: maildhooda@gmail.com";
    private static String BCC_EMAIL = "BCC: davinder_hooda@yahoo.co.in";
    private static String EMAIL_BODY = "hello there";
    private static String SENDER_NAME = "Davinder Hooda";
    
    private static String SUCCESS_MESSAGE = "Email has been sent.";
    private static String FAILURE_MESSAGE = "Can not sent out email.";
    private static String INVALID_EMAIL_ADDRESS = "Invalid Email Address.";

    @Override
    public void activate() throws Exception {
		LOGGER.debug("Inside emailutils activate method");
    	// Specify the template file to use (this is an absolute path in the JCR)
    	String templatePath = TEMPLATE_PATH;
    	//Set the dynamic vaiables of your email template
    	Map<String, String> emailParams = new HashMap<String,String>();
    	emailParams.put("message", EMAIL_BODY);
    	//  Customize the sender email address - if required
    	emailParams.put("senderEmailAddress", SENDER_EMAIL);
    	emailParams.put("senderName", SENDER_NAME);
    	emailParams.put("recipientName", RECIPIENT_NAME);
    	emailParams.put("ccEmail", CC_EMAIL);
    	emailParams.put("bccEmail", BCC_EMAIL);
    	emailParams.put("subject", SUBJECT);

    	// Array of email recipients
    	String[] recipients = { "dhooda@prokarma.com" }; //you will want to change this to your email address so you can recieve the email
    	// emailService.sendEmail(..) returns a list of all the recipients that could not be sent the email
    	// An empty list indicates 100% success
/*        try {
        	emailService = emailServiceWrapper.getEmailServiceWrapper();
        }
        catch(Exception e)
        {
         LOGGER.info("Exception to Email Service");
         e.printStackTrace();
        }
*/
    	emailService = getSlingScriptHelper().getService(EmailService.class);
    	LOGGER.debug("emailService : " + emailService);
    	List<String> failureList = emailService.sendEmail(templatePath, emailParams, recipients);
    	LOGGER.debug("failureList" + failureList);
    	if (failureList.isEmpty()) {
    		LOGGER.debug("Email sent successfully to the recipients");
    	} else {
    		LOGGER.debug("Email sent failed");
    	}
    	LOGGER.debug("failureList" + failureList);

    }

    public String getEmailString() {
    	return "Email Send";
    }


}
