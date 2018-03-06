package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.search.PredicateConverter;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

public class SearchResult extends WCMUsePojo {

    protected static final String DEFAULT_SELECTOR = "chisearchresults";
    protected static final String PARAM_FULLTEXT = "fulltext";
    protected static final String SEARCH_RESULT_NODE_NAME = "searchResult";
    private static final String PARAM_RESULTS_OFFSET = "resultsOffset";
    private static final String PREDICATE_FULLTEXT = "fulltext";
    private static final String PREDICATE_TYPE = "type";
    private static final String PREDICATE_PATH = "path";
    private static final String NN_STRUCTURE = "structure";
    private static final String PN_RESULTS_SIZE = "resultsSize";
    private static final String PN_RESULTS_OFFSET = "resultOffset";
    private static final String PN_SEARCH_ROOT = "searchRoot";
    private static final String PN_SEARCH_TERM_MINIMUM_LENGTH = "searchTermMinimumLength";
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResult.class);

    private int DEFAULT_SEARCH_TERM_MINIM_LENGTH = 3;
    private int DEFAULT_RESULT_SIZE = 10;
    private int DEFAULT_RESULT_OFFSET = 0;
    private String DEFAULT_SEARCH_ROOT = "/content/chinational";

    public List<PageListItem> results;
    public int resultsOffset;
    public int totalNumberPages;

    @Override
    public void activate() throws Exception {
        Page currentPage = getCurrentPage();
        SlingHttpServletRequest request = getRequest();
        results = getSearchResults(request, currentPage);
    }

    private List<PageListItem> getSearchResults(SlingHttpServletRequest request, Page currentPage) {
        int searchTermMinimumLength = DEFAULT_SEARCH_TERM_MINIM_LENGTH;
        int resultsSize = DEFAULT_RESULT_SIZE;
        int resultsOffset = DEFAULT_RESULT_OFFSET;
        String searchRoot = DEFAULT_SEARCH_ROOT;
        List<PageListItem> results = new ArrayList<>();

            ValueMap valueMap = getProperties();

            searchTermMinimumLength = valueMap.get(PN_SEARCH_TERM_MINIMUM_LENGTH, DEFAULT_SEARCH_TERM_MINIM_LENGTH);
            resultsSize = valueMap.get(PN_RESULTS_SIZE, DEFAULT_RESULT_SIZE);
            resultsOffset = valueMap.get(PN_RESULTS_OFFSET, DEFAULT_RESULT_OFFSET);
            searchRoot = valueMap.get(PN_SEARCH_ROOT, DEFAULT_SEARCH_ROOT);


        String fulltext = request.getParameter(PARAM_FULLTEXT);
        if (fulltext == null || fulltext.length() < searchTermMinimumLength) {
            return results;
        }

        Map<String, String> predicatesMap = new HashMap<>();
        predicatesMap.put(PREDICATE_FULLTEXT, fulltext);
        predicatesMap.put(PREDICATE_PATH, searchRoot);
        predicatesMap.put(PREDICATE_TYPE, NameConstants.NT_PAGE);
        PredicateGroup predicates = PredicateConverter.createPredicates(predicatesMap);
        ResourceResolver resourceResolver = request.getResource().getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        try {
            QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
            Query query = queryBuilder.createQuery(predicates, session);

            if (resultsSize != 0) {
                query.setHitsPerPage(resultsSize);
            }

            com.day.cq.search.result.SearchResult searchResult = query.getResult();

            List<Hit> hits = searchResult.getHits();
            if (hits != null) {
                for (Hit hit : hits) {
                    try {
                        Resource hitRes = hit.getResource();
                        Page page = getPage(hitRes);
                        if (page != null) {
                            results.add(new PageListItem(request, page));
                        }
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

    public List<PageListItem> getResults() {
        return results;
    }

    public int getResultsOffset() {
        return resultsOffset;
    }

    public int getTotalNumberPages() {
        return totalNumberPages;
    }
}