// 05/31/2018 - When order of items were changed, UI was displaying empty area rather than display the item
//            - This was happening as itemNode was not storing hash value, and logic was using carouselNode
//            - to pick up value, which was place dependant. itemNode has correct value and it should not change
//            - So, that value shouls be used. Also dialog was modified to carry over nodeName value during order change

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.Session;
import java.util.*;

public class MediaCollection extends WCMUsePojo {
	public static final Logger LOGGER = LoggerFactory.getLogger(MediaCollection.class);
	private List<CarouselItem> editViewItems;
	private List<List<CarouselItem>> previewItems;
	private CarouselItem firstItem;
	private String sectionHeading;
	static final String MEDIA_COLLECTION = "mediaCollection";
	static final String NODE_NAME = "nodeName";

	@Override
	public void activate() throws Exception {
		setEditViewItems();
	}

	public int getHash(String additionalHashString) {
		if (sectionHeading != null) {
			return (additionalHashString + sectionHeading).hashCode();
		}
		return (additionalHashString + MEDIA_COLLECTION).hashCode();
	}


	private void setEditViewItems() {
		boolean isFirst = true;
		editViewItems = new ArrayList<CarouselItem>();
		previewItems = new ArrayList<List<CarouselItem>>();
		List<CarouselItem> groupItems = new ArrayList<CarouselItem>();
		Resource mediaCollectionR = getResource().getChild(MEDIA_COLLECTION);
		CarouselItem item = null;
		Session session = getResourceResolver().adaptTo(Session.class);
		Resource mediaCarouselR = getResource();
		ValueMap carouselProps = ResourceUtil.getValueMap(mediaCarouselR);
		sectionHeading = carouselProps.get("sectionHeading", "");
		LOGGER.debug("common sectionHeading: " + sectionHeading);
		if(mediaCollectionR != null) {
			Node carouselNode = mediaCarouselR.adaptTo(Node.class);
			Iterable<Resource> medias = mediaCollectionR.getChildren();
			for(Resource aMedia : medias) {

				ValueMap properties = ResourceUtil.getValueMap(aMedia);
				int nodeName = getHash(aMedia.getPath());
				LOGGER.debug("aMedia.getPath(): " + aMedia.getPath()
						+ ", properties get hash: " + properties.get(NODE_NAME)
						+ ", hash- " + nodeName);
				if (properties.containsKey(NODE_NAME)) {
					nodeName = Integer.valueOf(properties.get(NODE_NAME).toString());
					LOGGER.debug("hash value is using what is coming from itemNode: " + nodeName);
				}
				if(properties.containsKey("name")) {
					try {
						Node itemNode = aMedia.adaptTo(Node.class);
						if(!properties.containsKey(NODE_NAME) && !carouselProps.containsKey(itemNode.getName())) {
							// save node for first time
							LOGGER.debug("Add node for first time to carousel: " + itemNode.getName());
							itemNode.setProperty(NODE_NAME, nodeName);
							carouselNode.setProperty(itemNode.getName(), nodeName);
							session.save();
							LOGGER.debug("Get node hash value from itemNode: " + itemNode.getProperty(NODE_NAME));
						}else if(carouselProps.containsKey(itemNode.getName())){
							LOGGER.debug("node exist in carousel: " + itemNode.getName()
									+ ", properties.containsKey(NODE_NAME): " + properties.containsKey(NODE_NAME));
							if (! properties.containsKey(NODE_NAME)) {
								nodeName = ((Long) carouselProps.get(itemNode.getName())).intValue();
								// lets add the nodeName to property, this is to take care of existing nodes
								itemNode.setProperty(NODE_NAME, nodeName);
								session.save();
								LOGGER.debug("Saved the nodeName to current itemNode, no need to do it again");
							}
						}else {
							LOGGER.debug("node does not exist in carousel?: " + itemNode.getName());
							nodeName = ((Long) properties.get(NODE_NAME)).intValue();
							carouselNode.setProperty(itemNode.getName(), nodeName);
							session.save();
						}
					}catch (Exception e) {
						LOGGER.error("Error - MediaCollection.setEditViewItems: " + e.getMessage());
					}
					item = new CarouselItem(nodeName,properties.get("name", String.class), properties.get("mediaType", String.class));
					editViewItems.add(item);
					if(isFirst) {
						firstItem = item;
						isFirst = false;
						continue;
					}
					groupItems.add(item);
					if(groupItems.size() == 2) {
						previewItems.add(groupItems);
						groupItems = new ArrayList<CarouselItem>();
					}
				}
			}
			if(groupItems.size() == 1) {
				previewItems.add(groupItems);
			}
		}
	}

    public CarouselItem getFirstItem() {
        return firstItem;
    }

    public List<CarouselItem> getEditViewItems(){
		return editViewItems;
	}

	public List<List<CarouselItem>> getPreviewItems(){
		return previewItems;
	}

	public String getSectionHeading() {
		return sectionHeading;
	}
}