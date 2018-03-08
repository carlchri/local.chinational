/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Model(adaptables = Resource.class)
public class Video {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(Video.class);
	    
	public static final String PROP_SECTION_HEADING = "sectionHeading";
	public static final String PROP_VIDEO_URL = "videoUrl";
	public static final String PROP_VIDEO_TEXT = "videoText";
	public static final String PROP_VIDEO_ON_RIGHT_RAIL = "rightRail";


	@Inject
	private ResourceResolver resourceResolver;

	@Inject
	@Named(PROP_SECTION_HEADING)
	@Optional
	private String sectionHeading;

	@Inject
	@Named(PROP_VIDEO_ON_RIGHT_RAIL)
	@Optional
	private boolean rightRail;

	@Inject
	@Named(PROP_VIDEO_URL)
	@Optional
	private String videoUrl;

	@Inject
	@Named(PROP_VIDEO_TEXT)
	@Optional
	private String videoText;

	private String imageUrl;

	@PostConstruct
	protected void init() {
	   if (StringUtils.isNotEmpty(videoUrl) && !"#".equals(videoUrl)) {
		   videoUrl = LinkUtils.externalize(videoUrl);
		   // create image URL
           imageUrl = LinkUtils.getYouTubeVideoThumbnail(videoUrl);
		}
	}

	public String getSectionHeading() {
		return sectionHeading;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public String getVideoText() {
		return videoText;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isRightRail() {
		return rightRail;
	}
}