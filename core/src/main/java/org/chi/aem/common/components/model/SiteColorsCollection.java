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

	public static final String PROP_SOLID_BUTTON_HOVER_COLOR = "solidHoverColor";
	public static final String PROP_SOLID_BUTTON_HOVER_COLOR_CLASS = "solidHoverColorClass";
	public static final String PROP_SOLID_BUTTON_HOVER_COLOR_STYLE = "solidHoverColorStyle";
	
	public static final String PROP_OUTLINED_BUTTON_COLOR = "outlinedColor";
	public static final String PROP_OUTLINED_BUTTON_COLOR_CLASS = "outlinedColorClass";
	public static final String PROP_OUTLINED_BUTTON_COLOR_STYLE = "outlinedColorStyle";
	
	public static final String PROP_SEARCH_BUTTON_COLOR = "searchButtonColor";
	public static final String PROP_SEARCH_BUTTON_COLOR_CLASS = "searchButtonColorClass";
	public static final String PROP_SEARCH_BUTTON_COLOR_STYLE = "searchButtonColorStyle";
	
	public static final String PROP_VIDEO_PLAY_BUTTON_COLOR = "videoPlayColor";
	public static final String PROP_VIDEO_PLAY_BUTTON_COLOR_CLASS = "videoPlayColorClass";
	public static final String PROP_VIDEO_PLAY_BUTTON_COLOR_STYLE = "videoPlayColorStyle";
	
	public static final String PROP_ARROW_CIRCLE_COLOR = "arrowCircleColor";
	public static final String PROP_ARROW_CIRCLE_COLOR_CLASS = "arrowCircleColorClass";
	public static final String PROP_ARROW_CIRCLE_COLOR_STYLE = "arrowCircleColorStyle";

	public static final String PROP_HEADING_COLOR = "headingTextColor";
	public static final String PROP_HEADING_COLOR_CLASS = "headingColorClass";
	public static final String PROP_HEADING_COLOR_STYLE = "headingColorStyle";
	
	public static final String PROP_TILES_HEADING_COLOR = "tilesHeadingColor";
	public static final String PROP_TILES_HEADING_COLOR_CLASS = "tilesHeadingColorClass";
	public static final String PROP_TILES_HEADING_COLOR_STYLE = "tilesHeadingColorStyle";

	public static final String PROP_ACCORDION_OPEN_HEADING_COLOR = "accordionOpenHeadingColor";
	public static final String PROP_ACCORDION_OPEN_HEADING_COLOR_CLASS = "accordionOpenHeadingColorClass";
	public static final String PROP_ACCORDION_OPEN_HEADING_COLOR_STYLE = "accordionOpenHeadingColorStyle";
	public static final String PROP_ACCORDION_OPEN_ARROW_COLOR_CLASS = "accordionOpenArrowColorClass";
	public static final String PROP_ACCORDION_OPEN_ARROW_COLOR_STYLE = "accordionOpenArrowColorStyle";
	
	public static final String PROP_ACCORDION_CLOSE_HEADING_COLOR = "accordionCloseHeadingColor";
	public static final String PROP_ACCORDION_CLOSE_HEADING_COLOR_CLASS = "accordionCloseHeadingColorClass";
	public static final String PROP_ACCORDION_CLOSE_HEADING_COLOR_STYLE = "accordionCloseHeadingColorStyle";
	public static final String PROP_ACCORDION_CLOSE_ARROW_COLOR_CLASS = "accordionCloseArrowColorClass";
	public static final String PROP_ACCORDION_CLOSE_ARROW_COLOR_STYLE = "accordionCloseArrowColorStyle";

	public static final String PROP_SEC_NAVIGATION_BG_COLOR = "secNavbgColor";
	public static final String PROP_SEC_NAVIGATION_BG_COLOR_CLASS = "secNavbgColorClass";
	public static final String PROP_SEC_NAVIGATION_BG_COLOR_STYLE = "secNavbgColorStyle";

	public static final String PROP_QUOTE_TEXT_COLOR = "quoteTextColor";
	public static final String PROP_QUOTE_TEXT_COLOR_CLASS = "quoteTextColorClass";
	public static final String PROP_QUOTE_TEXT_COLOR_STYLE = "quoteTextColorStyle";

	public static final String PROP_LINK_TEXT_COLOR = "linkTextColor";
	public static final String PROP_LINK_TEXT_COLOR_CLASS = "linkTextColorClass";
	public static final String PROP_LINK_TEXT_COLOR_STYLE = "linkTextColorStyle";

	public static final String PROP_SEPERATOR_COLOR = "seperatorColor";
	public static final String PROP_SEPERATOR_COLOR_CLASS = "seperatorColorClass";
	public static final String PROP_SEPERATOR_COLOR_STYLE = "seperatorColorStyle";

	Map<String, Object> colorsMap = new HashMap<String, Object>();

	@Override
	public void activate() throws Exception {
		LOGGER.info("currentStyle getPath:" + getCurrentStyle().getPath());
		LOGGER.info("currentDesign getPath:" + getCurrentDesign().getPath());

		// gets map from basepage, if available
		ValueMap designMap = DesignUtils.getDesignMap(getCurrentDesign(), getCurrentStyle());

        colorsMap.put("solidButton", createColorsMap(designMap, PROP_SOLID_BUTTON_COLOR, PROP_SOLID_BUTTON_COLOR_CLASS, PROP_SOLID_BUTTON_COLOR_STYLE));
        colorsMap.put("solidButtonHover", createColorsMap(designMap, PROP_SOLID_BUTTON_HOVER_COLOR, PROP_SOLID_BUTTON_HOVER_COLOR_CLASS, PROP_SOLID_BUTTON_HOVER_COLOR_STYLE));
        colorsMap.put("outlinedButton", createColorsMap(designMap, PROP_OUTLINED_BUTTON_COLOR, PROP_OUTLINED_BUTTON_COLOR_CLASS, PROP_OUTLINED_BUTTON_COLOR_STYLE));
        colorsMap.put("searchButton", createColorsMap(designMap, PROP_SEARCH_BUTTON_COLOR, PROP_SEARCH_BUTTON_COLOR_CLASS, PROP_SEARCH_BUTTON_COLOR_STYLE));
        colorsMap.put("videoPlayButton", createColorsMap(designMap, PROP_VIDEO_PLAY_BUTTON_COLOR, PROP_VIDEO_PLAY_BUTTON_COLOR_CLASS, PROP_VIDEO_PLAY_BUTTON_COLOR_STYLE));
        colorsMap.put("carouselArrowCircle", createColorsMap(designMap, PROP_ARROW_CIRCLE_COLOR, PROP_ARROW_CIRCLE_COLOR_CLASS, PROP_ARROW_CIRCLE_COLOR_STYLE));
        colorsMap.put("heading", createColorsMap(designMap, PROP_HEADING_COLOR, PROP_HEADING_COLOR_CLASS, PROP_HEADING_COLOR_STYLE));
        colorsMap.put("tilesHeading", createColorsMap(designMap, PROP_TILES_HEADING_COLOR, PROP_TILES_HEADING_COLOR_CLASS, PROP_TILES_HEADING_COLOR_STYLE));
        colorsMap.put("accordionOpenTitle", createColorsMap(designMap, PROP_ACCORDION_OPEN_HEADING_COLOR, PROP_ACCORDION_OPEN_HEADING_COLOR_CLASS, PROP_ACCORDION_OPEN_HEADING_COLOR_STYLE));
        colorsMap.put("accordionOpenArrow", createColorsMap(designMap, PROP_ACCORDION_OPEN_HEADING_COLOR, PROP_ACCORDION_OPEN_ARROW_COLOR_CLASS, PROP_ACCORDION_OPEN_ARROW_COLOR_STYLE));
        colorsMap.put("accordionCloseTitle", createColorsMap(designMap, PROP_ACCORDION_CLOSE_HEADING_COLOR, PROP_ACCORDION_CLOSE_HEADING_COLOR_CLASS, PROP_ACCORDION_CLOSE_HEADING_COLOR_STYLE));
        colorsMap.put("accordionCloseArrow", createColorsMap(designMap, PROP_ACCORDION_CLOSE_HEADING_COLOR, PROP_ACCORDION_CLOSE_ARROW_COLOR_CLASS, PROP_ACCORDION_CLOSE_ARROW_COLOR_STYLE));
        colorsMap.put("secondaryNavigation", createColorsMap(designMap, PROP_SEC_NAVIGATION_BG_COLOR, PROP_SEC_NAVIGATION_BG_COLOR_CLASS, PROP_SEC_NAVIGATION_BG_COLOR_STYLE));
        colorsMap.put("quote", createColorsMap(designMap, PROP_QUOTE_TEXT_COLOR, PROP_QUOTE_TEXT_COLOR_CLASS, PROP_QUOTE_TEXT_COLOR_STYLE));
        colorsMap.put("smallTextLink", createColorsMap(designMap, PROP_LINK_TEXT_COLOR, PROP_LINK_TEXT_COLOR_CLASS, PROP_LINK_TEXT_COLOR_STYLE));
        colorsMap.put("seperator", createColorsMap(designMap, PROP_SEPERATOR_COLOR, PROP_SEPERATOR_COLOR_CLASS, PROP_SEPERATOR_COLOR_STYLE));
	}

	public Map<String, String> createColorsMap(ValueMap valueMap, String value, String className, String style) {
		Map<String, String> map = new HashMap<String, String>();
        map.put("colorValue", valueMap.get(value, ""));
        map.put("colorClass", valueMap.get(className, ""));
        // LOGGER.info("colorClass" + valueMap.get(className, ""));
        map.put("colorStyle", valueMap.get(style, ""));
        return map;
	}
	
	public Map<String, Object> getColorsMap() {
		return colorsMap;
	}
}