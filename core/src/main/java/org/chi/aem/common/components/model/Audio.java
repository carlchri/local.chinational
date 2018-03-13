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
public class Audio {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(Audio.class);
	    
	public static final String PROP_SECTION_HEADING = "sectionHeading";
	public static final String PROP_VIDEO_URL = "audioUrl";
	public static final String PROP_VIDEO_TEXT = "audioText";


	@Inject
	private ResourceResolver resourceResolver;

	@Inject
	@Named(PROP_SECTION_HEADING)
	@Optional
	private String sectionHeading;

	@Inject
	@Named(PROP_VIDEO_URL)
	@Optional
	private String audioUrl;

	@Inject
	@Named(PROP_VIDEO_TEXT)
	@Optional
	private String audioText;

	private String imageUrl;

	@PostConstruct
	protected void init() {
	   if (StringUtils.isNotEmpty(audioUrl) && !"#".equals(audioUrl)) {
		   audioUrl = LinkUtils.externalize(audioUrl);
		   // create image URL
           imageUrl = LinkUtils.getYouTubeVideoThumbnail(audioUrl);
		}
	}

	public String getSectionHeading() {
		return sectionHeading;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public String getAudioText() {
		return audioText;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}