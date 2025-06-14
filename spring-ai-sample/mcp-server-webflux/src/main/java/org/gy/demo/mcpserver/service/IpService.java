package org.gy.demo.mcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * @author guanyang
 */
@Service
public class IpService {

    private static final String BASE_URL = "https://ip.xcloudapi.com";

    private final RestClient restClient;

    public IpService() {
        this.restClient = RestClient.builder().baseUrl(BASE_URL).defaultHeader("Accept", "application/geo+json").defaultHeader("User-Agent", "WeatherApiClient/1.0 (your@email.com)").build();
    }

    @Tool(description = "Get your ip information")
    public String getIp() {
        return restClient.get().uri("/geo").retrieve().body(String.class);
    }
}
