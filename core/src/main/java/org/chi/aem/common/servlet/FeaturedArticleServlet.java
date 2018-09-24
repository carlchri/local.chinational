package org.chi.aem.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.json.JsonWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.day.cq.wcm.api.Page;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)

@Properties({
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.selectors", value = "featuredArticleServlet"),
        @Property(name = "sling.servlet.extensions", value = "html"),
        @Property(name = "service.description", value = "For News List component"),
        @Property(name = "label", value = "Featured News List")
})

public class FeaturedArticleServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(FeaturedArticleServlet.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException{


        try {
            ResourceResolver resolver = request.getResourceResolver();
            Session session = resolver.adaptTo(Session.class);

//            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            String pathInfo = request.getPathInfo();

            String currentPagePath = pathInfo.split("\\.")[0];

//            pw.print("Page Url  :: " + currentPagePath  + "<br/>");

            ResourceResolver resourceResolver = request.getResource().getResourceResolver();
            String requestPagePath = currentPagePath  + "/jcr:content/news-list";
//            pw.write("requestPagePath :: "+requestPagePath + "<br/>");

            Resource newsListres = resourceResolver.getResource(requestPagePath);
            ValueMap newsListMap = newsListres.adaptTo(ValueMap.class);
            Node newsListNode = newsListres.adaptTo(Node.class);
            String parentPage = newsListMap.get("parentPage") + "/jcr:content";

            if (newsListNode.hasProperty("parentPage")) {
//                pw.write("News Link Node has ParentPage Property :: " + parentPage  + "<br/>");
            }

            Resource newsListChildRes = resourceResolver.getResource(parentPage);
            ValueMap newsListChildMap = newsListChildRes.adaptTo(ValueMap.class);
            Node newsListChildNode = newsListChildRes.adaptTo(Node.class);
            String featuredList = newsListChildMap.get("featuredList").toString();

            Value[] featuredPagesNode = newsListChildNode.getProperty("featuredList").getValues();
            JSONObject jsonResult = new JSONObject();
            JSONObject jsonArticles = new JSONObject();
            JSONArray jsonFeaturedList  = new JSONArray();
            Integer index = 0;


            if (newsListChildNode.hasProperty("featuredList")) {
                log.info("Node has featured list property");
                for (Value spn : featuredPagesNode) {
//                    jsonResult = new JSONObject();
                    String spnPath = spn.getString();
                    log.info("Spn Path String :: "+ spnPath);
                    Resource spnRes = resourceResolver.getResource(spnPath);
                    log.info("Resoure resolver has resolved");
                    ValueMap spnMap = spnRes.adaptTo(ValueMap.class);
                    log.info("Resource has resovedto Valuemap");
//                    jsonResult.put("newsHeading", spnMap.get("newsHeading"));
//                    pw.write(" news Heading :: " + spnMap.get("newsHeading") + "<br/>");
//                    jsonResult.put("publishDate", spnMap.get("publishDate"));
//                    jsonResult.put("excerpt", spnMap.get("excerpt"));
//                    jsonResult.put("imageSrc", spnMap.get("imageSrc"));
//                    jsonResult.put("blogsURL", spn.toString());
//                    jsonFeaturedList.put(jsonResult);
//                    index++;
                }

//                jsonFeaturedList.put(jsonArticles);
//                String jsonData = jsonFeaturedList.toString();
                // LOGGER.info("jsondata : " + jsonData);

                response.setContentType("application/json");
//                pw.write(jsonData);
                pw.flush();
            }

            session.save();
//                log.info("After Save");
            session.refresh(true);
//                log.info("After Refresh");

        } catch (Exception e) {
            log.error("Could not Update Url reference: " + e);
            e.printStackTrace();
        }
    }
}