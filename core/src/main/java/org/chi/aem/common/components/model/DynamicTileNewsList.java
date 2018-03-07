/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = DynamicTileNewsList.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class DynamicTileNewsList implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "chinational/components/composites/dynamicTilesNewsList";


    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicTileNewsList.class);
    /**
     * Name of the resource property storing the root page from which to build the list if the source of the list is <code>children</code>.
     *
     */
    String PN_PARENT_PAGE = "parentPage";

    private static final int FEATURED_LIMIT = 3;
    private static final int MAX_NEWS_LIST_LIMIT = 5;

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
    
	@Inject // injected as parameter 
	@Optional
	@Default(values="news")
	private String articleType; 

    @Self
    private SlingHttpServletRequest request;

    private int maxListItems = MAX_NEWS_LIST_LIMIT;

    private PageManager pageManager;
    
    // storing list of all news articles sorted by publishDate
    private java.util.List<Page> allNews;
    
    /* featuredNews - storing list of 3 featured or latest articles to be displayed as tiles
     */
    private java.util.List<Page> featuredNews;

    /* listNews - storing list of 5 news articles 
     * excluding featured article to be displayed as List, sorted by publish date
     */
    private java.util.List<Page> listNews;
    
    @PostConstruct
    private void initModel() {
    	// LOGGER.info("ArticleType: " + articleType);
    	allNews = new ArrayList<>();
    	listNews = new ArrayList<>();
        featuredNews = new ArrayList<>();
        
        pageManager = resourceResolver.adaptTo(PageManager.class);
        
        allNews = populateListItems(allNews); //to get all the news using defined template, sorted by Publish date
        featuredNews = populateListItems(featuredNews);  // extract featured articles, sorted by Publish date
        if(!featuredNews.isEmpty() && featuredNews.size() > FEATURED_LIMIT){
        	setMaxFeaturedNews(); //limit featured articles to 3.
        }
        listNews = populateListItems(listNews); //list of news sorted by Publish date, excluding FeaturedNews
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

    private java.util.List<Page> populateListItems(java.util.List<Page> list) {
        Map<String, String> map = new HashMap<String, String>();
        Object pageValue = properties.get(PN_PARENT_PAGE);
        LOGGER.info("populateListItems page property for parent page: " + pageValue);
        if (pageValue == null) {
            // same class is used by global (design_dialog) and regular component
            LOGGER.info("populateListItems page property for parent page is null, use style");
            pageValue = currentStyle.get(PN_PARENT_PAGE);
            LOGGER.info("populateListItems design dialog property for parent page: " + pageValue);
        }
        if (pageValue == null) {
            // no data
            return list;
        }
        map.put("path", pageValue.toString());
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        if(articleType.equals("news")){
        	map.put("property.value", "/apps/chinational/templates/newsdetailspage");
        } else {
        	map.put("property.value", "/apps/chinational/templates/blogsdetailspage");
        }
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");
        map.put("p.offset", "0");
        map.put("p.limit", "-1");
        if(list == featuredNews){
        	map.put("boolproperty", "jcr:content/isFeaturedArticle");
        	map.put("boolproperty.value", "true");
        }
        int i = 1;
        if(list == listNews){
            map.put("p.limit", String.valueOf(maxListItems));
        	for(Page item : featuredNews){
            	map.put(Integer.toString(i++)+"_excludepaths", item.getPath());
        	}
        }

        PredicateGroup group = PredicateGroup.create(map);
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(group, session);
                   
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
         
         // If no or less than 3 featured articles present, add latest article(s) as featured article(s).
         if(list == featuredNews && list.size() < 3){
        	 int pagesToAdd = 3 - list.size();
        	 for(int i = 0; i < pagesToAdd; i++){
        		 if(allNews.size() > i){
        			 list.add(allNews.get(i));
        		 }
        	 }
         }
         return list;
     }

    private void setMaxFeaturedNews() {
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