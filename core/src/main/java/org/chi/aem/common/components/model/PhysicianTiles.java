/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = PhysicianTiles.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PhysicianTiles implements ComponentExporter {

    protected static final String RESOURCE_TYPE = "chinational/components/content/physicianTiles";

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicianTiles.class);
    /**
     * Name of the resource property storing the root page from which to build the list if the source of the list is <code>children</code>.
     *
     */
    String PN_PARENT_PAGE = "parentPage";

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @Self
    private SlingHttpServletRequest request;

    private PageManager pageManager;
    
    // storing list of all physician Tiles
    private java.util.List<Page> listPhysicianTiles;
    
    @PostConstruct
    private void initModel() {
         pageManager = resourceResolver.adaptTo(PageManager.class);
         
         listPhysicianTiles = new ArrayList<>();
         Page rootPage = getRootPage(PN_PARENT_PAGE);
         if (rootPage != null) {
             collectChildren(rootPage);
         }
    }
    
    private Page getRootPage(String fieldName) {
        String parentPath = properties.get(fieldName, currentPage.getPath());
        LOGGER.info("parentPath: " + parentPath);        
        return pageManager.getContainingPage(resourceResolver.getResource(parentPath));
    }

    private void collectChildren(Page parent) {
       Iterator<Page> childIterator = parent.listChildren();
       while (childIterator.hasNext()) {
           Page child = childIterator.next();
           if(child.getProperties().get("cq:template").equals("/apps/chinational/templates/directoryprofilepage")){
        	   listPhysicianTiles.add(child);
           }
           Iterator<Page> gChildIterator = child.listChildren();
           if(gChildIterator.hasNext()){
        	   collectChildren(child);
           }
       }
   }

    public Collection<Page> getListPhysicianTiles() {
        LOGGER.info("listPhysicianTiles: " + listPhysicianTiles);
        return listPhysicianTiles;
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }

 }