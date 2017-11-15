package com.xyz.common;

public final class URLPath {
    private URLPath(){}
    public static final String PREFIX = "/WEB-INF/html/";
    public static final String SUFFIX = ".html";

    public static String getUrl(String middleUrl) {
        return PREFIX + middleUrl + SUFFIX;
    }
}
