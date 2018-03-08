package org.chi.aem.common.utils;

import com.adobe.cq.address.api.AddressException;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;
import javax.mail.internet.InternetAddress;
import java.util.*;

public class SendEmail {
    private static final Logger log = LoggerFactory.getLogger(SendEmail.class);

    private static String TEMPLATE_PATH = "/apps/chinational/components/structure/footerContactUs/emailTemplate.html";
    private static String SENDER_EMAIL = "experience.aem@gmail.com";
    private static String SENDER_NAME = "CHI Contact Us";
    private static String SENDER_EMAIL_ADDRESS = "senderEmailAddress";
    private static String SUCCESS_MESSAGE = "Email has been sent.";
    private static String FAILURE_MESSAGE = "Can not sent out email.";
    private static String INVALID_EMAIL_ADDRESS = "Invalid Email Address.";

    public String sendMail(ResourceResolver resolver, MessageGatewayService messageGatewayService, String recipientEmail){

        try{
            if(recipientEmail.isEmpty()) {
                return INVALID_EMAIL_ADDRESS;
            }
            InternetAddress internetAddress = new InternetAddress(recipientEmail);
            Map<String, String> emailParams = new HashMap<String,String>();

            emailParams.put(SENDER_EMAIL_ADDRESS, SENDER_EMAIL);
            emailParams.put("senderName", SENDER_NAME);

            MailTemplate mailTemplate = MailTemplate.create(TEMPLATE_PATH, resolver.adaptTo(Session.class));

            if (mailTemplate == null) {
                throw new Exception("Template missing - " + TEMPLATE_PATH);
            }

            Email email = mailTemplate.getEmail(StrLookup.mapLookup(emailParams), HtmlEmail.class);

            email.setTo(Collections.singleton(new InternetAddress(recipientEmail)));
            email.setFrom(SENDER_EMAIL);

            MessageGateway<Email> messageGateway = messageGatewayService.getGateway(email.getClass());

            messageGateway.send(email);
        }catch(AddressException e){
            return INVALID_EMAIL_ADDRESS;
        }catch(Exception e){
            log.error("Error sending email to " + recipientEmail, e);
            return FAILURE_MESSAGE;
        }

        return SUCCESS_MESSAGE;
    }

}
