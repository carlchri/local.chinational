package org.chi.aem.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
  
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
 
import org.apache.sling.api.servlets.SlingAllMethodsServlet;     
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import javax.jcr.RepositoryException;
import org.apache.felix.scr.annotations.Reference;
 
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
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceResolver; 
import org.apache.sling.api.resource.Resource; 
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.io.JSONStringer;

//QUeryBuilder APIs
import com.day.cq.search.QueryBuilder; 
import com.day.cq.search.Query; 
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.search.result.Hit; 
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Service(value = Servlet.class)
@Component(immediate = true, metatype = true)
@Properties({ 
		      @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
			  @Property(name = "sling.servlet.selectors", value = "blogsservlet"),
			  @Property(name = "sling.servlet.extensions", value = "json"),
			  @Property(name = "service.description", value = "for Blogs List component"),
			  @Property(name = "label", value = "Blogs List") 
		  })

public class BlogsListServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 9073952766248919847L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsListServlet.class);
    
    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;
                
    @Reference
    private QueryBuilder builder;
     
    private ResourceResolver resolver;
    
    private Session session;
    
    private PageManager pageManager;
    
    private static final int FEATURED_LIMIT = 3;
    private static final int HITS_PER_PAGE = 10;
    private static final int START_INDEX = 0;
    private static final String DEFAULT_NEWS_FILTER = "SortByMostRecent";
    private static final String DEFAULT_MEDIA_PAGE_PATH = "/content";
    
    // storing list of all blogs articles sorted by publishDate
    private java.util.List<Page> allBlogs;
    
    /* allFilteredBlogs - storing list of all blogs articles 
     * filtered based on selection in the dropdown
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     * required to get total results to Show-Hide LOAD MORE Button
     */
    private java.util.List<Page> allFilteredBlogs;

    /* listBlogs - storing list of all blogs articles used on page
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> listBlogs;
    
     /* featuredBlogs - storing list of featured blogs articles [Max 3] 
     * sorted by publish date
     */
    private java.util.List<Page> featuredBlogs;

    // storing list of years of published articles
    private java.util.List<String> listYears;
    
    // storing list of all tags and tagID attached to blogs articles page template
    private java.util.List<String> listTags;
    // storing map of all tags name and title attached to news articles page template
    private Map<String, String> tagsMap = new HashMap<String, String>();

    private String blogsFilter;
    private String media_page_path;
    private int start_index;
    private int hits_per_page;
    private int totalResults; // used for hide-show "LOAD MORE" button
             
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServerException, IOException {
       
        blogsFilter = "";
        media_page_path = DEFAULT_MEDIA_PAGE_PATH;
        start_index = START_INDEX;
        hits_per_page = HITS_PER_PAGE;
        totalResults = 0;
        
        try {
        	resolver = request.getResourceResolver();
            session = resolver.adaptTo(Session.class);
            
            Resource resource = request.getResource();
    		if (resource != null) {
    			Iterator<Resource> childResources = resource.listChildren();
    			while (childResources.hasNext()) {
    				// LOGGER.info("childResources : " + childResources.next().getPath());
    				 Resource property = childResources.next().getChild("news-list/parentPage");
    				    if (property == null) {
    				        continue;
    				    }
    				    if (property != null) {
    				    	media_page_path = property.adaptTo(String.class);
    				    }
    			}
    		}

    		if(media_page_path == null){
    			media_page_path = DEFAULT_MEDIA_PAGE_PATH;
    		}

	        String[] selectors = request.getRequestPathInfo().getSelectors();
	        if(selectors.length >= 2){
	        	blogsFilter = selectors[1];
	        } else {
	        	blogsFilter = DEFAULT_NEWS_FILTER;
	        }
	        
	        if(selectors.length >= 3 && (selectors[2].matches("[0-9]+"))){
	        	start_index = Integer.parseInt(selectors[2]);	
	        }
	        
	       	allBlogs = new ArrayList<>();
	    	listBlogs = new ArrayList<>();
	    	allFilteredBlogs = new ArrayList<>();
	        featuredBlogs = new ArrayList<>();
	        listYears = new ArrayList<>();
	        listTags = new ArrayList<>();
	        
	        pageManager = resolver.adaptTo(PageManager.class);
	        
	        allBlogs = populateListItems(allBlogs); //to get all the blogs using defined template, sorted by Publish date
	        listYears = populateListYears(allBlogs); // extract years from list of all blogs
	        listTags = populateListTags(allBlogs);   // extract author defined tags from list of all blogs
	        featuredBlogs = populateListItems(featuredBlogs);  // extract featured articles, sorted by Publish date
	        setMaxfeaturedBlogs(); //limit featured articles to maximum 3.
	        allFilteredBlogs = populateListItems(allFilteredBlogs); //filtered list of blogs based on year or Tag, sorted by Publish date
	        listBlogs = populateListItems(listBlogs); //filtered list of blogs based on year or Tag, sorted by Publish date
	        
	        totalResults = allFilteredBlogs.size();
	        
	        JSONObject jsonResult = new JSONObject();
	        JSONArray jsonArray = getJsonBlogs();
	        jsonResult.put("jsonBlogs", jsonArray);
	        jsonResult.put("total_results", totalResults);
	        String jsonData = jsonResult.toString();
	        // LOGGER.info("jsonBlogs :" + jsonResult.toString());
	        
	        // response.setContentType("application/json");
	         
	       PrintWriter out = response.getWriter();
	       out.write(jsonData);
	       out.flush();
	    } catch (Exception e) {
	        LOGGER.error("Exception in BlogsListServlet",e);
	        try {
	            response.getWriter().write(e.getMessage());
	        } catch (IOException e1) {
	            LOGGER.error("Exception in BlogsListServlet>>doget method",e1);
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

    public JSONArray getJsonBlogs() {
    	JSONArray jsonBlogs = new  JSONArray();
    	
        try {
	        for (Page item : listBlogs) {
	        	JSONObject jsonObject = new  JSONObject();
	            ValueMap sMap = item.getProperties();
	            if(sMap.get("newsHeading", String.class) != null) {
	            	jsonObject.put("blogsHeading", sMap.get("newsHeading", String.class));
	            }
	            if(sMap.get("publishDate", Calendar.class) != null) {
	               	Calendar date = item.getProperties().get("publishDate", Calendar.class);
	            	SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, YYYY"); 
	        		String formattedDate = formatter.format(date.getTime()).toUpperCase();
	        		jsonObject.put("publishDate", formattedDate);
	            }
	            if(item.getPath() != null) {
	            	jsonObject.put("blogsURL", item.getPath());
	            }
	            if(sMap.get("excerpt", String.class) != null) {
	            	jsonObject.put("excerpt", sMap.get("excerpt", String.class));
	            }
	            jsonBlogs.put(jsonObject);
	        }
	        
        } catch (Exception e) {
            LOGGER.error("Exception Occured in getJsonBlogs() method" + e, e);
        }
        return jsonBlogs;
    }

    private java.util.List<Page> populateListItems(java.util.List<Page> list) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("path", media_page_path);
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/chinational/templates/blogsdetailspage");
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");

        if(list == listBlogs){
    	    map.put("p.offset", String.valueOf(start_index)); // same as query.setStart(0) below
        	map.put("p.limit", String.valueOf(hits_per_page)); // same as query.setHitsPerPage(20) below
        }else{
    	    map.put("p.offset", String.valueOf(0)); // same as query.setStart(0) below
        	map.put("p.limit", String.valueOf(-1)); // same as query.setHitsPerPage(20) below
        }
        
        if(list == featuredBlogs){
        	map.put("boolproperty", "jcr:content/isFeaturedArticle");
        	map.put("boolproperty.value", "true");
        }
		
        int i = 1;
        if(list == listBlogs || list == allFilteredBlogs){
        	for(Page item : featuredBlogs){
        		// LOGGER.info("item.path : " + item.getPath());
            	map.put(Integer.toString(i++)+"_excludepaths", item.getPath());
        	}
        }
		
        if((list == listBlogs || list == allFilteredBlogs) && listYears.contains(blogsFilter)){
        	LOGGER.info("inside Query listYears- blogsFilter" + blogsFilter);
        	map.put("daterange.property", "jcr:content/publishDate");
   			map.put("daterange.lowerBound", blogsFilter + "-01-01");
   			map.put("daterange.lowerOperation", ">=");
        	map.put("daterange.upperBound", blogsFilter + "-12-31");
        	map.put("daterange.upperOperation", "<=");
        }

        if((list == listBlogs || list == allFilteredBlogs) && tagsMap.containsKey(blogsFilter)){
        	String tagSearch = tagsMap.get(blogsFilter);
        	map.put("tagsearch.property", "jcr:content/cq:tags");
        	map.put("tagsearch", tagSearch);
        	LOGGER.info("inside Query listTags - blogsFilter" + blogsFilter);
        }

        PredicateGroup group = PredicateGroup.create(map);
        Session session = resolver.adaptTo(Session.class);
        QueryBuilder builder = resolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(group, session);

        try {
            SearchResult result = query.getResult();
            list = collectSearchResults(query.getResult(), list);
        } catch (RepositoryException e) {
            LOGGER.error("Unable to retrieve search results for query.", e);
        }
       return list;
    }

     private java.util.List<Page> collectSearchResults(SearchResult result, java.util.List<Page> list) throws RepositoryException {

    	 for (Hit hit : result.getHits()) {
             Page containingPage = pageManager.getContainingPage(hit.getResource());
             if (containingPage != null) {
            	 list.add(containingPage);
             }
         }
         
         // If no featured article present, add the latest article as featured article.
         if(list == featuredBlogs && list.isEmpty()){
        	 list.add(allBlogs.get(0));
         }
         LOGGER.info("listBlogs.size() after recursive : " + listBlogs.size()); 

         return list;
     }

     private java.util.List<String> populateListYears(java.util.List<Page> list) {
    	 for(Page item : list) {
    		Calendar date = item.getProperties().get("publishDate", Calendar.class); 
    		//int year = date.get(Calendar.YEAR);
    		SimpleDateFormat formatter = new SimpleDateFormat("YYYY"); 
    		String year = formatter.format(date.getTime()).toUpperCase(); 
    		if(listYears.isEmpty()){
        		listYears.add(year);
    		} else if (!listYears.contains(year)){
    			listYears.add(year);
    		}
    	 }
         LOGGER.info("listYears [inside populateListYears() method]: " + listYears);
    	 return listYears;
     }

     private java.util.List<String> populateListTags(java.util.List<Page> list) {
    	 for(Page item : list) {
             TagManager tagManager = resolver.adaptTo(TagManager.class);
             Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);

             if(tags.length !=0){
            	 for(Tag tag : tags){
             		String tagName = tag.getTitle();

	         		if(listTags.isEmpty()){
	            		listTags.add(tagName);
	                    tagsMap.put(tag.getName(),tagName);
	        		} else if (!listTags.contains(tagName)){
	        			listTags.add(tagName);
	        			tagsMap.put(tag.getName(),tagName);
	        		}
            	 }
             }
             
    	 }
    	 return listTags;
     }
     
    private void setMaxfeaturedBlogs() {
    	java.util.List<Page> tmpListItems = new ArrayList<>();
        for (Page item : featuredBlogs) {
            if (tmpListItems.size() < FEATURED_LIMIT) {
                tmpListItems.add(item);
            } else {
                break;
            }
        }
            featuredBlogs = tmpListItems;
    }

}