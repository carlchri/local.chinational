<%--
  	This granite widget color field uses colorsPath as a configuration field to display the colors on it.
	The colorsPath value should point to the colors options as mentioned below.

	colorsPath="/etc/config/chinational/components/colorpicker"
--%>
<%@include file="/libs/granite/ui/global.jsp"
%><%@page session="false"
          import="java.util.HashMap,
                  java.util.Iterator,
                  java.util.Map,
                  org.apache.commons.lang.StringUtils,
                  org.apache.sling.commons.json.JSONObject,
                  com.adobe.granite.ui.components.AttrBuilder,
                  com.adobe.granite.ui.components.Config,
                  com.adobe.granite.ui.components.Field,
                  com.adobe.granite.ui.components.Tag"%>
<%
    Config cfg = cmp.getConfig();
    ValueMap vm = (ValueMap) request.getAttribute(Field.class.getName());

    Resource mappingsRes = null;
    String colorsResourcePath = cfg.get("colorsPath", "");
    Resource rt = resourceResolver.getResource(colorsResourcePath);
    if (rt != null) {
        mappingsRes = rt.getChild("colors");
    }
    
    Map<String, String> colors = new HashMap<String, String>();
    
    if (mappingsRes != null) {
        Iterator<Resource> itMappings = mappingsRes.listChildren();
        
        while (itMappings.hasNext()) {
            Config mappingCfg = new Config(itMappings.next());

            String name = mappingCfg.get("text", String.class);
            String value = mappingCfg.get("value", String.class);
            
            if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)) {
                colors.put(i18n.getVar(name), value);
            }
       }
    }
    
    Map<String, Boolean> pickerModes = new HashMap<String, Boolean>();
    pickerModes.put("classicPalette", cfg.get("classicPaletteType", false));
    pickerModes.put("freestylePalette", cfg.get("freestylePaletteType", true));
    pickerModes.put("edit", cfg.get("editType", false));
    
    // TODO use JSONStringer instead of JSONObject
    JSONObject colorpickerJson = new JSONObject();
    colorpickerJson.put("colors", colors);
    colorpickerJson.put("pickerModes", pickerModes);
    
    
    Tag tag = cmp.consumeTag();
    AttrBuilder attrs = tag.getAttrs();

    attrs.add("id", cfg.get("id", String.class));
    attrs.addRel(cfg.get("rel", String.class));
    attrs.addClass(cfg.get("class", String.class));
    attrs.add("title", i18n.getVar(cfg.get("title", String.class)));
    
    attrs.addClass("coral-ColorPicker");
    attrs.add("data-init", "colorpicker");
    attrs.add("data-name", cfg.get("name", String.class));
    attrs.add("data-config", colorpickerJson.toString());
    attrs.add("value", vm.get("value", String.class));
    
    if (cfg.get("disabled", false)) {
        attrs.add("data-disabled", true);
    }
    
    if (cfg.get("required", false)) {
        attrs.add("aria-required", true);
    }
    
    String validation = StringUtils.join(cfg.get("validation", new String[0]), " ");
    attrs.add("data-validation", validation);

    attrs.addOthers(cfg.getProperties(), "id", "class", "rel", "title", "name", "disabled", "required", "validation", "value", "classicPaletteType", "freestylePaletteType", "editType", "fieldLabel", "fieldDescription", "renderReadOnly", "ignoreData");

%><span <%= attrs.build() %>><button class="coral-ColorPicker-button coral-MinimalButton" type="button"></button></span>