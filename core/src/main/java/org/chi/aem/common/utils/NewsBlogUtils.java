package org.chi.aem.common.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.jcr.*;

import com.adobe.acs.commons.mcp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

public final class NewsBlogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsBlogUtils.class);
    public static final String DEFAULT_NEWS_FILTER = "AllItems";
    private static final String DEFAULT_NEWS_FILTER_YEAR = "ChooseYear";
    public static final String FEATURED_NODE = "featuredArticles";

    public static java.util.List<Page> populateListItems(String parentPage, ResourceResolver resourceResolver, String articlesTemplate, String requestPathInfo) {

        java.util.List<Page> list = new ArrayList<>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", parentPage);
        map.put("type", "cq:Page");
        map.put("property", "jcr:content/cq:template");
        map.put("property.value", articlesTemplate);
        map.put("orderby", "@jcr:content/publishDate");
        map.put("orderby.sort", "desc");
        map.put("p.guessTotal", "true");
        map.put("p.offset", String.valueOf(0)); // same as query.setStart(0) below
        map.put("p.limit", String.valueOf(-1)); // same as query.setHitsPerPage(20) below

        PredicateGroup group = PredicateGroup.create(map);
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(group, session);

        try {
            SearchResult result = query.getResult();
            LOGGER.debug("result.getTotalMatchees() : " + list + " : " + result.getTotalMatches());

            list = collectSearchResults(query.getResult(), list, resourceResolver);
        } catch (RepositoryException e) {
            LOGGER.error("Unable to retrieve search results for query.", e);
        }
        return list;
    }

    public static java.util.List<Page> collectSearchResults(SearchResult result, java.util.List<Page> list, ResourceResolver resourceResolver) throws RepositoryException {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        for (Hit hit : result.getHits()) {
            Page containingPage = pageManager.getContainingPage(hit.getResource());
            if (containingPage != null) {
                list.add(containingPage);
            }
        }

        return list;
    }

    public static Map<String, Object> populateYearsTagsFeatured(String parentPage, java.util.List<Page> allArticles, ResourceResolver resourceResolver, String articleFilter, int featured_limit, String requestPathInfo) {

        Map<String, Object> m_article = new HashMap<String, Object>();
        java.util.List<Page> filteredArticles = new ArrayList<>();
        Map<String, java.util.List<Page>> featuredMap = new HashMap<String, java.util.List<Page>>();
        java.util.List<String> listYears = new ArrayList<>();
        java.util.List<String> listTags = new ArrayList<>();
        Map<String, String> tagsMap = new HashMap<String, String>();
        Map<String, String> tagsDescMap = new HashMap<String, String>();

        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        for (Page item : allArticles) {

            // to get listYears
            Calendar date = item.getProperties().get("publishDate", Calendar.class);
            //int year = date.get(Calendar.YEAR);
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY");
            String year = formatter.format(date.getTime()).toUpperCase();

            // to get tags
            Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
            if (tags.length != 0) {
                for (Tag tag : tags) {
                    String tagName = tag.getTitle();
                    if (listTags.isEmpty()) {
                        listTags.add(tagName);
                        tagsMap.put(tag.getName(), tagName);
                        tagsDescMap.put(tag.getName(), tag.getDescription());
                    } else if (!listTags.contains(tagName)) {
                        listTags.add(tagName);
                        tagsMap.put(tag.getName(), tagName);
                        tagsDescMap.put(tag.getName(), tag.getDescription());
                    }

                    if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
                        addYear(listYears, year);
                    } else if (articleFilter.equals(tag.getName())) {
                        LOGGER.debug("Article Filter :: " + articleFilter);
                        LOGGER.debug("Tag Filter :: " + tag.getName());
                        addYear(listYears, year);
                        filteredArticles.add(item);
                    }
                }
            } else if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
                addYear(listYears, year);
            }
        }
        // TODO - check where this needs to be executed
        featuredMap = getFeaturedArticles(tagsMap.keySet(), resourceResolver, articleFilter, requestPathInfo);

        LOGGER.debug("newsblogUtils after for loop - filteredArticles Size : " + filteredArticles.size());

        // If article filter is allNews, return all articles list
        if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
            // filteredArticles = allArticles;
            filteredArticles.clear();
            filteredArticles.addAll(allArticles);
        }

        // to handle the use case when we have two BlogList component on the same page
        // and both have different parent page and different tags associated with it
        // Users selects a tag from dropdown in one BlogList component
        // and tag is not there in another Bloglist component dropdown on the same page
//         LOGGER.info("Article filter :: "+articleFilter);
//         LOGGER.info("Article default filter :: "+DEFAULT_NEWS_FILTER);
        //if (!tagsMap.keySet().contains(articleFilter) && !articleFilter.equals(DEFAULT_NEWS_FILTER)) {
            // TODO - what is the purpose of this self call again?
       //     LOGGER.info("Contains article filter ::::::::::::");
        //    populateYearsTagsFeatured(parentPage, allArticles, resourceResolver, DEFAULT_NEWS_FILTER, featured_limit, requestPathInfo);
        //}

        // If no featured article present, add the latest article as featured article.
