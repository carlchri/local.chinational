package org.chi.aem.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
    private static final int FEATURED_LIMIT = 3;
    private static final String DEFAULT_NEWS_FILTER = "SortByMostRecent";
    private static final String DEFAULT_NEWS_FILTER_YEAR = "ChooseYear";
    private static final Map<String, Object> m_article = new HashMap<String, Object>();
    private static final java.util.List<Page> featuredArticles = new ArrayList<>();;
    private static final java.util.List<Page> filteredArticles = new ArrayList<>();;
    private static final java.util.List<String> listYears = new ArrayList<>();;
    private static final java.util.List<String> listTags = new ArrayList<>();;
    private static final Map<String, String> tagsMap = new HashMap<String, String>();

    public static java.util.List<Page> populateListItems(String parentPage, ResourceResolver resourceResolver, String articlesTemplate) {

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

     public static Map<String, Object> populateYearsTagsFeatured(java.util.List<Page> allArticles, ResourceResolver resourceResolver, String articleFilter, String filterYear) {

	     TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
    	 for(Page item : allArticles) {
    		// to get listYears 
    		Calendar date = item.getProperties().get("publishDate", Calendar.class); 
    		//int year = date.get(Calendar.YEAR);
    		SimpleDateFormat formatter = new SimpleDateFormat("YYYY"); 
    		String year = formatter.format(date.getTime()).toUpperCase(); 

    		// addYear(listYears, year);
    		
            /* // to get filtered articles list, if filter contains Year
            if(listYears.contains(filterYear) && filterYear.equals(year)){
            	filteredArticles.add(item);
            }
			*/
    		
            // to get tags
            Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
            // LOGGER.info("tags: " + tags);
            if(tags.length !=0){
	           	 for(Tag tag : tags){
	           		String tagName = tag.getTitle();
	           		// LOGGER.info("tagTitle: " + tag.getTitle());
		         		if(listTags.isEmpty()){
		            		listTags.add(tagName);
		                    tagsMap.put(tag.getName(),tagName);
		        		} else if (!listTags.contains(tagName)){
		        			listTags.add(tagName);
		        			tagsMap.put(tag.getName(),tagName);
		        		}
		         		
		         		if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
		                	addYear(listYears, year);
		                }else if(articleFilter.equals(tag.getName())){
		         			addYear(listYears, year);
		         			if(filterYear.equals(DEFAULT_NEWS_FILTER_YEAR)){
		         				filteredArticles.add(item);
		         			}else if(filterYear.equals(year)){
		         				filteredArticles.add(item);
		         			}
		         		}

		         		/* // to get filtered articles list, if filter contains tag
		         		if(tagsMap.containsKey(articleFilter) && articleFilter.equals(tag.getName())){
		         			LOGGER.debug("INSIDE TAG FILTER");
		                	filteredArticles.add(item);
		                }
		                */
	           	 }
            }else if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
            	addYear(listYears, year);
            }
            // LOGGER.info("filtered news : " + filteredArticles.size());
            
            // to get featured articles
            if(featuredArticles.size() < FEATURED_LIMIT){
	            boolean isFeatured = item.getProperties().get("isFeaturedArticle",false);
	            if(isFeatured){
	            	featuredArticles.add(item);
	            }
            }
    	 }
    	 
         // If no featured article present, add the latest article as featured article.
         if(featuredArticles.isEmpty() && allArticles.size() > 0){
        	 featuredArticles.add(allArticles.get(0));
         }

         // If article filter does not contain Year or Tag, return all articles list
         if(filterYear.equals(DEFAULT_NEWS_FILTER_YEAR) && articleFilter.equals(DEFAULT_NEWS_FILTER)){
         	// filteredArticles = allArticles;
        	 filteredArticles.clear();
        	 filteredArticles.addAll(allArticles);
         }
         // LOGGER.info("filtered news : " + filteredArticles.size());

         // populate map
	     m_article.put("listYears", listYears);
	     m_article.put("listTags", listTags);
	     m_article.put("tagsMap", tagsMap);
	     m_article.put("featuredArticles", featuredArticles);
	     m_article.put("filteredArticles", filteredArticles);

         return m_article;
     }
     
     public static void addYear(java.util.List<String> listYears, String year){
 		if(listYears.isEmpty()){
    		listYears.add(year);
		} else if (!listYears.contains(year)){
			listYears.add(year);
		}
     }
     
     public static java.util.List<Page> populateListArticles(int start_index, int hits_per_page, java.util.List<Page> allArticles) {
	     java.util.List<Page> listArticles = new ArrayList<>();;
    	 int end_index = start_index + hits_per_page;
    	 for(int i = start_index; i < end_index; i++){
    		 if(allArticles.size() > i){
    			 listArticles.add(allArticles.get(i));
    		 } else {
                 break;
             }
    	 }
    	 
    	 return listArticles;
     }

}
