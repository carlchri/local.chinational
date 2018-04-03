/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.RangeIterator;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Predicate;
import com.day.cq.search.SimpleSearch;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = BlogsList.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BlogsList implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "chinational/components/content/blogslist";

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogsList.class);
    /**
     * Name of the resource property storing the root page from which to build the list 
     *
     */
    String PN_PARENT_PAGE = "parentPage";

    private static final int FEATURED_LIMIT = 3;
    private static final String PARAM_PAGE = "page"; // for Pagination
    private static final int HITS_PER_PAGE = 10;
    private static final int START_INDEX = 0;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @Self
    private SlingHttpServletRequest request;

    private PageManager pageManager;
    
    // storing list of all blogs articles sorted by publishDate
    private java.util.List<Page> allBlogs;
    
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
    // storing map of all tags name and title attached to blogs articles page template
    private Map<String, String> tagsMap = new HashMap<String, String>();
    
    private int start_index;
    private int hits_per_page;
    private int totalResults; // used for hide-show "LOAD MORE" button
    private List<Integer> pages; //for pagination
    private int totalNumberPages; // for pagination
    private int activePage; // for pagination and for getting offset

    @PostConstruct
    private void initModel() {
        start_index = START_INDEX;
        hits_per_page = HITS_PER_PAGE;
        totalResults = 0;
        totalNumberPages = 1;
        activePage = 1;
        
        String[] selectors = request.getRequestPathInfo().getSelectors();
        LOGGER.info("selectors length: " + selectors.length);
        if(selectors.length != 0 && (selectors[0].matches("[0-9]+"))){
        	activePage = Integer.parseInt(selectors[0]);	
        }
        LOGGER.info("active Page: " + activePage);
        if(activePage != 1 && (activePage > 1)) {
            start_index = ((activePage - 1) * hits_per_page);
        } else {
        	activePage = 1; //for Pagination active class when page load for the first time
        }
    	
    	allBlogs = new ArrayList<>();
    	listBlogs = new ArrayList<>();
        featuredBlogs = new ArrayList<>();
        listYears = new ArrayList<>();
        listTags = new ArrayList<>();

        pages = new ArrayList<Integer> ();
        
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
        allBlogs = populateListItems(allBlogs); //to get all the blogs using defined template, sorted by Publish date
        listYears = populateListYears(allBlogs); // extract years from list of all blogs
        listTags = populateListTags(allBlogs);   // extract author defined tags from list of all blogs
        featuredBlogs = populateListItems(featuredBlogs);  // extract featured articles, sorted by Publish date
        setMaxfeaturedBlogs(); //limit featured articles to maximum 3.
        listBlogs = populateListItems(listBlogs); //filtered list of blogs based on year or Tag, sorted by Publish date
        
		// For AJAX Call - to get Total Results initially for LOAD MORE
        totalResults = allBlogs.size() - featuredBlogs.size();
        LOGGER.info("totalResults using allBlogs : " + totalResults);
        
        // to get total no of pages and List of pages for data-sly-list for Pagination
    	int total = (allBlogs.size() - featuredBlogs.size())/hits_per_page;
    	if(((allBlogs.size() - featuredBlogs.size()) % hits_per_page) == 0){
    		totalNumberPages = total;
    	} else {
    		totalNumberPages = total + 1;
    	}
        LOGGER.info("totalNumberPages: " + totalNumberPages);
        for(int i = 1; i <= totalNumberPages; i++) {
            pages.add(i);
        }

    }

    public Collection<Page> getAllBlogs() {
        return allBlogs;
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }

    public Collection<Page> getFeaturedBlogs() {
        return featuredBlogs;
    }

    public Collection<Page> getListBlogs() {
        return listBlogs;
    }

    public Collection<String> getListYears() {
        return listYears;
    }

    public Map<String, String> getTagsMap() {
        return tagsMap;
    }
    
    public Collection<String> getListTags() {
        return listTags;
    }
    
    public int getStartIndex() {
   		return start_index;
    }

    public int getHitsPerPage() {
   		return hits_per_page;
    }

    public int getTotalResults() {
   		return totalResults;
    }

    public int getTotalPages() {
   		return totalNumberPages;
    }

    public int getActivePage() {
   		return activePage;
    }

    public int getPreviousPage() {
    	if(activePage <= 1){
    		return 1;
    	} else if(activePage > totalNumberPages){
    		return totalNumberPages;
    	} else {
    		return activePage - 1;	
    	}
    }

    public int getNextPage() {
    	if(activePage >= totalNumberPages){
    		return totalNumberPages;
    	}else{
    		return activePage + 1;	
    	}
    }

    public List<Integer> getPages() {
        return pages;
    }

    private java.util.List<Page> populateListItems(java.util.List<Page> list) {

        Map<String, String> map = new HashMap<String, String>();

        map.put("path", properties.get(PN_PARENT_PAGE, "/content"));
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
        if(list == listBlogs){
        	for(Page item : featuredBlogs){
        		LOGGER.info("item.path : " + item.getPath());
            	map.put(Integer.toString(i++)+"_excludepaths", item.getPath());
        	}
        }
		
        PredicateGroup group = PredicateGroup.create(map);
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(group, session);
        // LOGGER.info("Query : " + query.toString());
        // query.setStart(start_index);
        // query.setHitsPerPage(hits_per_page);
        
        try {
            SearchResult result = query.getResult();
            LOGGER.info("result.getTotalMatchefs() : " + list + " : " + result.getTotalMatches());

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
         if(list == featuredBlogs && list.isEmpty()&& allBlogs.size() > 0){
        	 list.add(allBlogs.get(0));
         }
 	 
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
    	 return listYears;
     }

     private java.util.List<String> populateListTags(java.util.List<Page> list) {
    	 for(Page item : list) {
             TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
             Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
             // LOGGER.info("tags: " + tags);
             if(tags.length !=0){
            	 for(Tag tag : tags){
            		String tagName = tag.getTitle();
            		// LOGGER.info("tagTitle: " + tag.getTitle());
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