//         if(featuredArticles.isEmpty() && filteredArticles.size() > 0){
////         	LOGGER.info("Articles are empty");
////         	LOGGER.info("Tags list :: "+tagName);
//			 LOGGER.info("Filtered list is empty :: ");
////         	featuredArticles.add(filteredArticles.get(0));
//			getFeaturedArticles(parentPage, resourceResolver, articleFilter);
//         }
        // Commented out by sbrown - prokarma

        // LOGGER.info("newsblogUtils end filteredArticles Size : " + filteredArticles.size());

        // populate map
        m_article.put("listYears", listYears);
        m_article.put("listTags", listTags);
        m_article.put("tagsMap", tagsMap);
        m_article.put("tagsDescMap", tagsDescMap);
        //m_article.put("featuredArticles", featuredArticles);
        m_article.put("featuredMap", featuredMap);
        m_article.put("filteredArticles", filteredArticles);

        return m_article;
    }


    public static Map<String, Object>
                        populateArticlesForPagination(String parentPage,
                                                      java.util.List<Page> allArticles,
                                                      ResourceResolver resourceResolver,
                                                      String articleFilter, String filterYear) {

        java.util.List<Page> filteredArticles = new ArrayList<>();
        Map<String, Object> m_article = new HashMap<String, Object>();
        Set<String> filterSet = new HashSet<>();
        filterSet.add(articleFilter);
        Map<String, java.util.List<Page>> featuredMap = getFeaturedArticles(filterSet, resourceResolver, articleFilter, parentPage);
        m_article.put("featuredMap", featuredMap);
        m_article.put("filteredArticles", filteredArticles);
        return m_article;
    }



    public static void addYear(java.util.List<String> listYears, String year) {
        if (listYears.isEmpty()) {
            listYears.add(year);
        } else if (!listYears.contains(year)) {
            listYears.add(year);
        }
    }

    public static java.util.List<Page> populateListArticles(int start_index, int hits_per_page, java.util.List<Page> allArticles) {
        java.util.List<Page> listArticles = new ArrayList<>();
        int end_index = start_index + hits_per_page;
        for (int i = start_index; i < end_index; i++) {
            if (allArticles.size() > i) {
                listArticles.add(allArticles.get(i));
            } else {
                break;
            }
        }

        return listArticles;
    }

    public static java.util.List<Page> getFeaturedArticleList( ResourceResolver resourceResolver,
                                                                         String articleFilter,
                                                                         String requestPathInfo) {
        java.util.List<Page> featuredList = new ArrayList<>();
        try {
            if (articleFilter == null) {
                articleFilter = DEFAULT_NEWS_FILTER;
            }
            String featuredPagesPath = requestPathInfo + "/jcr:content/" + FEATURED_NODE;
            LOGGER.debug("featuredPagesPath  :: " + featuredPagesPath + ", article filter: " + articleFilter);
            Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
            if (featuredPagesRes != null) {
                LOGGER.debug("featuredPagesPath resourceavailable, get featured items");
                Node featuredNode = featuredPagesRes.adaptTo(Node.class);
                featuredList = getFeaturedListItems(articleFilter, featuredNode, resourceResolver);
            }
            // TODO - where to add default one, if no featured article is available

        } catch (Exception e) {
            LOGGER.error("Could not get featured pages :: " + e);
            e.printStackTrace();
        }
        return featuredList;
    }


    public static Map<String, java.util.List<Page>> getFeaturedArticles( Set<String> tagSet,
                                                                        ResourceResolver resourceResolver,
                                                                        String articleFilter,
                                                                        String requestPathInfo) {
        Map<String, java.util.List<Page>> featuredMap = new HashMap<String, java.util.List<Page>>();
        try {

            String featuredPagesPath = requestPathInfo + "/jcr:content/" + FEATURED_NODE;
            LOGGER.debug("featuredPagesPath  :: " + featuredPagesPath + ", article filter: " + articleFilter);
            Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
            if (featuredPagesRes != null) {
                Node featuredNode = featuredPagesRes.adaptTo(Node.class);
                boolean addedAllItems = false;
                // get map for all tags
                for(String tagForItem: tagSet) {
                    populateFeaturedItems(tagForItem, featuredMap, featuredNode, resourceResolver);
                    if (tagForItem.equals(DEFAULT_NEWS_FILTER)) {
                        addedAllItems = true;
                    }
                }
                if (!addedAllItems) {
                    // add for allItems as well
                    populateFeaturedItems(DEFAULT_NEWS_FILTER, featuredMap, featuredNode, resourceResolver);
                }

            }
            // TODO - where to add default one, if no featured article is available

        } catch (Exception e) {
            LOGGER.error("Could not get featured pages :: " + e);
            e.printStackTrace();
        }
        return featuredMap;
    }

    private static java.util.List<Page> getFeaturedListItems(String tagName,
                                                             Node featuredNode,
                                                             ResourceResolver resourceResolver)
            throws RepositoryException{
        if (featuredNode != null && featuredNode.hasProperties()) {
            Value[] featuredList = null;
            if (featuredNode.hasProperty(tagName)) {
                LOGGER.debug("getFeaturedListItems has values for tag: " + tagName);
                featuredList = featuredNode.getProperty(tagName).getValues();
            }
            if (featuredList != null) {
                List<Page> featuredArticles = new ArrayList<>();
                for (Value fl : featuredList) {
                    String spnPath = fl.getString();
                    if (StringUtils.isNotEmpty(spnPath)) {
                        LOGGER.debug("Featured article path: " + spnPath);
                        Resource flRes = resourceResolver.getResource(spnPath);
                        Page flPage = flRes.adaptTo(Page.class);
                        featuredArticles.add(flPage);
                    }
                }
                return featuredArticles;
            }
        }
        return null;
    }

    private static void populateFeaturedItems(String tagForItem,
                                              Map<String, java.util.List<Page>> featuredMap,
                                              Node featuredNode,
                                              ResourceResolver resourceResolver) throws RepositoryException {
        LOGGER.debug("list of tags  :: " + tagForItem);
        List<Page> featuredList = getFeaturedListItems(tagForItem, featuredNode, resourceResolver);
        if (featuredList != null) {
            featuredMap.put(tagForItem, featuredList);
        }
    }


}

