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
        // console.log('Dual List box loaded! OK Great Simon!!!');
        var listBox = $('select[name="duallistbox_output[]"]').bootstrapDualListbox();
        var pageUrl = String(window.location.pathname);
        var pageUrlsplit = pageUrl.replace(".html","");
        // console.log("The page Url : "+pageUrlsplit);

        // Submit function
        $("#listBoxForm").submit(function() {
            var featuredListPages = $('[name="duallistbox_output[]"]').val();
            var featuredListPagesJsonString = featuredListPages.toString().replace("[", "{").replace("]","}").split(',');
            var featuredObj = {};

            console.log(featuredListPagesJsonString);
            console.log(featuredListPagesJsonString[0]);
            console.log(featuredListPagesJsonString[1]);
            console.log(featuredListPagesJsonString[2]);
            $.ajax({
                type: 'GET',
                url:'/bin/services/newslisttest',
                data:{'value1' : pageUrlsplit, 'featuredPagesList' : featuredListPagesJsonString }, //passing values to servlet
                success: function(msg){
                    //Success logic here(The response from servlet)
                    alert("Save Complete");
                },
                error: function(xhr, status, error) {
                    console.log(error);
                }
            });
            return false;
        });
    });
</script>
<%}%>