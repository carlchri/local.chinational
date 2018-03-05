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

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = NewsList.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsList implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "chinational/components/content/newslist";

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsList.class);
    /**
     * Name of the resource property storing the root page from which to build the list 
     *
     */
    String PN_PARENT_PAGE = "parentPage";

    private static final int FEATURED_LIMIT = 3;

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
    
    // storing list of all news articles sorted by publishDate
    private java.util.List<Page> allNews;
    
    /* listNews - storing list of all news articles 
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> listNews;
    
    /* featuredNews - storing list of all news articles 
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> featuredNews;

    // storing list of years of published articles
    private java.util.List<String> listYears;
    
    // storing list of all tags and tagID attached to news articles page template
    private java.util.List<String> listTags;
    private java.util.List<String> listTagsID;
    
    private String newsFilter;

    Map<String, Object> newsList = new HashMap<String, Object>();

    @PostConstruct
    private void initModel() {
    	if(request.getAttribute("news_filter") != null){
            newsFilter= (String) request.getAttribute("news_filter");    		
    	}else{
    		newsFilter = "";
    	}
        LOGGER.info("newsFilter" + newsFilter);
    	
    	allNews = new ArrayList<>();
    	listNews = new ArrayList<>();
        featuredNews = new ArrayList<>();
        listYears = new ArrayList<>();
        listTags = new ArrayList<>();
        listTagsID = new ArrayList<>();
        
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
        allNews = populateListItems(allNews); //to get all the news using defined template, sorted by Publish date
        listYears = populateListYears(allNews); // extract years from list of all news
        listTags = populateListTags(allNews);   // extract author defined tags from list of all news
        featuredNews = populateListItems(featuredNews);  // extract featured articles, sorted by Publish date
        setMaxfeaturedNews(); //limit featured articles to maximum 3.
        listNews = populateListItems(listNews); //filtered list of news based on year or Tag, sorted by Publish date
        //  filtering featured articles from the list. This is the list used on media page.
        // listNews = populateListNews(listNews, featuredNews);
        listNews = populateFilteredList(featuredNews, listNews);

        // setMaxItems();

    }

    public JSONObject getJsonNews() {
        JSONObject jsonNews = null;
        try {
        	jsonNews = new JSONObject();
        	jsonNews.put("allNews", new JSONArray(allNews));
        	jsonNews.put("featuredNews", new JSONArray (featuredNews));
        	jsonNews.put("listNews", new JSONArray(listNews));
        	jsonNews.put("listYears", new JSONArray(listYears));
        	jsonNews.put("listTags", new JSONArray(listTags));
        } catch (Exception e) {
            LOGGER.error("Could not create JSON", e);
        }
        LOGGER.info("jsonNews :" + jsonNews.toString());
        return jsonNews;
    }

    public Collection<Page> getAllNews() {
        return allNews;
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }

    public Collection<Page> getFeaturedNews() {
        return featuredNews;
    }

    public Collection<Page> getListNews() {
        return listNews;
    }

    public String getNewsFilter() {
   		return newsFilter;
    }
    
    public boolean getIsFilterYear(){
    	if(listYears.contains(newsFilter)){
    		return true;
    	}
    	return false;
    }

    public Collection<String> getListYears() {
        return listYears;
    }

    public Collection<String> getListTags() {
        return listTags;
    }

    private java.util.List<Page> populateListItems(java.util.List<Page> list) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("path", properties.get(PN_PARENT_PAGE, currentPage.getPath()));
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", "/apps/chinational/templates/newsdetailspage");
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");
        // can be done in map or with Query methods
        map.put("p.offset", "0"); // same as query.setStart(0) below
        map.put("p.limit", "-1"); // same as query.setHitsPerPage(20) below

        if(list == featuredNews){
        	map.put("boolproperty", "jcr:content/isFeaturedArticle");
        	map.put("boolproperty.value", "true");
        }

        if(list == listNews && listYears.contains(newsFilter)){
        	map.put("daterange.property", "jcr:content/publishDate");
   			map.put("daterange.lowerBound", newsFilter + "-01-01");
   			map.put("daterange.lowerOperation", ">=");
        	map.put("daterange.upperBound", newsFilter + "-12-31");
        	map.put("daterange.upperOperation", "<=");
        }

        if(list == listNews && listTags.contains(newsFilter)){
        	map.put("tagsearch.property", "jcr:content/cq:tags");
        	map.put("tagsearch", newsFilter);
/*        	LOGGER.info("inside Query - newsFilter" + newsFilter);
        	for(String tagID : listTagsID){
        		if(tagID.contains(newsFilter)){
                	map.put("tagid.property", "jcr:content/cq:tags");
           			map.put("tagid.1_value", tagID);
        		}
        	}
*/
        }

        PredicateGroup group = PredicateGroup.create(map);
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(group, session);

        // query.setStart(0);   // already set in map above
        // query.setHitsPerPage(-1); // already set in map above
                   
        SearchResult result = query.getResult();
        // LOGGER.info("Result : " + result.toString());
        
        try {
            list = collectSearchResults(result, list);
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
         if(list == featuredNews && list.isEmpty() && allNews.size() > 0){
        	 list.add(allNews.get(0));
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
            		// LOGGER.info("tagName: " + tag.getName());
            		// LOGGER.info("tagID: " + tag.getTagID());
            		// LOGGER.info("tagNamespace: " + tag.getNamespace());
            		// LOGGER.info("tagTitle: " + tag.getTitle());
	         		if(listTags.isEmpty()){
	            		listTags.add(tagName);
	        		} else if (!listTags.contains(tagName)){
	        			listTags.add(tagName);
	        		}
            		String tagID = tag.getTagID();
	         		if(listTagsID.isEmpty()){
	            		listTagsID.add(tagID);
	        		} else if (!listTagsID.contains(tagID)){
	        			listTagsID.add(tagID);
	        		}
            	 }
             }
             
    	 }
    	 return listTags;
     }
     
