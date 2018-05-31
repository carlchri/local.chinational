/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Catholic Health Initiatives
 ~ 5/30/18 - Created class to get link URL with correct extension
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

@Model(adaptables = Resource.class)
public class McImageTile {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(McImageTile.class);

	public static final String PROP_LINK_URL = "linkTo";

	@Inject
	private ResourceResolver resourceResolver;

	@Inject
	@Named(PROP_LINK_URL)
	@Optional
	@Default(values="#")
	private String linkUrl;


	@PostConstruct
	protected void init() {
	   if (StringUtils.isNotEmpty(linkUrl) && !"#".equals(linkUrl)) {
		   linkUrl = LinkUtils.externalize(linkUrl);
		}
	}



	public String getLinkUrl() {
		return linkUrl;
	}


}