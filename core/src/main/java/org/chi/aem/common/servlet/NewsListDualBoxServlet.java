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
        public String currentPagePath;

        @Reference
        private ResourceResolverFactory resolverFactory;

        public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{


            try {
                ResourceResolver resolver = request.getResourceResolver();
                Session session = resolver.adaptTo(Session.class);
                Node root = session.getRootNode();
                String rootPath = root.getPath();
                log.info("Root node path : "+ rootPath);

                if (request.getParameter("resourcePath")!=null) {
                    String resourcePath = request.getParameter("resourcePath");
                    log.info("Resource Path : "+resourcePath);
                }

                Node featuredListNode = resolver.adaptTo(Node.class);


//                log.info("Featured list path : "+featuredListNode.getPath());
    //                log.info("Set Featured List Node");
    //                log.info("Get value of featured page list : "+request.getParameter("featuredPagesList"));
    //                featuredListNode.setProperty("featuredList", request.getParameter("featuredPagesList"));
    //                log.info("Set Featured List Property");

//                if(featuredListNode.hasProperty("featuredList")) {
//                    log.info("featured Node has featuredList property");
//                }



                Resource pageResource = request.getResource().adaptTo(Page.class).getContentResource();
                currentPagePath = pageResource.getPath();


                ResourceResolver resourceResolver = request.getResource().getResourceResolver();
                String requestPagePath = request.getParameter("requestPagePath")  + "/jcr:content";
                String featuredPagesList  = request.getParameter("featuredPagesList");
                String [] featuredPagesArray = featuredPagesList.split(",");
                Resource res = resourceResolver.getResource(requestPagePath);
                Node pageNode = res.adaptTo(Node.class);
//                log.info("Get Current Property :"+ pageNode.getProperty("featuredList"));
                pageNode.setProperty("featuredList", featuredPagesArray);

                response.setContentType("text/html");
                response.getWriter().println("Page Featured List : "+featuredPagesArray);
                log.info("Featured page list : "+featuredPagesArray);
                log.info("Featured page request Path : "+ requestPagePath);


//                if(pageResource != null) {
//                    log.info("pageResource not null");
//                    ModifiableValueMap map = pageResource.adaptTo(ModifiableValueMap.class);
//                    map.put("featuredList", request.getParameter("featuredPagesList"));
//                    log.info("map info : "+map.get("featuredList"));
//                    log.info("Page Resource : "+pageResource.getPath());
//                    pageResource.getResourceResolver().commit();
//                }

                session.save();
                log.info("After Save");
                session.refresh(true);
                log.info("After Refresh");

            } catch (Exception e) {
                log.error("Could not update Featured List reference: " + e);
                e.printStackTrace();
            }
        }
    }