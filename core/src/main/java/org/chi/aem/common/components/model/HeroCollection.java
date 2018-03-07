package org.chi.aem.common.components.model;

import java.util.*;
import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

public class HeroCollection extends WCMUsePojo {
	private List<CarouselItem> items;

	@Override
	public void activate() throws Exception {
		items = new ArrayList<CarouselItem>();
		Resource heroCollectionR = getResource().getChild("heroCollection");
		CarouselItem item = null;
		if(heroCollectionR != null) {
			Iterable<Resource> heros = heroCollectionR.getChildren();
			for(Resource aHero : heros) {
				ValueMap properties = ResourceUtil.getValueMap(aHero);
				if(properties.containsKey("name")) {
					item = new CarouselItem(getGeneratedId(),properties.get("name", "Enter Name"), null);
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