/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.chi.aem.common.utils.ResourceResolverFactoryService;
import org.chi.aem.common.utils.NewsBlogUtils;

import java.io.IOException;
import java.util.*;
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

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = NewsList.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class NewsList implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "chinational/components/content/newslist";

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsList.class);
    /**
     * Name of the resource property storing the root page from which to build the list 
     *
     */
    private static final String PN_PARENT_PAGE = "parentPage";
    private static final String NEWS_TEMPLATE = "/apps/chinational/templates/newsdetailspage";
    private static final String DEFAULT_NEWS_FILTER = "AllItems";
    private static final String DEFAULT_NEWS_FILTER_YEAR = "ChooseYear";
    private static final int HITS_PER_PAGE = 10;
    private static final int START_INDEX = 0;
    private static final int NEWS_FEATURED_LIMIT = 3;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private Resource resource;

    // @SlingObject
    // private ResourceResolver resourceResolver;

    @Inject
    ResourceResolverFactoryService resourceResolverFactoryService;

    @Self
    private SlingHttpServletRequest request;

    //@Reference    
    ResourceResolverFactory resourceResolverFactory;

    ResourceResolver resourceResolver;

    // storing list of all news articles sorted by publishDate
    private java.util.List<Page> allNews;

    // Storing selected featured articles
    private java.util.List<Page> featuredArticlesSelected;

    // Storing selected featured articles
    private java.util.List<Page> featuredArticlesSelectionList;

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

    /* featuredNews - storing list of featured news articles [Max 3]
     * for default tag
     * sorted by publish date
    */
    private java.util.List<Page> featuredNews;

    /*
    * Map of tag name to list of featured articles for the map
     */
    private Map<String, java.util.List<Page>> featuredMap;

    private Map<String, String> featuredMapList;

    // storing list of years of published articles
    private java.util.List<String> listYears;

    // storing list of all tags and tagID attached to news articles page template
    private java.util.List<String> listTags;
    // storing map of all tags name and title attached to news articles page template
    private Map<String, String> tagsMap = new HashMap<String, String>();

    // storing map of all tags title and desc attached to news articles page template
    private Map<String, String> tagsDescMap = new HashMap<String, String>();

    private Map<String, Object> articleMap = new HashMap<String, Object>();

    private int start_index;
    private int hits_per_page;
    private int totalResults; // used for hide-show "LOAD MORE" button
    private List<Integer> pages; //for pagination
    private int totalNumberPages; // for pagination
    private int activePage; // for pagination and for getting offset
    private String parentPage;
    private String newsTemplate;
    private String news_filter;
    private String tagDesc;
    private int newsFeaturedLimit;
    private String article_type;
    
    @PostConstruct
    private void initModel() {
        start_index = START_INDEX;
        hits_per_page = HITS_PER_PAGE;
        totalResults = 0;
        newsFeaturedLimit = NEWS_FEATURED_LIMIT;
        totalNumberPages = 1;
        activePage = 1;
        parentPage = properties.get(PN_PARENT_PAGE, currentPage.getPath()); // properties.get(PN_PARENT_PAGE, "/content/national/en/media/news"); //currentPage.getPath()
        newsTemplate = NEWS_TEMPLATE;
        news_filter = DEFAULT_NEWS_FILTER;
        tagDesc = ""; 
        article_type = "";
        featuredArticlesSelected = new ArrayList<>();
        featuredArticlesSelectionList = new ArrayList<>();
        allNews = new ArrayList<>();
        listNews = new ArrayList<>();
        featuredNews = new ArrayList<>();
    	allFilteredNews = new ArrayList<>();
        listYears = new ArrayList<>();
        listTags = new ArrayList<>();
        pages = new ArrayList<Integer> ();
//        LOGGER.info("Parent Page from newsList :: "+parentPage);
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
        String requestPathInfo = currentPage.getPath();

        if(selectors.length != 0) {
        	if(selectors[0].matches("[0-9]+")){
        		activePage = Integer.parseInt(selectors[0]);
        	} else {
        		article_type = selectors[0];
        	}
        }

        if(selectors.length >= 2 && selectors[0].equals("news")){
        	news_filter = selectors[1];
        } else {
        	news_filter = DEFAULT_NEWS_FILTER;
        }
        
        if(activePage != 1 && (activePage > 1)) {
            start_index = ((activePage - 1) * hits_per_page);
        } else {
            activePage = 1; //for Pagination active class when page load for the first time
        }


        allNews = NewsBlogUtils.populateListItems(parentPage, resourceResolver, newsTemplate, requestPathInfo); //to get all the news using defined template, sorted by Publish date
        articleMap = NewsBlogUtils.populateYearsTagsFeatured(parentPage, allNews, resourceResolver, news_filter, newsFeaturedLimit, requestPathInfo);
        listYears = (List<String>) articleMap.get("listYears");
        listTags = (List<String>) articleMap.get("listTags");
        tagsMap = (Map<String, String>) articleMap.get("tagsMap");
        tagsDescMap = (Map<String, String>) articleMap.get("tagsDescMap");
        featuredMap = (Map<String, List<Page>>)articleMap.get("featuredMap");
        // populate featuredMapList for front end
        populateFeaturedMapList();

        featuredNews = featuredMap.get(news_filter);
        allFilteredNews= (List<Page>) articleMap.get("filteredArticles");
        featuredArticlesSelected = featuredMap.get(news_filter);
        // get a copy, as allFileterd news may update
        featuredArticlesSelectionList.addAll(allFilteredNews);

        if (( featuredNews == null || featuredNews.size() == 0) && allFilteredNews.size() > 0 ) {
            LOGGER.debug("empty featured news, add default with latest");
            // add default value
            // TODO? - add default for each tag
            featuredNews = new ArrayList<>();
            featuredNews.add(allFilteredNews.get(0));
            // remove first item from allFilteredNews, as featured item has been added from the allFilteredNews
            allFilteredNews.remove(0);
        }

        if (featuredArticlesSelected != null) {
            for (Page item : featuredArticlesSelected) {
                if (featuredArticlesSelectionList.contains(item)) {
                    featuredArticlesSelectionList.remove(item);
                }
            }
            for(Page item : featuredArticlesSelected) {
                if(allFilteredNews.contains(item)){
                    allFilteredNews.remove(item);
                }
            }
        }

        if(tagsDescMap != null){
	        for (Entry<String,String> pair : tagsDescMap.entrySet()){
	            if(pair.getKey().equals(news_filter)){
	            	tagDesc = pair.getValue();
	            }
	        }
        }

		 LOGGER.debug("newslist filtered news : " + allFilteredNews.size());
        listNews = NewsBlogUtils.populateListArticles(start_index, hits_per_page, allFilteredNews); //list of news, sorted by Publish date

        // For AJAX Call - to get Total Results initially for LOAD MORE
        totalResults = allFilteredNews.size();

        // to get total no of pages and List of pages for data-sly-list for Pagination
        int total = (allNews.size())/hits_per_page;
        if(((allNews.size()) % hits_per_page) == 0){
            totalNumberPages = total;
        } else {
            totalNumberPages = total + 1;
        }

        for(int i = 1; i <= totalNumberPages; i++) {
            pages.add(i);
        }
   }

   private void populateFeaturedMapList() {
        if (featuredMap != null && featuredMap.size() > 0) {
            featuredMapList = new HashMap<>();
            for(String mapKey: featuredMap.keySet()) {
                List<Page> mappedPages = featuredMap.get(mapKey);
                if (mappedPages != null && mappedPages.size() > 0) {
                    StringBuffer bufferString = new StringBuffer();
                    for(Page mapped: mappedPages) {
                        if (mapped != null && mapped.getPath() != null) {
                            bufferString.append(mapped.getPath()).append(",");
                        }
                    }
                    if (bufferString.length() > 0) {
                        featuredMapList.put( mapKey , bufferString.substring(0, bufferString.length()-1));
                    }
                }
            }
        }
   }

    public Collection<Page> getAllNews() {
        return allNews;
    }

    public Collection<Page> getFeaturedArticlesSelected() {
        return featuredArticlesSelected;
    }

    public Collection<Page> getFeaturedArticlesSelectionList() { return featuredArticlesSelectionList; }

    @Nonnull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }

    public Collection<Page> getFeaturedNews() {
        return featuredNews;
    }

    public Map<String, String> getFeaturedMapList(){
        return featuredMapList;
    }

    public Collection<Page> getListNews() {
        return listNews;
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

    public String getNewsFilter() {
        return news_filter;
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
        return "news" + UUID.randomUUID();
    }

}