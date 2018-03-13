package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

import java.util.*;

public class MediaCollection extends WCMUsePojo {
	private List<CarouselItem> editViewItems;
	private List<List<MediaItem>> previewItems;
	private MediaItem firstItem;

	static final String VIDEO = "video";
	static final String AUDIO = "audio";
	static final String MEDIA_COLLECTION = "mediaCollection";
	static final String VIDEO_RESOURCE_TYPE = "chinational/components/content/video";

	@Override
	public void activate() throws Exception {
		setEditViewItems();
		setPreviewItems();
	}

	private String getGeneratedId() {
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}

	private void setEditViewItems() {
		editViewItems = new ArrayList<CarouselItem>();
		Resource mediaCollectionR = getResource().getChild(MEDIA_COLLECTION);
		CarouselItem item = null;
		if(mediaCollectionR != null) {
			Iterable<Resource> medias = mediaCollectionR.getChildren();
			for(Resource aMedia : medias) {
				ValueMap properties = ResourceUtil.getValueMap(aMedia);
				if(properties.containsKey("name")) {
					item = new CarouselItem(getGeneratedId(),properties.get("name", String.class), properties.get("mediaType", String.class));
					editViewItems.add(item);
				}
			}
		}
	}

	private void setPreviewItems() {
		previewItems = new ArrayList<List<MediaItem>>();
		List<MediaItem> groupItems = new ArrayList<MediaItem>();
		Resource mediaCarouselR = getResource();
		boolean isFirst = true;
		int index = 0;
		Video aVideo = null;
		Audio aAudio = null;
		if(mediaCarouselR != null) {
			Iterable<Resource> medias = mediaCarouselR.getChildren();
			for(Resource aMedia : medias) {
				if(aMedia.getName().equalsIgnoreCase(MEDIA_COLLECTION)) {
					continue;
				}
				if(aMedia.isResourceType(VIDEO_RESOURCE_TYPE)) {
					aVideo = aMedia.adaptTo(Video.class);
					if(isFirst) {
						firstItem = new MediaItem(VIDEO, aVideo, null);
						isFirst = false;
					}else {
						groupItems.add(new MediaItem(VIDEO, aVideo, null));
						if(groupItems.size() == 2) {
							previewItems.add(groupItems);
							groupItems = new ArrayList<MediaItem>();
						}
					}
				}else {
					aAudio = aMedia.adaptTo(Audio.class);
					if(isFirst) {
						firstItem = new MediaItem(AUDIO, null, aAudio);
						isFirst = false;
					}else {
						groupItems.add(new MediaItem(AUDIO, null, aAudio));
						if(groupItems.size() == 2) {
							previewItems.add(groupItems);
							groupItems = new ArrayList<MediaItem>();
						}
					}
				}
			}
			if(groupItems.size() == 1) {
				previewItems.add(groupItems);
			}
		}
	}

    public MediaItem getFirstItem() {
        return firstItem;
    }

    public List<CarouselItem> getEditViewItems(){
		return editViewItems;
	}

	public List<List<MediaItem>> getPreviewItems(){
		return previewItems;
	}
}