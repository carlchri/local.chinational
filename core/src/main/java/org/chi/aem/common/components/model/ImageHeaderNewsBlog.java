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
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.chi.aem.common.components.internal.models.ImageImplV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.Session;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {Image.class, ComponentExporter.class}, resourceType = ImageHeaderNewsBlog.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ImageHeaderNewsBlog extends ImageImplV2 implements Image {

    public static final String RESOURCE_TYPE = "chinational/components/content/imageHeaderNewsBlog";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHeaderNewsBlog.class);
    protected static final String IMAGE_SRC_DIALOG = "fileReference";
    private String IMAGE_SRC = "imageSrc";
    // used to compare and check if oheader image is being updated vs tile image
    private final static String HEADER_IMAGE_NODE = "nb-header-image";
    private String IMAGE_NODE = HEADER_IMAGE_NODE;
    private static final String RES_TYPE = "sling:resourceType";
    private String RES_TYPE_VALUE = RESOURCE_TYPE;


    @ScriptVariable
    private Page currentPage;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    public ImageHeaderNewsBlog() {
        selector = AdaptiveImageServlet.CORE_DEFAULT_SELECTOR;
    }

    @PostConstruct
    protected void initModel() {
        initImageSetup();
        super.initModel();
    }

    /**
     * Used by children classes to update attribute name's
     * @param imageSrcName
     * @param imageNodeName
     */
    public void updateImageAttrs(String imageSrcName, String imageNodeName, String resType) {
        this.IMAGE_SRC = imageSrcName;
        this.IMAGE_NODE = imageNodeName;
        this.RES_TYPE_VALUE = resType;
    }

    /**
     * Setup image dialog for image editing
     */
    protected void initImageSetup(){
        boolean imageUpdated = false;
        String imageSrc = null;
        // get fileReference from page properties
        if (currentPage != null) {
            ValueMap pageProperties = currentPage.getProperties();
            imageSrc = pageProperties.get(IMAGE_SRC, "");
            LOGGER.info("IMAGE_SRC: " + IMAGE_SRC);
            if (imageSrc == null || imageSrc.equals("")) {
                // no need to update any node properties, as node will be hidden
                return;
            }
            if (fileReference != null && !fileReference.equals(imageSrc)){
                imageUpdated = true;
            }
            fileReference = imageSrc;
            LOGGER.info("New file reference: " + fileReference);
        } else {
            LOGGER.error("No page properties available");
        }
        if (resource !=  null && resourceResolver != null) {
            // save file reference to node, if not saved already
            ValueMap imageProps = ResourceUtil.getValueMap(resource);
            LOGGER.debug("Get image properties: " + imageProps);
            if (!imageProps.containsKey(IMAGE_SRC_DIALOG)) {
                LOGGER.info("Image nodes does not have file ref, create node and ref attr");
                createImageNode(imageSrc);
            } else if (imageUpdated) {
                LOGGER.info("Image either is updated or deleted");
                // update the node attribute and remove edit options
                try {
                    Session session = resourceResolver.adaptTo(Session.class);
                    Node imageNode = resource.adaptTo(Node.class);
                    imageNode.setProperty(IMAGE_SRC_DIALOG, imageSrc);
                    imageNode.setProperty("imageCrop", (javax.jcr.Value)null);
                    imageNode.setProperty("imageMap", (javax.jcr.Value)null);
                    imageNode.setProperty("imageRotate", (javax.jcr.Value)null);
                    session.save();
                    session.refresh(true);
                } catch (Exception e) {
                    LOGGER.error("Could not update image reference: " + e);
                    e.printStackTrace();
                }
            }
            // TODO add logic to copy image caption and useOriginalSize flag to parent node, if updated
            // only if it is header image, not for tile images
        }
    }


    /**
     * Create image node, if it does note exist
     */
    protected void createImageNode(String imageSrc) {
        if (imageSrc == null) {
            LOGGER.warn("Image source is null for creating image node");
            return;
        }
        LOGGER.info("Image props don't contain imageSrc attribute, lets save it");
        LOGGER.info("Resource path: " + resource.getPath());
        Resource parent = resource.getParent();
        // lets save it
        try {
            // first time, resource itself does not exist, so we need to save that
            Session session = resourceResolver.adaptTo(Session.class);
            if (parent.getChild(IMAGE_NODE) == null) {
                LOGGER.info("news-header-image does not exist, lets create this node first");
                Node parentNode = parent.adaptTo(Node.class);
                parentNode.addNode(IMAGE_NODE);
                session.save();
                //refresh the sesssion to get latest data
                session.refresh(true);
                // check if node is available
                Node childNode = parentNode.getNode(IMAGE_NODE);
                if (childNode != null) {
                    // set image and resourceType
                    childNode.setProperty(IMAGE_SRC_DIALOG, imageSrc);
                    // add resourceType as well
                    childNode.setProperty(RES_TYPE, RES_TYPE_VALUE);
                    session.save();
                } else {
                    LOGGER.error("Cant find image node, that as just saved");
                }

            }
        } catch (Exception e) {
            LOGGER.error("Could not save image reference: " + e);
            e.printStackTrace();
        }
    }

}
