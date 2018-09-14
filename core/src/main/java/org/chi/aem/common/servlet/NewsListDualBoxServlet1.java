package org.chi.aem.common.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.chi.aem.common.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Page;
import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.api.PageManager;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ 
			@Property(name = "sling.servlet.paths", value = "/bin/services/newslisttest"),
			@Property(name = "service.description", value = "Dual Box Select"),
			@Property(name = "label", value = "Dual Box Select") 
})

public class NewsListDualBoxServlet1 extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(NewsListDualBoxServlet1.class);

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{

        ResourceResolver resolver = request.getResourceResolver();
        String message = "News Select List Loaded!";
        response.getWriter().println("Hello Simon From the Servlet!");
        response.setContentType("text/html");
        Resource testResource = resolver.getResource(request.getParameter("value1" ) + "/jcr:content");
        if(testResource != null) {
            ModifiableValueMap map = testResource.adaptTo(ModifiableValueMap.class);
            map.put("jcrPath", request.getParameter("value1"));
            map.put("featuredList", request.getParameter("featuredPagesList"));
            testResource.getResourceResolver().commit();
        }
    }
}