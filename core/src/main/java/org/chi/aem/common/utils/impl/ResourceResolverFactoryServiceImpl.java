package org.chi.aem.common.utils.impl;

import org.chi.aem.common.utils.ResourceResolverFactoryService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ResourceResolverFactoryService Implementation...
 */
@Component(
        name="CHI National Resource Resolver Factory Getter...",
        metatype = true, immediate = true
)
@Property(name="service.description", value="CHI National Resource Resolver Factory Getter")
@Service
public class ResourceResolverFactoryServiceImpl implements ResourceResolverFactoryService {

    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Reference    
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public ResourceResolverFactory getResourceResolverFactory() throws Exception {
    	return resourceResolverFactory;
    }

}