/* This method used with allNews as List1 and featuredNews as list2 and then to populate listNews[exclusing featuredNews].
     private java.util.List<Page> populateListNews(java.util.List<Page> list1, java.util.List<Page> list2) {
    	 boolean add = true;
    	 for(Page item1 : list1) {
    		 for(Page item2 : list2) {
    			 if(item1 == item2){
    				 add = false;
    				 break;
    			 } 
    			 add = true;
    		 }
    		 if(add && !listNews.contains(item1)){
    			 listNews.add(item1);
    		 }
    	 }	 
         LOGGER.info("listNews : " + listNews);
    	 return listNews;
     }
*/
     private java.util.List<Page> populateFilteredList(java.util.List<Page> list1, java.util.List<Page> list2) {

    	 for(Page item1 : list1) {
    		 for(Page item2 : list2) {
    			 if(item1 == item2){
    				 listNews.remove(item1);
    				 break;
    			 } 
    		 }
    	 }	 
    	 return listNews;
     }

/*   This method not in use. Created to restrict the maximum number of items in the News List
 *    
     private void setMaxItems() {
        if (maxItems != 0) {
            java.util.List<Page> tmpListItems = new ArrayList<>();
            for (Page item : allNews) {
                if (tmpListItems.size() < maxItems) {
                    tmpListItems.add(item);
                } else {
                    break;
                }
            }
            allNews = tmpListItems;
        }
    }
*    
*/    

    private void setMaxfeaturedNews() {
    	java.util.List<Page> tmpListItems = new ArrayList<>();
        for (Page item : featuredNews) {
            if (tmpListItems.size() < FEATURED_LIMIT) {
                tmpListItems.add(item);
            } else {
                break;
            }
        }
            featuredNews = tmpListItems;
    }
 }