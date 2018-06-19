package org.chi.aem.common.servlet;

import com.adobe.acs.commons.email.EmailService;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
  
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.sling.api.servlets.SlingAllMethodsServlet;     
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Reference;
 
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;

//QUeryBuilder APIs
import com.day.cq.wcm.api.Page;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ 
		      @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
			  @Property(name = "sling.servlet.selectors", value = "photoBandFormServlet"),
			  @Property(name = "sling.servlet.extensions", value = "html"),
			  @Property(name = "service.description", value = "for Photo Band With Form component"),
			  @Property(name = "label", value = "Photo Band With Form") 
		  })

public class PhotoBandFormServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 9073952766248919847L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoBandFormServlet.class);
    
    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    EmailService emailService;

    private ResourceResolver resolver;
    
    private Session session;
    
    private static String TEMPLATE_PATH = "/etc/notification/email/chinational/emailTemplate.html";
    private static String SENDER_EMAIL = "admin@adobe.com";
    private static String SUBJECT = "Campaign Form Submitted";
    private static String RECIPIENT_NAME = "Naresh Devnani";
    private static String CC_EMAIL = "CC: davinderhooda@gmail.com\nCC: maildhooda@gmail.com";
    private static String BCC_EMAIL = "BCC: davinder_hooda@yahoo.co.in";
    private static String EMAIL_BODY = "hello there";
    private static String SENDER_NAME = "Davinder Hooda";
    
    private static String SUCCESS_MESSAGE = "Email has been sent.";
    private static String FAILURE_MESSAGE = "Can not sent out email.";
    private static String INVALID_EMAIL_ADDRESS = "Invalid Email Address.";

             
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
    	
		LOGGER.info("Inside emailutils activate method");
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
    	// emailService = getSlingScriptHelper().getService(EmailService.class);
    	LOGGER.info("emailService : " + emailService);
    	List<String> failureList = emailService.sendEmail(templatePath, emailParams, recipients);
    	LOGGER.info("failureList" + failureList);
    	if (failureList.isEmpty()) {
    		LOGGER.info("Email sent successfully to the recipients");
    	} else {
    		LOGGER.info("Email sent failed");
    	}
    	LOGGER.info("failureList" + failureList);
    	LOGGER.info("request.getParameterMap() : " + request.getParameterMap());
    	LOGGER.info("request.getParameterNames() : " + request.getParameterNames());
    		
	        // response.setContentType("application/json");
	         
	       PrintWriter out = response.getWriter();
	       out.write("Hello");
	       out.flush();
    }    
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}