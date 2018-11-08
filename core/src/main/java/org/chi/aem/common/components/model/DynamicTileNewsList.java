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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.chi.aem.common.utils.NewsBlogUtils;
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
import com.day.cq.tagging.TagConstants;

import static org.chi.aem.common.utils.NewsBlogUtils.getFeaturedArticleList;

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

    @Inject
    private ValueMap pageProperties;

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
        String currentPage = request.getRequestURI().replaceAll(".html", "");
        String pageTag = null;
        String tagFilterRequired = currentStyle.get("tagFilterRequired") != null ? currentStyle.get("tagFilterRequired").toString() : null;
        String pageTagFilterRequired = properties.get("tagFilterRequired") != null ? properties.get("tagFilterRequired").toString() : null;
        if((tagFilterRequired != null && tagFilterRequired.equalsIgnoreCase("true")) || (pageTagFilterRequired != null && pageTagFilterRequired.equalsIgnoreCase("true"))) {
            if(pageProperties.get(TagConstants.PN_TAGS) != null) {
                String[] tags = (String[]) pageProperties.get(TagConstants.PN_TAGS);
                for(String tag:tags) {
                    pageTag = tag;
                }
            }
        }
        allNews = populateListItems(allNews, currentPage, pageTag); //to get all the news using defined template, sorted by Publish date
        featuredNews = populateFeaturedItems(featuredNews, pageTag);  // extract featured articles, sorted by Publish date
        if(featuredNews != null && !featuredNews.isEmpty() && featuredNews.size() > FEATURED_LIMIT){
        	setMaxFeaturedNews(); //limit featured articles to 3.
        }
        listNews = populateListItems(listNews, currentPage, pageTag); //list of news sorted by Publish date, excluding FeaturedNews
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

    private java.util.List<Page> populateFeaturedItems(java.util.List<Page> list,
                                                   String pageTag) {

        String pageValue = getParentPage();
        if (pageValue == null) {
            // no data
            return list;
        }
        // update page tag and get last tag in the path
        if (pageTag != null && pageTag.lastIndexOf("/") > 0) {
            pageTag = pageTag.substring(pageTag.lastIndexOf("/")+1);
        }
        java.util.List<Page> featuredList = NewsBlogUtils.getFeaturedArticleList(resourceResolver, pageTag, pageValue);
        if (featuredList == null ) {
            LOGGER.debug("populateFeaturedItems: featured list is empty, send back input list");
            featuredList = list;
        }
        updateFeaturedList(featuredList);
        return featuredList;
    }

    private String getParentPage() {
        Object pageValue = properties.get(PN_PARENT_PAGE);
        // LOGGER.info("populateListItems page property for parent page: " + pageValue);
        if (pageValue == null) {
            // same class is used by global (design_dialog) and regular component
            // LOGGER.info("populateListItems page property for parent page is null, use style");
            pageValue = currentStyle.get(PN_PARENT_PAGE);
            // LOGGER.info("populateListItems design dialog property for parent page: " + pageValue);
        }
        if (pageValue != null){
            return pageValue.toString();
        }
        return null;
    }

    private java.util.List<Page> populateListItems(java.util.List<Page> list, String currentPage, String pageTag) {
        Map<String, String> map = new HashMap<String, String>();
        String pageValue = getParentPage();
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
        if(pageTag != null) {
            map.put("tagid", pageTag);
            map.put("tagid.property", "jcr:content/"+TagConstants.PN_TAGS);
        }
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");
        map.put("p.offset", "0");
        // TODO - why are we using limit = -1, shouldn't we get max 10 or so?
        map.put("p.limit", "-1");
        /*if(list == featuredNews){
        	map.put("boolproperty", "jcr:content/isFeaturedArticle");
        	map.put("boolproperty.value", "true");
        }*/
        int i = 1;
        if(list == listNews){
            map.put("p.limit", String.valueOf(maxListItems));
        	for(Page item : featuredNews){
            	map.put(Integer.toString(i++)+"_excludepaths", item.getPath());
        	}
        }
        //Excluding Current Page in Search Results
        map.put(Integer.toString(i++)+"_excludepaths", currentPage);
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
         if(list == featuredNews ){
             updateFeaturedList(list);
         }
         return list;
     }

    /**
     * Update feature list to display upto 3 items
     * @param list
     */
     private void updateFeaturedList(java.util.List<Page> list) {
         if (list != null ) {
             LOGGER.debug("updateFeaturedListis not null, size: " + list.size());
             if (list.size() < 3) {
                 int pagesToAdd = 3 - list.size();
                 for (int i = 0; i < pagesToAdd; i++) {
                     if (allNews.size() > i) {
                         if (featuredNews.contains(allNews.get(i))) {
                             pagesToAdd++;
                         } else {
                             list.add(allNews.get(i));
                         }
                     }
                 }
             }
         }
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