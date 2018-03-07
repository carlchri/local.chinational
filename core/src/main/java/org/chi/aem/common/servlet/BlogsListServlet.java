package org.chi.aem.common.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.ServerException;
import java.util.Dictionary;
  
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.StringWriter;
 
import java.util.HashMap;
import java.util.Map;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
      
 
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;     
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import javax.jcr.RepositoryException;
import org.apache.felix.scr.annotations.Reference;
import org.apache.jackrabbit.commons.JcrUtils;
 
 
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.Node; 
import java.util.UUID;
 
import javax.jcr.Session;
import javax.jcr.Node; 
import javax.servlet.Servlet;
import javax.servlet.ServletException;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory ; 
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
   
 
//QUeryBuilder APIs
import com.day.cq.search.QueryBuilder; 
import com.day.cq.search.Query; 
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;
import com.day.cq.search.result.Hit; 
import com.day.cq.wcm.api.NameConstants;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ @Property(name = "sling.servlet.paths", value = "/bin/services/blogsList"),
			  @Property(name = "service.description", value = "for Blogs List component"),
			  @Property(name = "label", value = "Blogs List") })
public class BlogsListServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 9073952766248919847L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsListServlet.class);
    
  //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;
                
    @Reference
    private QueryBuilder builder;
     
     
    private Session session;
             
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
       
        // Map<String, Object> param = new HashMap<String, Object>();
        // param.put(ResourceResolverFactory.SUBSERVICE, "jquerybuilder");
        ResourceResolver resolver = null;
        try {
            //Invoke the adaptTo method to create a Session used to create a QueryManager
            // resolver = resolverFactory.getServiceResourceResolver(param);
        	// resolver = resolverFactory.getAdministrativeResourceResolver(null);
        	resolver = request.getResourceResolver();
            session = resolver.adaptTo(Session.class);
        String blogs_filter = request.getParameter("search_blogs_list");
        if(blogs_filter !=null){
	        if(blogs_filter.equals("By Year")){
	        	blogs_filter = request.getParameter("search_blogs_year");
	        }
        }
        // LOGGER.info("blogsFilter" + blogs_filter);
        // LOGGER.info("search_blogs_year : " + request.getParameter("search_blogs_year"));
        String contextPath = request.getContextPath();                       
        // forward to
        String forwardTo = request.getParameter("media_page_path");
        if (forwardTo != null) {
            String path = contextPath + forwardTo;
            try {
                Node node = session.getNode(path);
                if (isPage(node)) {
                    forwardTo += ".html";
                }
            } catch (RepositoryException e) {
                response.getWriter().write("Resource Not Found:" + forwardTo);
            }
        }
        // LOGGER.info("forwardTo : " + forwardTo.toString());
        request.setAttribute("blogs_filter", blogs_filter);
        request.getRequestDispatcher(forwardTo).forward(request, response);
        
	    } catch (Exception e) {
	        LOGGER.error("Exception in BlogsListServlet",e);
	        try {
	            response.getWriter().write(e.getMessage());
	        } catch (IOException e1) {
	            LOGGER.error("Exception in BlogsListServlet>>>dopost method",e1);
	        }
	
	    }
	
	    finally {
	
	        if (session != null) {
	        	//close the session
	            session.logout();
	        }
	
	        if (resolver != null) {
	            resolver.close();
	        }
	    }
    }    
          
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    private boolean isPage(Node n) throws RepositoryException {
        return n.isNodeType(NameConstants.NT_PAGE) || n.isNodeType(NameConstants.NT_PSEUDO_PAGE);
        // NT_PAGE value is "cq:Page" and NT_PSEUDO_PAGE - value is "cq:PseudoPage"
        // https://docs.adobe.com/docs/en/aem/6-3/develop/ref/javadoc/constant-values.html
    }

}