package org.gy.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/1 16:18
 */
@Path("/api/test")
public class TestResource {

    @GET
    @Path("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello from RESTEasy Reactive");
        return map;
    }
}
