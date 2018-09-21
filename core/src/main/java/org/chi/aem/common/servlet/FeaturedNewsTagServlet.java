package org.chi.aem.common.servlet;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.day.cq.wcm.api.Page;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)

@Properties({
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.selectors", value = "featurednewslisttagservlet"),
        @Property(name = "sling.servlet.extensions", value = "html"),
        @Property(name = "service.description", value = "For News List component"),
        @Property(name = "label", value = "Featured News List")
})

public class FeaturedNewsTagServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(FeaturedNewsTagServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{


        try {
            ResourceResolver resolver = request.getResourceResolver();
            Session session = resolver.adaptTo(Session.class);

            ResourceResolver resourceResolver = request.getResource().getResourceResolver();
            String requestPagePath = request.getParameter("requestPagePath")  + "/jcr:content";
            log.info("requestPagePath : "+requestPagePath);
            String featuredPagesTag  = "chi:MediaCenter/" + request.getParameter("featuredPagesTag");
            log.info("Get Tag Property value : "+featuredPagesTag );

            Resource res = resourceResolver.getResource(requestPagePath);
            Node pageNode = res.adaptTo(Node.class);
            pageNode.setProperty("featuredPagesTag", featuredPagesTag);

            response.setContentType("text/html");
            response.getWriter().println("Page Featured List : "); // +featuredPagesTag

            session.save();
//                log.info("After Save");
            session.refresh(true);
//                log.info("After Refresh");

        } catch (Exception e) {
            log.error("Could not update tag reference: " + e);
            e.printStackTrace();
        }
    }
}