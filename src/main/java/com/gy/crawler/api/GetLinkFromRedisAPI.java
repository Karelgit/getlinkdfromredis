package com.gy.crawler.api;

import com.gy.crawler.service.LinkUnAvailService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by hongjun luo on 2015/7/20.
 * <p/>
 * web api
 */
@Path("api")
public class GetLinkFromRedisAPI {

    @Autowired
    private LinkUnAvailService linkUnAvailService;


    @Path("test")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {

        return "test";
    }

    @Path("linkunavail")
    @GET
    @Produces({MediaType.TEXT_PLAIN, "text/plain;charset=UTF-8"})
    public String getdata(@QueryParam("tid") String tid) {
        return linkUnAvailService.getLinkUnAvail(tid);
    }


}
