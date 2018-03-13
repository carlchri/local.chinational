/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ValueMap;
import org.chi.aem.common.utils.DesignUtils;
import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

public class SiteColorsCollection extends WCMUsePojo {
 
	public static final Logger LOGGER = LoggerFactory.getLogger(SiteColorsCollection.class);

	public static final String PROP_SOLID_BUTTON_COLOR = "solidColor";
	public static final String PROP_SOLID_BUTTON_COLOR_CLASS = "solidColorClass";
	public static final String PROP_SOLID_BUTTON_COLOR_STYLE = "solidColorStyle";
	public static final String PROP_OUTLINED_BUTTON_COLOR = "outlinedColor";
	public static final String PROP_OUTLINED_BUTTON_COLOR_CLASS = "outlinedColorClass";
	public static final String PROP_OUTLINED_BUTTON_COLOR_STYLE = "outlinedColorStyle";
	public static final String PROP_HEADING_COLOR = "headingTextColor";
	public static final String PROP_HEADING_COLOR_CLASS = "headingColorClass";
	public static final String PROP_HEADING_COLOR_STYLE = "headingColorStyle";
	public static final String PROP_HERO_BACKGROUND_COLOR = "heroBackgroundColor";
	public static final String PROP_HERO_BACKGROUND_COLOR_CLASS = "heroBackgroundColorClass";
	public static final String PROP_HERO_BACKGROUND_COLOR_STYLE = "heroBackgroundColorStyle";

    Map<String, Object> colorsMap = new HashMap<String, Object>();

	@Override
	public void activate() throws Exception {
		LOGGER.debug("currentStyle getPath:" + getCurrentStyle().getPath());
		LOGGER.debug("currentDesign getPath:" + getCurrentDesign().getPath());

		// gets map from basepage, if available
		ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());

        colorsMap.put("solidButton", createColorsMap(designMap, PROP_SOLID_BUTTON_COLOR, PROP_SOLID_BUTTON_COLOR_CLASS, PROP_SOLID_BUTTON_COLOR_STYLE));
        colorsMap.put("outlinedButton", createColorsMap(designMap, PROP_OUTLINED_BUTTON_COLOR, PROP_OUTLINED_BUTTON_COLOR_CLASS, PROP_OUTLINED_BUTTON_COLOR_STYLE));
        colorsMap.put("heading", createColorsMap(designMap, PROP_HEADING_COLOR, PROP_HEADING_COLOR_CLASS, PROP_HEADING_COLOR_STYLE));
        colorsMap.put("hero", createColorsMap(designMap, PROP_HERO_BACKGROUND_COLOR, PROP_HERO_BACKGROUND_COLOR_CLASS, PROP_HERO_BACKGROUND_COLOR_STYLE));
        
/*      Map<String, String> solid_colors_map = new HashMap<String, String>();
        solid_colors_map.put("colorValue", designMap.get(PROP_SOLID_BUTTON_COLOR, ""));
        solid_colors_map.put("colorClass", designMap.get(PROP_SOLID_BUTTON_COLOR_CLASS, ""));
        solid_colors_map.put("colorStyle", designMap.get(PROP_SOLID_BUTTON_COLOR_STYLE, ""));
        colorsMap.put("solidButton", solid_colors_map);
        
        Map<String, String> outlined_colors_map = new HashMap<String, String>();
        outlined_colors_map.put("colorValue", designMap.get(PROP_OUTLINED_BUTTON_COLOR, ""));
        outlined_colors_map.put("colorClass", designMap.get(PROP_OUTLINED_BUTTON_COLOR_CLASS, ""));
        outlined_colors_map.put("colorStyle", designMap.get(PROP_OUTLINED_BUTTON_COLOR_STYLE, ""));
        colorsMap.put("outlinedButton", outlined_colors_map);
*/
		   
	}

	public Map<String, String> createColorsMap(ValueMap valueMap, String value, String className, String style) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("colorValue", valueMap.get(value, ""));
        map.put("colorClass", valueMap.get(className, ""));
        map.put("colorStyle", valueMap.get(style, ""));
        return map;
	}
	
	public Map<String, Object> getColorsMap() {
		return colorsMap;
	}
}