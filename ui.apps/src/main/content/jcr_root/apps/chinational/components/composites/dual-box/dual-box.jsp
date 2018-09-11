    <%@include file="/libs/foundation/global.jsp"%><% 
%><%@ page import="java.util.Iterator,
        com.day.text.Text, 
        com.day.cq.wcm.api.PageFilter, com.day.cq.wcm.api.Page,
        com.day.cq.wcm.api.PageFilter, com.day.cq.wcm.api.Template;
        import com.day.cq.wcm.api.WCMMode;
        import org.apache.sling.api.resource.ResourceResolverFactory ; 
        import org.apache.sling.api.resource.ResourceResolver; 
        import org.apache.sling.api.resource.Resource; 
        import org.apache.sling.api.resource.ResourceUtil;
        import org.apache.sling.api.resource.ValueMap"
%><% 

    String pageTitle = "";
    String pagePath = "";
    int count = 0;
    String location = "/content/national/en/media/news";

    Resource myPageResource = resourceResolver.getResource(location);
    Page myPage = myPageResource.adaptTo(Page.class);
    Resource pageContentResource = myPage.getContentResource();
    Template pageTemplate = myPage.getTemplate();
    String templatePath = pageTemplate.getPath();
    String pageContentRecourceString = myPage.getPath();
    String cNode = currentNode.getPath();
%>

<%if (WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.PREVIEW ) { %>
                <form id="listBoxForm" action="#" method="post">
                    <select multiple="multiple" size="10" name="duallistbox_output[]" title="duallistbox_output[]">

<%
                for(Iterator<Page> children = myPage.listChildren(); children.hasNext();) {
                    Page child = children.next();
                    pageTitle = child.getTitle();
                    pagePath = child.getPath();
                    count++;
%>
                        <option value="<%=pagePath%>"><%=pageTitle%> </option>
<%
    }
%>

                    </select>
                    <br>
                    <button type="submit" class="btn btn-default btn-block">Submit data</button>
                </form>

<%
    // Node jcrContent = fileNode.getNode("jcr:content");
    currentNode.setProperty("selectedPages", "Test property value");
%>
<script>
$('document').ready(function(){
    console.log('Dual List box loaded!');
    var listBox = $('select[name="duallistbox_output[]"]').bootstrapDualListbox();
    $("#listBoxForm").submit(function() {
    alert($('[name="duallistbox_output[]"]').val());

        return false;
    });
});
        </script>
<%}%>
