package org.chi.aem.common.servlet;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

@Service(value = Servlet.class)
@Properties({ 
    @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	  @Property(name = "sling.servlet.selectors", value = "testServlet"),
	  @Property(name = "sling.servlet.extensions", value = "html"),
	  @Property(name = "service.description", value = "for Test component"),
	  @Property(name = "label", value = "Test Servlet") 
})

public class testServlet extends SlingAllMethodsServlet {

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().print("Hello Simon From the Servlet!");

	}

}
