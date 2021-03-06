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
		if(mediaCollectionR != null) {
			Node carouselNode = mediaCarouselR.adaptTo(Node.class);
			Iterable<Resource> medias = mediaCollectionR.getChildren();
			for(Resource aMedia : medias) {
				ValueMap properties = ResourceUtil.getValueMap(aMedia);
				int nodeName = getHash(aMedia.getPath());
				if(properties.containsKey("name")) {
					try {
						Node itemNode = aMedia.adaptTo(Node.class);
						if(!properties.containsKey(NODE_NAME) && !carouselProps.containsKey(itemNode.getName())) {
								itemNode.setProperty(NODE_NAME, nodeName);
							carouselNode.setProperty(itemNode.getName(), nodeName);
								session.save();
						}else if(carouselProps.containsKey(itemNode.getName())){
							nodeName = ((Long) carouselProps.get(itemNode.getName())).intValue();
						}else {
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