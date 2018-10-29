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

//QUeryBuilder APIs
import com.day.cq.wcm.api.Page;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({
        @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
        @Property(name = "sling.servlet.selectors", value = "newsservlet"),
        @Property(name = "sling.servlet.extensions", value = "html"),
        @Property(name = "service.description", value = "for News List component"),
        @Property(name = "label", value = "News List")
})

public class NewsListServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 9073952766248919847L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsListServlet.class);

    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    private ResourceResolver resolver;

    private Session session;

    private static final int HITS_PER_PAGE = 10;
    private static final int START_INDEX = 0;
    private static final String DEFAULT_NEWS_FILTER = "AllItems";
    private static final String DEFAULT_NEWS_FILTER_YEAR = "ChooseYear";
    private static final String NEWS_TEMPLATE = "/apps/chinational/templates/newsdetailspage";

    // storing list of all news articles sorted by publishDate
    private java.util.List<Page> allNews;

    /* allFilteredNews - storing list of all news articles
     * filtered based on selection in the dropdown
     * sorted by publish date
     * required to get no. of total results to Show-Hide LOAD MORE Button
     */
    private java.util.List<Page> allFilteredNews;

    /* listNews - storing list of all news articles used on page
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> listNews;

    /* featuredNews - storing list of featured news articles
     * sorted by publish date
     */
    private java.util.List<Page> featuredNews;

    // storing list of years of published articles
    // private java.util.List<String> listYears;

    private Map<String, Object> articleMap = new HashMap<String, Object>();

    private String newsFilter;
    private String filterYear;
    private String media_page_path;
    private int start_index;
    private int hits_per_page;
    private int totalResults; // used for hide-show "LOAD MORE" button
    private String newsTemplate;
    private String parentPage;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
        newsFilter = "";
        filterYear = "";
        media_page_path = "";
        start_index = START_INDEX;
        hits_per_page = HITS_PER_PAGE;
        totalResults = 0;
        newsTemplate = NEWS_TEMPLATE;
        allNews = new ArrayList<>();
        listNews = new ArrayList<>();
        allFilteredNews = new ArrayList<>();
        featuredNews = new ArrayList<>();
        // listYears = new ArrayList<>();
        parentPage = request.getPathInfo();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "tagManagement");
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);

            Resource resource = request.getResource();
            if(resource != null) {
                media_page_path = resource.getPath();
                LOGGER.info("media_page_path parent_path : " + resource.getPath());
            }
/*    		if (resource != null) {
    			Iterator<Resource> childResources = resource.listChildren();
    			while (childResources.hasNext()) {
    				// to get path of parent page
    				 Resource property = childResources.next().getChild("news-list/parentPage");
    				    if (property == null) {
    				        continue;
    				    }
    				    if (property != null) {
    				    	media_page_path = property.adaptTo(String.class);
    				    }
    			}
    		}
 */
            if(media_page_path == null || media_page_path.isEmpty()){
                media_page_path = request.getRequestURI().substring(0, request.getRequestURI().indexOf(".newsservlet"));
            }

            String[] selectors = request.getRequestPathInfo().getSelectors();
            if(selectors.length >= 2){
                newsFilter = selectors[1];
            } else {
                newsFilter = DEFAULT_NEWS_FILTER;
            }

            if(selectors.length >= 3 && (selectors[2].matches("[0-9]+"))){
                start_index = Integer.parseInt(selectors[2]);
            }

            if(selectors.length >= 4){
                filterYear = selectors[3];
            } else {
                filterYear = DEFAULT_NEWS_FILTER_YEAR;
            }

            allNews = NewsBlogUtils.populateListItems(media_page_path, resolver, newsTemplate , request.getPathInfo()); //to get all the news using defined template, sorted by Publish date
            articleMap = NewsBlogUtils.populateArticlesForPagination(parentPage, allNews, resolver, newsFilter, filterYear);
            // listYears = (List<String>) articleMap.get("listYears");
            featuredNews = (List<Page>) articleMap.get("featuredArticles");
            allFilteredNews= (List<Page>) articleMap.get("filteredArticles");
            // LOGGER.info("filtered news : " + allFilteredNews.size());

            for(Page item : featuredNews) {
                if(allFilteredNews.contains(item)){
                    allFilteredNews.remove(item);
                }
            }

            // list of news, sorted by Publish date
            listNews = NewsBlogUtils.populateListArticles(start_index, hits_per_page, allFilteredNews);

            totalResults = allFilteredNews.size();

            JSONObject jsonResult = new JSONObject();
            JSONArray jsonArray = getJsonNews();
            jsonResult.put("jsonNews", jsonArray);
            jsonResult.put("total_results", totalResults);
            // jsonResult.put("list_years", listYears);
            String jsonData = jsonResult.toString();
            // LOGGER.info("jsondata : " + jsonData);

            response.setContentType("application/json");

            PrintWriter out = response.getWriter();
            out.write(jsonData);
            out.flush();
        } catch (Exception e) {
            LOGGER.error("Exception in NewsListServlet",e);
            try {
                response.getWriter().write(e.getMessage());
            } catch (IOException e1) {
                LOGGER.error("Exception in NewsListServlet>>doget method",e1);
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

    public JSONArray getJsonNews() {
        JSONArray jsonNews = new  JSONArray();

        try {
            for (Page item : listNews) {
                JSONObject jsonObject = new  JSONObject();
                ValueMap sMap = item.getProperties();
                if(sMap.get("newsHeading", String.class) != null) {
                    jsonObject.put("newsHeading", sMap.get("newsHeading", String.class));
                }
                if(sMap.get("publishDate", Calendar.class) != null) {
                    Calendar date = item.getProperties().get("publishDate", Calendar.class);
                    SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, YYYY");
                    String formattedDate = formatter.format(date.getTime()).toUpperCase();
                    jsonObject.put("publishDate", formattedDate);
                }
                if(item.getPath() != null) {
                    jsonObject.put("newsURL", item.getPath());
                }
                if(sMap.get("excerpt", String.class) != null) {
                    jsonObject.put("excerpt", sMap.get("excerpt", String.class));
                }
                jsonNews.put(jsonObject);
            }

        } catch (Exception e) {
            LOGGER.error("Exception Occured in getJsonNews() method" + e, e);
        }
        return jsonNews;
    }

}