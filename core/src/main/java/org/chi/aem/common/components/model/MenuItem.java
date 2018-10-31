package org.chi.aem.common.components.model;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

/**
 * This class is used by Navigation and TopNav to build the list of items for navigation
 */
public class MenuItem {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MenuItem.class);
	private final static String DISP_THIRD_LEVEL_PROP = "allowTertiaryNav";
	private final static String MAX_THIRD_LEVEL_PROP = "maxTertiaryNavCount";
	private final static String SEE_MORE_LINK_TEXT = "See More";
	private final static String NAV_CLASS_PREFIX = "nav-toggle-id-";

	private String url;
	private String title;
	private boolean newWindow;
	private String navClass = NAV_CLASS_PREFIX;


	private boolean firstLevel = false;
	private boolean thirdLevel = false;
	private boolean displayThirdLevel = false;
	// default is level 9
	private int maxThirdLevelCount = 9;
	// this is  last link for desktop, if required
	private boolean seeMoreLink = false;
	private List<MenuItem> childItems = new ArrayList<MenuItem>();

	private Map<String, List<MenuItem>> childMap;

	public MenuItem(){
	}


	public MenuItem(int currCount, Page page) {
		this(currCount, page, true);
	}

	public MenuItem(int currCount, Page page, boolean populateNextLevel) {
		this.url = LinkUtils.externalize(page.getPath());
		this.navClass = this.navClass + currCount;
		// not used for nav - page.getPageTitle();
		String baseTitle = LinkUtils.isNullOrBlank(page.getTitle())?page.getName():page.getTitle();
		title = LinkUtils.isNullOrBlank(page.getNavigationTitle())?baseTitle:page.getNavigationTitle();
		if (populateNextLevel) {
			populateThirdLevelNav(currCount, page);
		}
	}

	public void setSeeMoreLinkItem(int currCount, Page parentPage ) {
		seeMoreLink = true;
		this.navClass = this.navClass + currCount;
		thirdLevel = true;
		title = SEE_MORE_LINK_TEXT;
		url = LinkUtils.externalize(parentPage.getPath());
	}

	private void populateThirdLevelNav(int currCount, Page page) {
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
					MenuItem cItem = new MenuItem(currCount, child);
					// so UI can display it accordingly
					cItem.setThirdLevel(true);
					childItems.add(cItem);
				} else {
					// more items left out, create seeMoreLink
					childItemsLeft = true;
					break;
				}
			}
			if (childItemsLeft) {
				LOGGER.debug("Add see more item");
				MenuItem seeMoreItem = new MenuItem();
				seeMoreItem.setSeeMoreLinkItem(currCount, page);
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

	public List<MenuItem> getChildItems() {
		return childItems;
	}

	public boolean isThirdLevelItems() {
		return childItems.size() > 0;
	}

	public Map<String, List<MenuItem>> getChildMap() {
		return childMap;
	}

	public void setChildMap(Map<String, List<MenuItem>> childMap) {
		this.childMap = childMap;
	}

	public boolean isThirdLevel() {
		return thirdLevel;
	}

	public void setThirdLevel(boolean thirdLevel) {
		this.thirdLevel = thirdLevel;
	}

	public boolean isFirstLevel() {
		return firstLevel;
	}

	public void setFirstLevel(boolean firstLevel) {
		this.firstLevel = firstLevel;
	}

	public String getNavClass() {
		return navClass;
	}

}