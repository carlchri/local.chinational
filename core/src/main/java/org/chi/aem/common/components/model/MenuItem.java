package org.chi.aem.common.components.model;

import com.day.cq.wcm.api.Page;
import org.chi.aem.common.utils.LinkUtils;

/**
 * This class is used by Navigation and TopNav to build the list of items for navigation
 */
public class MenuItem {

	private String url;
	private String title;
	private boolean newWindow;

	public MenuItem(){
	}

	public MenuItem(Page page) {
		this.url = LinkUtils.externalize(page.getPath());
		// not used for nav - page.getPageTitle();
		String baseTitle = LinkUtils.isNullOrBlank(page.getTitle())?page.getName():page.getTitle();
		title = LinkUtils.isNullOrBlank(page.getNavigationTitle())?baseTitle:page.getNavigationTitle();
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

}