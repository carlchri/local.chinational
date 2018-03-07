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
    
    /* listBlogs - storing list of all blogs articles 
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> listBlogs;
    
    /* featuredBlogs - storing list of all Blogs articles 
     * excluding featured article to be displayed at top of page
     * sorted by publish date
     */
    private java.util.List<Page> featuredBlogs;

    // storing list of years of published articles
    private java.util.List<String> listYears;
    
    // storing list of all tags and tagID attached to Blogs articles page template
    private java.util.List<String> listTags;
    private java.util.List<String> listTagsID;
    
    private String blogsFilter;

    Map<String, Object> blogsList = new HashMap<String, Object>();

    @PostConstruct
    private void initModel() {
    	if(request.getAttribute("blogs_filter") != null){
            blogsFilter= (String) request.getAttribute("blogs_filter");    		
    	}else{
    		blogsFilter = "";
    	}
        LOGGER.info("blogsFilter" + blogsFilter);
    	
    	allBlogs = new ArrayList<>();
    	listBlogs = new ArrayList<>();
        featuredBlogs = new ArrayList<>();
        listYears = new ArrayList<>();
        listTags = new ArrayList<>();
        listTagsID = new ArrayList<>();
        
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
        allBlogs = populateListItems(allBlogs); //to get all the Blogs using defined template, sorted by Publish date
        listYears = populateListYears(allBlogs); // extract years from list of all Blogs
        listTags = populateListTags(allBlogs);   // extract author defined tags from list of all Blogs
        featuredBlogs = populateListItems(featuredBlogs);  // extract featured articles, sorted by Publish date
        setMaxfeaturedBlogs(); //limit featured articles to maximum 3.
        listBlogs = populateListItems(listBlogs); //filtered list of Blogs based on year or Tag, sorted by Publish date
        //  filtering featured articles from the list. This is the list used on media page.
        // listBlogs = populateListBlogs(listBlogs, featuredBlogs);
        listBlogs = populateFilteredList(featuredBlogs, listBlogs);

        // setMaxItems();

    }

    public JSONObject getJsonBlogs() {
        JSONObject jsonBlogs = null;
        try {
        	jsonBlogs = new JSONObject();
        	jsonBlogs.put("allBlogs", new JSONArray(allBlogs));
        	jsonBlogs.put("featuredBlogs", new JSONArray (featuredBlogs));
        	jsonBlogs.put("listBlogs", new JSONArray(listBlogs));
        	jsonBlogs.put("listYears", new JSONArray(listYears));
        	jsonBlogs.put("listTags", new JSONArray(listTags));
        } catch (Exception e) {
            LOGGER.error("Could not create JSON", e);
        }
        LOGGER.info("jsonBlogs :" + jsonBlogs.toString());
        return jsonBlogs;
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

    public String getBlogsFilter() {
   		return blogsFilter;
    }
    
    public boolean getIsFilterYear(){
    	if(listYears.contains(blogsFilter)){
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
        map.put("property.value", "/apps/chinational/templates/blogsdetailspage");
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");
        // can be done in map or with Query methods
        map.put("p.offset", "0"); // same as query.setStart(0) below
        map.put("p.limit", "-1"); // same as query.setHitsPerPage(20) below

        if(list == featuredBlogs){
        	map.put("boolproperty", "jcr:content/isFeaturedArticle");
        	map.put("boolproperty.value", "true");
        }

        if(list == listBlogs && listYears.contains(blogsFilter)){
        	map.put("daterange.property", "jcr:content/publishDate");
   			map.put("daterange.lowerBound", blogsFilter + "-01-01");
   			map.put("daterange.lowerOperation", ">=");
        	map.put("daterange.upperBound", blogsFilter + "-12-31");
        	map.put("daterange.upperOperation", "<=");
        }

        if(list == listBlogs && listTags.contains(blogsFilter)){
        	map.put("tagsearch.property", "jcr:content/cq:tags");
        	map.put("tagsearch", blogsFilter);
/*        	LOGGER.info("inside Query - blogsFilter" + blogsFilter);
        	for(String tagID : listTagsID){
        		if(tagID.contains(blogsFilter)){
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
         if(list == featuredBlogs && list.isEmpty() && allBlogs.size() > 0){
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
     
/* This method used with allBlogs as List1 and featuredBlogs as list2 and then to populate listBlogs[exclusing featuredBlogs].
     private java.util.List<Page> populateListBlogs(java.util.List<Page> list1, java.util.List<Page> list2) {
    	 boolean add = true;
    	 for(Page item1 : list1) {
    		 for(Page item2 : list2) {
    			 if(item1 == item2){
    				 add = false;
    				 break;
    			 } 
    			 add = true;
    		 }
    		 if(add && !listBlogs.contains(item1)){
    			 listBlogs.add(item1);
    		 }
    	 }	 
         LOGGER.info("listBlogs : " + listBlogs);
    	 return listBlogs;
     }
*/
     private java.util.List<Page> populateFilteredList(java.util.List<Page> list1, java.util.List<Page> list2) {

    	 for(Page item1 : list1) {
    		 for(Page item2 : list2) {
    			 if(item1 == item2){
    				 listBlogs.remove(item1);
    				 break;
    			 } 
    		 }
    	 }	 
    	 return listBlogs;
     }

/*   This method not in use. Created to restrict the maximum number of items in the Blogs List
 *    
     private void setMaxItems() {
        if (maxItems != 0) {
            java.util.List<Page> tmpListItems = new ArrayList<>();
            for (Page item : allBlogs) {
                if (tmpListItems.size() < maxItems) {
                    tmpListItems.add(item);
                } else {
                    break;
                }
            }
            allBlogs = tmpListItems;
        }
    }
*    
*/    

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