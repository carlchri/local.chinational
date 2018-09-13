package org.chi.aem.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
  
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
import org.chi.aem.common.utils.NewsBlogUtils;
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

import com.day.cq.mailer.MessageGatewayService;
//QUeryBuilder APIs
import com.day.cq.wcm.api.Page;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ 
		      @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
			  @Property(name = "sling.servlet.selectors", value = "newslistSelectservlet"),
			  @Property(name = "sling.servlet.extensions", value = "html"),
			  @Property(name = "service.description", value = "for News List component"),
			  @Property(name = "label", value = "News List") 
		  })

public class NewsListServlet extends SlingAllMethodsServlet {


    private static final Logger log = LoggerFactory.getLogger(newsListDualBoxSelect.class);
   

    @Reference
    private MessageGatewayService messageGatewayService;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{

        ResourceResolver resolver = request.getResourceResolver();
        String message = "News Select List Loaded!";
        response.getWriter().print("Hello Simon From the Servlet!");
        log.info("Servlet test 2");
    }
}