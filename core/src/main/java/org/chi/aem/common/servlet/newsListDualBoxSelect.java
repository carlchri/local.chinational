package org.chi.aem.common.servlet;
import org.apache.sling.api.resource.Resource;
import com.day.cq.wcm.api.*;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
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
		@Property(name = "sling.servlet.selectors", value = "newslistfeaturedselect"),
        @Property(name = "service.description", value = "Dual Box Select"),
        @Property(name = "label", value = "Dual Box Select") 
})
public class newsListDualBoxSelect extends SlingAllMethodsServlet {
   

    @Reference
    private MessageGatewayService messageGatewayService;
    
    
    private Page getCurrentPage(SlingHttpServletRequest request) {
        Page currentPage = null;
        Resource currentResource = request.getResource();
        ResourceResolver resourceResolver = currentResource.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        if (pageManager != null) {
            currentPage = (Page) pageManager.getContainingPage(currentResource.getPath());
        }
        return currentPage;
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{

        ResourceResolver resolver = request.getResourceResolver();
        String location = "jcr:content";
        Resource myPageResource = resolver.getResource(location);
        String message = "News Select List Loaded!";
        response.getWriter().print("Hello Simon From the Servlet!");
        
        Page currentPage = getCurrentPage(request);
        
//        response.getWriter().print(currentPage.toString());
    }
}