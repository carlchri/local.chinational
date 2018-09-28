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
    //@Properties({
    //			@Property(name = "sling.servlet.paths", value = "/bin/services/newslisttest"),
    //			@Property(name = "service.description", value = "Dual Box Select"),
    //			@Property(name = "label", value = "Dual Box Select")
    //})

    @Properties({
            @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
            @Property(name = "sling.servlet.selectors", value = "featurednewslistservlet"),
            @Property(name = "sling.servlet.extensions", value = "html"),
            @Property(name = "service.description", value = "For News List component"),
            @Property(name = "label", value = "Featured News List")
    })

    public class NewsListDualBoxServlet extends SlingAllMethodsServlet {

        private static final Logger log = LoggerFactory.getLogger(NewsListDualBoxServlet.class);

        @Reference
        private ResourceResolverFactory resolverFactory;

        public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{


            try {
                ResourceResolver resolver = request.getResourceResolver();
                Session session = resolver.adaptTo(Session.class);
                Node root = session.getRootNode();

                // response.setContentType("text/html");
                response.setContentType("text/plain");

                ResourceResolver resourceResolver = request.getResource().getResourceResolver();
//                log.info("Resoure resolver set");
                String requestPagePath = request.getParameter("requestPagePath")  + "/jcr:content";
                String featuredPagesList  = request.getParameter("featuredPagesList");
                String featuredTag = request.getParameter("featuredTag");
                if (featuredTag == null) {
                    featuredTag = "AllItems";
                }
//                log.info("Featured Tag :: "+featuredTag);
//                log.info("Get request path");
//                log.info("Request path :: "+requestPagePath);
                Resource res = resourceResolver.getResource(requestPagePath);

//                log.info("Set feature page list string");
//                log.info("Feature pages list string :: "+ featuredPagesList);
                String [] featuredPagesArray = featuredPagesList.replace("[","").replace("]","").replace("\"", "").split(",");
//                log.info("Set featured page list array");

//                log.info("request page path :: "+requestPagePath);
                Node pageNode = res.adaptTo(Node.class);
//                log.info("Get Current Property :"+ pageNode.getProperty("featuredList"));
                pageNode.setProperty(featuredTag, featuredPagesArray);
                pageNode.setProperty("featuredTag", featuredTag);

                response.getWriter().println("Page Featured List : "+featuredPagesArray);
//                log.info("Featured page list : "+featuredPagesArray);
//                log.info("Featured page request Path : "+ requestPagePath);

//                }

                session.save();
//                log.info("After Save");
                session.refresh(true);
//                log.info("After Refresh");

            } catch (Exception e) {
                log.error("Could not update Featured List reference: " + e);
                e.printStackTrace();
            }
        }
    }