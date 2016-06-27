package com.gy.crawler.api;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rs")
public class GYApp extends ResourceConfig {
    public GYApp() {
        register(GetLinkFromRedisAPI.class);
    }
}
