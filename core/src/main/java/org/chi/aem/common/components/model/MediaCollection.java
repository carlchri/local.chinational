package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MediaCollection extends WCMUsePojo {
	private List<CarouselItem> items;

	@Override
	public void activate() throws Exception {
		items = new ArrayList<CarouselItem>();
		Resource mediaCollectionR = getResource().getChild("mediaCollection");
		CarouselItem item = null;
		if(mediaCollectionR != null) {
			Iterable<Resource> medias = mediaCollectionR.getChildren();
			for(Resource aMedia : medias) {
				ValueMap properties = ResourceUtil.getValueMap(aMedia);
				if(properties.containsKey("name")) {
					item = new CarouselItem(getGeneratedId(),properties.get("name", String.class), properties.get("mediaType", String.class));
					items.add(item);
				}
			}
		}
	}

	private String getGeneratedId() {
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}

	public List<CarouselItem> getItems(){
			return items;
	}
}