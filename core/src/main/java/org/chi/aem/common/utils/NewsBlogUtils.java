package org.chi.aem.common.utils;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.jcr.*;

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

    //	private static String articleFilter = "noArticle";
    //private java.util.List<Page> featuredSelectedArticles = new ArrayList<>();
    //private static final java.util.List<Page> featuredArticles = new ArrayList<>();
    // store featured articles for each tag




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

        // query.setStart(start_index);
        // query.setHitsPerPage(hits_per_page);
//        getFeaturedArticles(parentPage, resourceResolver, DEFAULT_NEWS_FILTER, requestPathInfo);
        try {
            SearchResult result = query.getResult();
            // LOGGER.info("result.getTotalMatchees() : " + list + " : " + result.getTotalMatches());

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

//        LOGGER.info("newsblogUtils start filteredArticles Size : " + filteredArticles.size());
//         LOGGER.info("Pagent Page :: "+parentPage);
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

//    		String[] itemsTags = item.getProperties().get("cq:tags", String[].class);
//    		if (itemsTags.length != 0) {
//                for (String it : itemsTags) {
//                    LOGGER.info("Items Tags :: " + it);
//                }
//            }
            // to get tags
            Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
//            LOGGER.info("tags: " + tags.length );
            if (tags.length != 0) {
                for (Tag tag : tags) {
                    String tagName = tag.getTitle();

//	           		    LOGGER.info("tagTitle: " + tag.getTitle());
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
//							LOGGER.info("Article Filter :: "+articleFilter);
                        addYear(listYears, year);
//		                	addFeaturedArticle(item, featured_limit);
                        //getFeaturedArticles(parentPage, resourceResolver, articleFilter, requestPathInfo);
                    } else if (articleFilter.equals(tag.getName())) {
                        LOGGER.debug("Article Filter :: " + articleFilter);
                        LOGGER.debug("Tag Filter :: " + tag.getName());
                        addYear(listYears, year);
                        filteredArticles.add(item);
//		                	addFeaturedArticle(item, featured_limit);

                    }
                }
            } else if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
                addYear(listYears, year);
//            	addFeaturedArticle(item, featured_limit);
                //getFeaturedArticles(parentPage, resourceResolver, articleFilter, requestPathInfo);
            }
        }
        // TODO - check where this needs to be executed
        featuredMap = getFeaturedArticles(tagsMap.keySet(), resourceResolver, articleFilter, requestPathInfo);

        LOGGER.info("newsblogUtils after for loop - filteredArticles Size : " + filteredArticles.size());

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

        // LOGGER.info("newsblogUtils servlet start filteredArticles Size : " + filteredArticles.size());
        java.util.List<Page> filteredArticles = new ArrayList<>();
        Map<String, Object> m_article = new HashMap<String, Object>();
        /*
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        for (Page item : allArticles) {
            // to get Year of article
            Calendar date = item.getProperties().get("publishDate", Calendar.class);
            //int year = date.get(Calendar.YEAR);
            SimpleDateFormat formatter = new SimpleDateFormat("YYYY");
            String year = formatter.format(date.getTime()).toUpperCase();

            if (filterYear.equals(year) || filterYear.equals(DEFAULT_NEWS_FILTER_YEAR)) {
                // to get tags
                Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
                LOGGER.debug("tags: " + tags);
                if (tags.length != 0) {
                    for (Tag tag : tags) {
                        if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
//		         				addFilteredArticles(item);
                            getFeaturedArticles(parentPage, resourceResolver, articleFilter, parentPage);
                        } else if (articleFilter.equals(tag.getName())) {
                            LOGGER.debug("INSIDE TAG FILTER");
//			         			addFilteredArticles(item);
                            getFeaturedArticles(parentPage, resourceResolver, articleFilter, parentPage);
                        }

                    }
                } else if (articleFilter.equals(DEFAULT_NEWS_FILTER)) {
//	            	addFilteredArticles(item);

                }
            }

        }
        */
        Set<String> filterSet = new HashSet<>();
        filterSet.add(articleFilter);
        Map<String, java.util.List<Page>> featuredMap = getFeaturedArticles(filterSet, resourceResolver, articleFilter, parentPage);
        // LOGGER.info("newsblogUtils servlet end filteredArticles Size : " + filteredArticles.size());

        // populate map
        //m_article.put("featuredArticles", featuredArticles);
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

    /*public static void addFilteredArticles(Page item) {
        if (filteredArticles.isEmpty()) {
            filteredArticles.add(item);
        } else if (!filteredArticles.contains(item)) {
            filteredArticles.add(item);
        }
    }*/

    //public static void addFeaturedArticle(Page item, int limit) {
