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
    import org.chi.aem.common.utils.NewsBlogUtils;
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

        public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
                throws ServletException, IOException{
            // TODO ignore execution in non-author mode
            try {
                ResourceResolver resolver = request.getResourceResolver();
                Session session = resolver.adaptTo(Session.class);
                response.setContentType("text/plain");

                ResourceResolver resourceResolver = request.getResource().getResourceResolver();
                String requestPagePath = request.getParameter("requestPagePath")  + "/jcr:content";
                log.info("Request Path from servlet :: "+requestPagePath);
                String featuredPagesList  = request.getParameter("featuredPagesList");
                String featuredPagesTag = request.getParameter("featuredPagesTag");
                log.info("featuredPagesTag :: "+featuredPagesTag);
                if (featuredPagesTag == null) {
                    featuredPagesTag = "AllItems";
                }
                Resource res = resourceResolver.getResource(requestPagePath);

                log.debug("Feature pages list string : Servlet "+ featuredPagesList);
                String [] featuredPagesArray = featuredPagesList.replace("[","").replace("]","").replace("\"", "").split(",");
                Node pageNode = res.adaptTo(Node.class);
                if (!pageNode.hasNode(NewsBlogUtils.FEATURED_NODE)) {
                    // create the node that stores the featured articles
                    log.info("Create featuredArticles node, as it does not exist");
                    pageNode.addNode(NewsBlogUtils.FEATURED_NODE);
                    session.save();
                    session.refresh(true);
                }
                if (pageNode.hasNode(NewsBlogUtils.FEATURED_NODE)) {
                    log.info("Store items for tag: " + featuredPagesTag + ", values: " + featuredPagesArray);
                    Node featuredNode = pageNode.getNode(NewsBlogUtils.FEATURED_NODE);
                    featuredNode.setProperty(featuredPagesTag, featuredPagesArray);
                    response.getWriter().println("Page Featured List : " + featuredPagesArray.toString());
                    session.save();
                } else {
                    log.error("Unable to find featuredArticle node");
                    // TODO send back error message
                }


            } catch (Exception e) {
                log.error("Could not update Featured List from servlet: " + e);
                e.printStackTrace();
            }
        }
    }