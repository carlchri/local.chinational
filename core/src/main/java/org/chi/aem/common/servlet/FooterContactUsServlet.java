package org.chi.aem.common.servlet;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import com.day.cq.mailer.MessageGatewayService;
import org.chi.aem.common.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ @Property(name = "sling.servlet.paths", value = "/bin/services/contactUs"),
        @Property(name = "service.description", value = "for send out contact us email."),
        @Property(name = "label", value = "Send Contact Us Email") })
public class FooterContactUsServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(FooterContactUsServlet.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException{

        ResourceResolver resolver = request.getResourceResolver();
        String recipientEmail = request.getParameter("senderEmailAddress");
        SendEmail emailService = new SendEmail();
        String message = emailService.sendMail(resolver, messageGatewayService, recipientEmail);
        response.getWriter().print(message);
    }
}