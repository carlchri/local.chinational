/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.chi.aem.common.utils.ResourceResolverFactoryService;
import org.chi.aem.common.utils.NewsBlogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolverFactory ;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;

import com.day.cq.wcm.api.Page;
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
    private static final String PN_PARENT_PAGE = "parentPage";
    private static final String BLOGS_TEMPLATE = "/apps/chinational/templates/blogsdetailspage";
    private static final String DEFAULT_BLOGS_FILTER = "AllItems";
    private static final String DEFAULT_BLOGS_FILTER_YEAR = "ChooseYear";
    private static final int HITS_PER_PAGE = 6;
    private static final int START_INDEX = 0;
    private static final int BLOGS_FEATURED_LIMIT = 2;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    // @SlingObject
    // private ResourceResolver resourceResolver;
    
    @Inject
    ResourceResolverFactoryService resourceResolverFactoryService;

    @SlingObject
    private Resource resource;

    @Self
    private SlingHttpServletRequest request;

    // @Reference    
    ResourceResolverFactory resourceResolverFactory;

    ResourceResolver resourceResolver;

    // storing list of all blogs articles sorted by publishDate
    private java.util.List<Page> allBlogs;

    // Storing selected featured articles
    private java.util.List<Page> featuredArticlesSelected;

    // Storing selected featured articles
    private java.util.List<Page> featuredArticlesSelectionList;
    
    /* allFilteredBlogs - storing list of all blogs articles 
     * filtered based on selection in the dropdown
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

    /*
     * Map of tag name to list of featured articles for the map
     */
    private Map<String, java.util.List<Page>> featuredMap;

    // storing list of years of published articles
    private java.util.List<String> listYears;
    
    // storing list of all tags and tagID attached to blogs articles page template
    private java.util.List<String> listTags;
    // storing map of all tags name and title attached to blogs articles page template
    private Map<String, String> tagsMap = new HashMap<String, String>();
    
    // storing map of all tags title and desc attached to blogs articles page template
    private Map<String, String> tagsDescMap = new HashMap<String, String>();

    private Map<String, Object> articleMap = new HashMap<String, Object>();

    private int start_index;
    private int hits_per_page;
    private int totalResults; // used for hide-show "LOAD MORE" button
    private List<Integer> pages; //for pagination
    private int totalNumberPages; // for pagination
    private int activePage; // for pagination and for getting offset
    private String parentPage;
    private String blogsTemplate;
    private String blogs_filter;
    private String tagDesc;
    private int blogsFeaturedLimit;
    private String article_type;

    @PostConstruct
    private void initModel() {
        start_index = START_INDEX;
        hits_per_page = HITS_PER_PAGE;
        blogsFeaturedLimit = BLOGS_FEATURED_LIMIT;
        totalResults = 0;
        totalNumberPages = 1;
        activePage = 1;
        parentPage = properties.get(PN_PARENT_PAGE, currentPage.getPath());
        blogsTemplate = BLOGS_TEMPLATE;
        blogs_filter = DEFAULT_BLOGS_FILTER;
        tagDesc = ""; 
        article_type = "";
        featuredArticlesSelected = new ArrayList<>();
        featuredArticlesSelectionList = new ArrayList<>();
    	allBlogs = new ArrayList<>();
    	allFilteredBlogs = new ArrayList<>();
    	listBlogs = new ArrayList<>();
        featuredBlogs = new ArrayList<>();
        listYears = new ArrayList<>();
        listTags = new ArrayList<>();
        pages = new ArrayList<Integer> ();
       
        Map<String, Object> param = new HashMap<String, Object>();             
        param.put(ResourceResolverFactory.SUBSERVICE, "tagManagement");
        
        try {
            // ResourceResolverFactoryService = getSlingScriptHelper().getService(ResourceResolverFactoryService.class);
            resourceResolverFactory = resourceResolverFactoryService.getResourceResolverFactory();
        	resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
        }
        catch(Exception e)
        {
         LOGGER.info("Exception to get resource resolver.");
         e.printStackTrace();
        }

        String[] selectors = request.getRequestPathInfo().getSelectors();
        if(selectors.length != 0) {
        	if(selectors[0].matches("[0-9]+")){
        		activePage = Integer.parseInt(selectors[0]);
        	} else {
        		article_type = selectors[0];
        	}
        }

        String requestPathInfo = currentPage.getPath();
//        LOGGER.info("Request path info Blogs List :: "+requestPathInfo);

        if(selectors.length >= 2 && selectors[0].equals("blogs")){
        	blogs_filter = selectors[1];
        } else {
        	blogs_filter = DEFAULT_BLOGS_FILTER;
        }
        
        if(activePage != 1 && (activePage > 1)) {
            start_index = ((activePage - 1) * hits_per_page);
        } else {
        	activePage = 1; //for Pagination active class when page load for the first time
        }


		// LOGGER.info("blogslist parent page : " + parentPage);
		// LOGGER.info("blogslist blogs_filter : " + blogs_filter);
        allBlogs = NewsBlogUtils.populateListItems(parentPage, resourceResolver, blogsTemplate, requestPathInfo); //to get all the blogs using defined template, sorted by Publish date
		// LOGGER.info("blogslist allBlogsSize : " + allBlogs.size());
        articleMap = NewsBlogUtils.populateYearsTagsFeatured(parentPage, allBlogs, resourceResolver, blogs_filter, blogsFeaturedLimit, requestPathInfo);
        listYears = (List<String>) articleMap.get("listYears");
        listTags = (List<String>) articleMap.get("listTags");
        tagsMap = (Map<String, String>) articleMap.get("tagsMap");
        tagsDescMap = (Map<String, String>) articleMap.get("tagsDescMap");
        //featuredBlogs = (List<Page>) articleMap.get("featuredArticles");
        featuredMap = (Map<String, List<Page>>)articleMap.get("featuredMap");
        featuredBlogs = featuredMap.get(NewsBlogUtils.DEFAULT_NEWS_FILTER);
        allFilteredBlogs= (List<Page>) articleMap.get("filteredArticles");

        if (( featuredBlogs == null || featuredBlogs.size() == 0) && allFilteredBlogs.size() > 0 ) {
            // add default value
            // TODO - add default for each tag
            featuredBlogs.add(allFilteredBlogs.get(0));
        }

        featuredArticlesSelected = featuredMap.get(NewsBlogUtils.DEFAULT_NEWS_FILTER);
        featuredArticlesSelectionList = allBlogs;

        if (featuredBlogs.size() == 0) {
            featuredBlogs.add(allFilteredBlogs.get(0));
        }

        for(Page item : featuredArticlesSelected) {
            if (featuredArticlesSelectionList.contains(item)) {
//                LOGGER.info(":: HAS ITEM ::");
                allBlogs.remove(item);
            }
        }


                if(tagsDescMap != null){
	        for (Entry<String,String> pair : tagsDescMap.entrySet()){
	            if(pair.getKey().equals(blogs_filter)){
	            	tagDesc = pair.getValue();
	            }
	        }
        }
        
    	for(Page item : featuredArticlesSelected) {
			 if(allFilteredBlogs.contains(item)){
				 allFilteredBlogs.remove(item);
			 } 
		 }	 

	   	// LOGGER.info("blogslist filtered blogs : " + allFilteredBlogs.size());
	   	
	    listBlogs = NewsBlogUtils.populateListArticles(start_index, hits_per_page, allFilteredBlogs); //list of blogs, sorted by Publish date
        
		// For AJAX Call - to get Total Results initially for LOAD MORE
        totalResults = allFilteredBlogs.size();
        
        // to get total no of pages and List of pages for data-sly-list for Pagination
    	int total = (allBlogs.size())/hits_per_page;
    	if(((allBlogs.size()) % hits_per_page) == 0){
    		totalNumberPages = total;
    	} else {
    		totalNumberPages = total + 1;
    	}

        for(int i = 1; i <= totalNumberPages; i++) {
            pages.add(i);
        }
    }

    public Collection<Page> getAllBlogs() {
        return allBlogs;
    }

    public Collection<Page> getFeaturedArticlesSelected() {
        return featuredArticlesSelected;
    }

    public  Collection<Page> geFeaturedArticlesSelectionList() { return featuredArticlesSelectionList; }

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
    
    public Map<String, String> getTagsDescMap() {
        return tagsDescMap;
    }

    public String getTagsDesc() {
        return tagDesc;
    }

    public Collection<String> getListTags() {
        return listTags;
    }
    
    public String getBlogsFilter() {
        return blogs_filter;
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

    public String getHash() {
        return "blogs" + UUID.randomUUID();
    }

}