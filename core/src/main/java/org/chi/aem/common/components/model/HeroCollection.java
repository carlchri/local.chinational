package org.chi.aem.common.components.model;

import java.util.*;
import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;

public class HeroCollection extends WCMUsePojo {
	private List<CarouselItem> items;
	private String singleItemName;
	static final String HERO_COLLECTION = "heroCollection";

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
					item = new CarouselItem(getHash(aHero.getPath()),properties.get("name", "Enter Name"), null);
					items.add(item);
				}
			}
		}
	}

	public int getHash(String additionalHashString) {
		return (additionalHashString + HERO_COLLECTION).hashCode();
	}

	public List<CarouselItem> getItems(){
			return items;
	}

	public boolean getIsSingleItem(){
		if(items.size() == 1) {
			singleItemName = items.get(0).getName();
			return true;
		}
		return false;
	}

	public String getSingleItemName() {
		return singleItemName;
	}
}