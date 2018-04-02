/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Model(adaptables = Resource.class)
public class Hero {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(Hero.class);
	    
	public static final String PROP_LINK_TO = "linkTo";
	public static final String PROP_VIDEO_URL = "videoUrl";
	public static final String PROP_VIDEO_TEXT = "videoText";
	public static final String PROP_VIDEO_ON_RIGHT_RAIL = "rightRail";
	public static final String PROP_VIDEO_THUMBNAIL_OPTION = "thumbnailOption";


	@Inject
	private ResourceResolver resourceResolver;

	@Inject
	@Named(PROP_LINK_TO)
	@Optional
	private String linkTo;

	@Inject
	@Named(PROP_VIDEO_URL)
	@Optional
	private String videoUrl;

	@Inject
	@Named(PROP_VIDEO_THUMBNAIL_OPTION)
	@Optional
	private String thumbnailOption;

	private String imageUrl;

	@PostConstruct
	protected void init() {
	   if (StringUtils.isNotEmpty(videoUrl) && !"#".equals(videoUrl)) {
		   videoUrl = LinkUtils.externalize(videoUrl);
		   // create image URL
           imageUrl = LinkUtils.getYouTubeVideoThumbnail(videoUrl, thumbnailOption);
		}
		if (StringUtils.isNotEmpty(linkTo) && !"#".equals(linkTo)) {
			linkTo = LinkUtils.externalize(linkTo);
		}
	}

	public String getLinkTo() {
		return linkTo;
	}

	public String getVideoUrl() {
		return videoUrl;
	}


	public String getImageUrl() {
		return imageUrl;
	}

}