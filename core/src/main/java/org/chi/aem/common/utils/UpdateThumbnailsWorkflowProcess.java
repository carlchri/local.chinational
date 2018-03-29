package org.chi.aem.common.utils;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetMetadata;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;
import java.util.Arrays;
import java.util.List;

@Component
@Service({com.adobe.granite.workflow.exec.WorkflowProcess.class})
@Properties({@Property(name = "process.label", value = "CHI National - Update Thumbnails for a DAM Asset ")})
public class UpdateThumbnailsWorkflowProcess implements WorkflowProcess {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    static final String JCR_CONTENT = "/jcr:content";
    static final String JCR_RENDITIONS = "/renditions";
    @Reference
    private RenditionsService renditionsService;

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        try {
            String thumbnailImagePath = null;
            Node renditionsNode = null;
            Session session = workflowSession.adaptTo(Session.class);
            Workspace workspace = session.getWorkspace();
            ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

            String path = getPayloadPath(workItem.getWorkflowData());

            if (StringUtils.isEmpty(path)) {
                throw new WorkflowException("Failed to get asset path from payload");
            }
            Resource resource = resourceResolver.getResource(path);
            String destinationPath = resource.getParent().getParent().getPath();
            Node thumbnailNode = resource.adaptTo(Node.class);
            if(thumbnailNode != null && thumbnailNode.hasProperty("thumbnailpath")) {
                thumbnailImagePath = thumbnailNode.getProperty("thumbnailpath").getString();
                if (thumbnailImagePath == null) {
                    throw new WorkflowException("Failed to get thumbnail path");
                }
                renditionsNode = thumbnailNode.getParent().getParent().getNode("renditions");
                renditionsNode.remove();
                session.save();
                workspace.copy(thumbnailImagePath + JCR_CONTENT + JCR_RENDITIONS, destinationPath + JCR_RENDITIONS);
            }
        } catch (Exception e) {
            LOGGER.error("Error executing generate thumbnail process", e);
        }
    }

    protected String getPayloadPath(WorkflowData wfData) {
        String payloadPath = null;

        if (wfData.getPayloadType().equals("JCR_PATH")) {
            payloadPath = (String)wfData.getPayload();
        }

        return payloadPath;
    }
}
