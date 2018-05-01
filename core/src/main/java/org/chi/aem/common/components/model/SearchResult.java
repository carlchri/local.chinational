package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.search.PredicateConverter;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

public class SearchResult extends WCMUsePojo {

    protected static final String PARAM_FULLTEXT = "fulltext";
    private static final String PARAM_PAGE = "page";
    private static final String PREDICATE_FULLTEXT = "fulltext";
    private static final String PN_RESULTS_SIZE = "resultsSize";
    private static final String PN_SEARCH_TERM_MINIMUM_LENGTH = "searchTermMinimumLength";
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResult.class);
    private static final int DEFAULT_SEARCH_TERM_MINIM_LENGTH = 3;
    private static final int DEFAULT_RESULT_SIZE = 10;
    private static final int DEFAULT_RESULT_OFFSET = 0;
    private static final String DEFAULT_SEARCH_ROOT = "/content/national";
    private static final String DEFAULT_RESULT_TYPE = "cq:Page";
    private static final String FIRST_GROUP = "1_group.";
    private static final String GROUP_PATH = "_group.path";
    private static final String SECOND_GROUP = "2_group.";
    private static final String GROUP_TYPE = "_group.type";
    private static final String P_OR = "p.or";
    private static final String PRO_NOINDEX = "noindex";
    public List<PageListItem> results;
    public List<String> pages;
    public int resultsOffset;
    public int totalNumberPages;
    public int currentPageNumber = 1;
    public int totalNumberOfResult;
    public String fulltext;
    public List<String> searchRoots;
    public List<String> resultTypes;

    @Override
    public void activate(){
        Page currentPage = getCurrentPage();
        pages = new ArrayList<String> ();
        SlingHttpServletRequest request = getRequest();
        results = getSearchResults(request, currentPage);
    }

    private List<PageListItem> getSearchResults(SlingHttpServletRequest request, Page currentPage) {
        int searchTermMinimumLength = DEFAULT_SEARCH_TERM_MINIM_LENGTH;
        int resultsSize = DEFAULT_RESULT_SIZE;
        int resultsOffset = DEFAULT_RESULT_OFFSET;
        List<PageListItem> results = new ArrayList<>();

        ValueMap valueMap = getProperties();

        searchTermMinimumLength = valueMap.get(PN_SEARCH_TERM_MINIMUM_LENGTH, DEFAULT_SEARCH_TERM_MINIM_LENGTH);
        resultsSize = valueMap.get(PN_RESULTS_SIZE, DEFAULT_RESULT_SIZE);
        LOGGER.debug("getSearchResults resultsSize - " + resultsSize);

        Resource resource = getResource();
        if(resource.hasChildren()) {
            Resource searchRootsR = resource.getChild("searchRoots");
            if(searchRootsR != null) {
                searchRoots = getMultiProperties(searchRootsR, "searchRoot", DEFAULT_SEARCH_ROOT);
            }
            Resource resultTypesR = resource.getChild("resultTypes");
            if(resultTypesR != null) {
                resultTypes = getMultiProperties(resultTypesR,  "resultType", DEFAULT_RESULT_TYPE);
            }
        }

        String pageString = request.getParameter(PARAM_PAGE);
        if(pageString != null && !pageString.equalsIgnoreCase("1")) {
            currentPageNumber = Integer.parseInt(pageString);
            resultsOffset = (currentPageNumber - 1) * resultsSize;
            LOGGER.info("getSearchResults currentPageNumber - " + currentPageNumber);
            LOGGER.info("getSearchResults resultsOffset - " + resultsOffset);
        }

        fulltext = request.getParameter(PARAM_FULLTEXT);
        if (fulltext == null || fulltext.length() < searchTermMinimumLength) {
            return results;
        }

        Map<String, String> predicatesMap = new HashMap<>();
        for(int i = 1; i <= searchRoots.size(); i++) {
            predicatesMap.put(FIRST_GROUP + i +  GROUP_PATH, searchRoots.get(i-1));
        }
        predicatesMap.put(FIRST_GROUP + P_OR, "true");
        for(int i = 1; i <= resultTypes.size(); i++) {
            predicatesMap.put(SECOND_GROUP + i +  GROUP_TYPE, resultTypes.get(i-1));
        }
        predicatesMap.put(SECOND_GROUP + P_OR, "true");
        predicatesMap.put(PREDICATE_FULLTEXT, fulltext);

        // hide results based on a property
        //map.put("boolproperty", "jcr:content/hideInNav");
        //map.put("boolproperty.value", "false");
        predicatesMap.put("boolproperty", "jcr:content/noindex");
        predicatesMap.put("boolproperty.value", "false");

        PredicateGroup predicates = PredicateConverter.createPredicates(predicatesMap);
        ResourceResolver resourceResolver = request.getResource().getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        try {
            QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
            Query query = queryBuilder.createQuery(predicates, session);

            if (resultsSize != 0) {
                query.setHitsPerPage(resultsSize);
            }

            if (resultsOffset != 0) {
                query.setStart(resultsOffset);
            }

            com.day.cq.search.result.SearchResult searchResult = query.getResult();

            totalNumberOfResult = (int) searchResult.getTotalMatches();
            LOGGER.info("getSearchResults totalNumberOfResult - " + totalNumberOfResult);
            int remainder = totalNumberOfResult % resultsSize;
            if (remainder == 0) {
                // if remainder is zero, we only need one extra page to from 1 to n, rather than 0 to n-1
                totalNumberPages = totalNumberOfResult/resultsSize + 1;
            } else {
                totalNumberPages = totalNumberOfResult/resultsSize + 2;
            }
            LOGGER.info("getSearchResults totalNumberPages - " + totalNumberPages);

            for(int i = 1; i < totalNumberPages; i++) {
                pages.add(Integer.toString(i));
            }
            List<Hit> hits = searchResult.getHits();
            if (hits != null) {
                for (Hit hit : hits) {
                    try {
                        Resource hitRes = hit.getResource();
                        results.add(new PageListItem(request, hitRes));
                    } catch (RepositoryException e) {
                        LOGGER.error("Unable to retrieve search results for query.", e);
                    }
                }
            }
            //convert the List into a Set
            Set<PageListItem> set = new HashSet<PageListItem>(results);
            //create a new List from the Set
            results = new ArrayList<PageListItem>(set);
        }catch(Exception e) {
            LOGGER.error(e.getMessage());
        }
        return results;
    }

    // No need for this code, as we are using this in query directly
    /*
    private boolean isNoIndex(Resource resource) {
        Page page = getPage(resource);
        if(page != null) {
            ValueMap properties = page.getProperties();
            if(properties.containsKey(PRO_NOINDEX) && properties.get(PRO_NOINDEX, Boolean.class).booleanValue() == true) {
                return true;
            }
        }
        return false;
    }*/

    private Page getPage(Resource resource) {
        if (resource != null) {
            ResourceResolver resourceResolver = resource.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                return pageManager.getContainingPage(resource);
            }
        }
        return null;
    }

    private List<String> getMultiProperties(Resource r, String propertyName, String defaultValue) {
        ValueMap valueMap = null;
        List<String> propertyValues = new ArrayList<String>();
        if(r.hasChildren()) {
            Iterable<Resource> itemRs = r.getChildren();
            for(Resource item : itemRs) {
                valueMap = item.getValueMap();
                propertyValues.add(valueMap.get(propertyName, defaultValue));
            }
        }
        if(propertyValues.isEmpty()) {
            propertyValues.add(defaultValue);
        }
        return propertyValues;
    }

    public List<PageListItem> getResults() {
        return results;
    }

    public int getResultsOffset() {
        return resultsOffset;
    }

    public int getTotalNumberPages() {
        return totalNumberPages;
    }

    public int getTotalNumberOfResults() { return totalNumberOfResult; }

    public int getCurrentPageNumber() { return currentPageNumber; }

    public String getFulltext() {
        return fulltext;
    }
}