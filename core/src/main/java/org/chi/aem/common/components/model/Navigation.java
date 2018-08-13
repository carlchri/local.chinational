/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.*;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.wcm.api.Page;
import org.chi.aem.common.utils.DesignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Defines the {@code Navigation} Sling Model used for the {@code /apps/chinational/foundation/structure/navigation}
 * component. This component gets list of pages for navigation (two levels only).
 */
public class  Navigation extends WCMUsePojo {

    /**
     * Name of the resource property storing the root page from which to build the list
     */
    String PN_PARENT_PAGE = "parentPage";
    private static int MAX_NAV_COUNT = 7;

    protected static final String RESOURCE_TYPE = "chinational/foundation/structure/navigation";

    private static final Logger LOGGER = LoggerFactory.getLogger(Navigation.class);

    private PageManager pageManager;
    private java.util.SortedMap<String, List<MenuItem>> mapItems;
    private List<MenuItem> childItems;

    public Navigation() {
        LOGGER.debug("Call Constructor");
    }

    @Override
    public void activate() throws Exception {
        pageManager = getResourceResolver().adaptTo(PageManager.class);
    }


    public Map<String, List<MenuItem>> getItems() {
        LOGGER.trace("getItems called");
        if (mapItems == null) {
            LOGGER.debug("mapItems is null, populate the values");
            populateChildListItems();
        }
        return mapItems;
    }

    public List<MenuItem> getChildItems() {
        LOGGER.trace("getChildItems called");
        if (childItems == null) {
            LOGGER.debug("childItems is null, populate the values");
            populateChildItems();
        }
        return childItems;
    }

    private Page getRootPage(String fieldName) {
        ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());
        String parentPath = designMap.get(fieldName, getCurrentPage().getPath());
        LOGGER.debug("getRootPage for : " + parentPath);
        return pageManager.getContainingPage(getResourceResolver().getResource(parentPath));
    }

    private void populateChildItems() {
        childItems = new ArrayList<>();
        Page rootPage = getRootPage(PN_PARENT_PAGE);
        if (rootPage != null) {
            LOGGER.debug("populateChildItems rootPage is not null");
            Iterator<Page> childIterator = rootPage.listChildren();
            while (childIterator.hasNext()) {
                Page child = childIterator.next();
                if (checkIfHidden(child)) {
                    LOGGER.debug("populateChildItems hidden child path: " + child.getPath());
                    continue;
                }
                childItems.add(new MenuItem(child));
            }
        }
    }

    private void populateChildListItems() {
        mapItems = new TreeMap<>();
        Page rootPage = getRootPage(PN_PARENT_PAGE);
        if (rootPage != null) {
            LOGGER.debug("populateChildListItems rootPage is not null");
            collectChildren(rootPage);
        }
    }

    private void collectChildren(Page parent) {
        Iterator<Page> childIterator = parent.listChildren();
        int count = 0;
        while (childIterator.hasNext()) {
            Page child = childIterator.next();
            if (checkIfHidden(child)) {
                LOGGER.debug("collectChildren hidden child path: " + child.getPath());
                continue;
            }
            if (count >= MAX_NAV_COUNT){
                LOGGER.debug("collectChildren child path" + child.getPath() + " not added to nav, " +
                        "as count is more than: "  + MAX_NAV_COUNT);
                break;
            }
            LOGGER.debug("collectChildren child path: " + child.getPath());
            // create new List
            List listItems = new ArrayList<MenuItem>();
            // first item is current child
            listItems.add(new MenuItem(child));
            // get grandchildren, if any
            Iterator<Page> grandChildIterator = child.listChildren();
            while (grandChildIterator.hasNext()) {
                Page grandChild = grandChildIterator.next();
                if (checkIfHidden(grandChild)) {
                    LOGGER.debug("collectChildren hidden grandChild path: " + grandChild.getPath());
                    continue;
                }
                LOGGER.debug("collectChildren grandChild path: " + grandChild.getPath());
                listItems.add(new MenuItem(grandChild));
            }
            mapItems.put(String.valueOf(count++), listItems);
        }
    }

    /**
     * Check if the page is suposed to be hidden in navigation
     * @param page
     * @return
     */
    public static boolean checkIfHidden(Page page) {
        return page.isHideInNav() ;
    }
}
