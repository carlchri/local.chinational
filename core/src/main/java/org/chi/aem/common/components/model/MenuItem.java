package org.chi.aem.common.components.model;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class is used by Navigation and TopNav to build the list of items for navigation
 */
public class MenuItem {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MenuItem.class);
	private final static String DISP_THIRD_LEVEL_PROP = "allowTertiaryNav";
	private final static String MAX_THIRD_LEVEL_PROP = "maxTertiaryNavCount";
	private final static String SEE_MORE_LINK_TEXT = "See More";

	private String url;
	private String title;
	private boolean newWindow;
	private boolean displayThirdLevel = false;
	// default is level 9
	private int maxThirdLevelCount = 9;
	// this is  last link for desktop, if required
	private boolean seeMoreLink = false;
	private List childItems = new ArrayList<MenuItem>();

	public MenuItem(){
	}


	public MenuItem(Page page) {
		this.url = LinkUtils.externalize(page.getPath());
		// not used for nav - page.getPageTitle();
		String baseTitle = LinkUtils.isNullOrBlank(page.getTitle())?page.getName():page.getTitle();
		title = LinkUtils.isNullOrBlank(page.getNavigationTitle())?baseTitle:page.getNavigationTitle();
		populateThirdLevelNav(page);
	}

	public void setSeeMoreLinkItem(Page parentPage ) {
		seeMoreLink = true;
		title = SEE_MORE_LINK_TEXT;
		url = LinkUtils.externalize(parentPage.getPath());
	}

	private void populateThirdLevelNav(Page page) {
		ValueMap pageProps = page.getProperties();
		displayThirdLevel = pageProps.get(DISP_THIRD_LEVEL_PROP, false);
		// 3rd level would be /content/sitename/en/level1/level2/level3
		// so get depth has to be 5, for it to work
		if (displayThirdLevel && (page.getDepth() == 5) ) {
            LOGGER.debug("build 3rd level nav - currPage: " + page.getPath()
                    + ", depth: " + page.getDepth()
                    + ", displayThirdLevel: " + displayThirdLevel);
			Iterator<Page> childIterator = page.listChildren();
			maxThirdLevelCount = pageProps.get(MAX_THIRD_LEVEL_PROP, 9);
			boolean childItemsLeft = false;
			while (childIterator.hasNext()) {
				Page child = childIterator.next();
				if (Navigation.checkIfHidden(child)) {
					LOGGER.debug("populateThirdLevelNav hidden child path: " + child.getPath());
					continue;
				}
				if (childItems.size() < maxThirdLevelCount) {
					LOGGER.debug("Add child path to list item: " + child.getPath());
					childItems.add(new MenuItem(child));
				} else {
					// more items left out, create seeMoreLink
					childItemsLeft = true;
					break;
				}
			}
			if (childItemsLeft) {
				LOGGER.debug("Add see more item");
				MenuItem seeMoreItem = new MenuItem();
				seeMoreItem.setSeeMoreLinkItem(page);
				childItems.add(seeMoreItem);
			}
		}
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isNewWindow() {
		return newWindow;
	}

	public void setNewWindow(boolean newWindow) {
		this.newWindow = newWindow;
	}

	public Boolean getIsValidLink() {
        return getUrl().isEmpty();
    }

	public boolean isSeeMoreLink() {
		return seeMoreLink;
	}

	public List getChildItems() {
		return childItems;
	}

	public boolean isThirdLevelItems() {
		return childItems.size() > 0;
	}
}