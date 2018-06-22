package org.chi.aem.common.servlet;

import com.adobe.acs.commons.email.EmailService;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Iterator;
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
    private static String SENDER_EMAIL = "admin@chinational.com";
    private static String SUBJECT = "Campaign Form Submitted";
    private static String CC_EMAIL = "";
    // private static String EMAIL_BODY = "Here are the details of the Campaign form submitted";
    // private static String SENDER_NAME = "CHI Web Team"; 
    // private static String RECIPIENT_NAME = "CHI Campaign Team";
    private static String[] TO_EMAIL = { "campaign@chinational.com" };
    
    //Set the dynamic vaiables of your email template
	private Map<String, String> emailParams;

	// Specify the template file to use (this is an absolute path in the JCR)
	private String templatePath;

	// Array of email recipients. 
	private String[] recipients;

	private Map<String, String[]> formParams;
	
	@Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
    	
    	emailParams = new HashMap<String,String>();
    	formParams = new HashMap<String,String[]>();
    	templatePath = TEMPLATE_PATH;
    	
    	//populate Email parameters with default values
    	// emailParams.put("message", EMAIL_BODY);
    	//  Customize the sender email address - if required
    	emailParams.put("senderEmailAddress", SENDER_EMAIL);
    	// emailParams.put("senderName", SENDER_NAME);
    	emailParams.put("ccEmail", CC_EMAIL);
    	emailParams.put("subject", SUBJECT);
    	// emailParams.put("recipientName", RECIPIENT_NAME);
    	recipients = TO_EMAIL;
    	
    	// populate Email parameters with values from dialog configured by author
    	resolver=request.getResourceResolver();

    	Resource emailResource = resolver.getResource(request.getParameter("current_resource_path"));
    	if(emailResource != null){
    		populateEmailParams(emailResource);
    	}
    	
    	// get form field name and values for emailTemplate
		formParams = request.getParameterMap();

    	int i=1;
    	for(String key: formParams.keySet()){
    		if(!(key.equals("current_page_path") || key.equals("current_resource_path"))){
    			emailParams.put("fieldName"+i, key + ": ");
    			String[] fieldValueArray = formParams.get(key);
    			String fieldValue = "";
    			for(int j=0; j<fieldValueArray.length; j++){
    					fieldValue += formParams.get(key)[j] + ", ";
    			}
    			if(fieldValue.trim().endsWith(",")){
    				fieldValue = fieldValue.substring(0, fieldValue.trim().lastIndexOf(","));
    			}
    			emailParams.put("fieldName"+i+"Value", fieldValue);
    		}
    		// LOGGER.info("fieldName"+i + " : " + key);
    		// LOGGER.info("fieldName"+i + "Value : " + emailParams.get("fieldName"+i+"Value"));
    		i++;
    	}
    	
    	// If form fields are less than 4, to clear extra field values from emailTemplate 
    	// If emailTemplate won't find those variables. it will print them as such
    	// k < 6 is used to take into account 2 hidden fields [4+2=6]
    	if(formParams.size() > 1){
	    	for(int k=formParams.size()-1; k < 6; k++){
	    		emailParams.put("fieldName"+k, "");
	    		emailParams.put("fieldName"+k+"Value", "");
	    	}
    	}
    
    	List<String> failureList = emailService.sendEmail(templatePath, emailParams, recipients);
    	if (failureList.isEmpty()) {
    		LOGGER.info("Email sent successfully to the recipients");
    	} else {
    		LOGGER.info("Email sent failed");
    	}
    		
        // response.setContentType("application/json");
	         
       PrintWriter out = response.getWriter();
       out.write("Form Submitted");
       out.flush();
    }    
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void populateEmailParams(Resource resource) {
    	
        try {
	            ValueMap sMap = resource.getValueMap();
	            if(sMap.get("emailFrom", String.class) != null) {
	            	emailParams.put("senderEmailAddress", sMap.get("emailFrom", String.class));
	            }
	            if(sMap.get("emailTo", String.class) != null) {
	            	recipients[0] = sMap.get("emailTo", String.class);
	            }
	            if(sMap.get("emailSubject", String.class) != null) {
	            	emailParams.put("subject", sMap.get("emailSubject", String.class));
	            }
	            
	            String ccEmail="CC: ";
	            Resource ccResource = resource.getChild("ccEmails");
	    		if (ccResource != null) {
	    			Iterator<Resource> ccResources = ccResource.listChildren();
	    			while (ccResources.hasNext()) {
	    				String property = ccResources.next().getValueMap().get("ccEmail", String.class);
	    				ccEmail += property + "\nCC: ";
	    			}
	    		}
	    		if(ccEmail.trim().endsWith("CC:")){
	    			ccEmail = ccEmail.substring(0, ccEmail.trim().lastIndexOf("CC:"));
	    		}
	        	emailParams.put("ccEmail", ccEmail);
	        
        } catch (Exception e) {
            LOGGER.error("Exception Occured in populateEmailParams() method" + e, e);
        }

    }

    

}