package org.chi.aem.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(LinkUtils.class);
    private static final String HTML = ".html";
    private static final String IMAGE_URL_PREFIX = "https://img.youtube.com/vi/";
    private static final String IMAGE_URL_SUFFIX = "/mqdefault.jpg";
    private static final String IMAGE_URL_SUFFIX_HI_RES = "/maxresdefault.jpg";
    private static final String HI_RES_TN_OPTION = "maxresdefault";
    private static final String REGEX_PATTERN = "\\/([^\\/]+)\\/?$";

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

    public static String getLastPartOfURLOrPath(String url) {
        String lastPart = null;
        if (url != null) {
            Pattern pattern = Pattern.compile(REGEX_PATTERN);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                lastPart = matcher.group(1);
            }  else {
                LOGGER.error("No match found for lastPart in url: " + url);
            }
        }
        return lastPart;
    }

    public static String getYouTubeVideoThumbnail(String videoUrl, String thumbnailOption) {
        String imageUrl = null;
        if (videoUrl != null) {
            String lastPart = getLastPartOfURLOrPath(videoUrl);
            if (lastPart != null) {
                String suffix = (thumbnailOption != null && thumbnailOption.equals(HI_RES_TN_OPTION))?IMAGE_URL_SUFFIX_HI_RES:IMAGE_URL_SUFFIX;
                imageUrl = IMAGE_URL_PREFIX + lastPart + suffix;
            }
        }
        return imageUrl;
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

    public static String convertSpaces(String url){
        if (isNullOrBlank (url)) {
            return url;
        }
        return url.replaceAll(" ", "%20");
    }

}
