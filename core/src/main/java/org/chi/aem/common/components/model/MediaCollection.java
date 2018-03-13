package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

import java.util.*;

public class MediaCollection extends WCMUsePojo {
	private List<CarouselItem> editViewItems;
	private List<List<CarouselItem>> previewItems;
	private CarouselItem firstItem;
	static final String MEDIA_COLLECTION = "mediaCollection";

	@Override
	public void activate() throws Exception {
		setEditViewItems();
	}

	private String getGeneratedId() {
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}

	private void setEditViewItems() {
		boolean isFirst = true;
		editViewItems = new ArrayList<CarouselItem>();
		previewItems = new ArrayList<List<CarouselItem>>();
		List<CarouselItem> groupItems = new ArrayList<CarouselItem>();
		Resource mediaCollectionR = getResource().getChild(MEDIA_COLLECTION);
		CarouselItem item = null;
		if(mediaCollectionR != null) {
			Iterable<Resource> medias = mediaCollectionR.getChildren();
			for(Resource aMedia : medias) {
				ValueMap properties = ResourceUtil.getValueMap(aMedia);
				if(properties.containsKey("name")) {
					item = new CarouselItem(getGeneratedId(),properties.get("name", String.class), properties.get("mediaType", String.class));
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
}