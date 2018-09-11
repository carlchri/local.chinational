package org.chi.aem.common.servlet;

import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MessageGatewayService;
import com.day.cq.wcm.api.Page;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.rmi.ServerException;

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

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ 
	@Property(name = "sling.servlet.paths", value = "/bin/services/dualBoxServlet"),
    @Property(name = "service.description", value = "Dual Box Servlet Test"),
    @Property(name = "label", value = "Dual Box Select List") 
})

public class dualBoxServlet extends SlingAllMethodsServlet {
		
	private java.util.List<Page> featureNewsList;

    @Reference
    private MessageGatewayService messageGatewayService;
    
    private static final long serialVersionUID = 9073952766248919847L;
//    private static final Logger LOGGER = LoggerFactory.getLogger(NewsListServlet.class);
  
    // Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;
              
    private ResourceResolver resolver;	
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {

        ResourceResolver resolver = request.getResourceResolver();
        String message = "The dual box Servlet is working!!!!!";
        response.getWriter().print(message);
    }
}
