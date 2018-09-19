/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package org.chi.aem.common.components.model;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.internal.servlets.AdaptiveImageServlet;
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.chi.aem.common.utils.NewsBlogImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {Image.class, ComponentExporter.class}, resourceType = ImageTileFromHeaderNewsBlog.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ImageTileFromHeaderNewsBlog extends ImageHeaderNewsBlog implements Image {

    public static final String RESOURCE_TYPE = "chinational/components/content/imageTileFromHeaderNewsBlog";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTileFromHeaderNewsBlog.class);
    private String IMAGE_SRC = NewsBlogImageUtils.HEADER_IMAGE_SRC;
    private String IMAGE_NODE = NewsBlogImageUtils.TILE_FROM_HEADER_IMAGE_NODE;

    public ImageTileFromHeaderNewsBlog() {
        selector = AdaptiveImageServlet.CORE_DEFAULT_SELECTOR;
    }

    @PostConstruct
    protected void initModel() {
        // by setting IMAGE_SRC and IMAGE_NODE, parent class should be able to do rest
        super.updateImageAttrs(this.IMAGE_SRC, this.IMAGE_NODE, RESOURCE_TYPE);
        super.initModel();
    }


}
