package org.chi.aem.common.components.model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.internal.servlets.AdaptiveImageServlet;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.chi.aem.common.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {Image.class, ComponentExporter.class}, resourceType = ImageIpad.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ImageIpad extends ImageEnhancement implements Image {

    public static final String RESOURCE_TYPE = "chinational/components/content/imageIpad";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageIpad.class);
    private String IMAGE_SRC = ImageUtils.IPAD_IMAGE_SRC;
    private String IMAGE_NODE = ImageUtils.IPAD_IMAGE_NODE;

    public ImageIpad() {
        selector = AdaptiveImageServlet.CORE_DEFAULT_SELECTOR;
    }

    @PostConstruct
    protected void initModel() {
        // by setting IMAGE_SRC and IMAGE_NODE, parent class should be able to do rest
        super.updateImageAttrs(this.IMAGE_SRC, this.IMAGE_NODE, RESOURCE_TYPE);
        super.initModel();
    }


}
