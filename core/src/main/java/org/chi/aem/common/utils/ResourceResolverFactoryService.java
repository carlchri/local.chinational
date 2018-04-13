package org.chi.aem.common.utils;

import org.apache.sling.api.resource.ResourceResolverFactory ;

/**
 * Resource Resolver Factory Service...
 */

public interface ResourceResolverFactoryService {

	public ResourceResolverFactory getResourceResolverFactory() throws Exception;
}