//         boolean isFeatured = item.getProperties().get("isFeaturedArticle",false);
//         if(featuredArticles.size() < limit && isFeatured && !featuredArticles.contains(item)){
//          	featuredArticles.add(item);
//	      }
    //}

    /*
    public static void addFeaturedArticles(java.util.List<Page> articles, int limit) {
        for (Page item : articles) {
            boolean isFeatured = item.getProperties().get("isFeaturedArticle", false);
            if (featuredArticles.size() < limit && isFeatured && !featuredArticles.contains(item)) {
                featuredArticles.add(item);
            }
        }
    }*/

    /*
    public static boolean isFeatured(Page item) {
        boolean isFeatured = item.getProperties().get("isFeaturedArticle", false);
        return isFeatured;
    }*/

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
            LOGGER.info("featuredPagesPath  :: " + featuredPagesPath + ", article filter: " + articleFilter);
            Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
            if (featuredPagesRes != null) {
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
            LOGGER.info("featuredPagesPath  :: " + featuredPagesPath + ", article filter: " + articleFilter);
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
                featuredList = featuredNode.getProperty(tagName).getValues();
            }
            if (featuredList != null) {
                List<Page> featuredArticles = new ArrayList<>();
                for (Value fl : featuredList) {
                    String spnPath = fl.getString();
                    Resource flRes = resourceResolver.getResource(spnPath);
                    Page flPage = flRes.adaptTo(Page.class);
                    featuredArticles.add(flPage);
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
        LOGGER.info("list of tags  :: " + tagForItem);
        List<Page> featuredList = getFeaturedListItems(tagForItem, featuredNode, resourceResolver);
        if (featuredList != null) {
            featuredMap.put(tagForItem, featuredList);
        }
    }

    /*
    public static java.util.List<Page> populateSelectedItems(String parentPage, ResourceResolver resourceResolver, String currentPagePath) {
//         LOGGER.info("Selected pages Started!");
        try {
            // get featured pages
            String featuredTag;
//            String featuredPagesPath = currentPagePath + "/jcr:content";
//            Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
//            ValueMap featuredPagesMap = featuredPagesRes.adaptTo(ValueMap.class);

            String currentPagesJCRPath = currentPagePath + "/jcr:content";
            Resource curentPageRes = resourceResolver.getResource(currentPagesJCRPath);
            ValueMap currentPagesMap = curentPageRes.adaptTo(ValueMap.class);
            Node currentPageNode = curentPageRes.adaptTo(Node.class);
//            ModifiableValueMap currentPathMap = curentPageRes.adaptTo(ModifiableValueMap.class);

            if (!currentPageNode.hasProperty("featuredPagesTag")) {

//             LOGGER.info("Featured tag null!!!! " + featuredSelectedArticles.size() +" and " + filteredArticles.size());
//             featuredSelectedArticles.add(filteredArticles.get(0));
//             LOGGER.info("Filtered articles added!!!!!");
//             LOGGER.info("Featured tag string is null!");
//                featuredTag = "FTAllItems";
//                if (parentPage.contains("Blogs")) {
//                    featuredTag = "FTAllItems";
//                }

            } else {
                String featuredTagString = currentPagesMap.get("featuredPagesTag", String.class);
                featuredTag = featuredTagString.split("/")[1];
//                LOGGER.info("Feature pages String:: " + featuredTagString);

//             LOGGER.info("Selected Featured Tag :: " + featuredTag);


                String[] featurdList = currentPagesMap.get(featuredTag, String[].class);

//                if (featurdList != null) {
//                    for (String fl : featurdList) {
//                     LOGGER.info("FeaturedList :: " + fl);
//
//                    }
//                }

                featuredSelectedArticles.clear();
                if (featurdList.length <= 3 && featurdList.length > 0) {
//			 LOGGER.info("Node has featured list property :: "+featurdList.length);

                    for (String fl : featurdList) {
                        String spnPath = fl;
//				 LOGGER.info("Spn Path String :: " + spnPath);
                        if (!spnPath.contains("NoArticle")) {
                            Resource flRes = resourceResolver.getResource(spnPath);
//					 LOGGER.info("Resoure resolver has resolved :: " + flRes.getPath());
                            Page flPage = flRes.adaptTo(Page.class);
                            featuredSelectedArticles.add(flPage);
                        }
                    }
                    for (Page fsa : featuredSelectedArticles) {
//				    LOGGER.info("Featured Seleted Title :: " + fsa.getTitle());
                    }
                }
            }
//         if (featuredSelectedArticles == null || featuredSelectedArticles.isEmpty() ) {
//             featuredSelectedArticles.add(filteredArticles.get(0));
//         }

        } catch (Exception e) {
            LOGGER.error("Could not populate selected pages pages tag reference: " + e);
            e.printStackTrace();
        }
        return featuredSelectedArticles;
    }*/
}

