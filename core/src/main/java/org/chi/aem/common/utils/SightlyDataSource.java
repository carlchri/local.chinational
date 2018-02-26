package org.chi.aem.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

import com.adobe.cq.sightly.WCMUsePojo;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;

/**
 * Sightly class act as a data source returning dynamic data.
 * 
 */
public class SightlyDataSource extends WCMUsePojo {
	@Override
	public void activate() throws Exception {
		ResourceResolver resolver = getResource().getResourceResolver();
       // Set a fallback DataSource to be an Empty DataSource
        getRequest().setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
        // The GraniteUI Widget should define the datasource beneath it using the nodeName "datasource"
        Resource datasource = getResource().getChild("datasource");
        // Configuration data can be passed into this Datasource impl by way of the datasource node.
        ValueMap dsProperties = datasource.getValueMap();
        String path = dsProperties.get("path", String.class);
        // Collect whatever data you want to expose in the datasource. This is a simple example selecting
        // nodes under a path specified on the datasource node
        Iterator<Resource> resources =
                resolver.findResources("SELECT * FROM [nt:base] WHERE ISDESCENDANTNODE([" + path + "])",
                Query.JCR_SQL2);
        if (resources.hasNext()) {
            // Create a list to capture the items to be added to the data source; This can be sorted, etc.
            List<Resource> fakeResourceList = new ArrayList<Resource>();
            while (resources.hasNext()) {
                // For each item to add to the datasource
                Resource item = resources.next();
                // Create a ValueMap which will represent an item to display via the datasource
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
                // Put the value for this item
                vm.put("value", item.getValueMap().get("value", "Missing value"));
                // Get the label to display to the Author that represents this item
                vm.put("text", item.getValueMap().get("text", "Missing title"));
                vm.put("selected", item.getValueMap().get("selected", "false"));
                // Some "magic". Create a list of "fake" resources and provide the populated ValueMap
                fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), "nt:unstructured", vm));
            }
            // Create a DataSource from the items
            DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
            // Set this DataSource to request
            getRequest().setAttribute(DataSource.class.getName(), ds);
        } else {
            // If no data for the datasource can be found, default to the EmptyDataSource set on the request above
        }
	}
}
