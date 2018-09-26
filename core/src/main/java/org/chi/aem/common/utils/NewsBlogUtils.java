package org.chi.aem.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

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
    private static final String DEFAULT_NEWS_FILTER = "AllItems";
    private static final String DEFAULT_NEWS_FILTER_YEAR = "ChooseYear";
	private static String tagName = "";
    private static final Map<String, Object> m_article = new HashMap<String, Object>();
	private static final java.util.List<Page> featuredSelectedArticles = new ArrayList<>();
    private static final java.util.List<Page> featuredArticles = new ArrayList<>();;
    private static final java.util.List<Page> filteredArticles = new ArrayList<>();;
    private static final java.util.List<String> listYears = new ArrayList<>();;
    private static final java.util.List<String> listTags = new ArrayList<>();;
    private static final Map<String, String> tagsMap = new HashMap<String, String>();
    private static final Map<String, String> tagsDescMap = new HashMap<String, String>();

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
		getFeaturedArticles(parentPage, resourceResolver, DEFAULT_NEWS_FILTER);
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

     public static Map<String, Object> populateYearsTagsFeatured(String parentPage, java.util.List<Page> allArticles, ResourceResolver resourceResolver, String articleFilter, int featured_limit) {

         // LOGGER.info("newsblogUtils start filteredArticles Size : " + filteredArticles.size());
         if(filteredArticles.size() !=0) {
        	 filteredArticles.clear();
         }
         if(featuredArticles.size() !=0) {
        	 featuredArticles.clear();
         }
         if(listYears.size() !=0) {
        	 listYears.clear();
         }
         if(listTags.size() !=0) {
        	 listTags.clear();
         }
         if(tagsMap.size() !=0) {
        	 tagsMap.clear();
         }
         if(tagsDescMap.size() !=0) {
        	 tagsDescMap.clear();
         }

    	 TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
    	 for(Page item : allArticles) {
    		// to get listYears 
    		Calendar date = item.getProperties().get("publishDate", Calendar.class); 
    		//int year = date.get(Calendar.YEAR);
    		SimpleDateFormat formatter = new SimpleDateFormat("YYYY"); 
    		String year = formatter.format(date.getTime()).toUpperCase(); 

            // to get tags
            Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
            // LOGGER.info("tags: " + tags);
            if(tags.length !=0){
	           	 for(Tag tag : tags){
	           		tagName = tag.getTitle();
	           		// LOGGER.info("tagTitle: " + tag.getTitle());
		         		if(listTags.isEmpty()){
		            		listTags.add(tagName);
		                    tagsMap.put(tag.getName(),tagName);
		                    tagsDescMap.put(tag.getName(),tag.getDescription());
		        		} else if (!listTags.contains(tagName)){
		        			listTags.add(tagName);
		        			tagsMap.put(tag.getName(),tagName);
		        			tagsDescMap.put(tag.getName(), tag.getDescription());
		        		}
		         		
		         		if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
//							LOGGER.info("Article Filter :: "+articleFilter);
		                	addYear(listYears, year);
		                	addFeaturedArticle(item, featured_limit);
//							getFeaturedArticles(parentPage, resourceResolver, articleFilter);
		                }else if(articleFilter.equals(tag.getName())){
//		         			LOGGER.info("Article Filter :: "+articleFilter);
		         			addYear(listYears, year);
		         			filteredArticles.add(item);
//		                	addFeaturedArticle(item, featured_limit);
		                	getFeaturedArticles(parentPage, resourceResolver, articleFilter);
		         		}
	           	 }
            }else if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
            	addYear(listYears, year);
            	addFeaturedArticle(item, featured_limit);
            }
    	 }
    	 
         // LOGGER.info("newsblogUtils after for loop - filteredArticles Size : " + filteredArticles.size());
    	 
        // If article filter is allNews, return all articles list
         if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
         	// filteredArticles = allArticles;
        	 filteredArticles.clear();
        	 filteredArticles.addAll(allArticles);
         }

         // to handle the use case when we have two BlogList component on the same page
         // and both have different parent page and different tags associated with it
         // Users selects a tag from dropdown in one BlogList component
         // and tag is not there in another Bloglist component dropdown on the same page
         if(!tagsMap.keySet().contains(articleFilter) && !articleFilter.equals(DEFAULT_NEWS_FILTER)){
         	populateYearsTagsFeatured(parentPage, allArticles, resourceResolver, DEFAULT_NEWS_FILTER, featured_limit);
          }

         // If no featured article present, add the latest article as featured article.
         if(featuredArticles.isEmpty() && filteredArticles.size() > 0){
//         	LOGGER.info("Articles are empty");
//         	LOGGER.info("Tags list :: "+tagName);
//         	featuredArticles.add(filteredArticles.get(0));
			getFeaturedArticles(parentPage, resourceResolver, articleFilter);
         }
         
         // LOGGER.info("newsblogUtils end filteredArticles Size : " + filteredArticles.size());

         // populate map
	     m_article.put("listYears", listYears);
	     m_article.put("listTags", listTags);
	     m_article.put("tagsMap", tagsMap);
	     m_article.put("tagsDescMap", tagsDescMap);
	     m_article.put("featuredArticles", featuredArticles);
	     m_article.put("filteredArticles", filteredArticles);

         return m_article;
     }
     
     public static Map<String, Object> populateYearsTagsFeatured(String parentPage, java.util.List<Page> allArticles, ResourceResolver resourceResolver, String articleFilter, String filterYear) {

         // LOGGER.info("newsblogUtils servlet start filteredArticles Size : " + filteredArticles.size());
         if(filteredArticles.size() !=0) {
        	 filteredArticles.clear();
         }
	     TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
    	 for(Page item : allArticles) {
    		// to get Year of article 
    		Calendar date = item.getProperties().get("publishDate", Calendar.class); 
    		//int year = date.get(Calendar.YEAR);
    		SimpleDateFormat formatter = new SimpleDateFormat("YYYY"); 
    		String year = formatter.format(date.getTime()).toUpperCase(); 
    		
    		if(filterYear.equals(year) || filterYear.equals(DEFAULT_NEWS_FILTER_YEAR)){
	            // to get tags
	            Tag[] tags = tagManager.getTagsForSubtree(item.adaptTo(Resource.class), false);
	            // LOGGER.info("tags: " + tags);
	            if(tags.length !=0){
		           	 for(Tag tag : tags){
		         			if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
		         				addFilteredArticles(item);
		         			} else if(articleFilter.equals(tag.getName())){
			         			// LOGGER.debug("INSIDE TAG FILTER");
			         			addFilteredArticles(item);
			                }
		           		
		           	 }
	            }else if(articleFilter.equals(DEFAULT_NEWS_FILTER)){
	            	addFilteredArticles(item);
	            }
    		}
            
    	 }
    	 
         // LOGGER.info("newsblogUtils servlet end filteredArticles Size : " + filteredArticles.size());
    	 
         // populate map
	     m_article.put("featuredArticles", featuredArticles);
	     m_article.put("filteredArticles", filteredArticles);

         return m_article;
     }

     public static void getFeaturedArticles(String parentPage, ResourceResolver resourceResolver, String articleFilter){

		 // get featured pages
		 String featuredPagesPath = parentPage + "/jcr:content";
		 Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
		 ValueMap featuredPagesMap = featuredPagesRes.adaptTo(ValueMap.class);
		 String featuredTag = featuredPagesMap.get("featuredTag", String.class);
		 LOGGER.info("featured page tag string :: "+featuredTag);
		 String [] featurdList = featuredPagesMap.get(featuredTag, String[].class);
//		 for (String fl : featurdList) {
//			LOGGER.info("FeaturedList ::  "+ fl);
//		 }

		 featuredArticles.clear();
		 String pageTag = tagName.replace(" ", "");
		 if (featurdList.length <= 3 && featurdList.length > 0) {
//			LOGGER.info("Node has featured list property");

			 for (String fl : featurdList) {
				 String spnPath = fl;
//				 LOGGER.info("Spn Path String :: " + spnPath);
				 Resource flRes = resourceResolver.getResource(spnPath);
//				 LOGGER.info("Resoure resolver has resolved :: "+flRes.getPath());
				 Page flPage = flRes.adaptTo(Page.class);
//				 LOGGER.info("Page Title :: "+flPage.getTitle());
//				 LOGGER.info(("Page Tag :: "+flPage.getProperties().get("cq:tags", String[].class)[0]));
//				 ValueMap flMap = flRes.adaptTo(ValueMap.class);
//				 LOGGER.info("Resource has resoved to Valuemap :: "+flMap);
				 if (flPage.getProperties().get("cq:tags", String[].class)[0].contains(articleFilter) || articleFilter.contains("AllItems") ) {
//				 	LOGGER.info("Article tags match");
				 	featuredArticles.add(flPage);
				 }
			 }
		 }
//		 for (Page fa : featuredArticles) {
//		 	LOGGER.info("Featured Article Tag :: "+ fa.getProperties().get("cq:tags", String[].class)[0]);
//		 	LOGGER.info("Featured Article List Page :: "+fa.getTitle());
//		 }
//		 LOGGER.info("Page Tag :: "+pageTag);

//		 LOGGER.info("Featured article length :: "+featuredArticles.size());
	 }
     
     public static void addYear(java.util.List<String> listYears, String year){
 		if(listYears.isEmpty()){
    		listYears.add(year);
		} else if (!listYears.contains(year)){
			listYears.add(year);
		}
     }
     
     public static void addFilteredArticles(Page item){
 		if(filteredArticles.isEmpty()){
 			filteredArticles.add(item);
		} else if (!filteredArticles.contains(item)){
			filteredArticles.add(item);
		}
     }
     
     public static void addFeaturedArticle(Page item, int limit){
//         boolean isFeatured = item.getProperties().get("isFeaturedArticle",false);
//         if(featuredArticles.size() < limit && isFeatured && !featuredArticles.contains(item)){
//          	featuredArticles.add(item);
//	      }
	  }
  
     public static void addFeaturedArticles(java.util.List<Page> articles, int limit){
    	 for(Page item : articles) {
	         boolean isFeatured = item.getProperties().get("isFeaturedArticle",false);
	         if(featuredArticles.size() < limit && isFeatured && !featuredArticles.contains(item)){
	          	featuredArticles.add(item);
	         }
	      }
	  }
  
     public static boolean isFeatured(Page item){
            boolean isFeatured = item.getProperties().get("isFeaturedArticle",false);
            return isFeatured;
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

     public static java.util.List<Page> populateSeletedItems(String parentPage, ResourceResolver resourceResolver) {
		 // get featured pages
		 String featuredPagesPath = parentPage + "/jcr:content";
		 Resource featuredPagesRes = resourceResolver.getResource(featuredPagesPath);
		 ValueMap featuredPagesMap = featuredPagesRes.adaptTo(ValueMap.class);
		 String featuredTag = featuredPagesMap.get("featuredPagesTag", String.class).split("/")[1];
		 LOGGER.info("Selected Featured Tag :: "+featuredTag);
		 String[] featurdList = featuredPagesMap.get(featuredTag, String[].class);

		 for (String fl : featurdList) {
			LOGGER.info("FeaturedList ::  "+ fl);
		 }

		 featuredSelectedArticles.clear();
		 String pageTag = tagName.replace(" ", "");
		 if (featurdList.length <= 3 && featurdList.length > 0) {
//			LOGGER.info("Node has featured list property");

			 for (String fl : featurdList) {
				 String spnPath = fl;
//				 LOGGER.info("Spn Path String :: " + spnPath);
				 Resource flRes = resourceResolver.getResource(spnPath);
//				 LOGGER.info("Resoure resolver has resolved :: " + flRes.getPath());
				 Page flPage = flRes.adaptTo(Page.class);
				 featuredSelectedArticles.add(flPage);
			 }
			 for (Page fsa : featuredSelectedArticles) {
//				 LOGGER.info("Featured Seleted Title" + fsa.getTitle());
			 }
		 }
		 return featuredSelectedArticles;
	 }

}
