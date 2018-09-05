/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.models.annotations.Default;

@Model(adaptables = Resource.class)
public class NewsListManual {
 
	    public static final Logger LOGGER = LoggerFactory.getLogger(NewsListManual.class);
	    
	    public static final String NEWS_HEADING = "newsHeading";	
	    public static final String PUBLISH_DATE = "publishDate";
	    public static final String EXCERPT = "excerpt";
	    public static final String PROP_LINK_URL = "linkUrl";
	    public static final String TARGET_BLANK = "targetBlank";

		@Inject
		@Named(NEWS_HEADING)
		@Optional
		@Default(values="News Heading")
		private String newsHeading;
	
		@Inject
		@Named(PUBLISH_DATE)
		@Optional
		private Calendar publishDate;
	
		@Inject
		@Named(EXCERPT)
		@Optional
		@Default(values="Excerpt")
		private String excerpt;
	
		@Inject
		@Named(PROP_LINK_URL)
		@Optional
		@Default(values="#")
		private String linkUrl;
	
		@Inject
		@Named(TARGET_BLANK)
		@Optional
		@Default(values="false")
		private boolean targetBlank;
	
		@PostConstruct
		protected void init() {
	       if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
	            linkUrl = LinkUtils.externalize(linkUrl);            
	        }
		}
	
		public String getNewsHeading() {
			return newsHeading;
		}
	
		public Calendar getPublishDate() {
			return publishDate;
		}
	
		public String getExcerpt() {
			return excerpt;
		}
	
		public String getLinkUrl() {
			return linkUrl;
		}
	
		public boolean getTargetBlank() {
			return targetBlank;
		}
}