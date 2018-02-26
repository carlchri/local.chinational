package org.chi.aem.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkUtils {


    private static final String HTML = ".html";

    /**
     * 'Externalize' AEM paths by appending ".html" to them.  This allows a link
     * based on the path to work correctly.
     *
     * @param path the path to externalize
     * @return the externalized path
     */

    public static String externalize(String path) {
        if (isLocalPath(path) && !path.contains(".htm") && !path.contains("?")) {
        	if(path.contains(".")){
        		return path;
        	} else {
        		return path + HTML;
        	}
        } else if (path != null && isValidUrl(path)) {
            return addMissingProtocol(path);
        }

        return path;
    }

    public static boolean isLocalPath(String path) {
        return path != null && path.startsWith("/") && !path.startsWith("//");
    }

    private static String addMissingProtocol(String url) {
        if (url == null) return url;

        return (url.startsWith("http://") || url.startsWith("https://"))
                ? url : "http://" + url;
    }

    private static Boolean isValidUrl(String url) {
        Pattern regex = Pattern.compile("\\b(?:(https?|ftp|file)://|www\\.)?[-A-Z0-9+&#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]\\.[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher regexMatcher = regex.matcher(url);
        return regexMatcher.matches();
    }

    public static boolean isNullOrBlank(String s)
    {
        return (s==null || s.trim().equals(""));
    }

}
