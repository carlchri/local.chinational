/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2017 Catholic Health Initiatives
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package org.chi.aem.common.components.model;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import org.chi.aem.common.utils.LinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;


@Model(adaptables = SlingHttpServletRequest.class, adapters = {ComponentExporter.class}, resourceType = Image.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class Image implements ComponentExporter {

   public static final String RESOURCE_TYPE = "chinational/components/content/image";
   private static final String DEFAULT_EXTENSION = "jpeg";

   private static final Logger LOGGER = LoggerFactory.getLogger(Image.class);
   private static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
   private static final String MIME_TYPE_IMAGE_PREFIX = "image/";
   private static final List<String> NON_SUPPORTED_IMAGE_MIMETYPE = Collections.singletonList("image/svg+xml");
   private static final String DOT = ".";
 
   //TODO - need to change size of image for Tablet and Mobile based on workflow jpeg image rendition
   // private static final String SUFFIX_DESKTOP_RENDITION = "/jcr:content/renditions/cq5dam.web.1280.1280.";
   private static final String SUFFIX_DESKTOP_RENDITION = "/jcr:content/renditions/original";
   private static final String RENDITION_SUFFIX = "/jcr:content/renditions/";
   private static final String TABLET_RENDITION_SIZE = "700.700";
   private static final String MOBILE_RENDITION_SIZE = "500.500";
   
   @Self
   private SlingHttpServletRequest request;

   @Inject
   private Resource resource;

   @ScriptVariable
   private PageManager pageManager;


   @ScriptVariable
   private ValueMap properties;

   @Inject
   @Source("osgi-services")
   private MimeTypeService mimeTypeService;

   @ValueMapValue(name = "fileReference", injectionStrategy = InjectionStrategy.OPTIONAL)
   private String fileReference;

   @ValueMapValue(name = "imgTabSrc", injectionStrategy = InjectionStrategy.OPTIONAL)
   private String imgTabSrc;

   @ValueMapValue(name = "imgMobSrc", injectionStrategy = InjectionStrategy.OPTIONAL)
   private String imgMobSrc;

   @ValueMapValue(name = ImageResource.PN_ALT, injectionStrategy = InjectionStrategy.OPTIONAL)
   private String alt;

   @ValueMapValue(name = ImageResource.PN_LINK_URL, injectionStrategy = InjectionStrategy.OPTIONAL)
   private String linkURL;

   @ValueMapValue(name = "targetBlank", injectionStrategy = InjectionStrategy.OPTIONAL)
   private boolean targetBlank;


   @PostConstruct
   private void initModel() {
	   fileReference = validateAndReturnSrc(fileReference);
	   imgTabSrc = validateAndReturnSrc(imgTabSrc);
	   imgMobSrc = validateAndReturnSrc(imgMobSrc);

	   getImageRenditions(fileReference, imgTabSrc, imgMobSrc);
	   
	   // LOGGER.info("Desktop Image Src : " + fileReference);
	   // LOGGER.info("Tablet Image Src : " + imgTabSrc);
	   // LOGGER.info("Mobile Image Src : " + imgMobSrc);
	   
       if (StringUtils.isNotEmpty(linkURL) && !"#".equals(linkURL)) {
           linkURL = LinkUtils.externalize(linkURL);            
       }
   }

   private String validateAndReturnSrc(String src) {
	   boolean hasContent = false;
       String mimeType = MIME_TYPE_IMAGE_JPEG;
       Asset asset = null;
       if (src != null && !src.isEmpty()) {
           final Resource assetResource = request.getResourceResolver().getResource(src);
           if (assetResource != null) {
               asset = assetResource.adaptTo(Asset.class);
               if (asset != null) {
                   mimeType = PropertiesUtil.toString(asset.getMimeType(), MIME_TYPE_IMAGE_JPEG);
                   hasContent = true;
               } else {
                   LOGGER.error("Unable to adapt resource '{}' used by image '{}' to an asset.", src, resource.getPath());
               }
           } else {
               LOGGER.error("Unable to find resource '{}' used by image '{}'.", src, resource.getPath());
           }
       }
       if (hasContent) {
           // validate if correct mime type (i.e. rasterized image)
           if (!mimeType.startsWith(MIME_TYPE_IMAGE_PREFIX)) {
               LOGGER.error("Image at {} uses a binary with a non-image mime type ({})", resource.getPath(), mimeType);
               return fileReference;
           }
           if (NON_SUPPORTED_IMAGE_MIMETYPE.contains(mimeType)) {
               LOGGER.error("Image at {} uses binary with a non-supported image mime type ({})", resource.getPath(), mimeType);
               return fileReference;
           }
           String extension = mimeTypeService.getExtension(mimeType);
           if (extension.equalsIgnoreCase("tif") || extension.equalsIgnoreCase("tiff")) {
               src.replace(extension, DEFAULT_EXTENSION);
           }
       }else {
    	   return fileReference;
       }
	   return src;
   }
   
   public void getImageRenditions(String desktopSrc, String tabletSrc, String mobileSrc){
	   List<Rendition> renditions;
	   Asset asset = null;
	   
	   /**
	    * Get Web Rendition [1280x1280] for the Desktop version of image.
	    */
	   if(request.getResourceResolver().getResource(desktopSrc) != null){
		   asset = request.getResourceResolver().getResource(desktopSrc).adaptTo(Asset.class);
	   }
	   if(asset !=null){
		   renditions = asset.getRenditions();
		   for (Rendition rendition : renditions) {
		       if (rendition.getName().startsWith("original")) {
		    	   fileReference += SUFFIX_DESKTOP_RENDITION;
		       }
		   }
	   }

	   /**
	    * Get Rendition for the Tablet version of image.
	    * If Author has run the DAM Rendition Workflow, then we get the tablet verion of the image.
	    * Otherwise, fallback to Web Rendition [1280x1280] 
	    * 
	    * If using seperate image for the Tablet, then get the web version of the tablet image
	    */
	   if(request.getResourceResolver().getResource(tabletSrc) != null){
		   asset = request.getResourceResolver().getResource(tabletSrc).adaptTo(Asset.class);
	   }
	   if(asset !=null){
		   renditions = asset.getRenditions();
		   if(!tabletSrc.equals(desktopSrc)){
			   for (Rendition trendition : renditions) {
			       if (trendition.getName().startsWith("original")) {
			    	   imgTabSrc += SUFFIX_DESKTOP_RENDITION;
			       }
			   }
		   } else {
			   for (Rendition drendition : renditions) {
			       if (drendition.getName().startsWith(DamConstants.PREFIX_ASSET_THUMBNAIL + DOT + TABLET_RENDITION_SIZE)) {
			           imgTabSrc += RENDITION_SUFFIX + DamConstants.PREFIX_ASSET_THUMBNAIL + DOT + TABLET_RENDITION_SIZE + DOT + mimeTypeService.getExtension(asset.getMimeType());
			       }
			   }
			   if(!imgTabSrc.contains(DamConstants.PREFIX_ASSET_THUMBNAIL)){
				   imgTabSrc = fileReference;
			   }
		   }
	   }
		   
	   /**
	    * Get Rendition for the Mobile version of image.
	    * If Author has run the DAM Rendition Workflow, then we get the mobile verion of the image.
	    * Otherwise, fallback to Web Rendition [1280x1280] 
	    * 
	    * If using seperate image for the Mobile, then get the web version of the mobile image
	    */
	   if(request.getResourceResolver().getResource(mobileSrc) != null){
		   asset = request.getResourceResolver().getResource(mobileSrc).adaptTo(Asset.class);
	   }
	   if(asset !=null){
		   renditions = asset.getRenditions();
		   if(!mobileSrc.equals(desktopSrc)){
			   for (Rendition trendition : renditions) {
			       if (trendition.getName().startsWith("original")) {
			    	   imgMobSrc += SUFFIX_DESKTOP_RENDITION;
			       }
			   }
		   } else {
			   for (Rendition drendition : renditions) {
			       if (drendition.getName().startsWith(DamConstants.PREFIX_ASSET_THUMBNAIL + DOT + MOBILE_RENDITION_SIZE)) {
			    	   imgMobSrc += RENDITION_SUFFIX + DamConstants.PREFIX_ASSET_THUMBNAIL + DOT + MOBILE_RENDITION_SIZE + DOT + mimeTypeService.getExtension(asset.getMimeType());
			       }
			   }
				   
			   if(!imgMobSrc.contains(DamConstants.PREFIX_ASSET_THUMBNAIL)){
				   imgMobSrc = fileReference;
			   }
		   }
	   }
   }

   public String getFileReference() {
       return fileReference;
   }

   public String getImgTabSrc() {
       return imgTabSrc;
   }
   
   public String getImgMobSrc() {
       return imgMobSrc;
   }
   
   public String getAlt() {
       return alt;
   }

   public String getLinkURL() {
       return linkURL;
   }

    public boolean getTargetBlank() {
       return targetBlank;
   }

   @Nonnull
   @Override
   public String getExportedType() {
       return resource.getResourceType();
   }

  }
