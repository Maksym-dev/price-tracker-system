package com.mhridin.pts_common.utils;

import java.net.URI;

public class DomainConfigUtils {

    private DomainConfigUtils() {}

    public static String getDomain(String url) {
        try {
            return new URI(url).toURL().getHost();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
