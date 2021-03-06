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
    //private static final String [] colNames = {"one", "two", "three", "four"};

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
                childItems.add(new MenuItem(0, child));
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
        int secondLvlCount=1;
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
            MenuItem cItem = new MenuItem(0, child);
            cItem.setFirstLevel(true);
            // get grandchildren, if any
            Iterator<Page> grandChildIterator = child.listChildren();
            int childAndGrandChildCount = 1;
            List childListItems = new ArrayList<MenuItem>();
            // add parent as first child , as parent link opens and expand rest of the menu
            childListItems.add(cItem);
            while (grandChildIterator.hasNext()) {
                Page grandChild = grandChildIterator.next();
                if (checkIfHidden(grandChild)) {
                    LOGGER.debug("collectChildren hidden grandChild path: " + grandChild.getPath());
                    continue;
                }
                LOGGER.debug("collectChildren grandChild path: " + grandChild.getPath());
                childAndGrandChildCount++;
                MenuItem childItem = new MenuItem(secondLvlCount, grandChild);
                childAndGrandChildCount = childAndGrandChildCount + childItem.getChildItems().size();
                childListItems.add(childItem);
                secondLvlCount++;
            }
            LOGGER.debug("Arrange items for child: " + child.getPath() + " wuth total count: " + childAndGrandChildCount);
            cItem.setChildMap(arrangeMenuItems(childListItems, childAndGrandChildCount));
            listItems.add(cItem);
            mapItems.put(String.valueOf(count++), listItems);
        }
    }

    /**
     * This method re-arranges children and grand-children menuItems as per this business logic:
     *
     * 1-4 stack vertically in the first column
     * 5-16 flow top-to-bottom, left-to-right to fill the remaining 4 columns
     * When the 17th is added, add a 5th row; everything should adjust to accommodate that 5th row, i.e., the first 3 columns should now have 5 links and the last should now have 2
     * When 4 more are added (bringing the total to 21), add the 6th row. The first 3 columns should now have 6 and the last should now have 3
     * And so on...
     * @param listItems
     */
    private SortedMap <String, List<MenuItem>> arrangeMenuItems(List<MenuItem> listItems, int totalCount) {
        // if total count is less than or equal to 16, treat it differently
        SortedMap <String, List<MenuItem>> colMenuMap  = new TreeMap<>();
        // update logic as per updated requirement
        // 1-2 in first column
        // 3-8, flow top-to-bottom, left-to-right to fill the remaining 4 columns
        // means 2 in each column till we fill 4 columns
        // 9th onward, every 4 in new rows
        if (totalCount <= 8) {
            arrangeMenuItemsInCols(colMenuMap, listItems, 2);
        } else {
            int rowsCount = (int)Math.floor(totalCount/4);
            if (totalCount%4 != 0) {
                // add extra row to cover nav
                rowsCount++;
            }
            arrangeMenuItemsInCols(colMenuMap, listItems, rowsCount);
        }
        return  colMenuMap;
    }

    private void arrangeMenuItemsInCols(SortedMap <String, List<MenuItem>> colMenuMap,
                                        List<MenuItem> listItems, int rowsCount) {
        int currCount = 0;
        int currColCount = 0;
        List<MenuItem> currColList = new ArrayList<>();
        LOGGER.debug("arrangeMenuItemsInCols rowCount: " + rowsCount + " for total child count: " + listItems.size());
        for(MenuItem item : listItems) {
            if (currCount != 0 && currCount%rowsCount == 0 ) {
                // create new column
                LOGGER.debug("Add list size" + currColList.size() + " to column: " + String.valueOf(currColCount));
                colMenuMap.put(String.valueOf(currColCount++), currColList);
                currColList = new ArrayList<>();
            }
            currColList.add(item);
            currCount++;

            if (item.isThirdLevelItems()) {
                for ( MenuItem cItem : item.getChildItems()) {
                    if (currCount%rowsCount == 0 ) {
                        // create new column
                        LOGGER.debug("Add cList size " + currColList.size() + " to column: " + String.valueOf(currColCount));
                        colMenuMap.put(String.valueOf(currColCount++), currColList);
                        currColList = new ArrayList<>();
                    }
                    currColList.add(cItem);
                    currCount++;
                }
            }
        }
        // if there are any items in currColList, lets add them to colMenuMap
        if (currColList.size() > 0) {
            LOGGER.debug("add to next map? currColCount: " + currColCount);
            colMenuMap.put(String.valueOf(currColCount), currColList);
        }
        // we should have 1-4 cols each with max 4 in each
        LOGGER.debug("arrangeMenuItemsInCols map: " + colMenuMap);
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